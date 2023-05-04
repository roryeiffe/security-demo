package com.revature.util;

import java.io.*;

public class HintRead {
    public static void main(String[] args) {
        read();
    }

    public static void read() {
        // Input and output file names
        String inputFile = "hints_encrypted.md";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));) {

            int c;
            while ((c = reader.read()) != -1) {
                char character = (char) c;
                if (Character.isLetter(character)) {
                    // Shift alphabetical characters
                    if (character == 'a') {
                        character = 'z';
                    } else if (character == 'A') {
                        character = 'Z';
                    } else {
                        character--;
                    }
                }
                System.out.print(character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void encrypt() {
        // Input and output file names
        String inputFile = "hints.md";
        String outputFile = "hints_encrypted.md";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            int c;
            while ((c = reader.read()) != -1) {
                char character = (char) c;
                if (Character.isLetter(character)) {
                    // Shift alphabetical characters
                    if (character == 'z') {
                        character = 'a';
                    } else if (character == 'Z') {
                        character = 'A';
                    } else {
                        character++;
                    }
                }
                writer.write(character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
