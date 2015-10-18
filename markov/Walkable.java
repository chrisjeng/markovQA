public interface Walkable {

	/* Returns a random walk of length numWords, starting with a random word. */
	public abstract StringBuilder generateSentence(int numWords);

	/* Returns a random walk of length numWords, starting with startWord. */
	public abstract StringBuilder generateSentence(int numWords, String startWord);

	/* Returns a random word, chosen uniformly (not weighted) from the set of 
	 * all words. */
	public String getRandomWord();
}