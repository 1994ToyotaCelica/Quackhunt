import javax.swing.JButton;

public class Hitbox extends JButton
{
    public Hitbox()
    {
        setOpaque(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusable(false);
    }

    public Hitbox(int x, int y, int width, int height)
    {
        this();
        setBounds(x, y, width, height);
    }
}