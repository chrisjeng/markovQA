# RelationshipGuruBot
Ask, and you shall get. Scrapes Twitter/Reddit queries to give you only the highest quality answers.

## How to run the code
#### Option 1: Makefile
Run the Makefile by calling `make`, followed by `make ask`. Respond with whether or not you would like relationship advice (highly recommended!), and if you'd like a serious response (prioritize upvoted answers or downvoted answers?). For Obama speech generation, run `make obama`.
#### Python
1. Run the python file as follows, where the weights flag tells the program to output each comment's weight at the top of its file. 
	`python answer_question.py --weights`
2. When the program prints out a message, respond with whether or not you would like relationship advice and if you'd like a serious response. Then, input your question and hit enter.
#### Java
1.  Run "java Markov" after compiling. Input a -h flag to see the help message,
	which is also outputted here for your convenience:

	Usage: Markov ...
	(optional) [-w or -weight] [read_mode or DOUBLE_NUMBER]
	(required) [-f or -file] [FILE_NAME or DIR_NAME]
	(recommended) [-n or -num_words] [INTEGER_NUMBER]
	(do NOT use with -n!) [-t or -twitter]

#### Running Tweet Generator Bot
1. run `python tweet_collector.py`. If you know the Twitter username of a person, run `python tweet-collector.py -u username` instead.
2. After the tweets has been stored in a file format $username_tweets, feed that file into Markov Chain by running `java Markov -f $username_tweet -t`. `-t` flag will output a Tweet-like message into the console.

NOTE: The program may run into issues with sending too many requests to the server. If this occurs, just wait a little while and execute the program again with the same query string.
