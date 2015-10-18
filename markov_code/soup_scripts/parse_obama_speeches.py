from bs4 import BeautifulSoup
import codecs
import os
import sys
import urllib2

if not os.path.exists('output'):
    os.mkdir('output')

# Open the url and find the table element with all the links
obama_link = 'http://obamaspeeches.com/'
response = urllib2.urlopen(obama_link)
soup = BeautifulSoup(response.read())
table_column_html = soup.findAll('table', width=250)[0]

# Grab all of the links from the table column and follow them
obama_speech_links = table_column_html.findAll('a', href=True)
parsed_links = set()
for link in obama_speech_links:
    link_address = link['href']
    if link_address not in parsed_links:
        full_link = obama_link + '/' + link_address
        response = urllib2.urlopen(full_link)
        speech_soup = BeautifulSoup(response.read())
        speech_html = speech_soup.findAll('table', recursive=True)[2]
        [s.extract() for s in speech_html('strong')]
        with codecs.open('output/' + link_address, 'w', 'utf-8') as outfile:
           try:
               cleaned_text = ' '.join(speech_html.getText().split())
               outfile.write(cleaned_text)
               print('successfull write')
           except:
               print('failed write')
        parsed_links.add(link_address)
