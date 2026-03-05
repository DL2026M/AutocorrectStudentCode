public class WordDistancePair
{
    private String word;
    private int editDistance;

    public WordDistancePair(String word, int editDistance)
    {
        this.word = word;
        this.editDistance = editDistance;
    }
    public String getWord()
    {
        return word;
    }
    public int getEditDistance()
    {
        return editDistance;
    }

}
