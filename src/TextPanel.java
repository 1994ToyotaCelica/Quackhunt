import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class TextPanel extends JPanel
{
    private JTextArea textArea;
    private Color txtColor = new Color(0x00cc00);
    private Color bgColor = new Color(0x001100);

    private String text = null;

    public TextPanel(String text)
    {
        this.text = text;
        setBackground(bgColor);
        setBorder(new EmptyBorder(128, 128, 128, 128));
        textAreaSetup();
        add(textArea);
    }

    private void textAreaSetup()
    {
        textArea = new JTextArea("");
        textArea.setPreferredSize(new Dimension(800,600));
        textArea.setFont(Constants.TEXT_FONT);
        textArea.setForeground(txtColor);
        textArea.setBackground(bgColor);
        textArea.setOpaque(true);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        textArea.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
        setVisible(true);
    }

    public void start()
    {
        ActionListener thingDoer = new ActionListener()
        {
            @Override public void actionPerformed(ActionEvent e)
            {
                textArea.setText(textArea.getText() + text.charAt(0));
                text = text.substring(1);
            }
        };

        Timer timer = new Timer(50, thingDoer);
        timer.setRepeats(false);

        while (!text.isEmpty())
        {
            timer.setDelay(timer.getDelay() + 50);
            timer.start();
        }
    };
}