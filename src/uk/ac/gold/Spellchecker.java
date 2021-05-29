/**
 *  Spellchecker checks a file for spelling mistakes and if a mistake is found, presents options to add the word to the dictionary or enter a replacement word. 
 */

package uk.ac.gold;
import java.io.*;
import java.util.*;

/**
 * @author mpeev001
 *
 */

public class Spellchecker {
	
	// **********************************************
    // Fields
    // **********************************************
	
	private final File DICTIONARY = new File("Dictionary.txt");
	private final String PATH = "";
	private final String SPECIALCHARS = " ~:'+[\\\\@^{%(-\"*|'&<`._=]!>;?#$)/,";
	private final String CHARS = "abcdefghijklmnopqrstuvwxyz";
	private File input;
	private FileOutputStream outputFOS;
	private PrintStream output;
	private File localDictionary;
	private String[] addedWords = new String[0];
	
	/**
	 * @return DICTIONARY dictionary file
	 */
	public File getDictionary() {
		return this.DICTIONARY;
	}
	
	/**
	 * @return PATH path to file
	 */
	public String getPath() {
		return this.PATH;
	}
	
	/**
	 * @return SPECIALCHARS special characters
	 */
	public String getSpecialChars() {
		return this.SPECIALCHARS;
	}
	
	/**
	 * @return CHARS characters
	 */
	public String getChars() {
		return this.CHARS;
	}
	
	/**
	 * @param input input file
	 */
	public void setInput(String input){
		this.input = new File(this.getPath()+input);
	}
	
	/**
	 * @return input input file
	 */
	public File getInput() {
		return this.input;
	}
	
	/**
	 * @return output output file
	 * @throws IOException
	 */
	public FileOutputStream getOutputFOS() throws IOException {
		return this.outputFOS;
	}
	
	/**
	 * @param output output file
	 * @throws IOException
	 */
	public void setOutputFOS(String output) throws IOException {
		File file = new File(this.getPath()+"temp-"+output);
		this.outputFOS = new FileOutputStream(file);
	}
	/**
	 * @return output output file
	 * @throws IOException
	 */
	public PrintStream getOutput() throws IOException {
		return this.output;
	}
	
	/**
	 * @param output output file
	 * @throws IOException
	 */
	public void setOutput(FileOutputStream fileOutputStream) throws IOException {
		this.output = new PrintStream(fileOutputStream);
	}
	
	/**
	 * @return localDictionary Local Dictionary
	 * @throws IOException
	 */
	public File getLocalDictionary() throws IOException {
		return this.localDictionary;
	}
	
	/**
	 * Set Local Dictionary
	 * @throws IOException
	 */
	public void setLocalDictionary() throws IOException {
		this.localDictionary = new File(this.getPath()+"LocalDictionary.txt");
	}
	
	/**
	 * @return addedWords Added Words
	 */
	public String[] getAddedWords() {
		String[] copy = new String[this.addedWords.length];
	    System.arraycopy(this.addedWords, 0, copy, 0, copy.length);
	    return copy;
	}
	
	/**
	 * @param addedWords Added Words
	 */
	public void setAddedWords(String[] addedWords) {
		this.addedWords = new String[addedWords.length];
		System.arraycopy(addedWords, 0, this.addedWords, 0, addedWords.length);
	}
	
	/**
	 * @param args Arguments
	 * @throws IOException
	 */
	public void getNotInDictionaryMethods(Object[] args) throws IOException {
		askWordNotInDictionary((String)args[0],
							   (PrintStream) args[1],
							   (Scanner) args[2],
							   (Object) args[3], 
							   (String) args[4],
							   (Object[]) args[5],
							   (String) args[6]);
	}
	
	// **********************************************
    // Constructor
    // **********************************************
	
	/**
	 * @param input Default Constructor
	 * @throws IOException
	 */
	public Spellchecker(String input) throws IOException {
		this.setInput(input);
		this.setOutputFOS(input);
		this.setOutput(this.getOutputFOS());
		this.setLocalDictionary();
	}

	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
																																																																																																																																																																																																																																																																																																																																																																
		System.out.println("Enter file path: ");
		String file = scan.next().trim();
		Spellchecker app = new Spellchecker(file);
		
		
		readInputFile(app.getDictionary(),
					  app.getLocalDictionary(),
					  app.getInput(),
					  app.getOutput(),
					  app.getSpecialChars(),
					  app.getChars(),
				 	  scan,
				 	  app);
		
		addNewWordsToDictionary(app.getDictionary(), 
								app.getLocalDictionary(),
								app.getChars(),
								app.getAddedWords());
		copyLocalDictionary(app.getDictionary(), 
							app.getLocalDictionary());
		
		scan.close();
		app.getOutputFOS().close();
		app.getOutput().close();
	}
	
	// **********************************************
    // Public static methods
    // **********************************************
	
	/**
	 * Reads the input file and presents options to add the word to dictionary or add a replacement word
	 * @param dictionary Dictionary
	 * @param localDictionary Local Dictionary
	 * @param input Input file
	 * @param output Output file
	 * @param specialChars Special Characters
	 * @param chars Characters
	 * @param scan Scanner
	 * @param obj The application
	 * @throws IOException
	 */
	public static void readInputFile(File dictionary,
									 File localDictionary,
									 File input,
									 PrintStream output,
									 String specialChars,
									 String chars,
									 Scanner scan,
									 Object obj) throws IOException {
		
		FileReader frD = new FileReader(dictionary);
		BufferedReader brD = new BufferedReader(frD);
		FileReader frI = new FileReader(input);
		BufferedReader brI = new BufferedReader(frI);
		PrintWriter pwLD = new PrintWriter(localDictionary);
		pwLD.close();
		FileOutputStream fosLD = new FileOutputStream(localDictionary);
		PrintStream psLD = new PrintStream(fosLD);
		
		String word = "";
		String dWord = "";
		String intWord = "";
		String intDWord = "";
		String lcWord = "";
		String answer = "";
		Object[] nextChar = new Object[1];
		boolean after = false;
		int n;
		String m;
		while((n = brI.read()) != -1) {
			nextChar[0] = (char)n;
			if(specialChars.contains(nextChar[0].toString())) {
				if(word.equals("")) {
					output.print(" ");
					continue;
				}
				lcWord = word.toLowerCase();
				intWord = convertStringToInt(chars, lcWord);
				frD = new FileReader(dictionary);
				brD = new BufferedReader(frD);
				fosLD = new FileOutputStream(localDictionary);
				psLD = new PrintStream(fosLD);
				answer = "";
				after = false;
				while((m = brD.readLine()) != null) {
					dWord = m.toLowerCase();
					psLD.print(dWord+"\n");
					intDWord = convertStringToInt(chars, dWord);
					if(compareStrings(intWord,intDWord) && !after) {
						output.print(word);
						after = true;
					} else if(largerThanString(chars,dWord, lcWord) && !after) {
						System.out.println(word+" is not in the Dictionary.");
						Object wordObj = word;
						Object outputObj = output;
						Object scanObj = scan;
						Object objObj = (Spellchecker) obj;
						Object lcWordObj = lcWord;
						Object nextCharObj = nextChar;
						Object answerObj = answer;
						Object[] args = { wordObj, outputObj, scanObj, objObj, lcWordObj, nextCharObj, answerObj };
						((Spellchecker)obj).getNotInDictionaryMethods(args);
						after = true;
					}
				}
				((Spellchecker) obj).getOutput().print(nextChar[0]);
				fosLD.close();
				psLD.close();
				frD.close();
				brD.close();
				word = "";
				copyLocalDictionary(((Spellchecker) obj).getDictionary(), 
									((Spellchecker) obj).getLocalDictionary());
			} else {
				word += nextChar[0];
			}
			
		}
		frD.close();
		brD.close();
		frI.close();
		brI.close();
		psLD.close();
		fosLD.close();
	}
	
	/**
	 * Presents options if the word is not in the dictionary
	 * @param word The word
	 * @param output Output file
	 * @param scan Scanner
	 * @param obj The application
	 * @param lcWord Lower case word
	 * @param nextChar Next character
	 * @param answer Answer
	 * @throws IOException
	 */
	public static void askWordNotInDictionary(String word, 
											  PrintStream output, 
											  Scanner scan,
											  Object obj,
											  String lcWord,
											  Object[] nextChar,
											  String answer) throws IOException {
		System.out.println("Add to Dictionary? (y/n)");
		answer = scan.next().trim();
		if(answer.equals("y")) {
			output.print(word);
			addNewWord(((Spellchecker) obj), lcWord);
		} else if(answer.equals("n")) {
			useReplacementWord(word, output, scan, nextChar, answer);
		}
	}
	
	/**
	 * Offers the option to use a replacement word
	 * @param word The word
	 * @param output Output file
	 * @param scan Scanner
	 * @param nextChar Next character
	 * @param answer Answer
	 * @throws IOException
	 */
	public static void useReplacementWord(String word, 
			 							  PrintStream output, 
			 							  Scanner scan,
			 							  Object[] nextChar,
			 							  String answer) throws IOException {
		System.out.println("Would you like to enter a replacement word? (y/n)");
		answer = scan.next().trim();
		if(answer.equals("y")) {
			System.out.println("Enter a replacement word:");
			answer = scan.next().trim();
			output.print(answer);
		} else if(answer.equals("n")) {
			output.print(word);
		}
	}
	
	/**
	 * Converts a string to a string of integers
	 * @param chars Characters
	 * @param str String to convert
	 * @return string of integers
	 */
	public static String convertStringToInt(String chars, String str) {
		String intStr = "";
		for(int j = 0; j < str.length(); j++) {
			intStr += Integer.toString(chars.indexOf(str.charAt(j))+1);
		}
		return intStr;
	}
	
	/**
	 * Compares two strings
	 * @param str1 String 1
	 * @param str2 String 2
	 * @return boolean True or False
	 */
	public static boolean compareStrings(String str1, String str2) {
		if(str1.length() != str2.length()) {
			return false;
		} else {
			for(int i = 0; i < str1.length(); i++) {
				if(str1.charAt(i) != str2.charAt(i)) {
					return false;
				}
			}
		} 
		return true;
	}
	/**
	 * Checks if a string is before or after another alphabetically
	 * @param chars Characters
	 * @param str1 String 1
	 * @param str2 String 2
	 * @return boolean True or False
	 */
	public static boolean largerThanString(String chars, String str1, String str2) {

		int length;
		if(str1.length() < str2.length()) {
			length = str1.length();
		} else {
			length = str2.length();
		}
		for(int i = 0; i < length; i++) {
			if(str1.charAt(i) < str2.charAt(i)) {
				return false;
			} else if(str1.charAt(i) > str2.charAt(i)){
				return true;
			} 
			if(i == str1.length() - 1 && i < str2.length() - 1) {
				return false;
			} else if(i == str2.length() - 1 && i < str1.length() - 1){
				return true;
			}
		}
		return true;
	}
	/**
	 * Add new word to be added to the dictionary
	 * @param obj The application
	 * @param word The word to be added
	 */
	public static void addNewWord(Object obj, String word) {
		String[] copy = new String[((Spellchecker) obj).getAddedWords().length + 1];
	    System.arraycopy(((Spellchecker) obj).getAddedWords(), 0, copy, 0, copy.length - 1);
	    copy[copy.length - 1] = word;
	    ((Spellchecker) obj).setAddedWords(copy);
	}
	
	/**
	 * Adds new words to the dictionary
	 * @param dictionary Dictionary
	 * @param localDictionary Local Dictionary
	 * @param chars Characters
	 * @param addedWords Words to add
	 * @throws IOException, NumberFormatException, ArrayIndexOutOfBoundsException
	 */
	public static void addNewWordsToDictionary(File dictionary, 
											   File localDictionary, 
											   String chars, 
											   String[] addedWords) throws IOException, 
																																NumberFormatException,
																																ArrayIndexOutOfBoundsException {
		if(addedWords.length > 0) {
			String copy;
			for(int i = 1; i < addedWords.length; i++) {
				for(int j = 0; j < addedWords.length - 1; j++) {
					if(largerThanString(chars, addedWords[j],addedWords[j+1])) {
						copy = addedWords[j+1];
						addedWords[j+1] = addedWords[j];
						addedWords[j] = copy;
					}
				}
			}
			String n;
			PrintWriter prLD = new PrintWriter(localDictionary);
			FileReader frD = new FileReader(dictionary);
			BufferedReader brD = new BufferedReader(frD);
			int i = 0;
			int count = 0;
			while((n = brD.readLine()) != null) {
				if(largerThanString(chars, n, addedWords[i])) {
					count++;
					if(count == 1) {
						prLD.println(addedWords[i]);
					}
					if(i < addedWords.length - 1) {
						count = 0;
						i++;
					}
				}
				prLD.println(n);
			}
			frD.close();
			brD.close();
			prLD.close();
		}
		System.out.println("Done!");
	}
	/**
	 * Copies the local dictionary to the dictionary
	 * @param dictionary Dictionary
	 * @param localDictionary Local Dictionary
	 * @throws IOException
	 */
	public static void copyLocalDictionary(File dictionary, File localDictionary) throws IOException {
		FileReader frLD = new FileReader(localDictionary);
		BufferedReader brLD = new BufferedReader(frLD);
		PrintWriter prD = new PrintWriter(dictionary);
		String n;
		while((n = brLD.readLine()) != null) {
			prD.println(n);
		}
		prD.close();
		brLD.close();
		frLD.close();
	}
}
