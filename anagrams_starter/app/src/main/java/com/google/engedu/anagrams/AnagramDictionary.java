package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private HashSet<String> wordSet = new HashSet<>();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashMap<String, ArrayList> lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList> sizeToWords = new HashMap<>();

    private int wordLength = DEFAULT_WORD_LENGTH;


    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;


        while((line = in.readLine()) != null) {
            String word = line.trim();
            String alphabetizedWord = alphabeticalOrder(word);
            wordSet.add(word);
            wordList.add(word);

            ArrayList<String> valueSizeToWords;
            if (!sizeToWords.containsKey(word.length())){
                // If sizeToWords does not contain key already
                valueSizeToWords = new ArrayList<String>(Collections.singletonList(word));
            } else {
                // Else sizeToWords already contains that key
                valueSizeToWords = sizeToWords.get(word.length());
                valueSizeToWords.add(word);
            }

            sizeToWords.put(word.length(), valueSizeToWords);



            ArrayList<String> valueHashMap;
            if (!lettersToWord.containsKey(alphabetizedWord)){
                // If lettersToWord does not contain key already
                valueHashMap = new ArrayList<>();
            } else {
                // Else lettersToWord already contains that key
                valueHashMap = lettersToWord.get(alphabetizedWord);
            }

            valueHashMap.add(word);
            // key: String of alphabetized, value: ArrayList of anagrams
            lettersToWord.put(alphabetizedWord, valueHashMap);
        }

        for(Object member: lettersToWord.get("abdeg")){
            Log.i("Member", member.toString());
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        ArrayList<String> result = new ArrayList<>();

        for(char letter: alphabet) {
            String alphabetizedKey = alphabeticalOrder(word + letter);

            if(lettersToWord.containsKey(alphabetizedKey)) {
                ArrayList<String> value = lettersToWord.get(alphabetizedKey);
                for(String v: value){
                    if (!v.contains(word))
                        result.add(v);
                }
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {
        int randomNum;
        String word;


        do {
            randomNum = random.nextInt(sizeToWords.get(wordLength).size());
            word = sizeToWords.get(wordLength).get(randomNum).toString();
        } while (getAnagramsWithOneMoreLetter(word).size() < MIN_NUM_ANAGRAMS);


        if (wordLength < MAX_WORD_LENGTH){ wordLength++; }

        return word;
    }

    public String alphabeticalOrder(String word) {
        char[] wordArray = word.toCharArray();
        Arrays.sort(wordArray);
        return new String(wordArray);
    }

}
