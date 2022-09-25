import javax.swing.SwingUtilities;

public class SpringUtilities
{
    public static void invokeEarlier(Runnable doRun)
    {
        SwingUtilities.invokeLater(doRun);
    }
}