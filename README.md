# Assignment_02_Databases

In order to make this project work you need to setup mongodb:
1. go to your vagrant or linux server.
2. type 'sudo apt update
3. type 'sudo apt install -y mongodb'
4. check status with: 'sudo systemctl status mongodb'
5. mongo should be up and running and hopefully it is set to ip localhost and port 27017...
6. now type: 'apt-get install wget'
7. type: 'apt-get install unzip'
8. type: 'wget http://cs.stanford.edu/people/alecmgo/trainingandtestdata.zip'
9. type: 'unzip trainingandtestdata.zip'
10. type: 'iconv -f ISO-8859-1 -t utf-8 training.1600000.processed.noemoticon.csv > converted-utf8.csv' (this converts it to utf8)
11. type: 'sed -i '1s;^;polarity,id,date,query,user,text\n;' converted-utf8.csv' to prepare headers...
12. type: 'mongoimport --host=127.0.0.1 -d tweets -c data --type csv --file converted-utf8.csv --headerlineconnected to: 127.0.0.1' to import to mongodb...



then...

1. clone the project
2. open the project in netbeans idé
3. push f6
4. type in the output console window:
  'howMany' to see the amount of users
  'links' to see the user who linked the most
  'mentioned' to see the most mentioned user
