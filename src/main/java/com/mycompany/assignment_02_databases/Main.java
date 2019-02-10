/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.assignment_02_databases;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
        System.out.println("*****************\nwelcome,\ntype 'howMany' to see how many twitter users\ntype 'links' to see user who links twitter users the most\ntype 'mentioned' to see the most mentioned twitter user\n************");
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
            String most = timesUser.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
            System.out.println("user who links the most to other twitter users: " + most + ", times is: " + timesUser.get(most));
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
                        timesUser.put(key, 0);
                        continue;
                    }
                    timesUser.put(key, timesUser.get(key) + 1);
                }

            }
            System.out.println("done?");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(timesUser);
        String most = timesUser.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
        System.out.println("user who was mentioned the most is: " + most + ", times was:  " + timesUser.get(most));
    }
}
