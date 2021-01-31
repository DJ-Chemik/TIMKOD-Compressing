package pl.chemik;

import java.io.IOException;
import java.util.BitSet;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Compressor compressor = new Compressor("norm_wiki_sample.txt");
        Map<String, BitSet> compressingCode = compressor.create();
        BitSet encodedText = compressor.encode(compressingCode);
        String decompressedText = compressor.decode(encodedText, compressingCode);
        compressor.save(encodedText, compressingCode, "test");
        BitSet loadedTextFromFile = compressor.load("test", compressingCode);
        String decodedTextFromFile = compressor.decode(loadedTextFromFile, compressingCode);


//        System.out.println(decompressedText);
//        System.out.println(decodedTextFromFile);

        System.out.println(decodedTextFromFile.equals(decompressedText));
    }
}
