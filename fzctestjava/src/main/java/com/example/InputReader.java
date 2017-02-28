package com.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class InputReader {

    private StringTokenizer tokenizer;
    private BufferedReader reader;

    public InputReader(InputStream inputStream) {
        tokenizer = null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }


    public boolean hasNext() {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            try {
                tokenizer = new StringTokenizer(reader.readLine());
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public String next() {
        return tokenizer.nextToken();
    }

}
