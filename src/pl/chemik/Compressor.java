package pl.chemik;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Compressor {

    private File file;
    private List<String> fileLetters;
    private Map<String, Float> probabilities;
    private Map<String, BitSet> lettersCode;

    public Compressor(String filename) {
        readInputFile(filename);
        checkProbabilities();
        create();
        encode();
    }

    private void displayText(List<String> wordsArray, int maxNumbers) {
        for (int i = 0; i < maxNumbers; i++) {
            System.out.printf(wordsArray.get(i));
        }
    }

    private void readInputFile(String filename) {
        file = new File("inputs/" + filename);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fileLetters = new ArrayList<>();
        while (scanner.hasNext()) {
            String word = scanner.next();
            for (int i = 0; i < word.length(); i++) {
                fileLetters.add(String.valueOf(word.charAt(i)));
            }
            fileLetters.add(" ");
        }
        scanner.close();
    }

    private void checkProbabilities() {
        Map<String, Integer> counts = new HashMap<>();
        float totalCount = 0;
        for (String letter : fileLetters) {
            if (!counts.keySet().contains(letter)) {
                counts.put(letter, 1);
            } else {
                counts.put(letter, counts.get(letter) + 1);
            }
            totalCount++;
        }

        probabilities = new HashMap<>();
        for (String letter : counts.keySet()) {
            probabilities.put(letter, counts.get(letter) / totalCount);
        }
    }

    private Map<String, BitSet> generateBasicCodes(List<Tuple> tuples) {
        Map<String, BitSet> codesMap = new HashMap<>();
        for (Tuple tuple : tuples) {
            BitSet code = new BitSet(6);
            int counter = 0;
            for (String bit : Integer.toBinaryString(tuple.getOrder()).split("")) {
                int bitValue = Integer.parseInt(bit);
                if (bitValue == 1) {
                    code.set(counter);
                }
                counter++;
            }
            codesMap.put(tuple.getLetter(), code);
        }
        return codesMap;
    }

    /**
     * Na podstawie listy częstości poszczególnych znaków tworzy kod
     */
    public void create() {
        List<Tuple> tuples = new ArrayList<>();
        for (String letter : probabilities.keySet()) {
            tuples.add(new Tuple(0, letter, probabilities.get(letter)));
        }
        tuples.sort((o1, o2) -> {
            int r1 = (int) Math.ceil((o1.getProbability() * 1000));
            int r2 = (int) Math.ceil((o2.getProbability() * 1000));
            return r2 - r1;
        });

        int order = 0;
        for (Tuple tuple : tuples) {
            tuple.setOrder(order++);
        }
        lettersCode = generateBasicCodes(tuples);
    }

    /**
     * Tworzy zakodowaną reprezentację tekstu
     */
    public void encode() {
        BitSet bitSet = new BitSet(6 * fileLetters.size());
        int globalBitNumber = 0;
        for (String letter : fileLetters) {
            BitSet letterCode = lettersCode.get(letter);
            for (int i = 0; i < 6; i++) {
                if(letterCode.get(i)) {
                    bitSet.set(globalBitNumber + i);
                }
            }
            globalBitNumber += 6;
        }
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
