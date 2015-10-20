public interface Walkable {

	/* Returns a random walk of length numWords, starting with a random word. */
	// public String genSentence(int numWords, String starting_word);
	public String genSentence(int numWords);

	/* Returns a random word, chosen uniformly (not weighted) from the set of 
	 * all words. */
	public String getRandomWord();

	/* Adds a mapping. The mapping does not have to be new. */
	public void parsePair(String start, String end, double weight);

	/* Parses a file or entire directory into a Markov chain. Assumes a default
	 * weight of 1. */
	public void parse(String path, double weight);
	public void parse(String path);
}