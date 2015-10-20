import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Markov implements Walkable {

	public static void main(String[] args) {
		if (args.length == 0) {
			printHelpMessage();
			return;
		}
		Markov m = new Markov();
		for (int i = 0; i < args.length; i++) {
			String curr = args[i];
			if ("-f".equals(curr) || "-file".equals(curr)) {
				curr = args[++i];
				if (i == args.length - 1) {
					String alert = "WARNING: ";
					alert += "You are going to parse this information, ";
					alert += "and do nothing with it.";
					System.out.println(alert);
				}
				m.parseDir(curr);
			} else if ("-n".equals(curr) || "-num_words".equals(curr)) {
				curr = args[++i];
				int numWords = Integer.parseInt(curr);
				String output = m.generateSentence(numWords);
				System.out.println(output);
			} else if ("-h".equals(curr) || "-help".equals(curr)) {
				printHelpMessage();
			} else {
				printHelpMessage();
			}
		}
	}

	private static void printHelpMessage() {
		String helpMSG = "Usage: ";
		helpMSG += "Markov [-f or -file] ";
		helpMSG += "[FILE_NAME or DIR_NAME] ";
		helpMSG += "[-n or -num_words] ";
		helpMSG += "[INTEGER_NUMBER]";
		System.out.println(helpMSG);
	}

	private HashMap<String, Word> allWords;
	private int numAdds;
	private int numUniqueAdds;

	public Markov() {
		allWords = new HashMap<String, Word>();
		numAdds = 0;
	}
	
	public String generateSentence(int numWords) {
		return generateSentence(numWords, getRandomWord()).toString();
	}

	public StringBuilder generateSentence(int numWords, String startWord) {
		StringBuilder answer = new StringBuilder();
		Word w = allWords.get(startWord);
		int wordsSoFar;
		for (wordsSoFar = 0; wordsSoFar < numWords; wordsSoFar++) {

			answer.append(tidyWord(w.val));
			answer.append(" ");
			Word next = w.randomWord();
			if (next == null) {
				System.out.println(w.val + " has no successors. Starting anew! ");
				// Start another sentence.
				answer.append(generateSentence(numWords - wordsSoFar - 1));
				return answer;
			}
			w = next;
			// TODO: Paragraph splicing.
		}
		answer.replace(answer.length() - 1, answer.length(), ".");
		return answer;
	}

	private static HashSet<String> titleWords = new HashSet<String>();
	private static HashSet<String> fullCapsWords = new HashSet<String>();
	private String tidyWord(String input) {

		/* Initialize if necessary */
		if (titleWords.isEmpty()) {
			// TODO: Export to a .txt file
			titleWords.add("i");
			titleWords.add("obama");
			titleWords.add("barack");
			titleWords.add("iraq");
			titleWords.add("america");
			titleWords.add("barack");
			titleWords.add("mrs.");
			titleWords.add("mr.");
			titleWords.add("chicago");
			titleWords.add("woodrow");
			titleWords.add("wilson");
			titleWords.add("alabama");
			titleWords.add("montana");
			titleWords.add("alaska");
			titleWords.add("nebraska");
			titleWords.add("arizona");
			titleWords.add("nevada");
			titleWords.add("arkansas");
			titleWords.add("california");
			titleWords.add("colorado");
			titleWords.add("mexico");
			titleWords.add("connecticut");
			titleWords.add("delaware");
			titleWords.add("florida");
			titleWords.add("georgia");
			titleWords.add("ohio");
			titleWords.add("hawaii");
			titleWords.add("oklahoma");
			titleWords.add("idaho");
			titleWords.add("oregon");
			titleWords.add("illinois");
			titleWords.add("pennsylvania");
			titleWords.add("indiana");
			titleWords.add("rhode");
			titleWords.add("iowa");
			titleWords.add("kansas");
			titleWords.add("kentucky");
			titleWords.add("tennessee");
			titleWords.add("louisiana");
			titleWords.add("texas");
			titleWords.add("maine");
			titleWords.add("utah");
			titleWords.add("maryland");
			titleWords.add("vermont");
			titleWords.add("massachusetts");
			titleWords.add("virginia");
			titleWords.add("michigan");
			titleWords.add("washington");
			titleWords.add("minnesota");
			titleWords.add("mississippi");
			titleWords.add("wisconsin");
			titleWords.add("missouri");
			titleWords.add("wyoming");


			fullCapsWords.add("u.s.");
			fullCapsWords.add("USA");
			fullCapsWords.add("AL");
			fullCapsWords.add("MT");
			fullCapsWords.add("AK");
			fullCapsWords.add("NE");
			fullCapsWords.add("AZ");
			fullCapsWords.add("NV");
			fullCapsWords.add("AR");
			fullCapsWords.add("NH");
			fullCapsWords.add("CA");
			fullCapsWords.add("NJ");
			fullCapsWords.add("CO");
			fullCapsWords.add("NM");
			fullCapsWords.add("CT");
			fullCapsWords.add("NY");
			fullCapsWords.add("DE");
			fullCapsWords.add("NC");
			fullCapsWords.add("FL");
			fullCapsWords.add("ND");
			fullCapsWords.add("GA");
			fullCapsWords.add("OH");
			fullCapsWords.add("HI");
			fullCapsWords.add("OK");
			fullCapsWords.add("ID");
			fullCapsWords.add("OR");
			fullCapsWords.add("IL");
			fullCapsWords.add("PA");
			fullCapsWords.add("IN");
			fullCapsWords.add("RI");
			fullCapsWords.add("IA");
			fullCapsWords.add("SC");
			fullCapsWords.add("KS");
			fullCapsWords.add("SD");
			fullCapsWords.add("KY");
			fullCapsWords.add("TN");
			fullCapsWords.add("LA");
			fullCapsWords.add("TX");
			fullCapsWords.add("ME");
			fullCapsWords.add("UT");
			fullCapsWords.add("MD");
			fullCapsWords.add("VT");
			fullCapsWords.add("MA");
			fullCapsWords.add("VA");
			fullCapsWords.add("MI");
			fullCapsWords.add("WA");
			fullCapsWords.add("MN");
			fullCapsWords.add("WV");
			fullCapsWords.add("MS");
			fullCapsWords.add("WI");
			fullCapsWords.add("MO");
			fullCapsWords.add("WY");

		}
		String answer = titleIfNecessary(input);
		if (titleWords.contains(input)) {
			shouldCaps = false;
			return titleCase(input);
		}
		if (fullCapsWords.contains(input)) {
			return input.toUpperCase();
		}
		return answer;
	}

	private boolean shouldCaps = true;
	private String titleIfNecessary(String input) {
		String answer;
		char last = input.charAt(input.length() - 1);
		if (shouldCaps) {
			answer = titleCase(input);
		} else {
			answer = input;
		}
		shouldCaps = last == '.' || last == '?' || last == '!';
		return answer;
	}

	private String titleCase(String input) {
		String answer = "" + input.charAt(0);
		answer = answer.toUpperCase();
		return answer + input.substring(1);
	}

	/* Returns a random word from this Markov chain. */
	public String getRandomWord() {
		Iterator<String> myIt = allWords.keySet().iterator();
		int randIndex = (int) (Math.random() * allWords.size());
		if (!myIt.hasNext()) {
			System.out.println("Error! Iterator issue!");
		}
		String currWord = myIt.next();
		for (int i = 0; i < randIndex; i++) {
			currWord = myIt.next();
		}
		return currWord;
	}

	/* Adds a mapping. The mapping does not have to be new. */
	public void parsePair(String start, String end, double weight) {
		start = start.toLowerCase();
		Word startWord = getOrCreate(start.toLowerCase());
		end = end.toLowerCase();
		getOrCreate(end);

		startWord.addWord(end.toLowerCase(), weight);
		numAdds++;
	}

	/* Parses a single file into the Markov Chain. */
	public void parseFile(String file_name) {
		// TODO
	}

	public void parseFile(File f) {
		Scanner input;
		try {
			input = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Done goofed!");
			System.exit(1);
			return;
		}
		if (!input.hasNext()) {
			input.close();
			return;
		}
		String start = input.next();
		while(input.hasNext()) {
		    String end = input.next();
		    parsePair(start, end, 1);
		    start = end;
		}
		input.close();
	}
	
	/* Parses an entire directory. calls parseFile() on each file. */
	public void parseDir(String dir_name) {
		File folder = new File(dir_name);
		parseRecursive(folder);
	}

	private void parseRecursive(File f) {
		if (f.isDirectory()) {
			for (File curr : f.listFiles()) {
				parseRecursive(curr);
			}
		}
		if (f.isFile()) {
			parseFile(f);
		}
	}

	/* If exists, will get the Word, or if not, creates a new Word. */
	private Word getOrCreate(String input) {
		if (allWords.containsKey(input)) {
			return allWords.get(input);
		} else {
			Word answer = new Word(input);
			allWords.put(input, answer);
			numUniqueAdds++;
			return answer;
		}
	}

	/* Represents a single node in our graph. */
	class Word {

		String val; // The actual word
		HashMap<String, Edge> str2edge; // Holds all the next potential words.
		LinkedList<String> allWords; // Maintains ordering among all words.
		double totalWeight; // Sum of all destination weights
		boolean normalized; // Have the probabilities been normalized recently?

		public Word(String input) {
			val = input;
			str2edge = new HashMap<String, Edge>();
			allWords = new LinkedList<String>();
			totalWeight = 0;
			normalized = false;
		}

		/* Randomly selects an Edge based on weights and returns it. */
		public Word randomWord() {
			if (allWords.isEmpty()) {
				return null;
			}
			if (!normalized) {
				normalizeWeights();
			}
			double rand = Math.random();
			double currSum = 0;
			for (String s : allWords) {
				Edge e = str2edge.get(s);
				double eVal = e.norm_weight;
				double summed = currSum + eVal;
				if (summed > rand && currSum < rand) {
					return e.end;
				} else {
					currSum = summed;
				}
			}
			throw new RuntimeException("randomWord failed!");
		}

		/* Adds an edge and its weight. The edge doesn't have to be new. 
		 * Returns the given word. */
		public Word addWord(String word, double weight) {
			/* Some houskeeping */
			totalWeight += weight;
			Word answer;
			/* Branch based on if the edge exists or not. */
			if (!str2edge.containsKey(word)) {
				answer = getOrCreate(word);
				str2edge.put(word, new Edge(this, answer, weight));
				allWords.add(word);
			} else {
				Edge existingEdge = str2edge.get(word);
				answer = existingEdge.end;
				existingEdge.orig_weight += weight;
			}
			return answer;
		}

		/* Normalizes each Edge's norm_weight value. */
		private void normalizeWeights() {

			/* If it's already normalized, don't break a sweat. */
			if (normalized) {
				return;
			}

			/* Otherwise, go ahead and normalize everything. */
			for (String word : allWords) {
				Edge e = str2edge.get(word);
				e.norm_weight = e.orig_weight / totalWeight;
			}
			normalized = true;
		}

		/* Represents an edge. Holds a start and end word, along with a weight. */
		class Edge {
			protected Word start;
			protected Word end;
			protected double orig_weight; // The original weight
			protected double norm_weight; // The normalized weight
			public Edge(Word start, Word end, double weight) {
				this.start = start;
				this.end = end;
				this.orig_weight = weight;
				norm_weight = weight;
			}
		}
	}
}