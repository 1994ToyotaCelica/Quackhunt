public class Start
{
    public static void main(String[] args) throws Exception
    {
        SpringUtilities.invokeEarlier(() -> { new Quack(true).start(); });
    }
}