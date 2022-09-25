import java.io.Serializable;
import java.util.List;

public class Score implements Serializable
{
    private String player;
    private Difficulty difficulty;
    private int points;
    private int headCount;
    private int casualties;
    private int secondsLasted;
    private int score;

    public Score(Difficulty d, int p, int h, int t, int c)
    {
        difficulty = d;
        points = p;
        headCount = h;
        casualties = c;
        secondsLasted = t;
        score = (int)((points * difficulty.modifier) + (points * headCount)/100 + (points * secondsLasted)/60);
    }

    public void setPlayerName(String pName)
    { player = pName; }

    public String getPlayerName()
    { return player; }
    public Difficulty getDifficulty()
    { return difficulty; }
    public int getPoints()
    { return points; }
    public int getHeadCount()
    { return headCount; }
    public int getTimeLasted()
    { return secondsLasted; }
    public int getScore()
    { return score; }

    @Override
    public String toString() {
        return  "Difficulty:    " + difficulty +
              "\nTime lasted:   " + secondsLasted + " s" +
              "\nPoints:         " + points +
              "\nHead count:    " + headCount +
              "\nCasualties:     " + casualties +
              "\nScore overall:  " + score;
    }

    public static Score[] listConverterThatDoesNotReturnAnArrayOfObjectObjectsYesImLookingAtYouToArrayMethodYouStupidPieceOfCrap(List<Score> scores)
    {
        Score[] scoreArray = new Score[scores.size()];

        for (int i = 0; i < scores.size(); )
        {
            scoreArray[i] = scores.get(i);
        }

        return scoreArray;
    }
}