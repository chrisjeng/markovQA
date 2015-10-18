import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Markov implements Walkable {

	public static void main(String[] args) {
		Markov m = new Markov();
		m.parsePair("first", "second", 1);
		m.parsePair("second", "first", 1);
		for (int i = 0; i < 1; i++) {
			System.out.println(m.generateSentence(2));
		}
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
			answer.append(w.val);
			answer.append(" ");
			Word next = w.randomWord();
			if (next == null) {
				System.out.println(w.val + " has no successors!");	
				break;
			}
			w = next;
		}
		return answer;
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
				answer = new Word(word);
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