import os
import tweepy
import re
from tweepy import OAuthHandler
from tweepy import Stream
from tweepy.streaming import StreamListener

access_token = "3166227044-nO5qPuJCuGeNLLQKeJ2Rg8XX0a0svxB4jccyxwL"
access_token_secret = "BWouzym7RPquQZX93uudaR2mh6ykSOyHF5tVCQr3eVC1K"
consumer_key = "rR3XLOtoOXnCEFdrnUhwVM7hA"
consumer_secret = "bPkfQmG6WBWnJFR0kliZsjBHeRz9dYmfDVdgU5L0EPwo0Hw15s"

auth = OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)

api=tweepy.API(auth)

class listener(StreamListener):
    def on_data(self, data):
        print(data)
        return True

    def on_error(self, status):
        print(status)

if __name__ == '__main__':
    # user = api.get_user('realDonaldTrump')
    # print(api.search_users("Donald Trump")[0].screen_name)
    #user = input("Whose tweet would you like to generate?\n")
    user = "HillaryClinton"
    filename = user + "_tweets"
    if os.path.exists(filename):
        os.remove(filename)
    tweets = []
    with open(filename, "a") as f:
        for tweet in tweepy.Cursor(api.user_timeline, id=user).items(1000):
            # do not count retweets
            tweet_text = tweet.text
            if "RT @" not in tweet_text:
                tweet_text = re.sub(r"(?:\@|https?\://)\S+", "", tweet_text)
                to_file = tweet_text.encode('utf-8') + '\n'
                f.write(to_file)

