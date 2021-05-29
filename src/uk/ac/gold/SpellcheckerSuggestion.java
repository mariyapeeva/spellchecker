/**
 *  SpellcheckerSuggestion checks a file for spelling mistakes and if a mistake is found, presents a list of suggestions to select from or options to add the word to the dictionary or enter a replacement word. 
 */
package uk.ac.gold;

import java.io.*;
import java.util.*;

/**
 * @author mpeev001
 *
 */
public class SpellcheckerSuggestion extends Spellchecker {
	
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
							   (String) args[6],
							   (File) this.getDictionary());
	}
	
	/**
	 * Default constructor
	 * @param input
	 * @throws IOException
	 */
	public SpellcheckerSuggestion(String input) throws IOException {
		super(input);
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Scanner scan = new Scanner(System.in);
		System.out.println(calcLevenshteinDistance("gjgpdsghs","djkv"));
		System.out.println("Enter file path: ");
		String file = scan.next().trim();
		
		SpellcheckerSuggestion app = new SpellcheckerSuggestion(file);
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
	
	/**
	 * Presents suggestions and options if the word is not in the dictionary
	 * @param word The word
	 * @param output Output file
	 * @param scan Scanner
	 * @param obj The application
	 * @param lcWord Lower case word
	 * @param nextChar Next character
	 * @param answer Answer
	 * @param dictionary Dictionary
	 * @throws IOException
	 */
	public static void askWordNotInDictionary(String word, 
											  PrintStream output, 
											  Scanner scan,
											  Object obj,
											  String lcWord,
											  Object[] nextChar,
											  String answer,
											  File dictionary) throws IOException {
		
		
		int lCount = 0;
		String[] suggestions = new String[0];
		String p;
		System.out.println("Did you mean one of the suggestions below?");
		FileReader frD2 = new FileReader(dictionary);
		BufferedReader brD2 = new BufferedReader(frD2);
		
		while((p = brD2.readLine()) != null) {
			if(calcLevenshteinDistance(p, lcWord) < 2) {
				addStrToArr(p, suggestions);
				System.out.println("["+(++lCount)+"] "+p+" "+calcLevenshteinDistance(p, lcWord) + " "+lcWord);
			}
		}
		brD2.close();
		frD2.close();
		System.out.println("Enter a suggestion: (suggestion/n)");
		answer = scan.next().trim();
		if(!answer.equals("n")) {
			output.print(answer);
		} else {
			System.out.println("Add to Dictionary? (y/n)");
			answer = scan.next().trim();
			if(answer.equals("y")) {
				output.print(word);
				addNewWord(((Spellchecker) obj), lcWord);
			} else if(answer.equals("n")) {
				useReplacementWord(word, output, scan, nextChar, answer);
			}
		}
	}
	
	/**
	 * Add string to array
	 * @param str String
	 * @param arr Array
	 * @return copy The array
	 */
	public static String[] addStrToArr(String str, String[] arr) {
		String[] copy = new String[arr.length + 1];
	    System.arraycopy(arr, 0, copy, 0, copy.length - 1);
	    copy[copy.length - 1] = str;
	    return copy;
	}
	
	/**
	 * Add integer to array
	 * @param ind String
	 * @param arr Array
	 * @return copy The array
	 */
	public static int[] addIntToArr(int ind, int[] arr) {
		int[] copy = new int[arr.length + 1];
	    System.arraycopy(arr, 0, copy, 0, copy.length - 1);
	    copy[copy.length - 1] = ind;
	    return copy;
	}
	
	/**
	 * Check if integer is in an array
	 * @param ind String
	 * @param arr Array
	 * @return copy The array
	 */
	public static int checkIntArr(int ind, int[] arr) {
		for(int i = 1; i <= arr.length; i++) {
			if(arr[i-1] == ind) {
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * Find missing characters in str2
	 * @param str1 String 1
	 * @param str2 String 2
	 * @return arr Array of indexes of missing characters in str2 
	 */
	public static int[] getMissingCharsFromString(String str1, String str2) {
		int[] arr = new int[0];
		boolean equal = false;
		for(int i = 1; i <= str2.length(); i++) {
			equal = false;
			for(int j = 1; j <= str1.length(); j++) {
				if(str1.charAt(j-1) == str2.charAt(i-1)) {
					equal = true;
					break;
				}
			}
			if(!equal) {
				arr = addIntToArr(i, arr);
			}
		}
		return arr;
	}
	
	/**
	 * Check Levenshtein Distance between 2 words
	 * @param str1 String 1
	 * @param str2 String 2
	 * @return totalDistance Levenshtein Distance
	 */
	public static int calcLevenshteinDistance(String str1, String str2) {
		int totalDistance = 0;
		Object[] objStr1 = new Object[1];
		Object[] objStr2 = new Object[1];
		int length;
		int[] skip = new int[0];
		int deletions = 0;
		
		int minLength;
		int[] str2deletions = new int[0];
		if(str1.length() >= str2.length()) {
			length = str1.length();
			minLength = str2.length();
			str2deletions = getMissingCharsFromString(str1, str2);
		} else {
			length = str2.length();
			minLength = str1.length();
			str2deletions = getMissingCharsFromString(str2, str1);
		}			
		for(int i = 1; i <= length; i++) {
			for(int j = 1; j <= length; j++) {
				if(length == str1.length()) {
					objStr1[0] = str1.charAt(i-1);
					if(j <= str2.length()) {
						objStr2[0] = str2.charAt(j-1);
					} else {
						objStr2[0] = null;
					}
				} else {
					objStr1[0] = str2.charAt(i-1);
					if(j <= str1.length()) {
						objStr2[0] = str1.charAt(j-1);
					} else {
						objStr2[0] = null;
					}
				}
				while(objStr1[0].equals(objStr2[0]) && checkIntArr(j, skip) != 0) {
					skip = addIntToArr(j, skip);
					j++;
					if(length == str1.length()) {
						if(j <= str2.length()) {
							objStr2[0] = str2.charAt(j-1);
						} else {
							objStr2[0] = null;
						}
					} else {
						if(j <= str1.length()) {
							objStr2[0] = str1.charAt(j-1);
						} else {
							objStr2[0] = null;
						}
					}
				}
				if(!objStr1[0].equals(objStr2[0]) || j > length) {
					if(j >= length) {
						if(checkIntArr(i, str2deletions) == 0) {
							totalDistance++;
						} 
						deletions++;
					}
				} else {
					skip = addIntToArr(j, skip);
					break;
				}
			}
		}
		int max = 0;
		int count = 0;
		int countDifference = 0;
		boolean firstDifference = false;
		int firstDifferenceIndK = 0;
		int firstDifferenceIndI = 0;
		int firstDifferenceCount = 0;
		int startInd = 0;
		int startK = 1;
		boolean difference = false;
		boolean setK = false;
		int[] counts = new int[0];

		for(int j = 1; j <= minLength; j++) {
			if(startK == firstDifferenceIndK) {
				break;
			}
			if(checkIntArr(j, skip) == 0 && j < minLength) {
				j++;
			}
			count = firstDifferenceCount;
			if(firstDifferenceIndK != 0) {
				startK = firstDifferenceIndK;
			} else {
				startK = 1;
			}
			firstDifference = false;
			setK = false;
			counts = new int[0];
			for(int k = startK; k <= minLength; k++) {
				while(checkIntArr(k, skip) == 0 && k < minLength) {
					k++;
				}
				countDifference = 0;
				difference = false;
				if(k == startK) {
					startInd = firstDifferenceIndI;
				}
				for(int i = startInd; i < skip.length; i++) {
					while(skip[i] != k && !firstDifference && i == skip.length - 1) {
						firstDifference = true;
					}
					if(skip[i] != k && !difference) {
						difference = true;
						
					}
					if(difference) {
						countDifference++;
					}
					
					if(skip[i] == k) {	
						count++;
						if(i < skip.length - 1) {
							counts = addIntToArr(skip[i+1], counts);
						}
						if(i == skip.length - 1) {
							startInd = i;
						} else {
							startInd = i;
						}
						break;
					} else if(!difference){
						if(i < skip.length && i > 0) {
							k = skip[i-1]+1;
						} else {
							k -= countDifference;
						}
					} 
					
					if(skip[i] != k && i == skip.length - 1 && !setK) {
						firstDifferenceIndK = k;
						startInd = checkIntArr(firstDifferenceIndK, skip) - 1;
						setK = true;
						
						for(int m = 0; m < counts.length; m++) {
							if(counts[m] == k) {
								firstDifferenceCount = m+1;
								break;
							}
						}
					}
				}
			}
			if(count > max) {
				max = count;
			}
			if(!setK) {
				break;
			}
		}
		totalDistance += (length - deletions - max)*2 + str2deletions.length;
		return totalDistance;
	}
}
