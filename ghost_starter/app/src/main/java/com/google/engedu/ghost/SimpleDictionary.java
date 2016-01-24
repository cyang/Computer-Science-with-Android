package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random random = new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix.isEmpty()){
            return words.get(random.nextInt(words.size()));
        } else {
            return binarySearch(words, prefix);
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }

    public String binarySearch(ArrayList<String> input, String prefix){
        int length = input.size();
        String current = input.get(length/2);
        System.out.println(length);


        if(current.startsWith(prefix))
            return current;

        if(!current.startsWith(prefix) && length == 1)
            return null;

        ArrayList<String> temp = new ArrayList<>();

        int i = 0;
        char[] prefixCharArray = prefix.toCharArray();
        char[] currentCharArray = current.toCharArray();

        // Find index of first non-matching character between prefix and current
        while(i < prefixCharArray.length){
            if(prefixCharArray[i] != currentCharArray[i]){
                break;
            }
            i++;
        }

        if(prefixCharArray[i] < currentCharArray[i]){
            // Check left
            temp.addAll(input.subList(0, length/2));
            return binarySearch(temp, prefix);
        } else {
            // Check right
            temp.addAll(input.subList(length/2, length));
            return binarySearch(temp, prefix);
        }
    }
}
