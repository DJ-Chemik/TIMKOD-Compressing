package pl.chemik;

import java.util.BitSet;
import java.util.Map;

public class Main {

    private static boolean checkIs2TextIdentical(String text1, String text2) {
        boolean result = text1.equals(text2);
        System.out.println(result);
        return result;
    }

    private static void useSixBitCompressor() {
        SixBitCompressor sixBitCompressor = new SixBitCompressor("norm_wiki_sample.txt");
        Map<String, BitSet> compressingCode = sixBitCompressor.create();
        BitSet encodedText = sixBitCompressor.encode(compressingCode);
        String decompressedText = sixBitCompressor.decode(encodedText, compressingCode);
        sixBitCompressor.save(encodedText, compressingCode, "test");
        LoadingResult laodingResult = sixBitCompressor.load("test");
        String decodedTextFromFile = sixBitCompressor.decode(laodingResult.getCompressedText(), laodingResult.getCode());
        //        System.out.println(decompressedText);
        //        System.out.println(decodedTextFromFile);
        checkIs2TextIdentical(decompressedText, decodedTextFromFile);
    }

    private static void useHuffmanCompressor() {
        SixBitCompressor sixBitCompressor = new SixBitCompressor("norm_wiki_sample.txt");
        Map<String, BitSet> compressingCode = sixBitCompressor.create();
        BitSet encodedText = sixBitCompressor.encode(compressingCode);
        String decompressedText = sixBitCompressor.decode(encodedText, compressingCode);
        sixBitCompressor.save(encodedText, compressingCode, "huffman");
        LoadingResult laodingResult = sixBitCompressor.load("huffman");
        String decodedTextFromFile = sixBitCompressor.decode(laodingResult.getCompressedText(), laodingResult.getCode());
        //        System.out.println(decompressedText);
        //        System.out.println(decodedTextFromFile);
        checkIs2TextIdentical(decompressedText, decodedTextFromFile);
    }

    public static void main(String[] args) {
        useSixBitCompressor();
//        useHuffmanCompressor();
    }
}
