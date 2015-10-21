from bs4 import BeautifulSoup
from urllib2 import HTTPError
import argparse
import codecs
import os
import subprocess
import time
import urllib2

MAX_NUM_POSTS = 10
MAX_NUM_COMMENTS = 5


parser = argparse.ArgumentParser(description='Answer some questions.')
parser.add_argument('--weights', action='store_true', default=False,
                    help='use non-uniform weights among files, '
                         'first line must be weight')
args = parser.parse_args()


def make_HTTP_request(url):
    hdr = { 'User-Agent' : 'super happy flair bot by /u/spladug' }
    req = urllib2.Request(url, headers=hdr)
    try:
        html = urllib2.urlopen(url)
    except HTTPError, e:
        print("Too Many Requests, retrying in 8 seconds")
        time.sleep(8)
        html = urllib2.urlopen(url)
        return html
    return html


def reddit_search(query_string):
    # Split up the query string in a plus-delimited query used in search
    plus_delimited_query = '+'.join(query_string.split())
    html = make_HTTP_request(
        'https://reddit.com/search?q={0}'.format(plus_delimited_query))

    # Find the div corresponding to top posts and process each post
    soup = BeautifulSoup(html) 
    search_listings = soup.findAll('div',
                                   {'class': 'listing search-result-listing'})
    if len(search_listings) < 2:
        print('Didn\'t find any Reddit posts :(')
        return
    search_result = search_listings[1]
    content_div = search_result.find('div', {'class': 'contents'})
    list_of_posts = content_div.findAll('div', recursive=False)
    print("Finished parsing search results")

    post_results = []
    for i in range(min(MAX_NUM_POSTS, len(list_of_posts))):
        post = list_of_posts[i]
        # Sleep for 2 seconds to account for Reddit request limit
        time.sleep(3) 

        header = post.find('header')
        post_link = header.find('a', href=True)
        processed_post = process_post(post_link['href'])
        print("({0} posts completed/{1} total posts)".format(i + 1,
            min(MAX_NUM_POSTS, len(list_of_posts))))
        if processed_post == None:
            continue
        post_results.append(processed_post)

    return post_results


def process_post(post_link):
    html = make_HTTP_request(post_link)
    soup = BeautifulSoup(html)
    comment_area = soup.find('div', {'class': 'commentarea'})
    if comment_area == None:
        print("No comments found and/or you are a terrible person")
        return None
    table_contents = comment_area.find('div', {'class': 'sitetable'})
    list_of_comments = table_contents.findAll('div', {'class': 'comment'},
                                              recursive=False)
    top_comments = []
    for i in range(min(MAX_NUM_COMMENTS, len(list_of_comments))):
        comment = list_of_comments[i]
        current_entry = comment.find('div', {'class': 'entry unvoted'})
        contents = current_entry.find('div', {'class': 'md'}).getText()
        # TODO: Input a scoring function
        score = min(MAX_NUM_COMMENTS, len(list_of_comments)) - i
        top_comments.append((1, contents))
         
    return top_comments


def output_results_to_file(post_results):
    print("Outputting results to temp directory")
    if not os.path.exists('reddit_posts'):
        os.mkdir('reddit_posts')
    num_posts = 0
    for post in post_results:
        if not os.path.exists('reddit_posts/{0}'.format(num_posts)):
            os.mkdir('reddit_posts/{0}'.format(num_posts))
        num_comments = 0
        for comment in post:
            score, text = comment
            with codecs.open('reddit_posts/{0}/{1}'.format(
                num_posts, num_comments), 'w', 'utf-8') as outfile:
                if args.weights:
                    outfile.write(str(score) + '\n')
                outfile.write(text)
            num_comments += 1
        num_posts += 1


def main():
    query_string = raw_input("Please enter a question and "
                             "we will attempt to answer :D\n")
    post_results = reddit_search(query_string)     
    output_results_to_file(post_results)
        

if __name__ == '__main__':
    main()
