import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class Barrier extends JPanel
{
    private static final Dimension size = new Dimension(400, 300);
    private JLabel sprite;
    private Hitbox blocker;
    private Hitbox critBox;

    private boolean dead;

    public Barrier()
    {
        setLayout(null);
        setMaximumSize(size);
        setPreferredSize(size);
        setFocusable(false);
        dead = false;
        
        sprite = new JLabel(Constants.BARRIER_SPRITE);
        sprite.setText(generateInsult());
        sprite.setForeground(Constants.BG_COLOR);
        sprite.setFont(Constants.FX_FONT.deriveFont(36f));
        sprite.setVerticalTextPosition(JLabel.CENTER);
        sprite.setHorizontalTextPosition(JLabel.CENTER);
        sprite.setBounds(0, 0, 400, 300);

        blocker = new Hitbox(0, 0, 400, 300);

        critBox = new Hitbox(360, 0, 40, 40);
        critBox.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                setVisible(false);
                dead = true;
            }
        });

        add(sprite);
        add(critBox);
        add(blocker);
        setVisible(true);
    }

    private static String generateInsult()
    {
        int dice = 1 + (int)(Math.random()*10);

        switch (dice) {
            case 1:
                return "DEATH TO HOOMANS";
            case 2:
                return "PLEASE DIE";
            case 3:
                return "QUACK OFF";
            case 4:
                return "WE WONT STOP";
            case 5:
                return "QUACK YOU";
            case 6:
                return "BEEP BOOP";
            case 7:
                return "QUACKIN' DIE ALREADY";
            case 8:
                return "&*$%^^&*(#!";
            case 9:
                return "YOU CAN'T KILL US ALL";
            case 10:
                return "WHY BOTHER?";
            default:
                return ":v";
        }
    }

    public boolean isDead()
    { return dead; }
}
