import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Particle extends JLabel
{   // you'll n'wahs don't even drink skooma 
    int hVec, vVec;

    public Particle()
    {
        hVec = (int)(Math.random()*9) - 4;
        vVec = (int)(Math.random()*3) + 2;

        setIcon(new ImageIcon("Quack Hunt/ass/particle.png"));
        setSize(5, 5);
    }
}