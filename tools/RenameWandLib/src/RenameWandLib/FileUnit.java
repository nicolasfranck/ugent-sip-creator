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

package RenameWandLib;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


/**
 * Represent a (candidate) matched file/directory.
 */
public class FileUnit
{
	/** source file/directory (absolute and canonical abstract pathname) */
	File source = null;

	/** parent directory identifier */
	int parentDirId = -1;

	/** global file/directory count */
	int globalCount = -1;

	/** local file/directory count */
	int localCount = -1;

	/** register values */
	String[] registerValues = null;

	/** target file/directory name */
	StringBuilder targetFilename = null;

	/** target file/directory (absolute and canonical abstract pathname) */
	File target = null;



        /*
         * Nicolas Franck
         */
        RenameWand renamer = null;

        public RenameWand getRenamer() {
            return renamer;
        }

        public FileUnit(RenameWand renamer){
            this.renamer = renamer;
        }

	/**
	 * Evaluate the specified macro, by returning the value of the specified
	 * macro or assigned register corresponding to this file/directory.
	 *
	 * @param macro
	 *     Macro or register name
	 * @return
	 *     String representation of the macro or assigned register value;
	 *     null if macro or register is invalid or unassigned
	 */
	String evaluateMacro(
			final String macro)
	{
		/************************
		 * (1) FILE NAME MACROS *
		 ************************/

		/* Example: "C:\Work\2007\Jan\Report.txt", with "C:\Work" as current directory */

		/* Filename ("Report.txt") */
		if ("FN".equals(macro))
		{
			String name = this.source.getName();

			/* trim off trailing separator */
			while (name.endsWith(File.separator))
				name = name.substring(0, name.length() - File.separator.length());

			return name;
		}

		/* File extension ("txt") */
		if ("FN.ext".equals(macro))
		{
			String name = this.source.getName();

			/* trim off trailing separator */
			while (name.endsWith(File.separator))
				name = name.substring(0, name.length() - File.separator.length());

			final int i = name.lastIndexOf(".");

			if (i < 0)
			{
				return "";
			}
			else
			{
				return name.substring(i + 1);
			}
		}

		/* Base filename without extension ("Report") */
		if ("FN.name".equals(macro))
		{
			String name = this.source.getName();

			/* trim off trailing separator */
			while (name.endsWith(File.separator))
				name = name.substring(0, name.length() - File.separator.length());

			final int i = name.lastIndexOf(".");

			if (i < 0)
			{
				return name;
			}
			else
			{
				return name.substring(0, i);
			}
		}

		/* Relative pathname ("2007\Jan\Report.txt") */
		if ("FN.path".equals(macro))
		{
			String path = this.source.getPath();

			if (path.startsWith(renamer.currentDirectoryFullPathname))
				path = path.substring(renamer.currentDirectoryFullPathname.length());

			/* trim off trailing separator */
			while (path.endsWith(File.separator))
				path = path.substring(0, path.length() - File.separator.length());

			return path;
		}

		/* Parent directory ("Jan") */
		if ("FN.parent".equals(macro))
		{
			final File parent = this.source.getParentFile();

			if ((parent == null) || (parent.equals(renamer.getCurrentDirectory())))
			{
				return "";
			}
			else
			{
				String name = parent.getName();

				/* trim off trailing separator */
				while (name.endsWith(File.separator))
					name = name.substring(0, name.length() - File.separator.length());

				return name;
			}
		}

		/* Relative pathname of parent directory ("2007\Jan") */
		if ("FN.parentpath".equals(macro))
		{
			final File parent = this.source.getParentFile();

			if ((parent == null) || (parent.equals(renamer.getCurrentDirectory())))
			{
				return "";
			}
			else
			{
				String path = parent.getPath();

				if (path.startsWith(renamer.currentDirectoryFullPathname))
					path = path.substring(renamer.currentDirectoryFullPathname.length());

				/* trim off trailing separator */
				while (path.endsWith(File.separator))
					path = path.substring(0, path.length() - File.separator.length());

				return path;
			}
		}


		/************************
		 * (2) FILE SIZE MACROS *
		 ************************/

		/* all values are cast as integers */

		/* File size in bytes */
		if ("FS".equals(macro))
			return this.source.length() + "";

		/* File size in kilobytes (2^10 bytes) */
		if ("FS.kB".equals(macro))
			return (this.source.length() / 1024L) + "";

		/* File size in megabytes (2^20 bytes) */
		if ("FS.MB".equals(macro))
			return (this.source.length() / 1048576L) + "";

		/* File size in gigabytes (2^30 bytes) */
		if ("FS.GB".equals(macro))
			return (this.source.length() / 1073741824L) + "";

		/* File size in terabytes (2^40 bytes) */
		if ("FS.TB".equals(macro))
			return (this.source.length() / 1099511627776L) + "";


		/**************************************
		 * (3) FILE LAST-MODIFIED TIME MACROS *
		 **************************************/

		/* Number of milliseconds since the epoch (January 1, 1970 00:00:00 GMT, Gregorian) */
		if ("FT".equals(macro))
			return this.source.lastModified() + "";

		/* Date in the form yyyyMMdd */
		if ("FT.date".equals(macro))
			return (new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH)).format(new Date(this.source.lastModified()));

		/* Time in the form HHmmss */
		if ("FT.time".equals(macro))
			return (new SimpleDateFormat("HHmmss", Locale.ENGLISH)).format(new Date(this.source.lastModified()));

		/* am/pm in lower case */
		if ("FT.ap".equals(macro))
			return (new SimpleDateFormat("a", Locale.ENGLISH)).format(new Date(this.source.lastModified())).toLowerCase(Locale.ENGLISH);

		/* AM/PM in upper case */
		if ("FT.AP".equals(macro))
			return (new SimpleDateFormat("a", Locale.ENGLISH)).format(new Date(this.source.lastModified())).toUpperCase(Locale.ENGLISH);

		/* Date and time pattern letters from Java */
		if (macro.startsWith("FT."))
		{
			final String macroName = macro.substring(macro.indexOf(".") + 1);

			if (macroName.length() > 0)
			{
				boolean validChars = true;

				for (int i = 0; i < macroName.length(); i++)
				{
					if (!"GyMwWDdFEaHkKhmsSzZ".contains(macroName.charAt(i) + ""))
					{
						validChars = false;
						break;
					}
				}

				if (validChars)
					return (new SimpleDateFormat(macroName, Locale.ENGLISH)).format(new Date(this.source.lastModified()));
			}
		}


		/***************************
		 * (4) CURRENT TIME MACROS *
		 ***************************/

		/* Number of milliseconds since the epoch (January 1, 1970 00:00:00 GMT, Gregorian) */
		if ("CT".equals(macro))
			return (new Date()).getTime() + "";

		/* Date in the form yyyyMMdd */
		if ("CT.date".equals(macro))
			return (new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH)).format(new Date());

		/* Time in the form HHmmss */
		if ("CT.time".equals(macro))
			return (new SimpleDateFormat("HHmmss", Locale.ENGLISH)).format(new Date());

		/* am/pm in lower case */
		if ("CT.ap".equals(macro))
			return (new SimpleDateFormat("a", Locale.ENGLISH)).format(new Date()).toLowerCase(Locale.ENGLISH);

		/* AM/PM in upper case */
		if ("CT.AP".equals(macro))
			return (new SimpleDateFormat("a", Locale.ENGLISH)).format(new Date()).toUpperCase(Locale.ENGLISH);

		/* Date and time pattern letters from Java */
		if (macro.startsWith("CT."))
		{
			final String macroName = macro.substring(macro.indexOf(".") + 1);

			if (macroName.length() > 0)
			{
				boolean validChars = true;

				for (int i = 0; i < macroName.length(); i++)
				{
					if (!"GyMwWDdFEaHkKhmsSzZ".contains(macroName.charAt(i) + ""))
					{
						validChars = false;
						break;
					}
				}

				if (validChars)
					return (new SimpleDateFormat(macroName, Locale.ENGLISH)).format(new Date());
			}
		}


		/*******************************************
		 * (5) SYSTEM ENVIRONMENT VARIABLES MACROS *
		 *******************************************/

		if (macro.startsWith("ENV."))
		{
			final String macroName = macro.substring(macro.indexOf(".") + 1);

			if (macroName.length() > 0)
			{
				final String value = System.getenv(macroName);

				if (value != null)
					return value;
			}
		}


		/********************************
		 * (6) SYSTEM PROPERTIES MACROS *
		 ********************************/

		if (macro.startsWith("SYS."))
		{
			final String macroName = macro.substring(macro.indexOf(".") + 1);

			if (macroName.length() > 0)
			{
				final String value = System.getProperty(macroName);

				if (value != null)
					return value;
			}
		}


		/****************************
		 * (7) MISCELLANEOUS MACROS *
		 ****************************/

		/* Full pathname of the current directory (e.g. "C:\Work") */
		if ("RW.cd".equals(macro))
		{
			String path = renamer.currentDirectoryFullPathname;

			/* trim off trailing separator */
			while (path.endsWith(File.separator))
				path = path.substring(0, path.length() - File.separator.length());

			return path;
		}

		/* Number of local matched files (i.e. in the file's subdirectory) */
		if ("RW.N".equals(macro) && (this.localCount > 0))
			return this.localCount + "";

		/* Number of global matched files (i.e. in all subdirectories) */
		if ("RW.NN".equals(macro) && (this.globalCount > 0))
			return this.globalCount + "";

		/* Generate a string of 10 random digits */
		if ("RW.random".equals(macro))
		{
			/* return a string of 10 random digits (0-9) */
			final Random rng = new Random();
			final StringBuilder randomString = new StringBuilder();

			/* append a digit */
			while (randomString.length() < 10)
				randomString.append(rng.nextInt(10));

			return randomString.substring(0, 10);
		}


		/***********************************************
		 * (8) MACRO/REGISTER MODIFIERS AND ATTRIBUTES *
		 ***********************************************/

		/* Example: "hello WORLD" */

		/* Length of the string (11) */
		if (macro.endsWith(".len"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return parentMacroValue.length() + "";
		}

		/* Convert to upper case ("HELLO WORLD") */
		if (macro.endsWith(".upper"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return parentMacroValue.toUpperCase();
		}

		/* Convert to lower case ("hello world") */
		if (macro.endsWith(".lower"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return parentMacroValue.toLowerCase();
		}

		/* Capitalize only the first character ("Hello world") */
		if (macro.endsWith(".capitalize"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
			{
				if (parentMacroValue.isEmpty())
					return parentMacroValue;

				return Character.toUpperCase(parentMacroValue.charAt(0)) +
						parentMacroValue.substring(1).toLowerCase();
			}
		}

		/* Convert to title case ("Hello World") */
		if (macro.endsWith(".title"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return StringManipulator.toTitleCase(parentMacroValue);
		}

		/* Convert to camelCase ("helloWorld") */
		if (macro.endsWith(".camel"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
			{
				final String value = StringManipulator.deleteWhitespace(StringManipulator.toTitleCase(parentMacroValue));

				if (value.isEmpty())
					return value;

				return Character.toLowerCase(value.charAt(0)) + value.substring(1);
			}
		}

		/* Convert to PascalCase ("HelloWorld") */
		if (macro.endsWith(".pascal"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return StringManipulator.deleteWhitespace(StringManipulator.toTitleCase(parentMacroValue));
		}

		/* Swap the case ("HELLO world") */
		if (macro.endsWith(".swapcase"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return StringManipulator.swapCase(parentMacroValue);
		}

		/* Abbreviate to initials ("h W") */
		if (macro.endsWith(".abbrev"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return StringManipulator.abbreviate(parentMacroValue);
		}

		/* Reverse the string ("DLROW olleh") */
		if (macro.endsWith(".reverse"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return StringManipulator.reverse(parentMacroValue);
		}

		/* Trim away whitespace on the left and right */
		if (macro.endsWith(".trim"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return parentMacroValue.trim();
		}

		/* Trim away whitespace on the left */
		if (macro.endsWith(".ltrim"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return StringManipulator.leftTrim(parentMacroValue);
		}

		/* Trim away whitespace on the right */
		if (macro.endsWith(".rtrim"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return StringManipulator.rightTrim(parentMacroValue);
		}

		/* Delete whitespace in the string */
		if (macro.endsWith(".delspace"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return StringManipulator.deleteWhitespace(parentMacroValue);
		}

		/* Delete extra whitespace by replacing contiguous whitespace with a single space */
		if (macro.endsWith(".delextraspace"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return StringManipulator.deleteExtraWhitespace(parentMacroValue);
		}

		/* Delete punctuation marks in the string */
		if (macro.endsWith(".delpunctuation"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return StringManipulator.deletePunctuation(parentMacroValue);
		}

		/* Space out words by inserting a space between connected words */
		if (macro.endsWith(".spaceout"))
		{
			final String parentMacro = macro.substring(0, macro.lastIndexOf("."));
			final String parentMacroValue = this.evaluateMacro(parentMacro);

			if (parentMacroValue != null)
				return StringManipulator.spaceOutWords(parentMacroValue);
		}


		/****************************
		 * (9) REGISTERS (ASSIGNED) *
		 ****************************/

		/* MUST BE THIRD-LAST BLOCK */

		if ((this.registerValues != null) &&
				renamer.getRegisterNames().containsKey(macro))
		{
			return this.registerValues[renamer.getRegisterNames().get(macro)];
		}


		/***************************************************
		 * (10) SHORTCUTS FOR SINGLE-LETTER REGISTER NAMES *
		 ***************************************************/

		/* MUST BE SECOND-LAST BLOCK */

		/* Example: a ---> aa AA Aa aA
		 * Rule: If name is not a register, and is two chars long,
		 * and first char == second char (ignore case), and exclusively
		 * either small or big letter is a register name, then return
		 * .lower, .upper, .title, .swapcase
		 */
		if ((this.registerValues != null) &&
				(macro.length() == 2))
		{
			final String c1 = macro.charAt(0) + "";
			final String c2 = macro.charAt(1) + "";

			if (c1.equalsIgnoreCase(c2))
			{
				final String upper = c1.toUpperCase(Locale.ENGLISH);
				final String lower = c1.toLowerCase(Locale.ENGLISH);
				final boolean upperIsRegister = renamer.getRegisterNames().containsKey(upper);
				final boolean lowerIsRegister = renamer.getRegisterNames().containsKey(lower);
				String parentMacro = null;

				if (upperIsRegister && !lowerIsRegister)
				{
					parentMacro = upper;
				}
				else if (lowerIsRegister && !upperIsRegister)
				{
					parentMacro = lower;
				}

				if (parentMacro != null)
				{
					if (macro.equals(lower + lower))
						return this.evaluateMacro(parentMacro + ".lower");

					if (macro.equals(upper + upper))
						return this.evaluateMacro(parentMacro + ".upper");

					if (macro.equals(upper + lower))
						return this.evaluateMacro(parentMacro + ".title");

					if (macro.equals(lower + upper))
						return this.evaluateMacro(parentMacro + ".swapcase");
				}
			}
		}


		/***************************
		 * (11) UNIDENTIFIED MACRO *
		 ***************************/

		/* MUST BE LAST BLOCK */

		return null; // this should be the only "return null" in this method
	}
}
