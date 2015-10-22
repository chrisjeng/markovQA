# Simple Makefile to run the Markov for you.

JFLAGS = -cp src/
JC = javac
OBAMA_FLAGS = -file tests/obama_speeches/ -num_words 100
ASK_FLAGS = -file reddit_posts -num_words 100

default:
	$(JC) $(JFLAGS) src/Markov.java -cp src/
clean:
	rm -rf src/*.class
	rm -rf reddit_posts
obama: default
	@echo "Parsing example Obama speeches and outputting a random paragraph..."
	java $(JFLAGS) Markov $(OBAMA_FLAGS)
ask: default
	python src/answer_question.py --weights
	java $(JFLAGS) Markov $(ASK_FLAGS)
