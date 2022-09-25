import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.event.MouseInputAdapter;

public class MenuButton extends JButton
{
    private Dimension buttonSize = new Dimension(600,80);
    private MouseInputAdapter effect = new MouseInputAdapter()
    {
        @Override
        public void mouseEntered(MouseEvent e)
        { setBackground(Constants.BG_SHADE_COLOR); setForeground(Constants.AC_COLOR); }

        @Override
        public void mouseExited(MouseEvent e)
        { setBackground(Constants.BG_COLOR); setForeground(Constants.FG_COLOR); }
    };

    public MenuButton(String text)
    {
        setText(text);
        setFont(Constants.TEXT_FONT);
        setForeground(Constants.FG_COLOR);
        setBackground(Constants.BG_COLOR);
        setAlignmentX(JButton.CENTER_ALIGNMENT);
        setAlignmentY(JButton.CENTER_ALIGNMENT);
        setOpaque(true);
        setFocusable(false);
        setBorderPainted(false);
        setPreferredSize(buttonSize);
        setMaximumSize(buttonSize);

        addMouseListener(effect);
    }

    public MouseInputAdapter getEffect()
    {
        return effect;
    }
}
