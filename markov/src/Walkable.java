public interface Walkable {

	/* Returns a random walk of length numWords, starting with a random word. */
	public String generateSentence(int numWords);

	/* Returns a random word, chosen uniformly (not weighted) from the set of 
	 * all words. */
	public String getRandomWord();

	/* Adds a mapping. The mapping does not have to be new. */
	public void parsePair(String start, String end, double weight);

	/* Parses a single file into the Markov Chain. */
	public void parseFile(String file_name);
	
	/* Parses an entire directory. calls parseFile() on each file. */
	public void parseDir(String dir_name);
}