package pl.chemik;

import java.util.BitSet;

public class Main {

    public static void main(String[] args) {
        Compressor compressor = new Compressor("norm_wiki_sample.txt");
        BitSet encodedText = compressor.encode();
        String decompressedText = compressor.decode(encodedText);
    }
}
