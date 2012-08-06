/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RenameWandLib;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
//import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TreeMap;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.io.FileUtils;

/*
 *  Nicolas Franck
 */


public class RenameWand {
    /**************************************
	 * CONSTANTS AND MISCELLANEOUS FIELDS *
	 **************************************/

	/** constant: program title */
	private static final String PROGRAM_TITLE = "RenameWand 2.2   Copyright 2007 Zach Scrivena   2007-12-09";

	/** constant: special construct separator character */
	private static final char SPECIAL_CONSTRUCT_SEPARATOR_CHAR = '|';

	/** constant: substring range character */
	private static final char SUBSTRING_RANGE_CHAR = ':';

	/** constant: substring delimiter character */
	private static final char SUBSTRING_DELIMITER_CHAR = ',';

	/** constant: integer filter indicator character */
	private static final char INTEGER_FILTER_INDICATOR_CHAR = '@';

	/** constant: regex pattern for register names */
	private static final Pattern REGISTER_NAME_PATTERN = Pattern.compile("[a-zA-Z_][a-zA-Z_0-9]*");

	/**
	 * constant: regex pattern for special construct "<length|@expr>" in source pattern string.
	 * Match groups: (1,"length"), (2,"@"), (3,"expr")
	 */
	private static final Pattern SOURCE_SPECIAL_CONSTRUCT_PATTERN = Pattern.compile(
			"\\<(?:([\\sa-zA-Z_0-9\\." +
			Pattern.quote("+-*/^()[]!" + SUBSTRING_RANGE_CHAR  + SUBSTRING_DELIMITER_CHAR) +
			"]+)" + Pattern.quote(SPECIAL_CONSTRUCT_SEPARATOR_CHAR + "") + ")?(" +
			Pattern.quote(INTEGER_FILTER_INDICATOR_CHAR + "") +
			")?([\\sa-zA-Z_0-9\\." +
			Pattern.quote("+-*/^()[]!" + SUBSTRING_RANGE_CHAR  + SUBSTRING_DELIMITER_CHAR) +
			"]+)\\>");

	/**
	 * constant: regex pattern for special construct "<length|expr>" in target pattern string.
	 * Match groups: (1,"length"), (2,"expr")
	 */
	private static final Pattern TARGET_SPECIAL_CONSTRUCT_PATTERN = Pattern.compile(
			"\\<(?:([\\sa-zA-Z_0-9\\." +
			Pattern.quote("+-*/^()[]#!@" + SUBSTRING_RANGE_CHAR  + SUBSTRING_DELIMITER_CHAR) +
			"]+)" + Pattern.quote(SPECIAL_CONSTRUCT_SEPARATOR_CHAR + "") +
			")?([\\sa-zA-Z_0-9\\." +
			Pattern.quote("+-*/^()[]#!@" + SUBSTRING_RANGE_CHAR  + SUBSTRING_DELIMITER_CHAR) +
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

	/** true if this is a Windows OS, false otherwise */
	private static boolean isWindowsOperatingSystem;

	/** current directory (absolute and canonical pathname) */
	private File currentDirectory = new File("");

        /** full pathname of the current directory (includes trailing separator) */
	static String currentDirectoryFullPathname;

	
	
	/** register names mapping (register name ---> capture group index) */
	private Map<String,Integer> registerNames = new TreeMap<String,Integer>();

	/** number of capture groups in source regex pattern */
	private int numCaptureGroups = 0;

	/** true if the source pattern string is reusable for different files/directories; false otherwise */
	private boolean sourcePatternIsReusable = false;

	/** subdirectory counter */
	private int numDirs = 0;

	/*********************
	 * RENAME PARAMETERS *
	 *********************/

	/** parameter: ignore warnings; do not pause (default = false) */
	private boolean ignoreWarnings = false;

	/** parameter: recurse into subdirectories (default = false) */
	private boolean recurseIntoSubdirectories = false;

	/** parameter: match relative pathname, not just the name, of the files/directories (default = false) */
	private boolean matchRelativePathname = false;

	/** parameter: match lower case name of the files/directories (default = false) */
	private boolean matchLowerCase = false;

	/** parameter: true if renaming directories; false if renaming files (default = false) */
	private boolean renameDirectories = false;

	/** parameter: default action on rename operation error (default = '\0') */
	private OnErrorAction defaultActionOnRenameOperationError = OnErrorAction.ignore;

	/** parameter: source pattern string */
	private String sourcePatternString;

	/** parameter: target pattern string */
	private String targetPatternString;

        
        private OnErrorAction onRenameOperationError = defaultActionOnRenameOperationError;

        private ArrayList<String> errors = new ArrayList<String>();
        private ArrayList<String> warnings = new ArrayList<String>();

        /*
         * Nicolas Franck: prompt callback for confirmation
         */
        private RenameListener renameListener = null;
        private int numRenameOperationsPerformed = 0;
        private boolean simulateOnly = false;
        private ArrayList<RenameFilePair>renamePairs;

        /*
         *  Nicolas Franck: copy is soms veiliger dan move!
         */
        private boolean copy = false;
        //indien kopiëren van mappen: moet contents ook mee gekopiëerd worden?
        private boolean copyDirectoryContent = false;

        private boolean overWrite = false;

        public boolean isOverWrite() {
            return overWrite;
        }
        public void setOverWrite(boolean overWrite) {
            this.overWrite = overWrite;
        }
        public boolean isCopy() {
            return copy;
        }

        public void setCopy(boolean copy) {
            this.copy = copy;
        }
        public void copy(File in,File out) throws FileNotFoundException, IOException{
            if(in.isFile()){
                FileUtils.copyFile(in, out);
            }else if(in.isDirectory()){
                if(copyDirectoryContent)
                    FileUtils.copyDirectory(in, out);
                else
                    FileUtils.copyFile(in, out);
            }
        }
        public ArrayList<RenameFilePair> getRenamePairs() {
            return renamePairs;
        }
        public void setRenamePairs(ArrayList<RenameFilePair> renamePairs) {
            this.renamePairs = renamePairs;
        }

        static {
            /* determine if this is a Windows OS */
            isWindowsOperatingSystem = System.getProperty("os.name").toUpperCase(Locale.ENGLISH).contains("WINDOWS") &&
                            (File.separatorChar == '\\');
            /* initialize operator precedence table for stack evaluation */
            OPERATOR_PRECEDENCE.put("#",   7);
            OPERATOR_PRECEDENCE.put("#!",  7);
            OPERATOR_PRECEDENCE.put("##",  7);
            OPERATOR_PRECEDENCE.put("##!", 7);
            OPERATOR_PRECEDENCE.put("@",   7);
            OPERATOR_PRECEDENCE.put("@!",  7);
            OPERATOR_PRECEDENCE.put("@@",  7);
            OPERATOR_PRECEDENCE.put("@@!", 7);
            OPERATOR_PRECEDENCE.put("^",   6);
            OPERATOR_PRECEDENCE.put("~",   5); // unary minus (negative) sign
            OPERATOR_PRECEDENCE.put("*",   4);
            OPERATOR_PRECEDENCE.put("/",   4);
            OPERATOR_PRECEDENCE.put("+",   3);
            OPERATOR_PRECEDENCE.put("-",   3);
            OPERATOR_PRECEDENCE.put(SUBSTRING_RANGE_CHAR  + "", 2);
            OPERATOR_PRECEDENCE.put(SUBSTRING_DELIMITER_CHAR + "", 1);
            
        }       
                
        public RenameWand(){            
        }
        public File getCurrentDirectory() {
            return currentDirectory;
        }
        public void setCurrentDirectory(File currentDirectory) throws IOException {
            String path = currentDirectory.getCanonicalPath();
            if(!path.endsWith(File.separator))
                    path = path+File.separator;
            setCurrentDirectoryFullPathname(path);

            this.currentDirectory = currentDirectory.getCanonicalFile();
        }

        private static String getCurrentDirectoryFullPathname() {
            return currentDirectoryFullPathname;
        }

        public static void setCurrentDirectoryFullPathname(String currentDirectoryFullPathname) {
            RenameWand.currentDirectoryFullPathname = currentDirectoryFullPathname;
        }

        public boolean isIgnoreWarnings() {
            return ignoreWarnings;
        }

        public boolean isMatchLowerCase() {
            return matchLowerCase;
        }

        public boolean isMatchRelativePathname() {
            return matchRelativePathname;
        }

        public boolean isSimulateOnly() {
            return simulateOnly;
        }

        public void setSimulateOnly(boolean simulateOnly) {
            this.simulateOnly = simulateOnly;
        }

        public int getNumCaptureGroups() {
            return numCaptureGroups;
        }

        public int getNumDirs() {
            return numDirs;
        }
        public int getNumRenameOperationsPerformed() {
            return numRenameOperationsPerformed;
        }

        private void setNumRenameOperationsPerformed(int numRenameOperationsPerformed) {
            this.numRenameOperationsPerformed = numRenameOperationsPerformed;
        }
        public boolean isRecurseIntoSubdirectories() {
            return recurseIntoSubdirectories;
        }

        public Map<String, Integer> getRegisterNames() {
            return registerNames;
        }

        public boolean isRenameDirectories() {
            return renameDirectories;
        }

        public boolean isSourcePatternIsReusable() {
            return sourcePatternIsReusable;
        }

        public String getSourcePatternString() {
            return sourcePatternString;
        }

        public String getTargetPatternString() {
            return targetPatternString;
        }  

        public void setIgnoreWarnings(boolean ignoreWarnings) {
            this.ignoreWarnings = ignoreWarnings;
        }

        public void setMatchLowerCase(boolean matchLowerCase) {
            this.matchLowerCase = matchLowerCase;
        }

        public void setMatchRelativePathname(boolean matchRelativePathname) {
            this.matchRelativePathname = matchRelativePathname;
        }

        public void setRecurseIntoSubdirectories(boolean recurseIntoSubdirectories) {
            this.recurseIntoSubdirectories = recurseIntoSubdirectories;
        }

        public void setRenameDirectories(boolean renameDirectories) {
            this.renameDirectories = renameDirectories;
        }

        public void setSourcePatternString(String sourcePatternString) {
            if(sourcePatternString.contains("\0"))throw new TerminatingException("Illegal null-character found in source pattern string.");
            this.sourcePatternString = sourcePatternString;
        }

        public void setTargetPatternString(String targetPatternString) {
            if(targetPatternString.contains("\0"))throw new TerminatingException("Illegal null-character found in source pattern string.");
            this.targetPatternString = targetPatternString;
        }

        public void setOnRenameOperationError(OnErrorAction onRenameOperationError) {
            this.onRenameOperationError = onRenameOperationError;
        }


        public ArrayList<String> getErrors() {
            return errors;
        }

        public ArrayList<String> getWarnings() {
            return warnings;
        }

        private void setErrors(ArrayList<String> errors) {
            this.errors = errors;
        }

        private void setWarnings(ArrayList<String> warnings) {
            this.warnings = warnings;
        }
        private void init(){
            warnings.clear();
            errors.clear();
            setNumRenameOperationsPerformed(0);
        }      
        public void rename() throws IOException{
            _rename();
            if(renameListener != null)renameListener.onEnd(getRenamePairs(),getNumRenameOperationsPerformed());
        }
        private void _rename() throws IOException{
            init();
            /* get match candidates */
            ArrayList<FileUnit> matchCandidates = getMatchCandidates();
            
            if(matchCandidates.size() == 0){
                if(renameListener != null)renameListener.onInit(matchCandidates,new ArrayList<FileUnit>());
                return;
            }
            /* perform matching */
            ArrayList<FileUnit> matches = performSourcePatternMatching(matchCandidates);
            if(matches.size() == 0){
                if(renameListener != null)renameListener.onInit(matchCandidates,matches);
                return;
            }
            evaluateTargetPattern(matches.toArray(new FileUnit[matches.size()]));
            /* determine renaming sequence and find clashes, bad names, etc. */
            final ArrayList<RenameFilePair> renameOperations = getRenameOperations(matches);
            setRenamePairs(renameOperations);
            final int numRenameOperations = renameOperations.size();
            final boolean proceedToRename = promptUserOnRename(matches, numRenameOperations);

            setNumRenameOperationsPerformed(proceedToRename ? performRenameOperations(renameOperations) : 0);
        }
      

        /**
	 * Scan current directory to get candidate files/directories for matching.
	 *
	 * @return
	 *     Candidate files/directories for matching
         *
         *      Nicolas Franck: dit geeft louter bestanden in een map terug
	 */
	public ArrayList<FileUnit> getMatchCandidates(){
            
            
            /* return value: match candidate files/directories */
            final ArrayList<FileUnit> matchCandidates = new ArrayList<FileUnit>();

            /* stack containing the subdirectories to be scanned */
            final Deque<File> subdirectories = new ArrayDeque<File>();
            subdirectories.push(currentDirectory);

            /* reset number of subdirectories scanned */
            numDirs = 0;

            /* perform a DFS scanning of the subdirectories */
            while (!subdirectories.isEmpty()){

                numDirs++;

                /* get a directory to be scanned */
                final File dir = subdirectories.pop();
                final File[] listFiles = dir.listFiles();

                if(listFiles == null){
                    final String path = dir.getPath();
                    warnings.add("Failed to get contents of directory \"" + path +
                            (path.endsWith(File.separator) ? "" : File.separator) +
                            "\".\nThis directory will be ignored.");
                }else{
                    /* subdirectories under this directory */
                    final ArrayList<File> subdirs = new ArrayList<File>();

                    for(File f: listFiles){
                        final boolean isDirectory = f.isDirectory();

                        if(renameDirectories == isDirectory){
                            final FileUnit u = new FileUnit(this);
                            u.source = f;
                            u.parentDirId = numDirs;
                            matchCandidates.add(u);
                        }

                        if(isDirectory)subdirs.add(f);
                    }

                    if(recurseIntoSubdirectories){
                        for(int i = subdirs.size() - 1; i >= 0; i--)
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
	public ArrayList<FileUnit> performSourcePatternMatching(final ArrayList<FileUnit> matchCandidates) throws IOException{
            /* return value: files/directories with names that match the source pattern */
            final ArrayList<FileUnit> matched = new ArrayList<FileUnit>();

            /* regex pattern used for matching file/directory names */
            Pattern sourcePattern = null;

            /* is the regex pattern matcher reusable for different files/directories? */
            sourcePatternIsReusable = false;

            /* match each candidate file or directory */
            for(FileUnit u: matchCandidates){
                if(!sourcePatternIsReusable)
                    sourcePattern = getFileSourcePattern(u);

                /* check if source pattern is successfully generated */
                if (sourcePattern != null){
                    /* name string to be matched */
                    String name = null;

                    if(matchRelativePathname){
                        name = u.source.getPath();
                        if(name.startsWith(currentDirectoryFullPathname))
                            name = name.substring(currentDirectoryFullPathname.length());
                    }
                    else{
                        name = u.source.getName();
                    }

                    /* trim off trailing separator */
                    while(name.endsWith(File.separator))
                        name = name.substring(0, name.length() - File.separator.length());

                    if(matchLowerCase)
                        name = name.toLowerCase(Locale.ENGLISH);

                    /* regex pattern matcher */
                    final Matcher sourceMatcher = sourcePattern.matcher(name);
                    

                    if(sourceMatcher.matches()){
                        /* add capture group values to FileUnit's registerValues, and */
                        /* add this file/directory to our list of successful matches  */                     

                        u.registerValues = new String[numCaptureGroups + 1]; // add index offset 1

                        for(int i = 1; i <= numCaptureGroups; i++)
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
	private Pattern getFileSourcePattern(final FileUnit u){
            /* reset register names and capture group counter */
            registerNames.clear();
            int captureGroupIndex = 0;

            /* assume that source pattern is reusable */
            sourcePatternIsReusable = true;

            /* Stack to keep track of the parser mode: */
            /* "--" : Base mode (first on the stack)   */
            /* "[]" : Square brackets mode "[...]"     */
            /* "{}" : Curly braces mode "{...}"        */
            final Deque<String> parserMode = new ArrayDeque<String>();
            parserMode.push("--"); // base mode

            final int sourcePatternStringLength = sourcePatternString.length();
            int index = 0; // index in sourcePatternString

            /* regex pattern equivalent to sourcePatternString */
            final StringBuilder sourceRegex = new StringBuilder();

            /* parse each character of the source pattern string */
            while(index < sourcePatternStringLength){
                char c = sourcePatternString.charAt(index++);

                if(c == '\\'){
                    /***********************
                     * (1) ESCAPE SEQUENCE *
                     ***********************/

                    if(index == sourcePatternStringLength){
                        /* no characters left, so treat '\' as literal char */
                        sourceRegex.append(Pattern.quote("\\"));
                    }
                    else{
                        /* read next character */
                        c = sourcePatternString.charAt(index);
                        final String s = c + "";

                        if(
                                (
                                    "--".equals(parserMode.peek()) && "\\<>[]{}?*".contains(s)
                                ) ||
                                ("[]".equals(parserMode.peek()) && "\\<>[]{}?*!-".contains(s))
                                ||
                                ("{}".equals(parserMode.peek()) && "\\<>[]{}?*,".contains(s))
                        ){
                            /* escape the construct char */
                            index++;
                            sourceRegex.append(Pattern.quote(s));
                        }
                        else{
                            /* treat '\' as literal char */
                            sourceRegex.append(Pattern.quote("\\"));
                        }
                    }
                }
                else if (c == '*'){
                    /************************
                     * (2) GLOB PATTERN '*' *
                     ************************/

                    /* create non-capturing group to match zero or more characters */
                    sourceRegex.append(".*");
                }
                else if (c == '?'){
                    /************************
                     * (3) GLOB PATTERN '?' *
                     ************************/

                    /* create non-capturing group to match exactly one character */
                    sourceRegex.append('.');
                }
                else if (c == '['){
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
                                    (sourcePatternString.charAt(index) == '!')){
                            index++;
                            sourceRegex.append('^');
                    }
                }
                else if ((c == ']') && "[]".equals(parserMode.peek())){
                    /* closing square bracket ']' */
                    sourceRegex.append(']');
                    parserMode.pop();
                }
                else if ((c == '-') && "[]".equals(parserMode.peek())){
                    /* character range '-' in "[...]" */
                    sourceRegex.append('-');
                }
                else if (c == '{'){
                    /****************************
                     * (5) GLOB PATTERN "{...}" *
                     ****************************/

                    /* opening curly brace '{' */
                    /* create non-capturing group to match one of the */
                    /* strings inside the sequence */
                    sourceRegex.append("(?:(?:");
                    parserMode.push("{}");
                }
                else if ((c == '}') && "{}".equals(parserMode.peek())) {
                    /* closing curly brace '}' */
                    sourceRegex.append("))");
                    parserMode.pop();
                }
                else if ((c == ',') && "{}".equals(parserMode.peek())){
                    /* comma between strings in "{...}" */
                    sourceRegex.append(")|(?:");
                }
                else if (c == '<'){
                    
                    /*********************************
                     * (6) SPECIAL CONSTRUCT "<...>" *
                     *********************************/

                    final StringBuilder specialConstruct = new StringBuilder("<");
                    boolean closingAngleBracketFound = false;

                    /* read until the first (unescaped) closing '>' */
                    while(!closingAngleBracketFound && (index < sourcePatternStringLength)){
                        c = sourcePatternString.charAt(index++);
                        specialConstruct.append(c);
                        
                        if ((c == '>') && (specialConstruct.charAt(specialConstruct.length() - 2) != '\\'))
                            closingAngleBracketFound = true;
                    }

                    if(!closingAngleBracketFound)
                        throw new TerminatingException("Failed to find matching closing angle bracket > for special construct \"" +
                                            specialConstruct + "\" in source pattern string. Please ensure that each literal < is escaped as \\<.");
                    
                    /* check if special construct is in the form "<length|@expr>" */
                    final Matcher specialConstructMatcher = SOURCE_SPECIAL_CONSTRUCT_PATTERN.matcher(specialConstruct);

                    if(!specialConstructMatcher.matches())
                        throw new TerminatingException("Invalid special construct \"" + specialConstruct + "\" in source pattern string: " +
                                            "Not in the form <length" + SPECIAL_CONSTRUCT_SEPARATOR_CHAR + INTEGER_FILTER_INDICATOR_CHAR + "expr>.");

                    /* match groups */
                    String length = specialConstructMatcher.group(1);
                    if (length != null) length = length.trim();
                    final boolean integerFilter = (specialConstructMatcher.group(2) != null);
                    String expr = specialConstructMatcher.group(3).trim();

                    /* evaluate the length string if it is not already a positive integer */
                    if((length != null) && !POSITIVE_INTEGER_PATTERN.matcher(length).matches()){
                        sourcePatternIsReusable = false; // this construct is file-specific
                        final EvaluationResult<String> result = evaluateSpecialConstruct(new FileUnit[] {u}, length);

                        if(result.success){
                            length = result.output[0];

                            /* check that length string is a positive integer now */
                            if(!POSITIVE_INTEGER_PATTERN.matcher(length).matches())
                                throw new TerminatingException("Invalid length string for special construct \"" +
                                                    specialConstruct + "\" in source pattern string for " +
                                                    SINGULAR_NOUN + " \"" + u.source.getPath() + "\": " +
                                                    length + " is not a positive integer.");
                        }
                        else{
                            warnings.add("Failed to evaluate length string of special construct \"" +
                                            specialConstruct + "\" in source pattern string for " +
                                            SINGULAR_NOUN + " \"" + u.source.getPath() + "\": " +
                                            result.error + "\nThis " + SINGULAR_NOUN + " will be ignored.");

                            return null; // failed to generate regex pattern
                        }
                    }

                    /* check if this construct is a register assignment or back reference */
                    if(
                            ((length == null) || POSITIVE_INTEGER_PATTERN.matcher(length).matches()) &&
                            (u.evaluateMacro(expr) == null) &&
                            REGISTER_NAME_PATTERN.matcher(expr).matches()
                    ){
                        if(registerNames.containsKey(expr)){
                            /* register is already captured, so we create a back reference to it */
                            if ((length != null) || integerFilter)
                                    throw new TerminatingException("Invalid back reference \"" + specialConstruct +
                                                    "\" to register \"" + expr + "\" near position " + index +
                                                    " of source pattern string: Length string and integer filter indicator @ not allowed.");

                            sourceRegex.append("\\" + registerNames.get(expr));
                        }
                        else{
                            /* register has not been captured, so we create a regex capturing group for it */
                            registerNames.put(expr, ++captureGroupIndex);

                            sourceRegex.append("(" + (integerFilter ? "[0-9]" : ".") +
                                            ((length == null) ? "*" : ("{" + Integer.parseInt(length.trim()) + "}")) + ")");
                        }
                    }
                    else{
                        /* proceed to parse the expression string */

                        sourcePatternIsReusable = false; // this construct is file-specific

                        if(integerFilter)
                                throw new TerminatingException("Invalid special construct expression \"" + specialConstruct +
                                                "\" in source pattern string: Integer filter indicator @ not allowed here because \"" +
                                                expr + "\" is not a register name.");

                        /* evaluate expr string */
                        final EvaluationResult<String> result = evaluateSpecialConstruct(new FileUnit[] {u}, expr);

                        if(result.success){
                            expr = result.output[0];

                            /* perform length formatting if specified */
                            if (length != null)
                                    expr = padString(expr, Integer.parseInt(length), isNumeric(expr));

                            /* convert literal string to a regex string */
                            sourceRegex.append(Pattern.quote(expr));
                        }
                        else{
                            warnings.add("Failed to evaluate expression string of special construct \"" +
                                            specialConstruct + "\" in source pattern string for " +
                                            SINGULAR_NOUN + " \"" + u.source.getPath() + "\": " +
                                            result.error + "\nThis " + SINGULAR_NOUN + " will be ignored.");

                            return null; // failed to generate regex pattern
                        }
                    }
                }
                else if ((c == '/') && isWindowsOperatingSystem){
                    /****************************************
                     * (7) ALTERNATE WINDOWS FILE SEPARATOR *
                     ****************************************/

                    sourceRegex.append(Pattern.quote("\\"));
                }
                else{
                        /*************************
                         * (8) LITERAL CHARACTER *
                         *************************/

                        /* convert literal character to a regex string */
                        sourceRegex.append(Pattern.quote(c + ""));
                }
            }

            /* check for mismatched [...] or {...} */
            if("[]".equals(parserMode.peek()))
                    throw new TerminatingException("Failed to find matching closing square bracket ] in source pattern string.");

            if("{}".equals(parserMode.peek()))
                    throw new TerminatingException("Failed to find matching closing curly brace } in source pattern string.");

            /* set total number of capture groups in the source pattern */
            numCaptureGroups = captureGroupIndex;

            /* compile regex string */
            Pattern sourceRegexCompiledPattern = null;

            try{
                sourceRegexCompiledPattern = Pattern.compile(sourceRegex.toString());                
            }
            catch(PatternSyntaxException e){
                throw new TerminatingException("Failed to compile source pattern string for " +
                                SINGULAR_NOUN + " \"" + u.source.getPath() + "\":\n"
                );
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
	public void evaluateTargetPattern(final FileUnit[] matchedFiles){
            /* number of matched files */
            final int n = matchedFiles.length;

            /* count number of files in each local directory */
            int[] localCounts = new int[numDirs + 1];

            Arrays.fill(localCounts, 0);

            for (FileUnit u : matchedFiles)
                localCounts[u.parentDirId]++;

            /* reset file/directory attributes */
            for (FileUnit u : matchedFiles){
                u.globalCount = n;
                u.localCount = localCounts[u.parentDirId];
                u.targetFilename = new StringBuilder();
            }

            final int targetPatternStringLength = targetPatternString.length();
            int index = 0; // index in targetPatternString

            /* parse each character of the target pattern string */
            while(index < targetPatternStringLength){
                char c = targetPatternString.charAt(index++);

                if(c == '\\'){
                    /***********************
                     * (1) ESCAPE SEQUENCE *
                     ***********************/

                    if (index == targetPatternStringLength){
                        /* no characters left, so treat '\' as literal char */
                        for (FileUnit u : matchedFiles)
                            u.targetFilename.append('\\');
                    }
                    else{
                        /* read next char */
                        c = targetPatternString.charAt(index);

                        if((c == '<') || (c == '\\')){
                            /* escape the construct char */
                            index++;

                            for (FileUnit u : matchedFiles)
                                u.targetFilename.append(c);
                        }
                        else{
                            /* treat '\' as literal char */
                            for (FileUnit u : matchedFiles)
                                u.targetFilename.append('\\');
                        }
                    }
                }
                else if (c == '<'){
                    /*********************************
                     * (2) SPECIAL CONSTRUCT "<...>" *
                     *********************************/

                    final StringBuilder specialConstruct = new StringBuilder("<");
                    boolean closingAngleBracketFound = false;

                    /* read until the first (unescaped) closing '>' */
                    while ((!closingAngleBracketFound) && (index < targetPatternStringLength)){
                        c = targetPatternString.charAt(index++);
                        specialConstruct.append(c);

                        if((c == '>') && (specialConstruct.charAt(specialConstruct.length() - 2) != '\\'))
                            closingAngleBracketFound = true;
                    }

                    if(!closingAngleBracketFound)
                        throw new TerminatingException("Failed to find matching closing angle bracket > for special construct \"" +
                                            specialConstruct + "\" in target pattern string. Please ensure that each literal < is escaped as \\<.");

                    /* check if special construct is in the form "<length|expr>" */
                    final Matcher specialConstructMatcher = TARGET_SPECIAL_CONSTRUCT_PATTERN.matcher(specialConstruct);

                    if(!specialConstructMatcher.matches())
                        throw new TerminatingException("Invalid special construct \"" + specialConstruct +
                                            "\" in target pattern string: Not in the form <length" + SPECIAL_CONSTRUCT_SEPARATOR_CHAR + "expr>.");

                    /* match groups */
                    final String length = specialConstructMatcher.group(1);
                    final String expr = specialConstructMatcher.group(2).trim();

                    String[] lengths = null;

                    /* evaluate the length string if it is not already a positive integer */
                    if ((
                            length != null) && (!POSITIVE_INTEGER_PATTERN.matcher(length).matches()
                    )){
                        final EvaluationResult<String> result = evaluateSpecialConstruct(matchedFiles, length);

                        if(result.success){
                            lengths = result.output;

                            /* check that length string is a positive integer now */
                            for (int i = 0; i < n; i++)
                                if (!POSITIVE_INTEGER_PATTERN.matcher(lengths[i]).matches())
                                    throw new TerminatingException("Invalid length string for special construct \"" +
                                            specialConstruct + "\" in target pattern string for " + SINGULAR_NOUN + " \"" +
                                            matchedFiles[i].source.getPath() + "\": " + lengths[i] + " is not a positive integer.");
                        }
                        else{
                            throw new TerminatingException("Failed to evaluate length string of special construct \"" +
                                    specialConstruct + "\" in target pattern string: " + result.error);
                        }
                    }

                    /* proceed to parse the expression string */
                    final EvaluationResult<String> result = evaluateSpecialConstruct(matchedFiles, expr);

                    if (!result.success)
                        throw new TerminatingException("Failed to evaluate expression string of special construct \"" +specialConstruct + "\" in target pattern string: " + result.error);

                    /* perform length formatting */
                    if(lengths == null){
                        if (length == null){
                            /* no length string specified */
                            for(int i = 0; i < n; i++)
                                matchedFiles[i].targetFilename.append(result.output[i]);
                        }
                        else{
                            /* constant length string specified */
                            final int len = Integer.parseInt(length);

                            /* check if all expr are numeric */
                            final boolean isNumeric = (getNonNumericIndex(result.output) < 0);

                            for(int i = 0; i < n; i++)
                                matchedFiles[i].targetFilename.append(
                                        padString(result.output[i], len, isNumeric)
                                );
                        }
                    }
                    else{
                        /* file-specific length strings used */

                        /* check if all expr are numeric */
                        final boolean isNumeric = (getNonNumericIndex(result.output) < 0);

                        for (int i = 0; i < n; i++)
                            matchedFiles[i].targetFilename.append(
                                padString(result.output[i], Integer.parseInt(lengths[i]), isNumeric)
                            );
                    }
                }
                else if ((c == '/') && isWindowsOperatingSystem){
                    /****************************************
                     * (3) ALTERNATE WINDOWS FILE SEPARATOR *
                     ****************************************/

                    for (FileUnit u : matchedFiles)
                        u.targetFilename.append('\\');
                }
                else{
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
	private EvaluationResult<String> evaluateSpecialConstruct(final FileUnit[] files,final String specialConstruct){
            /* use a stack to evaluate the special construct */

            /* return value */
            final EvaluationResult<String> result = new EvaluationResult<String>();

            /* tokenize expr */
            final StringManipulator.Token[] tempTokens = StringManipulator.tokenize(
                            "(" + specialConstruct + ")",  /* surround special construct with (...) */
                            "(((\\#++)\\!?)|((\\@++)\\!?)|[" +
                            Pattern.quote("[]()*/^+-" + SUBSTRING_RANGE_CHAR  + SUBSTRING_DELIMITER_CHAR) + "])",
                            true);

            /* preprocess tokens */
            final ArrayList<StringManipulator.Token> tokens = new ArrayList<StringManipulator.Token>();

            for (StringManipulator.Token token : tempTokens){
                token.val = token.val.trim();

                if (token.val.isEmpty())continue;

                if ("-".equals(token.val) &&
                                tokens.get(tokens.size() - 1).isDelimiter &&
                                !")]".contains(tokens.get(tokens.size() - 1).val)){
                    /* unary minus (negative) sign */
                    token.val = "~";
                }
                else if("(".equals(token.val) &&
                                (tokens.size() > 0) &&
                                (!tokens.get(tokens.size() - 1).isDelimiter ||
                                ")".equals(tokens.get(tokens.size() - 1).val))){
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
            for(StringManipulator.Token token : tokens){
                final String tokenVal = token.val;

                if (token.isDelimiter){
                    /* token is a delimiter */

                    final String op = token.val;

                    if("(".equals(op)){
                        operators.push(op);
                    }
                    else if ("[".equals(op)){
                        operators.push(op);
                    }
                    else if (")".equals(op)){
                        /* eval (...) */
                        while (!operators.isEmpty() &&
                                        !"(".equals(operators.peek())){
                            final EvaluationResult<Void> stepResult = evaluateStep(files, operators, operands);

                            if (!stepResult.success){
                                result.error = stepResult.error;
                                return result;
                            }
                        }

                        if (operators.isEmpty() || !"(".equals(operators.pop())){
                            result.error = "Mismatched brackets ( ) in special construct expression.";
                            return result;
                        }
                    }
                    else if ("]".equals(op)){
                            /* eval substring a[...] */
                            while ((!operators.isEmpty()) &&
                                            (!"[".equals(operators.peek()))){
                                final EvaluationResult<Void> stepResult = evaluateStep(files, operators, operands);

                                if (!stepResult.success){
                                    result.error = stepResult.error;
                                    return result;
                                }
                            }

                            if (operators.isEmpty() || !"[".equals(operators.pop())){
                                result.error = "Mismatched brackets [ ] in special construct expression.";
                                return result;
                            }

                            /* proceed to evaluate substring */
                            if (operands.size() < 2){
                                result.error = "Insufficient operands for substring [ ] operation.";
                                return result;
                            }

                            final String[] formats = operands.pop();
                            final String[] exprs = operands.pop();
                            String[] ans = new String[n];

                            for(int i = 0; i < n; i++){
                                final String format = formats[i];
                                final String expr = exprs[i];

                                ans[i] = StringManipulator.substring(
                                                expr,
                                                format,
                                                SUBSTRING_RANGE_CHAR ,
                                                SUBSTRING_DELIMITER_CHAR);

                                if (ans[i] == null){
                                    result.error = "Invalid substring operation \"" +
                                                    expr + "[" + format + "]\".";

                                    return result;
                                }
                            }

                            /* push answer on operand stack */
                            operands.push(ans);
                        }
                        else{
                            /* all other operators */

                            /* eval stack if possible */
                            while (!operators.isEmpty() &&
                                            !"[]()".contains(operators.peek()) &&
                                            (OPERATOR_PRECEDENCE.get(op).intValue() <=
                                            OPERATOR_PRECEDENCE.get(operators.peek()).intValue())){
                                final EvaluationResult<Void> stepResult = evaluateStep(files, operators, operands);

                                if (!stepResult.success){
                                    result.error = stepResult.error;
                                    return result;
                                }
                            }

                            operators.push(op);
                        }
                    }
                    else{
                        /* token is an operand */

                        String[] tokenVals = new String[n];

                        if (NUMERIC_PATTERN.matcher(tokenVal).matches()){
                                /* token is a numeric value */
                                for (int i = 0; i < n; i++)
                                        tokenVals[i] = tokenVal;
                        }
                        else if (files[0].evaluateMacro(tokenVal) != null){
                            /* token is a register or macro */
                            for (int i = 0; i < n; i++)
                                    tokenVals[i] = files[i].evaluateMacro(tokenVal);
                        }
                        else{
                            /* treat as literal text */
                            for (int i = 0; i < n; i++)
                                    tokenVals[i] = tokenVal;
                        }

                        /* push evaluated token onto operands stack */
                        operands.push(tokenVals);
                    }
            }

            /* extract final result */
            if (operators.isEmpty() && (operands.size() == 1)){
                /* valid return value */
                result.success = true;
                result.output = operands.pop();
            }
            else{
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
	private EvaluationResult<Void> evaluateStep(
			final FileUnit[] files,
			final Deque<String> operators,
			final Deque<String[]> operands
        ){
            final int n = files.length;

            /* return value */
            final EvaluationResult<Void> result = new EvaluationResult<Void>();

            if (operators.isEmpty()){
                result.error = "Operators stack is empty.";
                return result;
            }

            final String op = operators.pop();

            if ("^".equals(op)){
                /**************************
                 * (1) EXPONENTIATION '^' *
                 **************************/

                if (operands.size() < 2){
                    result.error = "Insufficient operands for exponentiation '^' operation.";
                    return result;
                }

                final String[] args2 = operands.pop();
                final String[] args1 = operands.pop();

                /* check that arguments are all numeric */
                final int args1NonNumericIndex = getNonNumericIndex(args1);
                final int args2NonNumericIndex = getNonNumericIndex(args2);

                if ((args1NonNumericIndex >= 0) || (args2NonNumericIndex >= 0)){
                    int nonNumericIndex;
                    String nonNumericArg;

                    if (args1NonNumericIndex >= 0){
                        nonNumericIndex = args1NonNumericIndex;
                        nonNumericArg = args1[args1NonNumericIndex];
                    }
                    else{
                        nonNumericIndex = args2NonNumericIndex;
                        nonNumericArg = args2[args2NonNumericIndex];
                    }

                    result.error = "Invalid operand encountered for exponentiation '^' operation: " +
                                    "The operand \"" + nonNumericArg + "\" corresponding to " +
                                    SINGULAR_NOUN + " \"" + files[nonNumericIndex].source.getPath() +
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
            else if ("*".equals(op)){
                /**************************
                 * (2) MULTIPLICATION '*' *
                 **************************/

                if (operands.size() < 2){
                    result.error = "Insufficient operands for multiplication '*' operation.";
                    return result;
                }

                final String[] args2 = operands.pop();
                final String[] args1 = operands.pop();

                /* check that arguments are all numeric */
                final int args1NonNumericIndex = getNonNumericIndex(args1);
                final int args2NonNumericIndex = getNonNumericIndex(args2);

                if ((args1NonNumericIndex >= 0) || (args2NonNumericIndex >= 0)){
                    int nonNumericIndex;
                    String nonNumericArg;

                    if (args1NonNumericIndex >= 0){
                        nonNumericIndex = args1NonNumericIndex;
                        nonNumericArg = args1[args1NonNumericIndex];
                    }
                    else{
                        nonNumericIndex = args2NonNumericIndex;
                        nonNumericArg = args2[args2NonNumericIndex];
                    }

                    result.error = "Invalid operand encountered for multiplication '*' operation: " +
                                    "The operand \"" + nonNumericArg + "\" corresponding to " +
                                    SINGULAR_NOUN + " \"" + files[nonNumericIndex].source.getPath() +
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
            else if ("/".equals(op)){
                /********************
                 * (3) DIVISION '/' *
                 ********************/

                if (operands.size() < 2){
                    result.error = "Insufficient operands for division '/' operation.";
                    return result;
                }

                final String[] args2 = operands.pop();
                final String[] args1 = operands.pop();

                /* check that arguments are numeric */
                final int args1NonNumericIndex = getNonNumericIndex(args1);
                final int args2NonNumericIndex = getNonNumericIndex(args2);

                if ((args1NonNumericIndex >= 0) || (args2NonNumericIndex >= 0)){
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
                                        SINGULAR_NOUN + " \"" + files[nonNumericIndex].source.getPath() +
                                        "\" is non-numeric.";

                        operators.push(op);
                        operands.push(args1);
                        operands.push(args2);
                        return result;
                }

                String[] ans = new String[n];

                for (int i = 0; i < n; i++){
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
            else if ("-".equals(op)){
                /***********************
                 * (4) SUBTRACTION '-' *
                 ***********************/

                if (operands.size() < 2){
                    result.error = "Insufficient operands for subtraction '-' operation.";
                    return result;
                }

                final String[] args2 = operands.pop();
                final String[] args1 = operands.pop();

                /* check that arguments are numeric */
                final int args1NonNumericIndex = getNonNumericIndex(args1);
                final int args2NonNumericIndex = getNonNumericIndex(args2);

                if ((args1NonNumericIndex >= 0) || (args2NonNumericIndex >= 0)){
                    int nonNumericIndex;
                    String nonNumericArg;

                    if (args1NonNumericIndex >= 0){
                        nonNumericIndex = args1NonNumericIndex;
                        nonNumericArg = args1[args1NonNumericIndex];
                    }
                    else{
                        nonNumericIndex = args2NonNumericIndex;
                        nonNumericArg = args2[args2NonNumericIndex];
                    }

                    result.error = "Invalid operand encountered for subtraction '-' operation: " +
                                    "The operand \"" + nonNumericArg + "\" corresponding to " +
                                    SINGULAR_NOUN + " \"" + files[nonNumericIndex].source.getPath() +
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
            else if ("+".equals(op)){
                /********************
                 * (5) ADDITION '+' *
                 ********************/

                if (operands.size() < 2){
                    result.error = "Insufficient operands for addition '+' operation.";
                    return result;
                }

                final String[] args2 = operands.pop();
                final String[] args1 = operands.pop();
                String[] ans = new String[n];

                /* check if arguments are numeric */
                final int args1NonNumericIndex = getNonNumericIndex(args1);
                final int args2NonNumericIndex = getNonNumericIndex(args2);

                if ((args1NonNumericIndex < 0) && (args2NonNumericIndex < 0)){
                    /* add the two arguments */
                    for (int i = 0; i < n; i++)
                            ans[i] = (((int) Double.parseDouble(args1[i])) + ((int) Double.parseDouble(args2[i]))) + "";
                }
                else{
                    /* concatenate the two arguments */
                    for (int i = 0; i < n; i++)
                            ans[i] = args1[i] + args2[i];
                }

                /* push answer on operand stack */
                operands.push(ans);
                result.success = true;
            }
            else if (":".equals(op)){
                /********************************************
                 * (6) RANGE OPERATOR FOR SUBSTRING "[ : ]" *
                 ********************************************/

                if (operands.size() < 2){
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
            else if (",".equals(op)){
                /************************************************
                 * (7) DELIMITER OPERATOR FOR SUBSTRING "[ , ]" *
                 ************************************************/

                if (operands.size() < 2){
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
            else if ("~".equals(op)){
                /***************************************
                 * (8) UNARY MINUS (NEGATIVE) SIGN '~' *
                 ***************************************/

                if (operands.size() < 1){
                    result.error = "Insufficient operands for negative sign '-' operator.";
                    return result;
                }

                final String[] args1 = operands.pop();

                /* check that argument is numeric */
                final int nonNumericIndex = getNonNumericIndex(args1);

                if (nonNumericIndex >= 0){
                    final String nonNumericArg = args1[nonNumericIndex];

                    result.error = "Invalid operand encountered for negative sign '-' operator: " +
                                    "The operand \"" + nonNumericArg + "\" corresponding to " +
                                    SINGULAR_NOUN + " \"" + files[nonNumericIndex].source.getPath() +
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
                            "@@!".equals(op)){
                /*****************************
                 * (9) ENUMERATION OPERATORS *
                 *****************************/

                if (operands.size() < 1){
                    result.error = "Insufficient operands for enumeration operator " + op + ".";
                    return result;
                }

                /* sort files, and enumerate them accordingly */

                final String[] args1 = operands.pop();

                /* global and local order */
                int[] globalOrder = new int[n];
                int[] localOrder = new int[n];

                /* directory counts */
                int[] dirCount = new int[numDirs + 1];
                Arrays.fill(dirCount, 0);

                if (getNonNumericIndex(args1) < 0){
                    /* treat arguments as doubles */
                    DoubleEnumerationUnit[] eu = new DoubleEnumerationUnit[n];

                    for (int i = 0; i < n; i++)
                            eu[i] = new DoubleEnumerationUnit(i, Double.parseDouble(args1[i]));

                    Arrays.sort(eu);

                    for (int i = 0; i < n; i++){
                        final int index = eu[i].index;
                        final int parentDirId = files[index].parentDirId;
                        dirCount[parentDirId]++;
                        globalOrder[index] = i + 1;
                        localOrder[index] = dirCount[parentDirId];
                    }
                }
                else{
                    /* treat arguments as strings */
                    StringEnumerationUnit[] eu = new StringEnumerationUnit[n];

                    for (int i = 0; i < n; i++)
                            eu[i] = new StringEnumerationUnit(i, args1[i]);

                    Arrays.sort(eu);

                    for(int i = 0; i < n; i++){
                        final int index = eu[i].index;
                        final int parentDirId = files[index].parentDirId;
                        dirCount[parentDirId]++;
                        globalOrder[index] = i + 1;
                        localOrder[index] = dirCount[parentDirId];
                    }
                }

                String[] ans = new String[n];

                if ("#".equals(op)){
                    /* directory ordering */
                    for (int i = 0; i < n; i++)
                            ans[i] = localOrder[i] + "";
                }
                else if ("#!".equals(op)){
                    /* reverse directory ordering */
                    for (int i = 0; i < n; i++)
                            ans[i] = (files[i].localCount + 1 - localOrder[i]) + "";
                }
                else if ("##".equals(op)){
                    /* global ordering */
                    for (int i = 0; i < n; i++)
                            ans[i] = globalOrder[i] + "";
                }
                else if ("##!".equals(op)){
                    /* reverse global ordering */
                    for (int i = 0; i < n; i++)
                            ans[i] = (n + 1 - globalOrder[i]) + "";
                }
                else if ("@".equals(op)){
                    /* first elements in directory ordering */
                    int[] firsts = new int[numDirs + 1];

                    for (int i = 0; i < n; i++)
                            if (localOrder[i] == 1) firsts[files[i].parentDirId] = i;

                    for (int i = 0; i < n; i++)
                            ans[i] = args1[firsts[files[i].parentDirId]];
                }
                else if ("@!".equals(op)){
                    /* last elements in directory ordering */
                    int[] lasts = new int[numDirs + 1];

                    for (int i = 0; i < n; i++)
                            if (localOrder[i] == files[i].localCount) lasts[files[i].parentDirId] = i;

                    for (int i = 0; i < n; i++)
                            ans[i] = args1[lasts[files[i].parentDirId]];
                }
                else if ("@@".equals(op)){
                    /* look for first element */
                    int first = 0;

                    for (int i = 0; i < n; i++)
                            if (globalOrder[i] == 1) first = i;

                    Arrays.fill(ans, args1[first]);
                }
                else if ("@@!".equals(op)){
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
            else{
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
	private ArrayList<RenameFilePair> getRenameOperations(final ArrayList<FileUnit> matchedFiles){
            /* determine target files, check validity, and detect clashes */
            final Map<File,FileUnit> targetFilesMap = new TreeMap<File,FileUnit>();

            for(FileUnit u : matchedFiles){
                final String targetFilename = u.targetFilename.toString();

                /* check for empty file/directory name */
                if(targetFilename.isEmpty())throw new TerminatingException("Invalid target " + SINGULAR_NOUN + " name encountered:\n" +
                                        "\"" + u.source.getPath() + "\" ---> \"" + targetFilename + "\"");

                if (targetFilename.contains(File.separator)){
                        /* contains a filename separator, so we rename          */
                        /* file/directory relative to the present work directory */
                        u.target = new File(targetFilename);
                }
                else{
                    /* does not contain filename separator, so we        */
                    /* rename file/directory in its original subdirectory */
                    u.target = new File(u.source.getParentFile(), targetFilename);
                }

                try{
                    /* get canonical pathname */
                    u.target = new File(u.target.getCanonicalFile().getParentFile(),
                                    u.target.getName());
                }
                catch (Exception e){
                    throw new TerminatingException("Invalid target " + SINGULAR_NOUN + " name encountered:\n" +
                                        "\"" + u.source.getPath() + "\" ---> \"" + targetFilename + "\"\n");
                }

                /* check for clash (i.e. nonunique target filenames) */
                final FileUnit w = targetFilesMap.get(u.target);

                if(w == null){
                    targetFilesMap.put(u.target, u);
                }
                else{
                    throw new TerminatingException("Target " + SINGULAR_NOUN + " name clash:\n" +
                            "[A] \"" + w.source.getPath() + "\"\n  ---> \"" + w.target.getPath() + "\"\n" +
                            "[B] \"" + u.source.getPath() + "\"\n  ---> \"" + u.target.getPath() + "\"");
                }
            }

            /* create (source,target) rename pairs, and determine renaming sequence */
            final NavigableMap<File,LinkedList<RenameFilePair>> sequenceHeads = new TreeMap<File,LinkedList<RenameFilePair>>();
            final NavigableMap<File,LinkedList<RenameFilePair>> sequenceTails = new TreeMap<File,LinkedList<RenameFilePair>>();

            for(FileUnit u : matchedFiles){
                /* check for unnecessary rename operations */
                if (u.source.getPath().equals(u.target.getPath()))
                        continue;

                /* look for a sequence head with source = this target */
                final LinkedList<RenameFilePair> headSequence = sequenceHeads.get(u.target);

                /* look for a sequence tail with target = this source */
                final LinkedList<RenameFilePair> tailSequence = sequenceTails.get(u.source);

                if ((headSequence == null) && (tailSequence == null)){
                    /* add this pair as a new sequence */
                    final LinkedList<RenameFilePair> ns = new LinkedList<RenameFilePair>();
                    ns.add(new RenameFilePair(u.source, u.target));
                    sequenceHeads.put(u.source, ns);
                    sequenceTails.put(u.target, ns);

                }
                else if ((headSequence != null) && (tailSequence == null)){
                    /* add this pair to the head of an existing sequence */
                    headSequence.addFirst(new RenameFilePair(u.source, u.target));
                    sequenceHeads.remove(u.target);
                    sequenceHeads.put(u.source, headSequence);
                }
                else if ((headSequence == null) && (tailSequence != null)){
                    /* add this pair to the tail of an existing sequence */
                    tailSequence.addLast(new RenameFilePair(u.source, u.target));
                    sequenceTails.remove(u.source);
                    sequenceTails.put(u.target, tailSequence);
                }
                else if ((headSequence != null) && (tailSequence != null)){
                    if (headSequence == tailSequence){
                        /* loop detected, so we use a temporary target file/directory name */

                        /* create a temporary file/directory name */
                        final File parentDir = u.target.getParentFile();
                        final String filename = u.target.getName();

                        File temp =  new File(parentDir, filename + ".rw");

                        if (temp.exists() || targetFilesMap.containsKey(temp)){
                            /* temp filename is already used; find another temp filename */
                            for (long i = 0; i < Long.MAX_VALUE; i++){
                                temp = new File(parentDir, filename + ".rw." + i);

                                if (temp.exists() || targetFilesMap.containsKey(temp)){
                                    /* temp filename is already used; find another temp filename */
                                    temp = null;
                                }
                                else{
                                    /* found an unused name */
                                    targetFilesMap.put(temp, null);
                                    break;
                                }
                            }
                        }

                        if (temp == null)
                                throw new TerminatingException("Ran out of suffixes for temporary name of " +
                                                SINGULAR_NOUN + " \"" + u.target.getPath() + "\".");

                        /* add a leading and trailing rename file pair to the existing sequence */
                        headSequence.addFirst(new RenameFilePair(temp, u.target));
                        tailSequence.addLast(new RenameFilePair(u.source, temp));
                        sequenceHeads.remove(u.target);
                        sequenceHeads.put(temp, headSequence);
                        sequenceTails.remove(u.source);
                        sequenceTails.put(temp, tailSequence);
                    }
                    else{
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
            final ArrayList<RenameFilePair> renameOperations = new ArrayList<RenameFilePair>();

            /* sequence deeper subdirectories for renaming first (approx), if renaming directories */
            final NavigableMap<File,LinkedList<RenameFilePair>> sequences =
                            renameDirectories ? sequenceHeads.descendingMap() : sequenceHeads;

            for (LinkedList<RenameFilePair> s : sequences.values()){
                /* get reversed order of rename file pairs within the sequence */
                final Iterator<RenameFilePair> iter = s.descendingIterator();

                while (iter.hasNext()){
                    final RenameFilePair r = iter.next();

                    if (!r.source.getPath().equals(r.target.getPath()))
                            renameOperations.add(r);
                }
            }

            return renameOperations;
	}

        /*
         * Nicolas Franck
         */
        public void setRenameListener(final RenameListener renameListener){
            this.renameListener = renameListener;
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
	private boolean promptUserOnRename(final ArrayList<FileUnit> matchedFiles,final int numRenameOperations){
            
            /* no rename operations to perform */
            if(numRenameOperations == 0)return false;
            /* prompt user on whether to continue with renaming operations */            
            if(renameListener != null){
                return renameListener.approveList(matchedFiles);
            }
            return true;
	}


	/**
	 * Perform rename operations on files/directories.
	 *
	 * @param renameOperations
	 *     Sequence of rename operations to be performed
	 * @return
	 *     Number of successful rename operations performed
	 */
	private int performRenameOperations(final ArrayList<RenameFilePair> renameOperations){

          
            for(int i = 0; i < renameOperations.size(); i++){            

                final RenameFilePair r = renameOperations.get(i);

                /*
                 * Nicolas Franck
                 */
                r.setSimulateOnly(this.isSimulateOnly());

                

                if(renameListener != null){
                    renameListener.onRenameStart(r);
                }

                OnErrorAction action = null;
                    
                
                if(this.isSimulateOnly()){
                   
                    if(!isOverWrite() && r.target.exists()){
                        r.success = false;
                        if(renameListener != null){
                            action = renameListener.onError(r, RenameError.TARGET_EXISTS,"target file "+r.target.getAbsolutePath()+" already exists");
                        }
                    }else if(!r.target.getParentFile().canWrite()){
                        action = renameListener.onError(r, RenameError.SYSTEM_ERROR,"cannot write to "+r.target.getAbsolutePath());
                    }else{
                        r.success = true;
                    }

                }else{
                    
                    if(!isOverWrite() && r.target.exists()){
                        r.success = false;
                        if(renameListener != null){
                            action = renameListener.onError(r, RenameError.TARGET_EXISTS,"target file "+r.target.getAbsolutePath()+" already exists");
                        }
                    }else{
                        r.target.getParentFile().mkdirs();
                        String error = null;
                        try{
                            if(isCopy()){
                                copy(r.source,r.target);
                            }else{
                                /*
                                 *  TODO: in Windows wordt target niet overschreven.
                                    Zie: http://stackoverflow.com/questions/595631/how-to-atomically-rename-a-file-in-java-even-if-the-dest-file-already-exists
                                 */                              
                                r.success = r.source.renameTo(r.target);                              
                            }
                        }catch(Exception e){                            
                            r.success = false;
                            error = e.getMessage();
                        }
                        if(!r.success){
                            if(renameListener != null){
                                action = renameListener.onError(r,RenameError.SYSTEM_ERROR,error);
                            }
                        }
                    }
                }

                if(action == null)action = defaultActionOnRenameOperationError;                

                /* check if renaming operation was successful */
                if(!r.success){

                    /* take action */
                    if(action == OnErrorAction.retry){
                        /* retry rename operation */
                        i--;
                    }
                    else if(action == OnErrorAction.skip){
                        /* skip to next file/directory */
                        continue;
                    }
                    else if(action == OnErrorAction.undoAll){

                        if(!this.isSimulateOnly()){

                            for(int j = i ; j >= 0; j--){
                                final RenameFilePair t = renameOperations.get(j);

                                if(t.success){

                                    String error = null;
                                    t.source.getParentFile().mkdirs();
                                    try{
                                        if(isCopy()){
                                            if(t.target.exists()){
                                                t.target.delete();
                                            }
                                        }else{
                                            t.success = !t.target.renameTo(t.source);
                                        }
                                    }catch(SecurityException e){                                      
                                        t.success = true;
                                        error = e.getMessage();
                                    }

                                    if(t.success)warnings.add("Rename operation failed.");
                                }
                            }
                        }

                        break;
                    }
                    else if(action == OnErrorAction.abort){
                        /* abort */
                        break;
                    }
                }else{
                    renameListener.onRenameSuccess(r);
                }

                if(renameListener != null){
                    renameListener.onRenameEnd(r);
                }
            }

            /* return value */
            int num = 0;

            for(RenameFilePair r : renameOperations)
                if(r.success)num++;

            return num;
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
	private String padString(final String in,final int len,final boolean isNumeric){
            /* return value */
            final StringBuilder out = new StringBuilder();

            /* number of additional characters to insert */
            final int padLen = len - in.length();

            if (isNumeric){
                /* pad with leading zeros */
                final Matcher numericMatcher = NUMERIC_PATTERN.matcher(in);

                if(numericMatcher.matches()){
                    /* match groups */
                    final String sign = numericMatcher.group(1);
                    final String val = numericMatcher.group(2);
                    if (sign != null)out.append(sign);
                    for (int i = 0; i < padLen; i++)out.append('0');
                    out.append(val);
                    return out.toString();
                }
            }

            /* pad with trailing spaces */
            out.append(in);
            for (int i = 0; i < padLen; i++)out.append(' ');
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
	private boolean isNumeric(final String arg){
            return (!arg.isEmpty() && NUMERIC_PATTERN.matcher(arg).matches());
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
	private int getNonNumericIndex(final String[] args){
            /* check if all arguments are numeric */
            for (int i = 0; i < args.length; i++){
                /* check if empty string */
                if (args[i].isEmpty())return i;

                /* check if string matches numeric pattern */
                if (!NUMERIC_PATTERN.matcher(args[i]).matches())return i;
            }
            /* all strings are numeric */
            return -1;
	}

        /*
         * Nicolas Franck
         */
        private CleanListener cleanListener;
        private String [] [] substitutes = new String [] [] {
            { "[^a-zA-Z0-9-_.]","_" },
            { "_+","_" },
            { "_$","" }
        };
        private HashMap<String,Pattern> patternMap = new HashMap();
        private ArrayList<File>cleanCandidates = new ArrayList<File>();
        private ArrayList<RenameFilePair>cleanPairs = new ArrayList<RenameFilePair>();
        private boolean cleanDirectories = false;

        public String[][] getSubstitutes() {
            return substitutes;
        }
        public void setSubstitutes(String[][] substitutes) {
            this.substitutes = substitutes;
        }
        public boolean isCleanDirectories() {
            return cleanDirectories;
        }
        public void setCleanDirectories(boolean cleanDirectories) {
            this.cleanDirectories = cleanDirectories;
        }
        public ArrayList<File> getCleanCandidates(){
            return cleanCandidates;
        }
        public void setCleanCandidates(ArrayList<File> cleanCandidates) {
            this.cleanCandidates = cleanCandidates;
            findCleanPairs();
        }
        public ArrayList<RenameFilePair> getCleanPairs() {
            return cleanPairs;
        }
        public void setCleanPairs(ArrayList<RenameFilePair> cleanPairs) {
            this.cleanPairs = cleanPairs;
        }
        public CleanListener getCleanListener(){
            if(cleanListener == null)cleanListener = new CleanListenerAdapter();
            return cleanListener;
        }
        public void setCleanListener(CleanListener cleanListener) {
            this.cleanListener = cleanListener;
        }
        protected Pattern getPattern(String patternString){
            Pattern pattern = null;           
            if(patternMap.containsKey(patternString))return patternMap.get(patternString);
            try{
                pattern = Pattern.compile(patternString);
                patternMap.put(patternString,pattern);
            }catch(Exception e){                
            }
            return pattern;
        }
        protected void findCleanPairs(){

            ArrayList<RenameFilePair> pairs = new ArrayList<RenameFilePair>();

            for(File source:getCleanCandidates()){

                if(!isCleanDirectories() && source.isDirectory())continue;

                String parent = source.getParent();
                String sourceName = source.getName();
                String suffix = "";
                int lastIndex = sourceName.lastIndexOf('.');
                if(lastIndex >= 0){
                    suffix = sourceName.substring(lastIndex);
                    sourceName = sourceName.substring(0,lastIndex);
                }
                String destinationName = sourceName;

                for(String [] substitutePair:substitutes){
                    String sourcePattern = substitutePair[0];
                    String destinationPattern = substitutePair[1];
                    Pattern pattern = getPattern(sourcePattern);
                    destinationName = pattern.matcher(destinationName).replaceAll(destinationPattern);
                    if(suffix.compareTo("") != 0){
                        suffix = pattern.matcher(suffix).replaceAll(destinationPattern);
                    }
                }
                String sourcePath = parent+"/"+sourceName+suffix;
                String destinationPath = parent+"/"+destinationName+suffix;
                File destination = new File(destinationPath);

                if(sourcePath.compareTo(destinationPath) != 0){
                    pairs.add(new RenameFilePair(source,destination));
                }
            }
            setCleanPairs(pairs);
        }
        public void clean(){
            int numSuccess = _clean();
            getCleanListener().onEnd(getCleanPairs(),numSuccess);
        }
        private int _clean(){
            CleanListener cl = getCleanListener();

            int numSuccess = 0;

            cl.onInit(getCleanPairs());

            boolean undoAll = false;

            for(RenameFilePair pair:getCleanPairs()){

                pair.setSimulateOnly(this.isSimulateOnly());
                
                if(!cl.doClean(pair))continue;

                boolean retry = false;
                OnErrorAction errorAction = OnErrorAction.ignore;

                do{
                    retry = false;
                    RenameError renameError = null;
                    String renameErrorStr = null;
                    pair.success = false;

                    cl.onCleanStart(pair);

                    try{
                        if(pair.target.exists() && !isOverWrite())
                            throw new IOException(pair.target.getAbsolutePath()+"' already exists");
                        if(!pair.target.getParentFile().canWrite())
                            throw new IOException("cannot write to parent directory '"+pair.target.getParentFile().getAbsolutePath()+"'");
                        if(!isSimulateOnly()){
                            if(isCopy()){
                                copy(pair.source,pair.target);
                                pair.success = true;
                            }else{
                                pair.success = pair.source.renameTo(pair.target);
                            }
                        }else{
                            pair.success = true;
                        }
                    }catch(SecurityException e){                        
                        //zelden: indien een SecurityManager bestaat (vooral in Applets)
                        pair.success = false;
                        renameError = RenameError.SECURITY_EXCEPTION;
                        renameErrorStr = e.getLocalizedMessage();                        
                    }catch(IOException e){                        
                        pair.success = false;
                        renameError = renameError.IO_EXCEPTION;
                        renameErrorStr = e.getMessage();
                    }finally{
                        //soms geeft copy of rename geen enkele error..
                        if(!pair.success && renameError == null){
                            renameError = RenameError.UNKNOWN_ERROR;
                        }
                    }
                    
                    
                    if(pair.success){                                               
                        cl.onCleanSuccess(pair);

                    }else{                        
                        errorAction = cl.onError(pair,renameError,renameErrorStr);
                        if(errorAction == OnErrorAction.retry)
                            retry = true;                        
                    }

                    cl.onCleanEnd(pair);

                }while(retry);
                
                if(!pair.success){
                    if(errorAction == OnErrorAction.abort){                        
                        return numSuccess;
                    }
                    else if(errorAction == OnErrorAction.undoAll)
                        undoAll = true;                                            
                }else{
                    numSuccess++;
                }               
                
            }
            
            if(undoAll){
                
                for(int i = getCleanPairs().size() - 1;i >= 0;i--){
                    RenameFilePair pair = getCleanPairs().get(i);
                    
                    if(!pair.success)
                        continue;
                    pair.source.getParentFile().mkdirs();
                    try{
                        
                        if(isCopy()){
                            
                            if(pair.target.exists()){
                                
                                pair.success = !pair.target.delete();
                            }
                        }else{
                            pair.success = !pair.target.renameTo(pair.source);
                        }
                    }catch(SecurityException e){
                        pair.success = true;
                    }
                }
            }
            return numSuccess;
        }
        public static void main(String [] args){
            try{

                CleanListener cl = new CleanListenerAdapter(){
                    @Override
                    public void onInit(ArrayList<RenameFilePair>pairs) {
                        System.out.println("CleanListener::onInit() => "+pairs.size()+" pairs found");
                    }
                    @Override
                    public void onCleanStart(RenameFilePair pair) {
                        System.out.println("CleanListener::onRenameStart() "+pair.source.getAbsolutePath()+" => "+pair.target.getAbsolutePath());
                    }
                    @Override
                    public OnErrorAction onError(RenameFilePair pair,RenameError errorType,final String errorStr) {
                        System.out.println("CleanListener::onError() "+pair.source.getAbsolutePath()+" => "+pair.target.getAbsolutePath());
                        System.out.println(errorStr);
                        return OnErrorAction.undoAll;
                    }
                    @Override
                    public void onEnd(ArrayList<RenameFilePair> pairs,int numSuccess) {
                        System.out.println("CleanListener::onEnd(), numSuccess:"+numSuccess);
                    }
                };

                RenameWand renamer = new RenameWand();
                renamer.setCleanListener(cl);                
                renamer.setCleanCandidates(helper.FileUtils.listFiles("/home/nicolas/brol"));
                renamer.setCopy(true);
                renamer.clean();
                

            }catch(Exception e){
                e.printStackTrace();
            }

        }
}
