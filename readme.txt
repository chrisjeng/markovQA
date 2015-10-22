Members:
Chris Jeng
Don Han
Giulio Zhou

HOW TO RUN THE CODE:
%%%%%%%%%%%%%%%%%%%%%%%%%%% Option 1: Makefile %%%%%%%%%%%%%%%%%%%%%%%%%%%
1. Run the Makefile by calling "make", followed by "make ask". Respond with
   whether or not you would like relationship advice (highly recommended) and
   if you'd like a serious response (prioritized upvoted answers or 
   downvoted answers). For Obama speech generation, run:
   "make obama"
%%%%%%%%%%%%%%%%%%%%%%%%%%%% Option 2: Python %%%%%%%%%%%%%%%%%%%%%%%%%%%%
1. Run the python file as follows, where the weights flag tells the 
   program to output each comment's weight at the top of its file.
   >>> python answer_question.py --weights
2. When the program prints out a message, respond with whether or not
   you would like relationship advice and if you'd like a serious response.
   Then, input your question and hit enter.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Option 3: Java %%%%%%%%%%%%%%%%%%%%%%%%%%%%%
1.  Run "java Markov" after compiling. Input a -h flag to see the help message,
    which is also outputted here for your convenience:
        Usage: Markov ...
        (optional) [-w or -weight] [read_mode or DOUBLE_NUMBER]
        (required) [-f or -file] [FILE_NAME or DIR_NAME]
        (recommended) [-n or -num_words] [INTEGER_NUMBER]
        (do NOT use with -n!) [-t or -twitter] 

NOTE: The program may run into issues with sending too many requests
      to the server. If this occurs, just wait a little while and 
      execute the program again with the same query string.
