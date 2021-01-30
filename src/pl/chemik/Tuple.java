package pl.chemik;

public class Tuple {
    private int order;
    private String letter;
    private float probability;

    public Tuple() {
    }

    public Tuple(int order, String letter, float probability) {
        this.order = order;
        this.letter = letter;
        this.probability = probability;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }
}
