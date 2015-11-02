# Twitter bot that given a relationship-related question, outputs an advice formed by a Markov Chain in a QA format
from config import generator_settings
from tweepy import OAuthHandler
import tweepy
from answer_question import reddit_search


consumer_key = "n3edJaNSMAAnmevNL2QD5slhi"
access_token = "4095945018-hr6OMPAjyngY693BVuo31oczphOESK2rWNrMEVQ"
consumer_secret = generator_settings['consumer_secret']
access_token_secret = generator_settings['access_token_secret']

## EXECUTE ##

# TODO: avoid answering same questions (option 1. get only mentions after last reply; option 2. streamlistener (Raspberry Pi needed))
#myLastTweet = api.me().timeline(count=1)
#mentions = api.mentions_timeline(since_id=myLastTweet.id)

# process mention
#mentions = api.mentions_timeline(count=2)
#for mention in mentions:
#    question_screen_name = mention.user.screen_name

class MyStreamListener(tweepy.StreamListener):
    def on_connect(self):
        print("[INFO] Stream server connected")
    def on_status(self, status):
        question = ' '.join(word for word in status.text.split() if word.lower() != bot_mention.lower())
        # TODO: form the answer
        # TODO: handle too much request error
        print(question)
        print(reddit_search(question, search_url='https://www.reddit.com/r/relationships/search?q={0}'))
        #answer ="This is the answer"
        #question_answer ='From @{}.\nQ. "{}"   \nA. {}'.format(question_screen_name, question, answer)
        #api.update_status(status=question_answer)
    def on_error(self, status):
        print(status)
        #if status_code == 420:
            #return False


if __name__ == '__main__':
    auth = OAuthHandler(consumer_key, consumer_secret)
    auth.set_access_token(access_token, access_token_secret)
    api = tweepy.API(auth)
    bot_mention = "@{}".format(api.me().screen_name)
    track = [bot_mention]
    myStream = tweepy.Stream(auth = auth, listener = MyStreamListener())
    myStream.filter(track=track)
