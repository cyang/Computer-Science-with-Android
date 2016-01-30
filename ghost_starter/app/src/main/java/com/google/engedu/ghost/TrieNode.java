package com.google.engedu.ghost;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        int i = 0;
        HashMap<String, TrieNode> cursor = children;

        while(i < s.length()){

            if (cursor.containsKey(String.valueOf(s.charAt(i)))) {
                // The character already exists, then go to the next children
                cursor = cursor.get(String.valueOf(s.charAt(i))).children;
            } else {
                // The character doesn't exist so create a new neighbor node
                TrieNode insertNode = new TrieNode();

                cursor.put(String.valueOf(s.charAt(i)), insertNode);
                cursor = insertNode.children;

                if(i+1 == s.length()){
                    insertNode.isWord = true;
                }
            }

            i++;
        }


    }

    public boolean isWord(String s) {
        Log.i("what", s);

        int i = 0;
        HashMap<String, TrieNode> cursor = children;
        TrieNode currentNode = new TrieNode();

        while(i < s.length()){
            if (cursor.containsKey(String.valueOf(s.charAt(i)))){
                // The character exists, then go to the next children
                currentNode = cursor.get(String.valueOf(s.charAt(i)));
                cursor = currentNode.children;
            } else {
                // The character doesn't exist
                return false;
            }

            i++;
        }

        return currentNode.isWord;

    }

    public String getAnyWordStartingWith(String s) {
        String output = "";
        int i = 0;
        HashMap<String, TrieNode> cursor = children;

        if(!s.isEmpty()){
            while(i < s.length()){
                // Travel through the prefix
                if(cursor.containsKey(String.valueOf(s.charAt(i)))) {
                    cursor = cursor.get(String.valueOf(s.charAt(i))).children;
                } else {
                    return null;
                }
                i++;
            }

            output = s;

            while(cursor != null){
                // Get the remaining suffix of the word by traversing through the end of the trie
                Iterator keyIterator = cursor.keySet().iterator();

                if(keyIterator.hasNext()) {
                    String currentChar = keyIterator.next().toString();
                    cursor = cursor.get(String.valueOf(currentChar)).children;

                    output = output.concat(currentChar);
                } else {
                    break;
                }
            }
        } else {
            // Grab a random word from the dictionary
            Iterator keyIterator = cursor.keySet().iterator();

            while(cursor != null) {
                Random random = new Random();
                int numIteration = random.nextInt(cursor.keySet().size());

                int j = 0;
                String currentChar = "";

                while (j < numIteration && keyIterator.hasNext()) {
                    currentChar = keyIterator.next().toString();
                    j++;
                }

                output = output.concat(currentChar);

                if(cursor.containsKey(currentChar)) {
                    cursor = cursor.get(currentChar).children;
                } else {
                    break;
                }
            }
        }

        return output;
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
