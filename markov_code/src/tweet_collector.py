from config import settings
from tweepy import OAuthHandler
from tweepy import Stream
from tweepy.streaming import StreamListener
import tweepy
import re
import os
import argparse

consumer_key = "2UiujM3WG929c4svJ1iVrEkWe"
access_token = "3166227044-nO5qPuJCuGeNLLQKeJ2Rg8XX0a0svxB4jccyxwL"

consumer_secret = settings['consumer_secret']
access_token_secret = settings['access_token_secret']

TWEET_NUM = 1000

parser = argparse.ArgumentParser(description='Collects tweets without mention or hyperlink')
parser.add_argument('-u','--username', help='when you know the exact Twitter username')
args = parser.parse_args()

if __name__ == '__main__':
    # user = api.get_user('realDonaldTrump')
    auth = OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)
    api = tweepy.API(auth)

    if args.username:
        user_name = args.username
    else:
        while True:
            user = raw_input("Whose tweet would you like to generate?\n")
            search_result = api.search_users(user)
            if search_result:
                user_name = search_result[0].screen_name
                print("Processing tweets from " + search_result[0].name + " (@"+user_name+")")
                break
            else:
                print("Cannot find anyone by that name. Try again?")

    filename = user_name + "_tweets"
    if os.path.exists(filename):
        os.remove(filename)
    with open(filename, "a") as f:
        print("Start processing " + str(TWEET_NUM) + " tweets")
        try:
            for tweet in tweepy.Cursor(api.user_timeline, id=user_name).items(TWEET_NUM):
                tweet_text = tweet.text
                # do not count retweets
                if "RT @" not in tweet_text:
                    # removes hyperlinks or Twitter mentions
                    tweet_text = re.sub(r"(?:\@|https?\://)\S+", "", tweet_text)
                    tweet_text = re.sub("&amp;", "&", tweet_text)
                    to_file = tweet_text.encode('utf-8') + '\n'
                    f.write(to_file)
        except tweepy.error.TweepError:
            print("The user doesn't exist. Trying to get the closest Twitter account")


    print("DONE")

