package pl.chemik;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Compressor {

    private File file;
    private List<String> fileLetters;
    private Map<String, Float> probabilities;

    public Compressor(String filename) {
        readInputFile(filename);
        checkProbabilities();
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
            String[] stringBits = Integer.toBinaryString(tuple.getOrder()).split("");
            List<String> bits = new ArrayList<>();
            int firstValue = 6 - stringBits.length;
            for (int i = 0; i < 6; i++) {
                if (i<firstValue) {
                    bits.add("0");
                }else {
                    bits.add(stringBits[i-firstValue]);
                }
            }
            for (int i = 0; i < 6; i++) {
                int bitValue = Integer.parseInt(bits.get(i));
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
     * @return
     */
    public Map<String, BitSet> create() {
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
        Map<String, BitSet> lettersCode;
        lettersCode = generateBasicCodes(tuples);
        return lettersCode;
    }

    /**
     * Tworzy zakodowaną reprezentację tekstu
     * @return
     */
    public BitSet encode(Map<String, BitSet> lettersCode) {
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
        return bitSet;
    }

    /**
     * Odkodowuje zakodowany tekst
     * @return
     */
    public String decode(BitSet textToDecode, Map<String, BitSet> lettersCode) {
        StringBuilder decompressedText = new StringBuilder("");
        HashMap<BitSet, String> decodingCode = new HashMap<>();
        lettersCode.forEach((letter, code) -> decodingCode.put(code, letter));

        BitSet tmp = new BitSet(6);
        int count = 0;
        for (int i = 0; i < textToDecode.length(); i++) {
            if (textToDecode.get(i)) {
                tmp.set(count);
            }
            count++;
            if (count == 6) {
                String foundLetter = decodingCode.get(tmp);
                decompressedText.append(foundLetter);
                count = 0;
                tmp = new BitSet(6);
            }
        }
        return decompressedText.toString();
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
