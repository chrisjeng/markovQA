# Simple Makefile to run Obama tests

JFLAGS = -cp src/
JC = javac
OBAMA_FLAGS = -file tests/obama_speeches/ -num_words 100

default:
	@$(JC) $(JFLAGS) src/Markov.java -cp src/
clean:
	@rm -rf src/*.class
obama: default
	java $(JFLAGS) Markov $(OBAMA_FLAGS)
