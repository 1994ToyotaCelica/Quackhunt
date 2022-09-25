public enum Difficulty
{
    BABY (0.5f),
    SOLDIER         (1f),
    CYBER_COMMANDO  (1.5f),
    DUCK_HUNTER     (2f);

    public float modifier;

    private Difficulty(float modifier)
    {
        this.modifier = modifier;
    }
}