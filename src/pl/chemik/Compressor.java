package pl.chemik;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Compressor {

    private File file;
    private List<String> fileLetters;

    public Compressor() {

    }

    private void displayText(List<String> wordsArray, int maxNumbers) {
        for (int i = 0; i < maxNumbers; i++) {
            System.out.printf(wordsArray.get(i));
        }
    }

    public void readInputFile(String filename) {
        file = new File("inputs/" + filename);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fileLetters = new ArrayList<>();
        while (scanner.hasNext ()) {
            String word = scanner.next();
            for (int i = 0; i < word.length(); i++) {
                fileLetters.add(String.valueOf(word.charAt(i)));
            }
            fileLetters.add(" ");
        }
        scanner.close();
    }

    /**
     * Na podstawie listy częstości poszczególnych znaków tworzy kod
     */
    public void create() {

    }

    /**
     * Tworzy zakodowaną reprezentację tekstu
     */
    public void encode() {

    }

    /**
     * Odkodowuje zakodowany tekst
     */
    public void decode() {

    }

    /**
     * zapisuje kod oraz zakodowany tekst
     */
    public void save() {

    }

    /**
     * Wczytuje zakodowany tekst oraz kod
     */
    public void load() {

    }
}
