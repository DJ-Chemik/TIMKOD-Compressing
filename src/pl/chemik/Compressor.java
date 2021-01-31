package pl.chemik;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                if (i < firstValue) {
                    bits.add("0");
                } else {
                    bits.add(stringBits[i - firstValue]);
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
     *
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
     *
     * @return
     */
    public BitSet encode(Map<String, BitSet> lettersCode) {
        BitSet bitSet = new BitSet(6 * fileLetters.size());
        int globalBitNumber = 0;
        for (String letter : fileLetters) {
            BitSet letterCode = lettersCode.get(letter);
            for (int i = 0; i < 6; i++) {
                if (letterCode.get(i)) {
                    bitSet.set(globalBitNumber + i);
                }
            }
            globalBitNumber += 6;
        }
        return bitSet;
    }

    /**
     * Odkodowuje zakodowany tekst
     *
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

    private BitSet changeCodeMapToBitSet(Map<String, BitSet> lettersCode) {
        BitSet bitSet = new BitSet(lettersCode.size() * (8 + 6));
        int globalNumber = 0;
        for (String key : lettersCode.keySet()) {
            BitSet letterAsBitSet = BitSet.valueOf(key.getBytes());
            BitSet value = lettersCode.get(key);
            for (int i = 0; i < 8; i++) {
                if (letterAsBitSet.get(i)) {
                    bitSet.set(globalNumber + i);
                }
            }
            globalNumber += 8;
            for (int i = 0; i < 6; i++) {
                if (value.get(i)) {
                    bitSet.set(globalNumber + i);
                }
            }
            globalNumber += 6;
//            System.out.printf(key + " --> ");
//            System.out.printf(letterAsBitSet + " --> ");
//            System.out.println(value);
        }
        return bitSet;
    }

    /**
     * zapisuje kod oraz zakodowany tekst
     */
    public void save(BitSet textToSave, Map<String, BitSet> lettersCode, String filename) {
        File outCodeFile = new File("outputs/" + filename + "_code.cpd");
        File outFile = new File("outputs/" + filename + ".cpr");
        BitSet codeToSave = changeCodeMapToBitSet(lettersCode);
        try {
            // encoded textcodeToSave
            FileOutputStream fos = new FileOutputStream(outFile);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
            fos.write(textToSave.toByteArray());
            fos.close();

            // compressing code
            FileOutputStream fosCode = new FileOutputStream(outCodeFile);
            fosCode.write(codeToSave.toByteArray());
            fosCode.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wczytuje zakodowany tekst oraz kod
     */
    public BitSet load(String filename, Map<String, BitSet> lettersCode) {
        Path path = Paths.get("outputs/" + filename + ".cpr");
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitSet bitSet = BitSet.valueOf(bytes);

        int global = 0;
        for (int x = 0; x < bitSet.length(); x++) {
            BitSet code = new BitSet(6);
            for (int i = 0; i < 6; i++) {
                if (bitSet.get(global + i)) {
                    code.set(i);
                }
            }
            global += 6;
        }
        return bitSet;
    }
}
