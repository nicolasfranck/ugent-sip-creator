/**
 * RenameWand 2.2
 * Copyright 2007 Zach Scrivena
 * 2007-12-09
 * zachscrivena@gmail.com
 * http://renamewand.sourceforge.net/
 *
 * RenameWand is a simple command-line utility for renaming files or
 * directories using an intuitive but powerful syntax.
 *
 * TERMS AND CONDITIONS:
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package renamewand;

import java.io.Console;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeMap;
import java.util.regex.PatternSyntaxException;


/**
 * RenameWand is a simple command-line utility for renaming files or
 * directories using an intuitive but powerful syntax.
 */
public class RenameWand
{
	/**************************************
	 * CONSTANTS AND MISCELLANEOUS FIELDS *
	 **************************************/

	/** constant: program title */
	private static final String PROGRAM_TITLE =
			"RenameWand 2.2   Copyright 2007 Zach Scrivena   2007-12-09";

	/** constant: special construct separator character */
	private static final char SPECIAL_CONSTRUCT_SEPARATOR_CHAR = '|';

	/** constant: substring range character */
	private static final char SUBSTRING_RANGE_CHAR = ':';

	/** constant: substring delimiter character */
	private static final char SUBSTRING_DELIMITER_CHAR = ',';

	/** constant: integer filter indicator character */
	private static final char INTEGER_FILTER_INDICATOR_CHAR = '@';

	/** constant: regex pattern for register names */
	private static final Pattern REGISTER_NAME_PATTERN = Pattern.compile(
			"[a-zA-Z_][a-zA-Z_0-9]*");

	/**
	 * constant: regex pattern for special construct "<length|@expr>" in source pattern string.
	 * Match groups: (1,"length"), (2,"@"), (3,"expr")
	 */
	private static final Pattern SOURCE_SPECIAL_CONSTRUCT_PATTERN = Pattern.compile(
			"\\<(?:([\\sa-zA-Z_0-9\\." +
			Pattern.quote("+-*/^()[]!" + RenameWand.SUBSTRING_RANGE_CHAR  + RenameWand.SUBSTRING_DELIMITER_CHAR) +
			"]+)" + Pattern.quote(RenameWand.SPECIAL_CONSTRUCT_SEPARATOR_CHAR + "") + ")?(" +
			Pattern.quote(RenameWand.INTEGER_FILTER_INDICATOR_CHAR + "") +
			")?([\\sa-zA-Z_0-9\\." +
			Pattern.quote("+-*/^()[]!" + RenameWand.SUBSTRING_RANGE_CHAR  + RenameWand.SUBSTRING_DELIMITER_CHAR) +
			"]+)\\>");

	/**
	 * constant: regex pattern for special construct "<length|expr>" in target pattern string.
	 * Match groups: (1,"length"), (2,"expr")
	 */
	private static final Pattern TARGET_SPECIAL_CONSTRUCT_PATTERN = Pattern.compile(
			"\\<(?:([\\sa-zA-Z_0-9\\." +
			Pattern.quote("+-*/^()[]#!@" + RenameWand.SUBSTRING_RANGE_CHAR  + RenameWand.SUBSTRING_DELIMITER_CHAR) +
			"]+)" + Pattern.quote(RenameWand.SPECIAL_CONSTRUCT_SEPARATOR_CHAR + "") +
			")?([\\sa-zA-Z_0-9\\." +
			Pattern.quote("+-*/^()[]#!@" + RenameWand.SUBSTRING_RANGE_CHAR  + RenameWand.SUBSTRING_DELIMITER_CHAR) +
			"]+)\\>");

	/**
	 * constant: regex pattern for a numeric pattern, e.g. -123.45
	 * Match groups: (1,"+" or "-"), (2,"123.45")
	 */
	private static final Pattern NUMERIC_PATTERN = Pattern.compile(
			"([\\+\\-]?)([0-9]*(?:\\.[0-9]*)?)");

	/** constant: regex pattern for a positive integer pattern, e.g. 42 */
	private static final Pattern POSITIVE_INTEGER_PATTERN = Pattern.compile(
			"\\+?[0-9]+");

	/** operator precedence table for stack evaluation */
	private static final Map<String,Integer> OPERATOR_PRECEDENCE = new TreeMap<String,Integer>();

	/** singular noun for file/directory */
	private static String SINGULAR_NOUN;

	/** plural noun for files/directories */
	private static String PLURAL_NOUN;

	/** standard output */
	static PrintWriter stdout = null;

	/** standard error */
	static PrintWriter stderr = null;

	/** true if this is a Windows OS, false otherwise */
	private static boolean isWindowsOperatingSystem;

	/** current directory (absolute and canonical pathname) */
	static File currentDirectory;

	/** full pathname of the current directory (includes trailing separator) */
	static String currentDirectoryFullPathname;

	/** length of the full pathname of the current directory */
	static int currentDirectoryFullPathnameLength;

	/** register names mapping (register name ---> capture group index) */
	static final Map<String,Integer> registerNames = new TreeMap<String,Integer>();

	/** number of capture groups in source regex pattern */
	private static int numCaptureGroups;

	/** true if the source pattern string is reusable for different files/directories; false otherwise */
	private static boolean sourcePatternIsReusable = false;

	/** subdirectory counter */
	private static int numDirs = 0;

	/*********************
	 * RENAME PARAMETERS *
	 *********************/

	/** parameter: simulate only; do not actually rename files/directories (default = false) */
	private static boolean simulateOnly = false;

	/** parameter: ignore warnings; do not pause (default = false) */
	private static boolean ignoreWarnings = false;

	/** parameter: recurse into subdirectories (default = false) */
	private static boolean recurseIntoSubdirectories = false;

	/** parameter: automatically rename files/directories without prompting (default = false) */
	private static boolean automaticRename = false;

	/** parameter: match relative pathname, not just the name, of the files/directories (default = false) */
	private static boolean matchRelativePathname = false;

	/** parameter: match lower case name of the files/directories (default = false) */
	private static boolean matchLowerCase = false;

	/** parameter: true if renaming directories; false if renaming files (default = false) */
	private static boolean renameDirectories = false;

	/** parameter: default action on rename operation error (default = '\0') */
	private static char defaultActionOnRenameOperationError = '\0';

	/** parameter: source pattern string */
	private static String sourcePatternString;

	/** parameter: target pattern string */
	private static String targetPatternString;

	/*********************
	 * REPORT STATISTICS *
	 *********************/

	/** statistic: number of warnings encountered */
	private static int reportNumWarnings = 0;


	/**
	 * Main entry point for the RenameWand program.
	 *
	 * @param args
	 *     Command-line argument strings
	 */
	public static void main(
			final String[] args)
	{
		/* initialize standard output and error streams */
		final Console console = System.console();

		if (console == null)
		{
			RenameWand.stdout = new PrintWriter(System.out);
			RenameWand.stderr = new PrintWriter(System.err);
		}
		else
		{
			RenameWand.stdout = console.writer();
			RenameWand.stderr = console.writer();
		}

		RenameWand.stdout.print("\n" + RenameWand.PROGRAM_TITLE);
		RenameWand.stdout.flush();

		/* exit status code to be reported to the OS when exiting (default = 0) */
		int exitCode = 0;

		try
		{
			/* determine if this is a Windows OS */
			RenameWand.isWindowsOperatingSystem = System.getProperty("os.name").toUpperCase(Locale.ENGLISH).contains("WINDOWS") &&
					(File.separatorChar == '\\');

			/* initialize operator precedence table for stack evaluation */
			RenameWand.OPERATOR_PRECEDENCE.put("#",   7);
			RenameWand.OPERATOR_PRECEDENCE.put("#!",  7);
			RenameWand.OPERATOR_PRECEDENCE.put("##",  7);
			RenameWand.OPERATOR_PRECEDENCE.put("##!", 7);
			RenameWand.OPERATOR_PRECEDENCE.put("@",   7);
			RenameWand.OPERATOR_PRECEDENCE.put("@!",  7);
			RenameWand.OPERATOR_PRECEDENCE.put("@@",  7);
			RenameWand.OPERATOR_PRECEDENCE.put("@@!", 7);
			RenameWand.OPERATOR_PRECEDENCE.put("^",   6);
			RenameWand.OPERATOR_PRECEDENCE.put("~",   5); // unary minus (negative) sign
			RenameWand.OPERATOR_PRECEDENCE.put("*",   4);
			RenameWand.OPERATOR_PRECEDENCE.put("/",   4);
			RenameWand.OPERATOR_PRECEDENCE.put("+",   3);
			RenameWand.OPERATOR_PRECEDENCE.put("-",   3);
			RenameWand.OPERATOR_PRECEDENCE.put(RenameWand.SUBSTRING_RANGE_CHAR  + "", 2);
			RenameWand.OPERATOR_PRECEDENCE.put(RenameWand.SUBSTRING_DELIMITER_CHAR + "", 1);

			/* process command-line arguments and configure rename parameters */
			processArguments(args);

			/* nouns for file/directory */
			RenameWand.SINGULAR_NOUN = RenameWand.renameDirectories ? "directory" : "file";
			RenameWand.PLURAL_NOUN = RenameWand.renameDirectories ? "directories" : "files";

			RenameWand.stdout.print("\n\nSource pattern: \"" + RenameWand.sourcePatternString +
					"\"\nTarget pattern: \"" + RenameWand.targetPatternString +
					"\"\n\nGetting all candidate " + RenameWand.SINGULAR_NOUN +
					" names in the current directory" +
					(RenameWand.recurseIntoSubdirectories ? " recursively..." : "..."));
                        RenameWand.stdout.print(new File(".").getAbsolutePath());
			RenameWand.stdout.flush();

                        

			/* files/directories to be renamed */
			List<FileUnit> files = null;

			/* get match candidates */
			files = getMatchCandidates();
			final int numMatchCandidates = files.size();

			if (numMatchCandidates == 0)
			{
				RenameWand.stdout.print("\nNo candidate " + RenameWand.SINGULAR_NOUN + " names to match.");
			}
			else
			{
				/* perform source pattern matching on candidate file/directory names */
				RenameWand.stdout.print("\nPerforming source pattern matching on " + numMatchCandidates +
						" candidate " + RenameWand.SINGULAR_NOUN + " " +
						((numMatchCandidates == 1) ? "name" : "names") + "...");
				RenameWand.stdout.flush();

				files = performSourcePatternMatching(files);
				final int numMatched = files.size();

				RenameWand.stdout.print("\n" + numMatched + " out of " + numMatchCandidates +
						" " + RenameWand.SINGULAR_NOUN + " " +
						((numMatched == 1) ? "name" : "names") + " matched.");
				RenameWand.stdout.flush();

				if (numMatched == 0)
				{
					RenameWand.stdout.print("\nNo " + RenameWand.PLURAL_NOUN + " to rename.");
				}
				else
				{
					/* evaluate target file/directory names */
					RenameWand.stdout.print("\nDetermining target " + RenameWand.SINGULAR_NOUN + " " +
							((numMatched == 1) ? "name" : "names") + " and renaming sequence...");
					RenameWand.stdout.flush();

					evaluateTargetPattern(files.toArray(new FileUnit[numMatched]));

					/* determine renaming sequence and find clashes, bad names, etc. */
					final List<RenameFilePair> renameOperations = getRenameOperations(files);
					final int numRenameOperations = renameOperations.size();

                                        for(RenameFilePair pair:renameOperations){
                                            RenameWand.stdout.println(pair.source.getAbsolutePath()+" => "+pair.target.getAbsolutePath());
                                        }

					/* prompt user before renaming */
					final boolean proceedToRename = promptUserOnRename(files, numRenameOperations);

					/* perform rename operations */
					final int numRenameOperationsPerformed = proceedToRename ? performRenameOperations(renameOperations) : 0;

					if (!RenameWand.simulateOnly && proceedToRename)
					{
						RenameWand.stdout.print("\n\n" + numRenameOperationsPerformed + " out of " +
								numRenameOperations + " " + RenameWand.SINGULAR_NOUN + " rename " +
								((numRenameOperations == 1) ? "operation" : "operations") + " performed.");
						RenameWand.stdout.flush();
					}

					/* report statistics */
					final StringBuilder report = new StringBuilder();
					report.append("\n\n" + (RenameWand.renameDirectories ? "DIRECTORY" : "FILE") + " RENAME REPORT");

					if (RenameWand.reportNumWarnings > 0)
					{
						report.append("\n " + RenameWand.reportNumWarnings + " " +
								((RenameWand.reportNumWarnings == 1) ? "warning" : "warnings") + " encountered.");
					}

					report.append("\n No. of candidate " + RenameWand.SINGULAR_NOUN + " names to match  : " + numMatchCandidates +
							"\n No. of " + RenameWand.SINGULAR_NOUN + " names matched             : " + numMatched +
							"\n No. of " + RenameWand.SINGULAR_NOUN + " rename operations required: " + numRenameOperations);

					if (!RenameWand.simulateOnly && (numRenameOperations > 0))
					{
						report.append("\n No. of successful " + RenameWand.SINGULAR_NOUN +
								" rename operations performed: " + numRenameOperationsPerformed);
					}

					RenameWand.stdout.print(report.toString());
				}
			}

			RenameWand.stdout.print("\n\nRenameWand is done!\n\n");
		}
		catch (TerminatingException e)
		{
			/* terminating exception thrown; proceed to abort program */
			/* (this should be the only place where a TerminatingException is caught) */

			exitCode = e.getExitCode();

			if (exitCode != 0)
			{
				/* abnormal termination; print error message */
				RenameWand.stderr.print("\n\nERROR: " + e.getMessage() + "\n");
				RenameWand.stdout.print("\nRenameWand aborted.\n\n");
			}
		}
		catch (Exception e)
		{
			/* catch all other exceptions; proceed to abort program */
			RenameWand.stderr.print("\n\nERROR: An unexpected error has occurred:\n" +
					getExceptionInformation(e) + "\n");

			exitCode = 1;
			RenameWand.stdout.print("\nRenameWand aborted.\n\n");
		}
		finally
		{
			/* perform clean-up before exiting */
			RenameWand.stderr.flush();
			RenameWand.stdout.flush();
		}

		System.exit(exitCode);
	}


	/**
	 * Process command-line arguments.
	 *
	 * @param args
	 *     Command-line argument strings
	 */
	private static void processArguments(
			final String[] args)
	{
		final String howHelp = "\nTo display help, run RenameWand without any command-line arguments.";

		/* print usage documentation, if no arguments */
		if (args.length == 0)
		{
			printUsage();
			throw new TerminatingException(null, 0);
		}

		if (args.length < 2)
			throw new TerminatingException("Insufficient arguments:\nThe source and target pattern strings must be specified." + howHelp);

		/* source and target pattern strings */
		RenameWand.sourcePatternString = args[args.length - 2];
		RenameWand.targetPatternString = args[args.length - 1];

		/* check for illegal characters in pattern strings */
		if (RenameWand.sourcePatternString.contains("\0"))
			throw new TerminatingException("Illegal null-character found in source pattern string.");

		if (RenameWand.targetPatternString.contains("\0"))
			throw new TerminatingException("Illegal null-character found in target pattern string.");

		/* default action on rename operation error */
		int skipOnRenameOperationError = 0;
		int undoAllOnRenameOperationError = 0;
		int abortOnRenameOperationError = 0;

		/* process command-line switches */
		for (int i = 0; i < args.length - 2; i++)
		{
			final String sw = args[i];

			if ("--recurse".equals(sw) || "-r".equals(sw))
			{
				/* recurse into subdirectories */
				RenameWand.recurseIntoSubdirectories = true;
			}
			else if ("--dirs".equals(sw) || "-d".equals(sw))
			{
				/* rename directories instead of files */
				RenameWand.renameDirectories = true;
			}
			else if ("--path".equals(sw) || "-p".equals(sw))
			{
				/* match relative pathname, not just the name, of the files/directories */
				RenameWand.matchRelativePathname = true;
			}
			else if ("--lower".equals(sw) || "-l".equals(sw))
			{
				/* match lower case name of the files/directories */
				RenameWand.matchLowerCase = true;
			}
			else if ("--yes".equals(sw) || "-y".equals(sw))
			{
				/* automatically rename files/directories without prompting */
				RenameWand.automaticRename = true;
			}
			else if ("--simulate".equals(sw) || "-s".equals(sw))
			{
				/* simulate only; do not actually rename files/directories */
				RenameWand.simulateOnly = true;
				RenameWand.ignoreWarnings = true;
			}
			else if ("--ignorewarnings".equals(sw) || "-i".equals(sw))
			{
				/* ignore warnings; do not pause */
				RenameWand.ignoreWarnings = true;
			}
			else if ("--skip".equals(sw))
			{
				/* skip on rename operation error */
				skipOnRenameOperationError = 1;
			}
			else if ("--undoall".equals(sw))
			{
				/* undo all on rename operation error */
				undoAllOnRenameOperationError = 1;
			}
			else if ("--abort".equals(sw))
			{
				/* abort on rename operation error */
				abortOnRenameOperationError = 1;
			}
			else
			{
				/* invalid switch */
				throw new TerminatingException("\"" + sw + "\" is not a valid switch." + howHelp);
			}
		}

		if (RenameWand.simulateOnly && RenameWand.automaticRename)
			throw new TerminatingException("Switches --simulate and --yes cannot be used together." + howHelp);

		if (skipOnRenameOperationError + undoAllOnRenameOperationError + abortOnRenameOperationError > 1)
			throw new TerminatingException("Only one of the three switches --skip, --undoall, and --abort may be specified." + howHelp);

		if (RenameWand.simulateOnly && (skipOnRenameOperationError + undoAllOnRenameOperationError + abortOnRenameOperationError > 0))
			throw new TerminatingException("Switches --skip, --undoall, and --abort cannot be used together with --simulate." + howHelp);

		/* default action on rename operation error */
		if (skipOnRenameOperationError > 0)
			RenameWand.defaultActionOnRenameOperationError = 'S';

		if (undoAllOnRenameOperationError > 0)
			RenameWand.defaultActionOnRenameOperationError = 'U';

		if (abortOnRenameOperationError > 0)
			RenameWand.defaultActionOnRenameOperationError = 'A';
	}


	/**
	 * Scan current directory to get candidate files/directories for matching.
	 *
	 * @return
	 *     Candidate files/directories for matching
	 */
	private static List<FileUnit> getMatchCandidates()
	{
		/* get absolute canonical path of the current directory */
		RenameWand.currentDirectory = new File("");

		try
		{
			RenameWand.currentDirectory = RenameWand.currentDirectory.getCanonicalFile();
		}
		catch (Exception e)
		{
			throw new TerminatingException("Failed to get full pathname of the current directory \"" +
					RenameWand.currentDirectory.getPath() + "\":\n" + getExceptionInformation(e));
		}

		RenameWand.currentDirectoryFullPathname = RenameWand.currentDirectory.getPath();

		/* include trailing separator */
		if (!RenameWand.currentDirectoryFullPathname.endsWith(File.separator))
			RenameWand.currentDirectoryFullPathname += File.separator;

		RenameWand.currentDirectoryFullPathnameLength = RenameWand.currentDirectoryFullPathname.length();

		/* return value: match candidate files/directories */
		final List<FileUnit> matchCandidates = new ArrayList<FileUnit>();

		/* stack containing the subdirectories to be scanned */
		final Deque<File> subdirectories = new ArrayDeque<File>();
		subdirectories.push(RenameWand.currentDirectory);

		/* reset number of subdirectories scanned */
		RenameWand.numDirs = 0;

		/* perform a DFS scanning of the subdirectories */
		while (!subdirectories.isEmpty())
		{
			RenameWand.numDirs++;

			/* get a directory to be scanned */
			final File dir = subdirectories.pop();
			final File[] listFiles = dir.listFiles();

			if (listFiles == null)
			{
				final String path = dir.getPath();

				reportWarning("Failed to get contents of directory \"" + path +
						(path.endsWith(File.separator) ? "" : File.separator) +
						"\".\nThis directory will be ignored.");
			}
			else
			{
				/* subdirectories under this directory */
				final List<File> subdirs = new ArrayList<File>();

				for (File f : listFiles)
				{
					final boolean isDirectory = f.isDirectory();

					if (RenameWand.renameDirectories == isDirectory)
					{
						final FileUnit u = new FileUnit();
						u.source = f;
						u.parentDirId = RenameWand.numDirs;
						matchCandidates.add(u);
					}

					if (isDirectory)
						subdirs.add(f);
				}

				if (RenameWand.recurseIntoSubdirectories)
				{
					for (int i = subdirs.size() - 1; i >= 0; i--)
						subdirectories.push(subdirs.get(i));
				}
			}
		}

		return matchCandidates;
	}


	/**
	 * Perform source pattern matching against the names of the candidate
	 * files/directories, and return files/directories that match.
	 *
	 * @param matchCandidates
	 *     Candidate files/directories
	 * @return
	 *     Files/directories with names that match the source pattern
	 */
	private static List<FileUnit> performSourcePatternMatching(
			final List<FileUnit> matchCandidates)
	{
		/* return value: files/directories with names that match the source pattern */
		final List<FileUnit> matched = new ArrayList<FileUnit>();

		/* regex pattern used for matching file/directory names */
		Pattern sourcePattern = null;

		/* is the regex pattern matcher reusable for different files/directories? */
		RenameWand.sourcePatternIsReusable = false;

		/* match each candidate file or directory */
		for (FileUnit u : matchCandidates)
		{
			if (!RenameWand.sourcePatternIsReusable)
				sourcePattern = getFileSourcePattern(u);

			/* check if source pattern is successfully generated */
			if (sourcePattern != null)
			{
				/* name string to be matched */
				String name = null;

				if (RenameWand.matchRelativePathname)
				{
					name = u.source.getPath();

					if (name.startsWith(RenameWand.currentDirectoryFullPathname))
						name = name.substring(RenameWand.currentDirectoryFullPathnameLength);
				}
				else
				{
					name = u.source.getName();
				}

				/* trim off trailing separator */
				while (name.endsWith(File.separator))
					name = name.substring(0, name.length() - File.separator.length());

				if (RenameWand.matchLowerCase)
					name = name.toLowerCase(Locale.ENGLISH);

				/* regex pattern matcher */
				final Matcher sourceMatcher = sourcePattern.matcher(name);

				if (sourceMatcher.matches())
				{
					/* add capture group values to FileUnit's registerValues, and */
					/* add this file/directory to our list of successful matches  */

					u.registerValues = new String[RenameWand.numCaptureGroups + 1]; // add index offset 1

					for (int i = 1; i <= RenameWand.numCaptureGroups; i++)
						u.registerValues[i] = sourceMatcher.group(i);

					matched.add(u);
				}
			}
		}

		return matched;
	}


	/**
	 * Generate source regex pattern corresponding to the given file/directory.
	 *
	 * @param u
	 *     File/directory for which to generate source regex pattern
	 * @return
	 *     Source regex pattern corresponding to the given file/directory;
	 *     null if regex pattern cannot be generated.
	 */
	private static Pattern getFileSourcePattern(
			final FileUnit u)
	{
		/* reset register names and capture group counter */
		RenameWand.registerNames.clear();
		int captureGroupIndex = 0;

		/* assume that source pattern is reusable */
		RenameWand.sourcePatternIsReusable = true;

		/* Stack to keep track of the parser mode: */
		/* "--" : Base mode (first on the stack)   */
		/* "[]" : Square brackets mode "[...]"     */
		/* "{}" : Curly braces mode "{...}"        */
		final Deque<String> parserMode = new ArrayDeque<String>();
		parserMode.push("--"); // base mode

		final int sourcePatternStringLength = RenameWand.sourcePatternString.length();
		int index = 0; // index in sourcePatternString

		/* regex pattern equivalent to sourcePatternString */
		final StringBuilder sourceRegex = new StringBuilder();

		/* parse each character of the source pattern string */
		while (index < sourcePatternStringLength)
		{
			char c = RenameWand.sourcePatternString.charAt(index++);

			if (c == '\\')
			{
				/***********************
				 * (1) ESCAPE SEQUENCE *
				 ***********************/

				if (index == sourcePatternStringLength)
				{
					/* no characters left, so treat '\' as literal char */
					sourceRegex.append(Pattern.quote("\\"));
				}
				else
				{
					/* read next character */
					c = RenameWand.sourcePatternString.charAt(index);
					final String s = c + "";

					if (("--".equals(parserMode.peek()) && "\\<>[]{}?*".contains(s)) ||
							("[]".equals(parserMode.peek()) && "\\<>[]{}?*!-".contains(s)) ||
							("{}".equals(parserMode.peek()) && "\\<>[]{}?*,".contains(s)))
					{
						/* escape the construct char */
						index++;
						sourceRegex.append(Pattern.quote(s));
					}
					else
					{
						/* treat '\' as literal char */
						sourceRegex.append(Pattern.quote("\\"));
					}
				}
			}
			else if (c == '*')
			{
				/************************
				 * (2) GLOB PATTERN '*' *
				 ************************/

				/* create non-capturing group to match zero or more characters */
				sourceRegex.append(".*");
			}
			else if (c == '?')
			{
				/************************
				 * (3) GLOB PATTERN '?' *
				 ************************/

				/* create non-capturing group to match exactly one character */
				sourceRegex.append('.');
			}
			else if (c == '[')
			{
				/****************************
				 * (4) GLOB PATTERN "[...]" *
				 ****************************/

				/* opening square bracket '[' */
				/* create non-capturing group to match exactly one character */
				/* inside the sequence */
				sourceRegex.append('[');
				parserMode.push("[]");

				/* check for negation character '!' immediately after */
				/* the opening bracket '[' */
				if ((index < sourcePatternStringLength) &&
						(RenameWand.sourcePatternString.charAt(index) == '!'))
				{
					index++;
					sourceRegex.append('^');
				}
			}
			else if ((c == ']') && "[]".equals(parserMode.peek()))
			{
				/* closing square bracket ']' */
				sourceRegex.append(']');
				parserMode.pop();
			}
			else if ((c == '-') && "[]".equals(parserMode.peek()))
			{
				/* character range '-' in "[...]" */
				sourceRegex.append('-');
			}
			else if (c == '{')
			{
				/****************************
				 * (5) GLOB PATTERN "{...}" *
				 ****************************/

				/* opening curly brace '{' */
				/* create non-capturing group to match one of the */
				/* strings inside the sequence */
				sourceRegex.append("(?:(?:");
				parserMode.push("{}");
			}
			else if ((c == '}') && "{}".equals(parserMode.peek()))
			{
				/* closing curly brace '}' */
				sourceRegex.append("))");
				parserMode.pop();
			}
			else if ((c == ',') && "{}".equals(parserMode.peek()))
			{
				/* comma between strings in "{...}" */
				sourceRegex.append(")|(?:");
			}
			else if (c == '<')
			{
				/*********************************
				 * (6) SPECIAL CONSTRUCT "<...>" *
				 *********************************/

				final StringBuilder specialConstruct = new StringBuilder("<");
				boolean closingAngleBracketFound = false;

				/* read until the first (unescaped) closing '>' */
				while (!closingAngleBracketFound && (index < sourcePatternStringLength))
				{
					c = RenameWand.sourcePatternString.charAt(index++);
					specialConstruct.append(c);

					if ((c == '>') && (specialConstruct.charAt(specialConstruct.length() - 2) != '\\'))
						closingAngleBracketFound = true;
				}

				if (!closingAngleBracketFound)
					throw new TerminatingException("Failed to find matching closing angle bracket > for special construct \"" +
							specialConstruct + "\" in source pattern string. Please ensure that each literal < is escaped as \\<.");

				/* check if special construct is in the form "<length|@expr>" */
				final Matcher specialConstructMatcher =
						RenameWand.SOURCE_SPECIAL_CONSTRUCT_PATTERN.matcher(specialConstruct);

				if (!specialConstructMatcher.matches())
					throw new TerminatingException("Invalid special construct \"" + specialConstruct + "\" in source pattern string: " +
							"Not in the form <length" + RenameWand.SPECIAL_CONSTRUCT_SEPARATOR_CHAR + RenameWand.INTEGER_FILTER_INDICATOR_CHAR + "expr>.");

				/* match groups */
				String length = specialConstructMatcher.group(1);
				if (length != null) length = length.trim();
				final boolean integerFilter = (specialConstructMatcher.group(2) != null);
				String expr = specialConstructMatcher.group(3).trim();

				/* evaluate the length string if it is not already a positive integer */
				if ((length != null) &&
						!RenameWand.POSITIVE_INTEGER_PATTERN.matcher(length).matches())
				{
					RenameWand.sourcePatternIsReusable = false; // this construct is file-specific
					final EvaluationResult<String> result = evaluateSpecialConstruct(new FileUnit[] {u}, length);

					if (result.success)
					{
						length = result.output[0];

						/* check that length string is a positive integer now */
						if (!RenameWand.POSITIVE_INTEGER_PATTERN.matcher(length).matches())
							throw new TerminatingException("Invalid length string for special construct \"" +
									specialConstruct + "\" in source pattern string for " +
									RenameWand.SINGULAR_NOUN + " \"" + u.source.getPath() + "\": " +
									length + " is not a positive integer.");
					}
					else
					{
						reportWarning("Failed to evaluate length string of special construct \"" +
								specialConstruct + "\" in source pattern string for " +
								RenameWand.SINGULAR_NOUN + " \"" + u.source.getPath() + "\": " +
								result.error + "\nThis " + RenameWand.SINGULAR_NOUN + " will be ignored.");

						return null; // failed to generate regex pattern
					}
				}

				/* check if this construct is a register assignment or back reference */
				if (((length == null) || RenameWand.POSITIVE_INTEGER_PATTERN.matcher(length).matches()) &&
						(u.evaluateMacro(expr) == null) &&
						RenameWand.REGISTER_NAME_PATTERN.matcher(expr).matches())
				{
					if (RenameWand.registerNames.containsKey(expr))
					{
						/* register is already captured, so we create a back reference to it */
						if ((length != null) || integerFilter)
							throw new TerminatingException("Invalid back reference \"" + specialConstruct +
									"\" to register \"" + expr + "\" near position " + index +
									" of source pattern string: Length string and integer filter indicator @ not allowed.");

						sourceRegex.append("\\" + RenameWand.registerNames.get(expr));
					}
					else
					{
						/* register has not been captured, so we create a regex capturing group for it */
						RenameWand.registerNames.put(expr, ++captureGroupIndex);

						sourceRegex.append("(" + (integerFilter ? "[0-9]" : ".") +
								((length == null) ? "*" : ("{" + Integer.parseInt(length.trim()) + "}")) + ")");
					}
				}
				else
				{
					/* proceed to parse the expression string */

					RenameWand.sourcePatternIsReusable = false; // this construct is file-specific

					if (integerFilter)
						throw new TerminatingException("Invalid special construct expression \"" + specialConstruct +
								"\" in source pattern string: Integer filter indicator @ not allowed here because \"" +
								expr + "\" is not a register name.");

					/* evaluate expr string */
					final EvaluationResult<String> result = evaluateSpecialConstruct(new FileUnit[] {u}, expr);

					if (result.success)
					{
						expr = result.output[0];

						/* perform length formatting if specified */
						if (length != null)
							expr = padString(expr, Integer.parseInt(length), isNumeric(expr));

						/* convert literal string to a regex string */
						sourceRegex.append(Pattern.quote(expr));
					}
					else
					{
						reportWarning("Failed to evaluate expression string of special construct \"" +
								specialConstruct + "\" in source pattern string for " +
								RenameWand.SINGULAR_NOUN + " \"" + u.source.getPath() + "\": " +
								result.error + "\nThis " + RenameWand.SINGULAR_NOUN + " will be ignored.");

						return null; // failed to generate regex pattern
					}
				}
			}
			else if ((c == '/') && RenameWand.isWindowsOperatingSystem)
			{
				/****************************************
				 * (7) ALTERNATE WINDOWS FILE SEPARATOR *
				 ****************************************/

				sourceRegex.append(Pattern.quote("\\"));
			}
			else
			{
				/*************************
				 * (8) LITERAL CHARACTER *
				 *************************/

				/* convert literal character to a regex string */
				sourceRegex.append(Pattern.quote(c + ""));
			}
		}

		/* check for mismatched [...] or {...} */
		if ("[]".equals(parserMode.peek()))
			throw new TerminatingException("Failed to find matching closing square bracket ] in source pattern string.");

		if ("{}".equals(parserMode.peek()))
			throw new TerminatingException("Failed to find matching closing curly brace } in source pattern string.");

		/* set total number of capture groups in the source pattern */
		RenameWand.numCaptureGroups = captureGroupIndex;

		/* compile regex string */
		Pattern sourceRegexCompiledPattern = null;

		try
		{
			sourceRegexCompiledPattern = Pattern.compile(sourceRegex.toString());
		}
		catch (PatternSyntaxException e)
		{
			throw new TerminatingException("Failed to compile source pattern string for " +
					RenameWand.SINGULAR_NOUN + " \"" + u.source.getPath() + "\":\n" +
					getExceptionInformation(e));
		}

		return sourceRegexCompiledPattern;
	}


	/**
	 * Evaluate the target pattern for the given matched files/directories,
	 * and store the results in the respective FileUnit's.
	 *
	 * @param matchedFiles
	 *     Matched files/directories for which to evaluate the target pattern
	 */
	private static void evaluateTargetPattern(
			final FileUnit[] matchedFiles)
	{
		/* number of matched files */
		final int n = matchedFiles.length;

		/* count number of files in each local directory */
		int[] localCounts = new int[RenameWand.numDirs + 1];

		Arrays.fill(localCounts, 0);

		for (FileUnit u : matchedFiles)
			localCounts[u.parentDirId]++;

		/* reset file/directory attributes */
		for (FileUnit u : matchedFiles)
		{
			u.globalCount = n;
			u.localCount = localCounts[u.parentDirId];
			u.targetFilename = new StringBuilder();
		}

		final int targetPatternStringLength = RenameWand.targetPatternString.length();
		int index = 0; // index in targetPatternString

		/* parse each character of the target pattern string */
		while (index < targetPatternStringLength)
		{
			char c = RenameWand.targetPatternString.charAt(index++);

			if (c == '\\')
			{
				/***********************
				 * (1) ESCAPE SEQUENCE *
				 ***********************/

				if (index == targetPatternStringLength)
				{
					/* no characters left, so treat '\' as literal char */
					for (FileUnit u : matchedFiles)
						u.targetFilename.append('\\');
				}
				else
				{
					/* read next char */
					c = RenameWand.targetPatternString.charAt(index);

					if ((c == '<') || (c == '\\'))
					{
						/* escape the construct char */
						index++;

						for (FileUnit u : matchedFiles)
							u.targetFilename.append(c);
					}
					else
					{
						/* treat '\' as literal char */
						for (FileUnit u : matchedFiles)
							u.targetFilename.append('\\');
					}
				}
			}
			else if (c == '<')
			{
				/*********************************
				 * (2) SPECIAL CONSTRUCT "<...>" *
				 *********************************/

				final StringBuilder specialConstruct = new StringBuilder("<");
				boolean closingAngleBracketFound = false;

				/* read until the first (unescaped) closing '>' */
				while ((!closingAngleBracketFound) && (index < targetPatternStringLength))
				{
					c = RenameWand.targetPatternString.charAt(index++);
					specialConstruct.append(c);

					if ((c == '>') && (specialConstruct.charAt(specialConstruct.length() - 2) != '\\'))
						closingAngleBracketFound = true;
				}

				if (!closingAngleBracketFound)
					throw new TerminatingException("Failed to find matching closing angle bracket > for special construct \"" +
							specialConstruct + "\" in target pattern string. Please ensure that each literal < is escaped as \\<.");

				/* check if special construct is in the form "<length|expr>" */
				final Matcher specialConstructMatcher =
						RenameWand.TARGET_SPECIAL_CONSTRUCT_PATTERN.matcher(specialConstruct);

				if (!specialConstructMatcher.matches())
					throw new TerminatingException("Invalid special construct \"" + specialConstruct +
							"\" in target pattern string: Not in the form <length" + RenameWand.SPECIAL_CONSTRUCT_SEPARATOR_CHAR + "expr>.");

				/* match groups */
				final String length = specialConstructMatcher.group(1);
				final String expr = specialConstructMatcher.group(2).trim();

				String[] lengths = null;

				/* evaluate the length string if it is not already a positive integer */
				if ((length != null) &&
						(!RenameWand.POSITIVE_INTEGER_PATTERN.matcher(length).matches()))
				{
					final EvaluationResult<String> result = evaluateSpecialConstruct(matchedFiles, length);

					if (result.success)
					{
						lengths = result.output;

						/* check that length string is a positive integer now */
						for (int i = 0; i < n; i++)
							if (!RenameWand.POSITIVE_INTEGER_PATTERN.matcher(lengths[i]).matches())
								throw new TerminatingException("Invalid length string for special construct \"" +
										specialConstruct + "\" in target pattern string for " + SINGULAR_NOUN + " \"" +
										matchedFiles[i].source.getPath() + "\": " + lengths[i] + " is not a positive integer.");
					}
					else
					{
						throw new TerminatingException("Failed to evaluate length string of special construct \"" +
								specialConstruct + "\" in target pattern string: " + result.error);
					}
				}

				/* proceed to parse the expression string */
				final EvaluationResult<String> result = evaluateSpecialConstruct(matchedFiles, expr);

				if (!result.success)
					throw new TerminatingException("Failed to evaluate expression string of special construct \"" +
							specialConstruct + "\" in target pattern string: " + result.error);

				/* perform length formatting */
				if (lengths == null)
				{
					if (length == null)
					{
						/* no length string specified */
						for (int i = 0; i < n; i++)
							matchedFiles[i].targetFilename.append(result.output[i]);
					}
					else
					{
						/* constant length string specified */
						final int len = Integer.parseInt(length);

						/* check if all expr are numeric */
						final boolean isNumeric = (getNonNumericIndex(result.output) < 0);

						for (int i = 0; i < n; i++)
							matchedFiles[i].targetFilename.append(
									padString(result.output[i], len, isNumeric));
					}
				}
				else
				{
					/* file-specific length strings used */

					/* check if all expr are numeric */
					final boolean isNumeric = (getNonNumericIndex(result.output) < 0);

					for (int i = 0; i < n; i++)
						matchedFiles[i].targetFilename.append(
								padString(result.output[i], Integer.parseInt(lengths[i]), isNumeric));
				}
			}
			else if ((c == '/') && RenameWand.isWindowsOperatingSystem)
			{
				/****************************************
				 * (3) ALTERNATE WINDOWS FILE SEPARATOR *
				 ****************************************/

				for (FileUnit u : matchedFiles)
					u.targetFilename.append('\\');
			}
			else
			{
				/*************************
				 * (4) LITERAL CHARACTER *
				 *************************/

				/* handle all other characters as literals */
				for (FileUnit u : matchedFiles)
					u.targetFilename.append(c);
			}
		}
	}


	/**
	 * Evaluate the given special construct for the given files/directories, and
	 * return the results.
	 *
	 * @param file
	 *     Files/directories for which to evaluate the given special construct
	 * @param specialConstruct
	 *     Special construct string
	 * @return
	 *     Results of evaluation
	 */
	private static EvaluationResult<String> evaluateSpecialConstruct(
			final FileUnit[] files,
			final String specialConstruct)
	{
		/* use a stack to evaluate the special construct */

		/* return value */
		final EvaluationResult<String> result = new EvaluationResult<String>();

		/* tokenize expr */
		final StringManipulator.Token[] tempTokens = StringManipulator.tokenize(
				"(" + specialConstruct + ")",  /* surround special construct with (...) */
				"(((\\#++)\\!?)|((\\@++)\\!?)|[" +
				Pattern.quote("[]()*/^+-" + RenameWand.SUBSTRING_RANGE_CHAR  + RenameWand.SUBSTRING_DELIMITER_CHAR) + "])",
				true);

		/* preprocess tokens */
		final List<StringManipulator.Token> tokens = new ArrayList<StringManipulator.Token>();

		for (StringManipulator.Token token : tempTokens)
		{
			token.val = token.val.trim();

			if (token.val.isEmpty())
				continue;

			if ("-".equals(token.val) &&
					tokens.get(tokens.size() - 1).isDelimiter &&
					!")]".contains(tokens.get(tokens.size() - 1).val))
			{
				/* unary minus (negative) sign */
				token.val = "~";
			}
			else if  ("(".equals(token.val) &&
					(tokens.size() > 0) &&
					(!tokens.get(tokens.size() - 1).isDelimiter ||
					")".equals(tokens.get(tokens.size() - 1).val)))
			{
				/* implicit multiplication sign */
				final StringManipulator.Token multiplicationSign =
						new StringManipulator.Token("*", true);

				tokens.add(multiplicationSign);
			}

			tokens.add(token);
		}

		/* number of file units */
		final int n = files.length;

		/* operator and operand stacks */
		final Deque<String> operators = new ArrayDeque<String>();
		final Deque<String[]> operands = new ArrayDeque<String[]>();

		/* process each token */
		ProcessNextToken:
		for (StringManipulator.Token token : tokens)
		{
			final String tokenVal = token.val;

			if (token.isDelimiter)
			{
				/* token is a delimiter */

				final String op = token.val;

				if ("(".equals(op))
				{
					operators.push(op);
				}
				else if ("[".equals(op))
				{
					operators.push(op);
				}
				else if (")".equals(op))
				{
					/* eval (...) */
					while (!operators.isEmpty() &&
							!"(".equals(operators.peek()))
					{
						final EvaluationResult<Void> stepResult = evaluateStep(files, operators, operands);

						if (!stepResult.success)
						{
							result.error = stepResult.error;
							return result;
						}
					}

					if (operators.isEmpty() || !"(".equals(operators.pop()))
					{
						result.error = "Mismatched brackets ( ) in special construct expression.";
						return result;
					}
				}
				else if ("]".equals(op))
				{
					/* eval substring a[...] */
					while ((!operators.isEmpty()) &&
							(!"[".equals(operators.peek())))
					{
						final EvaluationResult<Void> stepResult = evaluateStep(files, operators, operands);

						if (!stepResult.success)
						{
							result.error = stepResult.error;
							return result;
						}
					}

					if (operators.isEmpty() || !"[".equals(operators.pop()))
					{
						result.error = "Mismatched brackets [ ] in special construct expression.";
						return result;
					}

					/* proceed to evaluate substring */
					if (operands.size() < 2)
					{
						result.error = "Insufficient operands for substring [ ] operation.";
						return result;
					}

					final String[] formats = operands.pop();
					final String[] exprs = operands.pop();
					String[] ans = new String[n];

					for (int i = 0; i < n; i++)
					{
						final String format = formats[i];
						final String expr = exprs[i];

						ans[i] = StringManipulator.substring(
								expr,
								format,
								RenameWand.SUBSTRING_RANGE_CHAR ,
								RenameWand.SUBSTRING_DELIMITER_CHAR);

						if (ans[i] == null)
						{
							result.error = "Invalid substring operation \"" +
									expr + "[" + format + "]\".";

							return result;
						}
					}

					/* push answer on operand stack */
					operands.push(ans);
				}
				else
				{
					/* all other operators */

					/* eval stack if possible */
					while (!operators.isEmpty() &&
							!"[]()".contains(operators.peek()) &&
							(RenameWand.OPERATOR_PRECEDENCE.get(op).intValue() <=
							RenameWand.OPERATOR_PRECEDENCE.get(operators.peek()).intValue()))
					{
						final EvaluationResult<Void> stepResult = evaluateStep(files, operators, operands);

						if (!stepResult.success)
						{
							result.error = stepResult.error;
							return result;
						}
					}

					operators.push(op);
				}
			}
			else
			{
				/* token is an operand */

				String[] tokenVals = new String[n];

				if (RenameWand.NUMERIC_PATTERN.matcher(tokenVal).matches())
				{
					/* token is a numeric value */
					for (int i = 0; i < n; i++)
						tokenVals[i] = tokenVal;
				}
				else if (files[0].evaluateMacro(tokenVal) != null)
				{
					/* token is a register or macro */
					for (int i = 0; i < n; i++)
						tokenVals[i] = files[i].evaluateMacro(tokenVal);
				}
				else
				{
					/* treat as literal text */
					for (int i = 0; i < n; i++)
						tokenVals[i] = tokenVal;
				}

				/* push evaluated token onto operands stack */
				operands.push(tokenVals);
			}
		}

		/* extract final result */
		if (operators.isEmpty() && (operands.size() == 1))
		{
			/* valid return value */
			result.success = true;
			result.output = operands.pop();
		}
		else
		{
			/* error in evaluation */
			result.error = "Mismatched operators/operands in special construct expression.";
		}

		return result;
	}


	/**
	 * Evaluate a single step, given the operators and operands stacks, for the
	 * given files/directories.
	 *
	 * @param files
	 *     Files/directories for which to evaluate the single step
	 * @param operators
	 *     Operators stack
	 * @param operands
	 *     Operands stack
	 * @return
	 *     Results of the evaluation
	 */
	private static EvaluationResult<Void> evaluateStep(
			final FileUnit[] files,
			final Deque<String> operators,
			final Deque<String[]> operands)
	{
		final int n = files.length;

		/* return value */
		final EvaluationResult<Void> result = new EvaluationResult<Void>();

		if (operators.isEmpty())
		{
			result.error = "Operators stack is empty.";
			return result;
		}

		final String op = operators.pop();

		if ("^".equals(op))
		{
			/**************************
			 * (1) EXPONENTIATION '^' *
			 **************************/

			if (operands.size() < 2)
			{
				result.error = "Insufficient operands for exponentiation '^' operation.";
				return result;
			}

			final String[] args2 = operands.pop();
			final String[] args1 = operands.pop();

			/* check that arguments are all numeric */
			final int args1NonNumericIndex = getNonNumericIndex(args1);
			final int args2NonNumericIndex = getNonNumericIndex(args2);

			if ((args1NonNumericIndex >= 0) || (args2NonNumericIndex >= 0))
			{
				int nonNumericIndex;
				String nonNumericArg;

				if (args1NonNumericIndex >= 0)
				{
					nonNumericIndex = args1NonNumericIndex;
					nonNumericArg = args1[args1NonNumericIndex];
				}
				else
				{
					nonNumericIndex = args2NonNumericIndex;
					nonNumericArg = args2[args2NonNumericIndex];
				}

				result.error = "Invalid operand encountered for exponentiation '^' operation: " +
						"The operand \"" + nonNumericArg + "\" corresponding to " +
						RenameWand.SINGULAR_NOUN + " \"" + files[nonNumericIndex].source.getPath() +
						"\" is non-numeric.";

				operators.push(op);
				operands.push(args1);
				operands.push(args2);
				return result;
			}

			String[] ans = new String[n];

			for (int i = 0; i < n; i++)
				ans[i] = (int) Math.pow((int) Double.parseDouble(args1[i]), (int) Double.parseDouble(args2[i])) + "";

			/* push answer on operand stack */
			operands.push(ans);
			result.success = true;
		}
		else if ("*".equals(op))
		{
			/**************************
			 * (2) MULTIPLICATION '*' *
			 **************************/

			if (operands.size() < 2)
			{
				result.error = "Insufficient operands for multiplication '*' operation.";
				return result;
			}

			final String[] args2 = operands.pop();
			final String[] args1 = operands.pop();

			/* check that arguments are all numeric */
			final int args1NonNumericIndex = getNonNumericIndex(args1);
			final int args2NonNumericIndex = getNonNumericIndex(args2);

			if ((args1NonNumericIndex >= 0) || (args2NonNumericIndex >= 0))
			{
				int nonNumericIndex;
				String nonNumericArg;

				if (args1NonNumericIndex >= 0)
				{
					nonNumericIndex = args1NonNumericIndex;
					nonNumericArg = args1[args1NonNumericIndex];
				}
				else
				{
					nonNumericIndex = args2NonNumericIndex;
					nonNumericArg = args2[args2NonNumericIndex];
				}

				result.error = "Invalid operand encountered for multiplication '*' operation: " +
						"The operand \"" + nonNumericArg + "\" corresponding to " +
						RenameWand.SINGULAR_NOUN + " \"" + files[nonNumericIndex].source.getPath() +
						"\" is non-numeric.";

				operators.push(op);
				operands.push(args1);
				operands.push(args2);
				return result;
			}

			String[] ans = new String[n];

			for (int i = 0; i < n; i++)
				ans[i] = (((int) Double.parseDouble(args1[i])) * ((int) Double.parseDouble(args2[i]))) + "";

			/* push answer on operand stack */
			operands.push(ans);
			result.success = true;
		}
		else if ("/".equals(op))
		{
			/********************
			 * (3) DIVISION '/' *
			 ********************/

			if (operands.size() < 2)
			{
				result.error = "Insufficient operands for division '/' operation.";
				return result;
			}

			final String[] args2 = operands.pop();
			final String[] args1 = operands.pop();

			/* check that arguments are numeric */
			final int args1NonNumericIndex = getNonNumericIndex(args1);
			final int args2NonNumericIndex = getNonNumericIndex(args2);

			if ((args1NonNumericIndex >= 0) || (args2NonNumericIndex >= 0))
			{
				int nonNumericIndex;
				String nonNumericArg;

				if (args1NonNumericIndex >= 0)
				{
					nonNumericIndex = args1NonNumericIndex;
					nonNumericArg = args1[args1NonNumericIndex];
				}
				else
				{
					nonNumericIndex = args2NonNumericIndex;
					nonNumericArg = args2[args2NonNumericIndex];
				}

				result.error = "Invalid operand encountered for division '/' operation: " +
						"The operand \"" + nonNumericArg + "\" corresponding to " +
						RenameWand.SINGULAR_NOUN + " \"" + files[nonNumericIndex].source.getPath() +
						"\" is non-numeric.";

				operators.push(op);
				operands.push(args1);
				operands.push(args2);
				return result;
			}

			String[] ans = new String[n];

			for (int i = 0; i < n; i++)
			{
				try
				{
					ans[i] = (((int) Double.parseDouble(args1[i])) / ((int) Double.parseDouble(args2[i]))) + "";
				}
				catch (ArithmeticException e)
				{
					result.error = "Division by zero.";
					operators.push(op);
					operands.push(args1);
					operands.push(args2);
					return result;
				}
			}

			/* push answer on operand stack */
			operands.push(ans);
			result.success = true;
		}
		else if ("-".equals(op))
		{
			/***********************
			 * (4) SUBTRACTION '-' *
			 ***********************/

			if (operands.size() < 2)
			{
				result.error = "Insufficient operands for subtraction '-' operation.";
				return result;
			}

			final String[] args2 = operands.pop();
			final String[] args1 = operands.pop();

			/* check that arguments are numeric */
			final int args1NonNumericIndex = getNonNumericIndex(args1);
			final int args2NonNumericIndex = getNonNumericIndex(args2);

			if ((args1NonNumericIndex >= 0) || (args2NonNumericIndex >= 0))
			{
				int nonNumericIndex;
				String nonNumericArg;

				if (args1NonNumericIndex >= 0)
				{
					nonNumericIndex = args1NonNumericIndex;
					nonNumericArg = args1[args1NonNumericIndex];
				}
				else
				{
					nonNumericIndex = args2NonNumericIndex;
					nonNumericArg = args2[args2NonNumericIndex];
				}

				result.error = "Invalid operand encountered for subtraction '-' operation: " +
						"The operand \"" + nonNumericArg + "\" corresponding to " +
						RenameWand.SINGULAR_NOUN + " \"" + files[nonNumericIndex].source.getPath() +
						"\" is non-numeric.";

				operators.push(op);
				operands.push(args1);
				operands.push(args2);
				return result;
			}

			String[] ans = new String[n];

			for (int i = 0; i < n; i++)
				ans[i] = (((int) Double.parseDouble(args1[i])) - ((int) Double.parseDouble(args2[i]))) + "";

			/* push answer on operand stack */
			operands.push(ans);
			result.success = true;
		}
		else if ("+".equals(op))
		{
			/********************
			 * (5) ADDITION '+' *
			 ********************/

			if (operands.size() < 2)
			{
				result.error = "Insufficient operands for addition '+' operation.";
				return result;
			}

			final String[] args2 = operands.pop();
			final String[] args1 = operands.pop();
			String[] ans = new String[n];

			/* check if arguments are numeric */
			final int args1NonNumericIndex = getNonNumericIndex(args1);
			final int args2NonNumericIndex = getNonNumericIndex(args2);

			if ((args1NonNumericIndex < 0) && (args2NonNumericIndex < 0))
			{
				/* add the two arguments */
				for (int i = 0; i < n; i++)
					ans[i] = (((int) Double.parseDouble(args1[i])) + ((int) Double.parseDouble(args2[i]))) + "";
			}
			else
			{
				/* concatenate the two arguments */
				for (int i = 0; i < n; i++)
					ans[i] = args1[i] + args2[i];
			}

			/* push answer on operand stack */
			operands.push(ans);
			result.success = true;
		}
		else if (":".equals(op))
		{
			/********************************************
			 * (6) RANGE OPERATOR FOR SUBSTRING "[ : ]" *
			 ********************************************/

			if (operands.size() < 2)
			{
				result.error = "Insufficient operands for substring range operator ':'.";
				return result;
			}

			final String[] args2 = operands.pop();
			final String[] args1 = operands.pop();
			String[] ans = new String[n];

			for (int i = 0; i < n; i++)
				ans[i] = args1[i] + ":" + args2[i];

			/* push answer on operand stack */
			operands.push(ans);
			result.success = true;
		}
		else if (",".equals(op))
		{
			/************************************************
			 * (7) DELIMITER OPERATOR FOR SUBSTRING "[ , ]" *
			 ************************************************/

			if (operands.size() < 2)
			{
				result.error = "Insufficient operands for substring delimiter operator ','.";
				return result;
			}

			final String[] args2 = operands.pop();
			final String[] args1 = operands.pop();
			String[] ans = new String[n];

			for (int i = 0; i < n; i++)
				ans[i] = args1[i] + "," + args2[i];

			/* push answer on operand stack */
			operands.push(ans);
			result.success = true;
		}
		else if ("~".equals(op))
		{
			/***************************************
			 * (8) UNARY MINUS (NEGATIVE) SIGN '~' *
			 ***************************************/

			if (operands.size() < 1)
			{
				result.error = "Insufficient operands for negative sign '-' operator.";
				return result;
			}

			final String[] args1 = operands.pop();

			/* check that argument is numeric */
			final int nonNumericIndex = getNonNumericIndex(args1);

			if (nonNumericIndex >= 0)
			{
				final String nonNumericArg = args1[nonNumericIndex];

				result.error = "Invalid operand encountered for negative sign '-' operator: " +
						"The operand \"" + nonNumericArg + "\" corresponding to " +
						RenameWand.SINGULAR_NOUN + " \"" + files[nonNumericIndex].source.getPath() +
						"\" is non-numeric.";

				operators.push(op);
				operands.push(args1);
				return result;
			}

			String[] ans = new String[n];

			for (int i = 0; i < n; i++)
				ans[i] = (-((int) Double.parseDouble(args1[i]))) + "";

			/* push answer on operand stack */
			operands.push(ans);
			result.success = true;

		}
		else if ("#".equals(op) ||
				"#!".equals(op) ||
				"##".equals(op) ||
				"##!".equals(op) ||
				"@".equals(op) ||
				"@!".equals(op) ||
				"@@".equals(op) ||
				"@@!".equals(op))
		{
			/*****************************
			 * (9) ENUMERATION OPERATORS *
			 *****************************/

			if (operands.size() < 1)
			{
				result.error = "Insufficient operands for enumeration operator " + op + ".";
				return result;
			}

			/* sort files, and enumerate them accordingly */

			final String[] args1 = operands.pop();

			/* global and local order */
			int[] globalOrder = new int[n];
			int[] localOrder = new int[n];

			/* directory counts */
			int[] dirCount = new int[RenameWand.numDirs + 1];
			Arrays.fill(dirCount, 0);

			if (getNonNumericIndex(args1) < 0)
			{
				/* treat arguments as doubles */
				DoubleEnumerationUnit[] eu = new DoubleEnumerationUnit[n];

				for (int i = 0; i < n; i++)
					eu[i] = new DoubleEnumerationUnit(i, Double.parseDouble(args1[i]));

				Arrays.sort(eu);

				for (int i = 0; i < n; i++)
				{
					final int index = eu[i].index;
					final int parentDirId = files[index].parentDirId;
					dirCount[parentDirId]++;
					globalOrder[index] = i + 1;
					localOrder[index] = dirCount[parentDirId];
				}
			}
			else
			{
				/* treat arguments as strings */
				StringEnumerationUnit[] eu = new StringEnumerationUnit[n];

				for (int i = 0; i < n; i++)
					eu[i] = new StringEnumerationUnit(i, args1[i]);

				Arrays.sort(eu);

				for (int i = 0; i < n; i++)
				{
					final int index = eu[i].index;
					final int parentDirId = files[index].parentDirId;
					dirCount[parentDirId]++;
					globalOrder[index] = i + 1;
					localOrder[index] = dirCount[parentDirId];
				}
			}

			String[] ans = new String[n];

			if ("#".equals(op))
			{
				/* directory ordering */
				for (int i = 0; i < n; i++)
					ans[i] = localOrder[i] + "";
			}
			else if ("#!".equals(op))
			{
				/* reverse directory ordering */
				for (int i = 0; i < n; i++)
					ans[i] = (files[i].localCount + 1 - localOrder[i]) + "";
			}
			else if ("##".equals(op))
			{
				/* global ordering */
				for (int i = 0; i < n; i++)
					ans[i] = globalOrder[i] + "";
			}
			else if ("##!".equals(op))
			{
				/* reverse global ordering */
				for (int i = 0; i < n; i++)
					ans[i] = (n + 1 - globalOrder[i]) + "";
			}
			else if ("@".equals(op))
			{
				/* first elements in directory ordering */
				int[] firsts = new int[RenameWand.numDirs + 1];

				for (int i = 0; i < n; i++)
					if (localOrder[i] == 1) firsts[files[i].parentDirId] = i;

				for (int i = 0; i < n; i++)
					ans[i] = args1[firsts[files[i].parentDirId]];
			}
			else if ("@!".equals(op))
			{
				/* last elements in directory ordering */
				int[] lasts = new int[RenameWand.numDirs + 1];

				for (int i = 0; i < n; i++)
					if (localOrder[i] == files[i].localCount) lasts[files[i].parentDirId] = i;

				for (int i = 0; i < n; i++)
					ans[i] = args1[lasts[files[i].parentDirId]];
			}
			else if ("@@".equals(op))
			{
				/* look for first element */
				int first = 0;

				for (int i = 0; i < n; i++)
					if (globalOrder[i] == 1) first = i;

				Arrays.fill(ans, args1[first]);
			}
			else if ("@@!".equals(op))
			{
				/* look for last element */
				int last = 0;

				for (int i = 0; i < n; i++)
					if (globalOrder[i] == n) last = i;

				Arrays.fill(ans, args1[last]);
			}

			/* push answer on operands stack */
			operands.push(ans);
			result.success = true;
		}
		else
		{
			/* error */
			result.error = "Unexpected operator '" + op + "' encountered.";
		}

		return result;
	}


	/**
	 * Determine sequence of rename operations to be performed, in order to rename
	 * the given matched files/directories.
	 *
	 * @param matchedFiles
	 *     Matched files/directories to be renamed
	 * @return
	 *     Rename file/directory pairs indicating sequence of rename operations
	 */
	private static List<RenameFilePair> getRenameOperations(
			final List<FileUnit> matchedFiles)
	{
		/* determine target files, check validity, and detect clashes */
		final Map<File,FileUnit> targetFilesMap = new TreeMap<File,FileUnit>();

		for (FileUnit u : matchedFiles)
		{
			final String targetFilename = u.targetFilename.toString();

			/* check for empty file/directory name */
			if (targetFilename.isEmpty())
				throw new TerminatingException("Invalid target " + RenameWand.SINGULAR_NOUN + " name encountered:\n" +
						"\"" + u.source.getPath() + "\" ---> \"" + targetFilename + "\"");

			if (targetFilename.contains(File.separator))
			{
				/* contains a filename separator, so we rename          */
				/* file/directory relative to the present work directory */
				u.target = new File(targetFilename);
			}
			else
			{
				/* does not contain filename separator, so we        */
				/* rename file/directory in its original subdirectory */
				u.target = new File(u.source.getParentFile(), targetFilename);
			}

			try
			{
				/* get canonical pathname */
				u.target = new File(u.target.getCanonicalFile().getParentFile(),
						u.target.getName());
			}
			catch (Exception e)
			{
				throw new TerminatingException("Invalid target " + RenameWand.SINGULAR_NOUN + " name encountered:\n" +
						"\"" + u.source.getPath() + "\" ---> \"" + targetFilename + "\"\n" +
						getExceptionInformation(e));
			}

			/* check for clash (i.e. nonunique target filenames) */
			final FileUnit w = targetFilesMap.get(u.target);

			if (w == null)
			{
				targetFilesMap.put(u.target, u);
			}
			else
			{
				throw new TerminatingException("Target " + RenameWand.SINGULAR_NOUN + " name clash:\n" +
					"[A] \"" + w.source.getPath() + "\"\n  ---> \"" + w.target.getPath() + "\"\n" +
					"[B] \"" + u.source.getPath() + "\"\n  ---> \"" + u.target.getPath() + "\"");
			}
		}

		/* create (source,target) rename pairs, and determine renaming sequence */
		final NavigableMap<File,LinkedList<RenameFilePair>> sequenceHeads = new TreeMap<File,LinkedList<RenameFilePair>>();
		final NavigableMap<File,LinkedList<RenameFilePair>> sequenceTails = new TreeMap<File,LinkedList<RenameFilePair>>();

		for (FileUnit u : matchedFiles)
		{
			/* check for unnecessary rename operations */
			if (u.source.getPath().equals(u.target.getPath()))
				continue;

			/* look for a sequence head with source = this target */
			final LinkedList<RenameFilePair> headSequence = sequenceHeads.get(u.target);

			/* look for a sequence tail with target = this source */
			final LinkedList<RenameFilePair> tailSequence = sequenceTails.get(u.source);

			if ((headSequence == null) && (tailSequence == null))
			{
				/* add this pair as a new sequence */
				final LinkedList<RenameFilePair> ns = new LinkedList<RenameFilePair>();
				ns.add(new RenameFilePair(u.source, u.target));
				sequenceHeads.put(u.source, ns);
				sequenceTails.put(u.target, ns);

			}
			else if ((headSequence != null) && (tailSequence == null))
			{
				/* add this pair to the head of an existing sequence */
				headSequence.addFirst(new RenameFilePair(u.source, u.target));
				sequenceHeads.remove(u.target);
				sequenceHeads.put(u.source, headSequence);
			}
			else if ((headSequence == null) && (tailSequence != null))
			{
				/* add this pair to the tail of an existing sequence */
				tailSequence.addLast(new RenameFilePair(u.source, u.target));
				sequenceTails.remove(u.source);
				sequenceTails.put(u.target, tailSequence);
			}
			else if ((headSequence != null) && (tailSequence != null))
			{
				if (headSequence == tailSequence)
				{
					/* loop detected, so we use a temporary target file/directory name */

					/* create a temporary file/directory name */
					final File parentDir = u.target.getParentFile();
					final String filename = u.target.getName();

					File temp =  new File(parentDir, filename + ".rw");

					if (temp.exists() || targetFilesMap.containsKey(temp))
					{
						/* temp filename is already used; find another temp filename */
						for (long i = 0; i < Long.MAX_VALUE; i++)
						{
							temp = new File(parentDir, filename + ".rw." + i);

							if (temp.exists() || targetFilesMap.containsKey(temp))
							{
								/* temp filename is already used; find another temp filename */
								temp = null;
							}
							else
							{
								/* found an unused name */
								targetFilesMap.put(temp, null);
								break;
							}
						}
					}

					if (temp == null)
						throw new TerminatingException("Ran out of suffixes for temporary name of " +
								RenameWand.SINGULAR_NOUN + " \"" + u.target.getPath() + "\".");

					/* add a leading and trailing rename file pair to the existing sequence */
					headSequence.addFirst(new RenameFilePair(temp, u.target));
					tailSequence.addLast(new RenameFilePair(u.source, temp));
					sequenceHeads.remove(u.target);
					sequenceHeads.put(temp, headSequence);
					sequenceTails.remove(u.source);
					sequenceTails.put(temp, tailSequence);
				}
				else
				{
					/* link two distinct sequences together */
					tailSequence.addLast(new RenameFilePair(u.source, u.target));
					tailSequence.addAll(headSequence);
					sequenceHeads.remove(u.target);
					sequenceTails.remove(u.source);
					sequenceTails.put(tailSequence.peekLast().target, tailSequence);
				}
			}
		}

		/* return value */
		final List<RenameFilePair> renameOperations = new ArrayList<RenameFilePair>();

		/* sequence deeper subdirectories for renaming first (approx), if renaming directories */
		final NavigableMap<File,LinkedList<RenameFilePair>> sequences =
				RenameWand.renameDirectories ? sequenceHeads.descendingMap() : sequenceHeads;

		for (LinkedList<RenameFilePair> s : sequences.values())
		{
			/* get reversed order of rename file pairs within the sequence */
			final Iterator<RenameFilePair> iter = s.descendingIterator();

			while (iter.hasNext())
			{
				final RenameFilePair r = iter.next();

				if (!r.source.getPath().equals(r.target.getPath()))
					renameOperations.add(r);
			}
		}

		return renameOperations;
	}


	/**
	 * Print the matched files/directories and their target names, and
	 * prompt the user on whether to proceed with the renaming operations.
	 *
	 * @param matchedFiles
	 *     Matched files to be renamed
	 * @param numRenameOperations
	 *     Number of rename operations to be performed
	 * @return
	 *     True if proceeding with rename; false otherwise
	 */
	private static boolean promptUserOnRename(
			final List<FileUnit> matchedFiles,
			final int numRenameOperations)
	{
		/* categorize files/directories by their subdirectory */
		final List<List<FileUnit>> subdirs = new ArrayList<List<FileUnit>>();

		subdirs.add(0, null);

		for (int parentDirId = 1; parentDirId <= RenameWand.numDirs; parentDirId++)
			subdirs.add(parentDirId, new ArrayList<FileUnit>());

		for (FileUnit u : matchedFiles)
			subdirs.get(u.parentDirId).add(u);

		/* subdirectory counter */
		int subdirCount = 0;

		/* file/directory counter */
		int fileCount = 0;

		for (List<FileUnit> subdir : subdirs)
		{
			if ((subdir == null) || subdir.isEmpty())
				continue;

			subdirCount++;

			final String subdirPath = subdir.get(0).source.getParent();

			RenameWand.stdout.print("\n\nSUBDIRECTORY: \"" +
					(subdirPath.endsWith(File.separator) ? subdirPath : (subdirPath + File.separator)) + "\"");

			for (FileUnit u : subdir)
			{
				RenameWand.stdout.print("\n["+ (++fileCount) +"] " +
						"\"" + u.source.getName() + "\" ---> \"" + u.targetFilename + "\"");
			}
		}

		/* number of files/directories to rename */
		final int n = matchedFiles.size();

		RenameWand.stdout.print("\n\n" + numRenameOperations + " " +
				((numRenameOperations == 1) ? "operation" : "operations") +
				" required to rename the above " +
				((n == 1) ? RenameWand.SINGULAR_NOUN : (n + " " + RenameWand.PLURAL_NOUN)) +
				" in " + subdirCount + " " +
				((subdirCount == 1) ? "subdirectory" : "subdirectories") + ".\n");
		RenameWand.stdout.flush();

		/* no rename operations to perform */
		if (numRenameOperations == 0)
			return false;

		/* prompt user on whether to continue with renaming operations */
		char choice = '\0';

		if (RenameWand.simulateOnly)
		{
			choice =  'Y';
		}
		else if (RenameWand.automaticRename)
		{
			RenameWand.stdout.print("Proceed to rename " +
					((n == 1) ? RenameWand.SINGULAR_NOUN : RenameWand.PLURAL_NOUN) +
					"? (Y)es/(N)o: Y");
			RenameWand.stdout.flush();

			choice =  'Y';
		}
		else
		{
			choice = UserIO.userCharPrompt("Proceed to rename " +
					((n == 1) ? RenameWand.SINGULAR_NOUN : RenameWand.PLURAL_NOUN) +
					"? (Y)es/(N)o: ",
					"YN");
		}

		return (choice == 'Y');
	}


	/**
	 * Perform rename operations on files/directories.
	 *
	 * @param renameOperations
	 *     Sequence of rename operations to be performed
	 * @return
	 *     Number of successful rename operations performed
	 */
	private static int performRenameOperations(
			final List<RenameFilePair> renameOperations)
	{
		if (RenameWand.simulateOnly)
		{
			RenameWand.stdout.print("\n\nSimulating renaming of " +
					((renameOperations.size() == 1) ? RenameWand.SINGULAR_NOUN : RenameWand.PLURAL_NOUN) +
					"...");
			RenameWand.stdout.flush();
		}
		else
		{
			RenameWand.stdout.print("\n\nRenaming " +
					((renameOperations.size() == 1) ? RenameWand.SINGULAR_NOUN : RenameWand.PLURAL_NOUN) +
					"...");
			RenameWand.stdout.flush();
		}

		for (int i = 0; i < renameOperations.size(); i++)
		{
			final RenameFilePair r = renameOperations.get(i);

			RenameWand.stdout.print("\n[R" + (i + 1) + "] "+
					"\"" + r.source.getPath() + "\"\n  ---> \"" + r.target.getPath() + "\"");
			RenameWand.stdout.flush();

			/* if simulating, just continue to the next rename operation */
			if (RenameWand.simulateOnly)
				continue;

			/* check for existing distinct target file/directory */
			if (r.target.exists() && !r.target.equals(r.source))
			{
				r.success = false;

				RenameWand.stdout.print("\nRename operation failed: A " +
						(r.target.isDirectory() ? "directory" : "file") +
						" of the same target name already exists.\n");
				RenameWand.stdout.flush();
			}
			else
			{
				r.target.getParentFile().mkdirs();
				r.success = r.source.renameTo(r.target);

				if (!r.success)
				{
					RenameWand.stdout.print("\nRename operation failed. ");
					RenameWand.stdout.flush();
				}
			}

			/* check if renaming operation was successful */
			if (!r.success)
			{
				/* prompt user on action */
				char choice = '\0';

				if (RenameWand.defaultActionOnRenameOperationError == '\0')
				{
					if (RenameWand.automaticRename)
					{
						RenameWand.stdout.print("(R)etry/(S)kip/(U)ndo all/(A)bort: U\n");
						RenameWand.stdout.flush();

						choice = 'U';
					}
					else
					{
						choice = UserIO.userCharPrompt("(R)etry/(S)kip/(U)ndo all/(A)bort: ", "RSUA");
					}
				}
				else
				{
					/* use default action */
					choice = RenameWand.defaultActionOnRenameOperationError;
				}

				/* take action */
				if (choice == 'R')
				{
					/* retry rename operation */
					i--;
				}
				else if (choice == 'S')
				{
					/* skip to next file/directory */
					continue;
				}
				else if (choice == 'U')
				{
					/* undo all previous rename operations */
					RenameWand.stdout.print("\nUndoing previous " + RenameWand.SINGULAR_NOUN + " rename operations...");
					RenameWand.stdout.flush();

					for (int j = i - 1; j >= 0; j--)
					{
						final RenameFilePair t = renameOperations.get(j);

						if (t.success)
						{
							RenameWand.stdout.print("\n[R" + (j + 1) + "] "+
									"\"" + t.source.getPath() + "\"\n  <--- \"" + t.target.getPath() + "\"");
							RenameWand.stdout.flush();

							t.source.getParentFile().mkdirs();
							t.success = !t.target.renameTo(t.source);

							if (t.success)
								reportWarning("Rename operation failed.");
						}
					}

					break;
				}
				else if (choice == 'A')
				{
					/* abort */
					break;
				}
			}
		}

		/* return value */
		int numRenameOperationsPerformed = 0;

		for (RenameFilePair r : renameOperations)
			if (r.success) numRenameOperationsPerformed++;

		return numRenameOperationsPerformed;
	}


	/**
	 * Pad the given string so that it is at least the specified number of
	 * character long. If the string is numeric, pad it with leading zeros;
	 * otherwise, pad it with trailing spaces.
	 *
	 * @param in
	 *     String to be padded
	 * @param len
	 *     Desired length of padded string
	 * @param isNumeric
	 *     Indicates if given string is to be treated as numeric
	 * @return
	 *     The padded string
	 */
	private static String padString(
			final String in,
			final int len,
			final boolean isNumeric)
	{
		/* return value */
		final StringBuilder out = new StringBuilder();

		/* number of additional characters to insert */
		final int padLen = len - in.length();

		if (isNumeric)
		{
			/* pad with leading zeros */
			final Matcher numericMatcher = RenameWand.NUMERIC_PATTERN.matcher(in);

			if (numericMatcher.matches())
			{
				/* match groups */
				final String sign = numericMatcher.group(1);
				final String val = numericMatcher.group(2);

				if (sign != null)
					out.append(sign);

				for (int i = 0; i < padLen; i++)
					out.append('0');

				out.append(val);
				return out.toString();
			}
		}

		/* pad with trailing spaces */
		out.append(in);

		for (int i = 0; i < padLen; i++)
			out.append(' ');

		return out.toString();
	}


	/**
	 * Return true if the given string is numeric; false otherwise.
	 *
	 * @param arg
	 *     String to be tested
	 * @return
	 *     True if the given string is numeric; false otherwise
	 */
	private static boolean isNumeric(
			final String arg)
	{
		/* check if argument is numeric */

		/* check if empty string */
		if (arg.isEmpty())
			return false;

		/* check if string matches numeric pattern */
		if (!RenameWand.NUMERIC_PATTERN.matcher(arg).matches())
			return false;

		/* argument is numeric */
		return true;
	}


	/**
	 * Return -1 if all the strings in the given array are numeric;
	 * otherwise, return the index of a non-numeric string.
	 *
	 * @param arg
	 *     Strings to be tested
	 * @return
	 *     -1 if all the strings in the given array are numeric;
	 *     otherwise, return the index of a non-numeric string
	 */
	private static int getNonNumericIndex(
			final String[] args)
	{
		/* check if all arguments are numeric */
		for (int i = 0; i < args.length; i++)
		{
			/* check if empty string */
			if (args[i].isEmpty())
				return i;

			/* check if string matches numeric pattern */
			if (!RenameWand.NUMERIC_PATTERN.matcher(args[i]).matches())
				return i;
		}

		/* all strings are numeric */
		return -1;
	}


	/**
	 * Print a warning message and pause.
	 *
	 * @param message
	 *     Warning message to be printed on issuing the warning
	 */
	private static void reportWarning(
			final Object message)
	{
		RenameWand.reportNumWarnings++;

		if (RenameWand.ignoreWarnings)
		{
			RenameWand.stderr.print("\n\nWARNING: " + message + "\n");
			RenameWand.stderr.flush();
		}
		else
		{
			RenameWand.stderr.print("\n\nWARNING: " + message + "\nPress ENTER to continue...");
			RenameWand.stderr.flush();

			(new Scanner(System.in)).nextLine(); // blocks until user responds
		}
	}


	/**
	 * Get custom exception information string for the given exception.
	 * String contains the exception class name, error description string,
	 * and stack trace.
	 *
	 * @param e
	 *     Exception for which to generate the custom exception information string
	 * @return
	 *     Custom exception information string
	 */
	private static String getExceptionInformation(
			final Exception e)
	{
		final StringBuilder s = new StringBuilder();

		s.append("\nJava exception information (" + e.getClass() +
				"):\n\"" + e.getMessage() + "\"");

		for (StackTraceElement t : e.getStackTrace())
		{
			s.append("\n  at ");
			s.append(t.toString());
		}

		s.append('\n');
		return s.toString();
	}


	/**
	 * Print out usage syntax, notes, and comments.
	 */
	private static void printUsage()
	{
		/* RULER   00000000011111111112222222222333333333344444444445555555555666666666677777777778 */
		/* RULER   12345678901234567890123456789012345678901234567890123456789012345678901234567890 */
		RenameWand.stdout.print("\n" +
				"\nRenameWand is a simple command-line utility for renaming files or directories" +
				"\nusing an intuitive but powerful syntax." +
				"\n" +
				"\nUSAGE:" +
				"\n" +
				"\njava -jar RenameWand.jar  <switches>  [\"SourcePattern\"]  [\"TargetPattern\"]" +
				"\n" +
				"\nFor each file in the current directory with a name that matches" +
				"\n[\"SourcePattern\"], rename it to [\"TargetPattern\"]. Patterns should be in" +
				"\nquotes so that the shell passes them to RenameWand correctly." +
				"\nThe user is prompted before files are renamed, and whenever errors occur." +
				"\nFile rename operations are sequenced so that they are conflict-free," +
				"\nand temporary filenames are automatically used when necessary." +
				"\n" +
				"\n<Switches>:" +
				"\n" +
				"\n -r, --recurse         Recurse into subdirectories" +
				"\n -d, --dirs            Rename directories instead of files" +
				"\n -p, --path            Match [\"SourcePattern\"] against relative pathnames" +
				"\n                        (e.g. \"2007\\Jan\\Report.txt\" instead of \"Report.txt\")" +
				"\n -l, --lower           Match [\"SourcePattern\"] against lower case names" +
				"\n                        (e.g. \"HelloWorld2007.JPG\" ---> \"helloworld2007.jpg\")" +
				"\n -y, --yes             Automatically rename files without prompting" +
				"\n -s, --simulate        Simulate only; do not actually rename files" +
				"\n -i, --ignorewarnings  Ignore warnings; do not pause" +
				"\n     --skip            Skip files that cannot be renamed successfully" +
				"\n     --undoall         Undo all previous renames when a file cannot be renamed" +
				"\n                        successfully" +
				"\n     --abort           Abort subsequent renames when a file cannot be renamed" +
				"\n                        successfully" +
				"\n" +
				"\n[\"SourcePattern\"]:" +
				"\n" +
				"\n Files with names matching [\"SourcePattern\"] are renamed. This pattern string" +
				"\n may contain literal (i.e. ordinary) characters and the following constructs" +
				"\n (more details below):" +
				"\n" +
				"\n  1. Glob patterns and wildcards, e.g. *, ?, [ ], { }" +
				"\n  2. Register capture groups, e.g. <a>, <5|song>, <2|@track>" +
				"\n  3. Special construct <...> that may involve macros," +
				"\n      e.g. <FN.parent>, <CT.date>, <FS+FT.yyyy>" +
				"\n" +
				"\n To use a construct symbol (e.g. [, {, ?) as a literal character, insert a" +
				"\n backslash before it, e.g. use \\[ for the literal character [." +
				"\n Use \\\\ for the literal backslash character \\." +
				"\n" +
				"\n The file separator in Windows can be specified by \\\\ or /." +
				"\n" +
				"\n[\"TargetPattern\"]:" +
				"\n" +
				"\n The target names of the matched files are specified by [\"TargetPattern\"]." +
				"\n This pattern string may contain literal (i.e. ordinary) characters and the" +
				"\n special construct <...> that may involve registers and macros (more details" +
				"\n below)." +
				"\n" +
				"\n To use a construct symbol (e.g. <, >) as a literal character, insert a" +
				"\n backslash before it, e.g. use \\< for the literal character <." +
				"\n Use \\\\ for the literal backslash character \\." +
				"\n" +
				"\n The file separator in Windows can be specified by \\\\ or /." +
				"\n" +
				"\n If the evaluated pattern string contains a file separator (e.g. / or \\)," +
				"\n then the target name is resolved with respect to the current directory;" +
				"\n otherwise, the target name shares the same parent directory as the source" +
				"\n file (i.e. the source is renamed \"in place\")." +
				"\n" +
				"\nGLOB PATTERNS AND WILDCARDS" +
				"\n" +
				"\n The four common glob patterns are supported in [\"SourcePattern\"]:" +
				"\n" +
				"\n  *    Match a string of 0 or more characters" +
				"\n  ?    Match exactly 1 character" +
				"\n [ ]   Match exactly 1 character inside the brackets:" +
				"\n         [abc]       Match a, b, or c" +
				"\n         [!abc]      Match any character except a, b, or c (negation)" +
				"\n         [a-z0-9]    Match any character a through z, or 0 through 9," +
				"\n                      inclusive (range)" +
				"\n { }   Match exactly 1 comma-delimited string inside the braces:" +
				"\n         {a,bc,def}  Match either a, bc, or def" +
				"\n" +
				"\nREGISTER CAPTURE GROUPS" +
				"\n" +
				"\n Register capture groups are used in [\"SourcePattern\"] to capture a string of" +
				"\n zero or more characters. A register name can contain only letters, digits, or" +
				"\n underscores, but cannot begin with a digit." +
				"\n" +
				"\n  <myreg>     Capture a string of 0 or more characters" +
				"\n  <@myreg>    Capture a string of 0 or more digits" +
				"\n  <n|myreg>   Capture a string of exactly n characters" +
				"\n  <n|@myreg>  Capture a string of exactly n digits" +
				"\n" +
				"\n Backreferences can be applied by reusing the register name, e.g." +
				"\n \"<3|myreg><myreg>.txt\" matches a filename that is a repetition of a" +
				"\n three-character string, followed by the txt extension." +
				"\n" +
				"\nSPECIAL CONSTRUCTS" +
				"\n" +
				"\n Special constructs are supported in both [\"SourcePattern\"] and" +
				"\n [\"TargetPattern\"]. In general, they take the form <length|expr>, where the" +
				"\n expressions \"length\" and \"expr\" can involve registers, macros, and use" +
				"\n operations involving arithmetic, substrings, and enumerations (more details" +
				"\n below). Parentheses ( ) can be used to group values." +
				"\n" +
				"\n  <expr>         Insert the evaluated expression \"expr\" using as many" +
				"\n                  characters as necessary" +
				"\n  <length|expr>  Insert the evaluated expression \"expr\" with padding:" +
				"\n                  If the evaluated expression is numeric, pad the number with" +
				"\n                  leading zeros so that it occupies at least \"length\"" +
				"\n                  characters;" +
				"\n                  if the evaluated expression is non-numeric, pad the string" +
				"\n                  with trailing spaces so that it occupies at least \"length\"" +
				"\n                  characters." +
				"\n" +
				"\nARITHMETIC OPERATIONS" +
				"\n" +
				"\n The standard arithmetic operations + - * / ^ are supported for numeric values;" +
				"\n for non-numeric strings, + is interpreted as concatenation. The standard rules" +
				"\n of operator precedence are observed. Values are automatically cast as integers" +
				"\n before and after every operation." +
				"\n" +
				"\n For example, <a+b/(c-d)^(e*f)> evaluates an arithmetic expression involving" +
				"\n registers a, b, c, d, e, and f." +
				"\n" +
				"\nSUBSTRING OPERATIONS" +
				"\n" +
				"\n Substrings can be extracted from any value by inserting a comma-delimited list" +
				"\n of indices and index ranges in brackets [ ] after the value. Index 1 denotes" +
				"\n the 1st character, index 2 the 2nd character, and so on." +
				"\n" +
				"\n Negative indices denote character positions counting from the end of the" +
				"\n string, i.e. index -1 denotes the last character, index -2 the second-last" +
				"\n character, and so on." +
				"\n" +
				"\n Index ranges are denoted using three parameters, e.g. 1:2:11 is equivalent to" +
				"\n indices 1, 3, 5, ..., 11. The middle parameter is optional; it is assumed to" +
				"\n be 1 if missing, e.g. 5:8 is equivalent to indices 5, 6, 7, 8." +
				"\n" +
				"\n  myreg[1,5,3]   Extract the 1st, 5th, and 3rd characters, in that order" +
				"\n  myreg[2:6,1]   Extract the 2nd through 6th characters, followed by the 1st" +
				"\n                  character, in that order" +
				"\n  myreg[1:2:-1]  Extract the 1st, 3rd, 5th, ... characters" +
				"\n  myreg[-1:1]    Extract the last through first characters" +
				"\n                  (effectively reverses the string)" +
				"\n" +
				"\n Indices are automatically clipped if they are too big or small, e.g. if" +
				"\n myreg is a 3-character string, then myreg[10] is equivalent to myreg[3]." +
				"\n" +
				"\nENUMERATION OPERATIONS" +
				"\n" +
				"\n Matched files can be sorted by any value, and then numbered in sequence." +
				"\n Numerical sorting is applied if all the values are numeric; otherwise," +
				"\n lexicographic sorting is applied." +
				"\n" +
				"\n The 1st file in the sorted sequence is assigned the number 1, the 2nd is" +
				"\n assigned number 2, and so on; ties are broken arbitrarily." +
				"\n" +
				"\n  #myreg   Return the sequence number of the respective file when sorted in" +
				"\n            ascending order of the value in register myreg" +
				"\n  #!myreg  Return the sequence number of the respective file when sorted in" +
				"\n            descending order of the value in register myreg" +
				"\n  @myreg   Return the value in register myreg of the first file in the" +
				"\n            sequence sorted in ascending order" +
				"\n  @!myreg  Return the value in register myreg of the first file in the" +
				"\n            sequence sorted in descending order" +
				"\n" +
				"\n The above operations apply to matched files enumerated locally within their" +
				"\n respective subdirectories; to enumerate all matched files globally (i.e. when" +
				"\n using the --recurse switch), use ## instead of #, and @@ instead of @." +
				"\n" +
				"\n For example, to enumerate matched files locally and globally by their" +
				"\n last-modified time, we can use the target pattern string" +
				"\n \"Local <2|#FT> of <2|RW.N> -vs- Global <2|##FT> of <2|RW.NN>.txt\"," +
				"\n where macros FT, RW.N, and RW.NN represent the last-modified time of the file," +
				"\n the number of local matched files, and the number of global matched files," +
				"\n respectively (more details below)." +
				"\n" +
				"\n Arbitrary values such as arithmetic expressions and substrings can also be" +
				"\n used for enumeration, e.g. <#(a*(b+c))>, <#(FN.name[1:3])>." +
				"\n" +
				"\nMACROS" +
				"\n" +
				"\n Macros are defined for a variety of file and system attributes, such as" +
				"\n file name, file size, file last-modified time, current time," +
				"\n system environment variables, and system properties." +
				"\n" +
				"\n FILE NAME" +
				"\n" +
				"\n  Example: \"C:\\Work\\2007\\Jan\\Report.txt\", with \"C:\\Work\" as current directory" +
				"\n" +
				"\n  FN              Filename (\"Report.txt\")" +
				"\n  FN.ext          File extension (\"txt\")" +
				"\n  FN.name         Base filename without extension (\"Report\")" +
				"\n  FN.path         Relative pathname (\"2007\\Jan\\Report.txt\")" +
				"\n  FN.parent       Parent directory (\"Jan\")" +
				"\n  FN.parentpath   Relative pathname of parent directory (\"2007\\Jan\")" +
				"\n" +
				"\n FILE SIZE (all values are cast as integers)" +
				"\n" +
				"\n  FS              File size in bytes" +
				"\n  FS.kB           File size in kilobytes (2^10 bytes)" +
				"\n  FS.MB           File size in megabytes (2^20 bytes)" +
				"\n  FS.GB           File size in gigabytes (2^30 bytes)" +
				"\n  FS.TB           File size in terabytes (2^40 bytes)" +
				"\n" +
				"\n FILE LAST-MODIFIED TIME" +
				"\n" +
				"\n  FT              Number of milliseconds since the epoch" +
				"\n                   (January 1, 1970 00:00:00.000 GMT, Gregorian)" +
				"\n  FT.date         Date in the form yyyyMMdd" +
				"\n  FT.time         Time in the form HHmmss" +
				"\n  FT.ap           am/pm in lower case" +
				"\n  FT.AP           AM/PM in upper case" +
				"\n" +
				"\n  Date and time pattern letters from Java are also supported." +
				"\n  Repeat letters to change the representation, e.g. FT.MMMM, FT.MMM," +
				"\n  FT.MM could represent \"April\", \"Apr\", \"4\", respectively." +
				"\n" +
				"\n    G  Era designator            H  Hour in day (0-23)" +
				"\n    y  Year                      k  Hour in day (1-24)" +
				"\n    M  Month in year             K  Hour in AM/PM (0-11)" +
				"\n    w  Week in year              h  Hour in AM/PM (1-12)" +
				"\n    W  Week in month             m  Minute in hour" +
				"\n    D  Day in year               s  Second in minute" +
				"\n    d  Day in month              S  Millisecond" +
				"\n    F  Day of week in month      z  Time zone (general time zone)" +
				"\n    E  Day in week               Z  Time zone (RFC 822 time zone)" +
				"\n    a  AM/PM marker" +
				"\n" +
				"\n CURRENT TIME" +
				"\n" +
				"\n  Macros for the current time are obtained by using CT instead of FT in the" +
				"\n  above macros for file last-modified time." +
				"\n" +
				"\n SYSTEM ENVIRONMENT VARIABLES" +
				"\n" +
				"\n  ENV.var   System environment variable named \"var\"" +
				"\n" +
				"\n SYSTEM PROPERTIES (see Java API for the full list)" +
				"\n" +
				"\n  SYS.os.name         Operating system name" +
				"\n  SYS.os.arch         Operating system architecture" +
				"\n  SYS.os.version      Operating system version" +
				"\n  SYS.file.separator  File separator (e.g. \"/\" or \"\\\")" +
				"\n  SYS.path.separator  Path separator (e.g. \":\" or \";\")" +
				"\n  SYS.line.separator  Line separator" +
				"\n  SYS.user.name       User's account name" +
				"\n  SYS.user.home       User's home directory" +
				"\n  SYS.user.dir        User's current working directory" +
				"\n" +
				"\n MISCELLANEOUS" +
				"\n" +
				"\n  RW.cd      Full pathname of the current directory (e.g. \"C:\\Work\")" +
				"\n  RW.N       Number of local matched files (i.e. in the file's subdirectory)" +
				"\n  RW.NN      Number of global matched files (i.e. in all subdirectories)" +
				"\n  RW.random  Generate a string of 10 random digits" +
				"\n" +
				"\nMACRO & REGISTER MODIFIERS" +
				"\n" +
				"\n To use the following modifiers, append a period followed by the modifier name" +
				"\n to a macro or register name. Modifiers can also be chained, e.g." +
				"\n myreg.title.trim, FN.name.pascal.trim." +
				"\n" +
				"\n  Example: \"hello WORLD\"" +
				"\n" +
				"\n  len             Length of the string (11)" +
				"\n  upper           Convert to upper case (\"HELLO WORLD\")" +
				"\n  lower           Convert to lower case (\"hello world\")" +
				"\n  capitalize      Capitalize only the first character (\"Hello world\")" +
				"\n  title           Convert to title case (\"Hello World\")" +
				"\n  camel           Convert to camelCase (\"helloWorld\")" +
				"\n  pascal          Convert to PascalCase (\"HelloWorld\")" +
				"\n  swapcase        Swap the case (\"HELLO world\")" +
				"\n  abbrev          Abbreviate to initials (\"h W\")" +
				"\n  reverse         Reverse the string (\"DLROW olleh\")" +
				"\n  trim            Trim away whitespace on the left and right" +
				"\n  ltrim           Trim away whitespace on the left" +
				"\n  rtrim           Trim away whitespace on the right" +
				"\n  delspace        Delete whitespace in the string (\"helloWORLD\")" +
				"\n  delextraspace   Delete extra whitespace by replacing contiguous whitespace" +
				"\n                   with a single space (\"How   are  YOU\" ---> \"How are YOU\")" +
				"\n  delpunctuation  Delete punctuation marks in the string" +
				"\n  spaceout        Space out words by inserting a space between connected words" +
				"\n                   (\"HowAreYou\" ---> \"How Are You\")" +
				"\n" +
				"\n Shortcut macros are defined for single-letter register names for convenience." +
				"\n Suppose we have the register \"a\"; then the following macros are automatically" +
				"\n defined if there are no name clashes:" +
				"\n" +
				"\n  AA  Convert to upper case (.upper)" +
				"\n  aa  Convert to lower case (.lower)" +
				"\n  Aa  Convert to title case (.title)" +
				"\n  aA  Swap the case (.swapcase)" +
				"\n" +
				"\nEXAMPLES:" +
				"\n" +
				"\n1. Convert the filename, less extension, to title case," +
				"\n    e.g. \"foo bar.txt\" ---> \"Foo Bar.txt\":" +
				"\n   java -jar RenameWand.jar \"<a>.<b>\" \"<Aa>.<b>\"" +
				"\n" +
				"\n2. Convert disc and track numbers to a single number, and swap artist name" +
				"\n    with song name," +
				"\n    e.g. \"Disc 2 Track 5_SONG_ARTIST.mp3\" --->" +
				"\n    \"015-Artist-Song.mp3\":" +
				"\n   java -jar RenameWand.jar \"Disc <@disc> Track <@track>_<song>_<artist>.mp3\"" +
				"\n    \"<3|10*(disc-1)+track>-<artist.title>-<song.title>.mp3\"" +
				"\n" +
				"\n3. Insert the file date, and enumerate files by their last-modified time," +
				"\n    e.g. \"SCAN004001.jpg\" ---> \"doc20050512 (Page 01 of 42).jpg\":" +
				"\n   java -jar RenameWand.jar \"SCAN*.jpg\"" +
				"\n    \"doc<FT.date> (Page <2|#FT> of <2|RW.N>).jpg\"" +
				"\n" +
				"\n4. Rename files into directories based on their names," +
				"\n    e.g. \"Daily Report May-28-2007.doc\" ---> \"2007/May/Daily Report 28.doc\":" +
				"\n   java -jar RenameWand.jar \"Daily Report <month>-<day>-<year>.doc\"" +
				"\n    \"<year>/<month>/Daily Report <day>.doc\"" +
				"\n\n");
	}
}
