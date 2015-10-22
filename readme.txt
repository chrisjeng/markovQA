Members:
Chris Jeng
Don Han
Giulio Zhou

HOW TO RUN THE CODE:
%%%%%%%%%%%%%%%%%%%%%%%%%%% Option 1: Makefile %%%%%%%%%%%%%%%%%%%%%%%%%%%
1. Run the Makefile by calling "make", followed by "make ask". Respond with
   whether or not you would like relationship advice (highly recommended) and
   if you'd like a series response.
%%%%%%%%%%%%%%%%%%%%%%%%%%%% Option 2: Python %%%%%%%%%%%%%%%%%%%%%%%%%%%%
1. Run the python file as follows, where the weights flag tells the 
   program to output each comment's weight at the top of its file.
   >>> python answer_question.py --weights
2. When the program prints out a message, respond with whether or not
   you would like relationship advice and if you'd like a serious response.
   Then, input your question and hit enter.

NOTE: The program may run into issues with sending too many requests
      to the server. If this occurs, just wait a little while and 
      execute the program again with the same query string.
