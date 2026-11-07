package org.tolis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) {
        Dictionary<String, Integer> dictionary = new HashMap<>();

        System.out.println("Please, enter the absolute path of the file you want to scan:");

        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line);

                while (tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken();

                    Integer frequency = dictionary.get(word);

                    if (frequency == null) {
                        dictionary.put(word, 1);
                    } else {
                        dictionary.put(word, frequency + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading the file: " + e.getMessage());
        } finally {
            scanner.close();
        }

        for (Dictionary.Entry<String, Integer> entry : dictionary) {
            System.out.println("Word: " + entry.getKey() + " appeared " + entry.getValue() + " times");
        }
    }

}