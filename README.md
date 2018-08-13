#Run

java - jar multicrawler.jar --create --thread.count=5 http://google.com http://facebook.com http://twitter.com

* --create - create db tables
* --thread.count - number of threads
* any other arguments will be treated as domain names

#Create DB:

* CREATE DATABASE multicrawler;
* CREATE USER multicrawler WITH password 'multicrawler';
* GRANT ALL privileges ON DATABASE multicrawler TO multicrawler;

