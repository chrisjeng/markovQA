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
		double input_weight = 1.0;
		for (int i = 0; i < args.length; i++) {
			String curr = args[i];
			if ("-w".equalsIgnoreCase(curr) || "-weight".equalsIgnoreCase(curr)) {
				curr = args[++i];
				if (curr.equalsIgnoreCase("read_mode")) {
					m.isWeightEmbedded = true;
				} else {
					try {
						input_weight = Double.parseDouble(curr);
					} catch (NumberFormatException e) {
						System.out.println("Please enter a valid weight value!");
						return;
					}
				}
			} else if ("-f".equalsIgnoreCase(curr) || "-file".equalsIgnoreCase(curr)) {
				curr = args[++i];
				if (i == args.length - 1) {
					String alert = "WARNING: ";
					alert += "You are going to parse this information, ";
					alert += "and do nothing with it.";
					System.out.println(alert);
				}
				m.parse(curr, input_weight);
			} else if ("-n".equalsIgnoreCase(curr) || "-num_words".equalsIgnoreCase(curr)) {
				curr = args[++i];
				int numWords = Integer.parseInt(curr);
				String output = m.genSentence(numWords);
				System.out.println(output);
			} else if ("-h".equalsIgnoreCase(curr) || "-help".equalsIgnoreCase(curr)) {
				printHelpMessage();
				return;
			} else {
				printHelpMessage();
				return;
			}
		}
		/* TODO: Read in from stdin and repeat repetitions. */
	}

	/* Prints out the usage help message. */
	private static void printHelpMessage() {
		String helpMSG = "Usage: ";
		helpMSG += "Markov ";
		helpMSG += "[-w or -weight] [read_mode or DOUBLE_NUMBER]";
		helpMSG += "[-f or -file] [FILE_NAME or DIR_NAME] ";
		helpMSG += "[-n or -num_words] [INTEGER_NUMBER]";
		System.out.println(helpMSG);
	}

	private boolean isWeightEmbedded = false;
	private HashMap<String, Word> allWords;
	private int numAdds;
	private int numUniqueAdds;

	public Markov() {
		allWords = new HashMap<String, Word>();
		numAdds = 0;
	}
	
	/* Returns a random sentence of at least numWords words. Starts on a random
	 * word. */
	public String genSentence(int numWords) {
		return genSentence(numWords, getRandomWord()).toString();
	}

	/* Maximum number of additional words before we force a punctuation. */
	public static final int MAX_EX = 20;

	/* Returns a random sentence of at least numWords words. Starts on 
	 * starting_word. */
	public StringBuilder genSentence(int numWords, String starting_word) {
		starting_word = starting_word.toLowerCase();
		if (!allWords.containsKey(starting_word)) {
			System.out.println(starting_word + " is not a valid starting word!");
			return null;
		}
		StringBuilder answer = new StringBuilder();
		Word w = allWords.get(starting_word);
		int cnt = 0;
		int ex_words = 0;
		while (w != null && cnt < numWords || !endsPunct(w.val) && ex_words++ < MAX_EX) {
			answer.append(tidyWord(w.val));
			answer.append(" ");
			w = w.randomWord();
			if (w == null) {
				w = getFruitfulWord();
			}
			cnt++;
		}
		answer.replace(answer.length() - 1, answer.length(), ".");
		return answer;
	}

	/* Determines the maximum number of tries before we quit on finding a 
	 * fruitful word. */
	public static final int MAX_TRIES = 5;

	/* A word is "fruitful" if it has at least one edge to any other word. 
	 * Will attempt to find a fruitful word MAX_TRIES number of times 
	 * before giving up and return null. */
	public Word getFruitfulWord() {
		for (int i = 0; i < MAX_TRIES; i++) {
			Word w = allWords.get(getRandomWord());
			if (w.hasEdge()) {
				return w;
			}
		}
		return null;
	}

	private static HashSet<String> titleWords = new HashSet<String>();
	private static HashSet<String> fullCapsWords = new HashSet<String>();

	/* Handle all the capitalization stuff as necessary. */
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

	/* Will capitalize the word if the previously outputted word ended in a 
	 * punctuation. */
	private String titleIfNecessary(String input) {
		String answer;
		if (shouldCaps) {
			answer = titleCase(input);
		} else {
			answer = input;
		}
		shouldCaps = endsPunct(input);
		return answer;
	}

	/* Returns whether or not the input String ended in punctuation. */
	private boolean endsPunct(String input) {
		char last = input.charAt(input.length() - 1);
		return last == '.' || last == '?' || last == '!';
	}

	/* Returns the title-case version of the input String (capitalizes the 
	 * first character). */
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

	private void parseFile(File f, double weight) {
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
		if (isWeightEmbedded) {
			/* Weight is embedded. Read that instead of taking the argument. */
			weight = Double.parseDouble(start);
			start = input.next();
		}
		while(input.hasNext()) {
		    String end = input.next();
		    parsePair(start, end, weight);
		    start = end;
		}
		input.close();
	}

	public void parse(String path) {
		parse(path, 1);
	}

	public void parse(String path, double weight) {
		File folder = new File(path);
		parseRecursive(folder, weight);
	}

	private void parseRecursive(File f, double weight) {
		if (f.isDirectory()) {
			for (File curr : f.listFiles()) {
				parseRecursive(curr, weight);
			}
		} else if (f.isFile()) {
			parseFile(f, weight);
		} else {
			String msg = "File " + f.getName() + " is neither a directory ";
			msg += " or file!";
			System.out.println(msg);
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

		public boolean hasEdge() {
			return !allWords.isEmpty();
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