package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    public Random r=new Random();
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

        if(prefix.equals("")){
            return words.get(r.nextInt(words.size()));
        }else {

            int pos=Collections.binarySearch(words,prefix);
            Log.d("prefix",""+prefix);
            Log.d("words "+words.get(Math.abs(pos)-2),""+words.get(Math.abs(pos)-1));
            Log.d("pos",""+pos);
            if(words.get(Math.abs(pos)-1).contains(prefix)){
                return words.get(Math.abs(pos)-1);
            }

        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
