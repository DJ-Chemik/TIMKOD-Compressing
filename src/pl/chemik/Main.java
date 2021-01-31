package pl.chemik;

import java.util.BitSet;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Compressor compressor = new Compressor("norm_wiki_sample.txt");
        Map<String, BitSet> compressingCode = compressor.create();
        BitSet encodedText = compressor.encode(compressingCode);
        String decompressedText = compressor.decode(encodedText, compressingCode);
//        System.out.println(decompressedText);
    }
}
