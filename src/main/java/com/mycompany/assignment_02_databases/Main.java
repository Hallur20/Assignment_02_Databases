package com.mycompany.assignment_02_databases;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.Map.Entry.comparingByValue;
import java.util.Scanner;
import java.util.Set;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author Hallur
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB database = mongoClient.getDB("tweets");
        DBCollection c = database.getCollection("data");
        System.out.println("*****************\nwelcome,\ntype 'howMany' to see how many twitter users\ntype 'links' to see user who links twitter users the most\ntype 'mentioned' to see the most mentioned twitter user\ntype 'active' too see most active users\n************");
        while (true) {
            String cmd = scanner.nextLine();
            if (cmd.equals("howMany")) {
                howManyUsers(c);
            }
            if (cmd.equals("links")) {
                mostLinks(c);
            }
            if (cmd.equals("mentioned")) {
                mostMentionedTwitterUsers(c);
            }
            if(cmd.equals("active")){
                mostActiveUsers(c);
            }
        }
    }

    public static void howManyUsers(DBCollection c) {
        Set<String> users = new HashSet<>();
        DBCursor cursor = c.find();
        System.out.println("calculating hold on...");
        while (cursor.hasNext()) {
            users.add(String.valueOf(cursor.next().get("user")));
        }
        System.out.println("Twitter users amount: " + users.size());
    }

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
                        timesUser.put(key, 1);
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
    public static void mostActiveUsers(DBCollection c){
        DBCursor cursor = c.find();
        HashMap<String,Integer> timesUser = new HashMap<>();   
        while(cursor.hasNext()){
            String user = String.valueOf(cursor.next().get("user"));
            if(!timesUser.containsKey(user)){
                timesUser.put(user, 1);
                continue;
            }
            timesUser.put(user, timesUser.get(user)+1);
        }
        
        sortHashMapAndShowTopList(timesUser, 10);
    }
    public static void sortHashMapAndShowTopList(HashMap<String,Integer> hm, int topListSize){
                    Map<String, Integer> sorted = hm
        .entrySet()
        .stream()
        .sorted(comparingByValue())
        .collect(
            toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                LinkedHashMap::new));
        for (int i = 0; i < topListSize; i++) {
            String key = String.valueOf(sorted.keySet().toArray()[sorted.size()-1-i]);
            System.out.println("nr: " + (i+1) + " is " + key + " which was " + hm.get(key) + " times.");
        }
    }
}
