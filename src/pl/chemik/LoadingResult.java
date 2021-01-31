package pl.chemik;

import java.util.BitSet;
import java.util.Map;

public class LoadingResult {
    private final BitSet compressedText;
    private final Map<String, BitSet> code;

    public LoadingResult(BitSet compressedText, Map<String, BitSet> code) {
        this.compressedText = compressedText;
        this.code = code;
    }

    public BitSet getCompressedText() {
        return compressedText;
    }

    public Map<String, BitSet> getCode() {
        return code;
    }

}
