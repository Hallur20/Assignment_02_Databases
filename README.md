# Assignment_02_Databases

In order to make this project work you need to setup mongodb:
1. go to your vagrant or linux terminal.
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
12. type: 'mongoimport --host=127.0.0.1 -d tweets -c data --type csv --file converted-utf8.csv --headerlineconnected to: 127.0.0.1'

check in your prefered mongodb software (or just in terminal by typing 'mongo') if you have now a database called 'tweets' and a collection called 'data'.


then...

1. clone the project
2. open the project in netbeans idé (the project is a java maven project + you need jdk 8)
3. push f6 or right click on Main.java and click 'run file'
4. type in the output console window:<br>
  'howMany' to see the amount of users<br>
  'links' to see the users who linked the most<br>
  'mentioned' to see the most mentioned users<br>
  'active' to see most active users
  
 
 
<h2>documentation</h2>

```java
    public static void howManyUsers(DBCollection c) {
        Set<String> users = new HashSet<>();
        DBCursor cursor = c.find();
        System.out.println("calculating hold on...");
        while (cursor.hasNext()) {
            users.add(String.valueOf(cursor.next().get("user")));
        }
        System.out.println("Twitter users amount: " + users.size());
    }
```
```java
    public static void mostLinks(DBCollection c) {
        DBCursor cursor = c.find(new BasicDBObject("text", new BasicDBObject("$regex", "@")));
        HashMap<String, Integer> timesUser = new HashMap<>();
        try {
            System.out.println("calculating hold on...");
            while (cursor.hasNext()) {
                String userKey = String.valueOf(cursor.next().get("user"));

                if (!timesUser.containsKey(userKey)) {
                    timesUser.put(String.valueOf(cursor.next().get("user")), 1);
                    continue;
                }
                timesUser.put(userKey, timesUser.get(userKey) + 1);
            }
        sortHashMapAndShowTopList(timesUser, 10);
        } finally {
            cursor.close();
        }
    }
``` 

```java
        public static void mostMentionedTwitterUsers(DBCollection c) {
        DBCursor cursor = c.find(new BasicDBObject("text", new BasicDBObject("$regex", "@")));
        HashMap<String, Integer> timesUser = new HashMap<>();
        try {
            while (cursor.hasNext()) {
                String line = String.valueOf(cursor.next().get("text"));
                String[] users = line.split("@");
                for (int i = 1; i < users.length; i++) {
                    String key = users[i].split(" ")[0];
                    if (key.isEmpty()) {
                        continue;
                    }
                    if (!timesUser.containsKey(key)) {
                        timesUser.put(key, 0);
                        continue;
                    }
                    timesUser.put(key, timesUser.get(key) + 1);
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        sortHashMapAndShowTopList(timesUser, 5);
    }
   
```
    
```java
    public static void mostActiveUsers(DBCollection c){
        DBCursor cursor = c.find();
        HashMap<String,Integer> timesUser = new HashMap<>();   
        while(cursor.hasNext()){
            String user = String.valueOf(cursor.next().get("user"));
            if(!timesUser.containsKey(user)){
                timesUser.put(user, 0);
                continue;
            }
            timesUser.put(user, timesUser.get(user)+1);
        }
        
        sortHashMapAndShowTopList(timesUser, 10);
    }

```
