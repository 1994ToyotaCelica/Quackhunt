import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

public class Game extends JLayeredPane
{
    private Quack menu;
    private Game that = this;

    private JLabel clockLabel;
    private JLabel clockDisplay;
    private JLabel clockDisplayShadow;

    private JLabel pointLabel;
    private JLabel pointDisplay;
    private JLabel pointDisplayShadow;

    private JLabel hpLabel;
    private JProgressBar hpBar;

    private JLabel wpnBox;

    private Thread clock;
    private Thread duckFactory;
    private Thread glitchFactory;
    private Thread thingDoer;
    private Thread glitchDoer;
    
    private float diffModifier;
    private float timeModifier;
    
    private ArrayList<Quacker> enemies;
    private ArrayList<Barrier> barriers;
    private int health;
    private int wpnPower;
    private int time;
    private boolean ended;
    
    private Difficulty difficulty;
    private int points;
    private int headCount;
    private int casualties;
    
    public Game(Difficulty difficulty, Quack that)
    {
        menu = that;
        this.difficulty = difficulty;
        setup(difficulty);
        clockDisplaySetup();
        pointDisplaySetup();
        healthDisplaySetup();
        wpnBoxSetup();

        add(clockLabel, new Integer(3));    // Rozwiązanie z dokumentacji Oracle
        add(clockDisplay, new Integer(3));
        add(clockDisplayShadow, new Integer(3));
        add(pointLabel, new Integer(3));
        add(pointDisplay, new Integer(3));
        add(pointDisplayShadow, new Integer(3));
        add(hpLabel, new Integer(3));
        add(hpBar, new Integer(3));
        add(wpnBox, new Integer(3));
    }

    private void setup(Difficulty difficulty)
    {
        setLayout(null);

        thingDoer = new Thread()
        {
            @Override
            public void run()
            {
                while (!ended)
                {
                    try { sleep(10); }
                    catch (InterruptedException e) {}

                    if (health <= 0)
                        gameOver();

                    if (!wpnBox.isVisible() && ((wpnPower == 1 && points >= 200) || (wpnPower == 2 && points >= 500)))
                        wpnBox.setVisible(true);
                    


                    for (Iterator<Quacker> iterator = enemies.iterator(); iterator.hasNext();)
                    {
                        Quacker q = iterator.next();
                        if (!q.isRunning())
                        {
                            iterator.remove();
                            that.remove(q);
                            if(q.isDead())
                            {
                                if(q.getType() == EnemyType.CIVILIAN)
                                    casualties++;
                                else
                                    headCount++;
                                points += q.getType().points;
                                pointDisplay.setText("" + points);
                                pointDisplayShadow.setText("" + points);
                            }
                            else
                            {
                                if (q.getType() == EnemyType.CIVILIAN)
                                    continue;
                                health -= (1 + q.getHealthPoints()/20);
                                if (health < 0)
                                    health = 0;

                                hpBar.setValue(health);
                                if (health <= 3)
                                    hpBar.setForeground(Constants.AC_COLOR);

                                System.out.println("Health: " + health);
                            }
                        }
                    }
                }
            }
        };

        glitchDoer = new Thread()
        {
            @Override
            public void run()
            {
                while (!ended)
                {
                    try { sleep(10); }
                    catch (InterruptedException e) {}

                    for (Iterator<Barrier> iterator = barriers.iterator(); iterator.hasNext();)
                    {
                        Barrier b = iterator.next();
                        if (b.isDead())
                        {
                            iterator.remove();
                            that.remove(b);
                        }
                    }
                }
            }
        };

        glitchFactory = new Thread()
        {
            @Override
            public void run()
            {
                while (!ended)
                {
                    try { sleep(4000); }
                    catch (InterruptedException e) {}

                    double seed = Math.random();
                    if (seed < 0.5)
                    {
                        int x = 100 + (int)(Math.random()*700);
                        int y = 100 + (int)(Math.random()*500);
                        Barrier tmp = new Barrier();
                        tmp.setBounds(x, y, 400, 300);

                        add(tmp, new Integer(2));
                        barriers.add(tmp);
                    }
                }
            }
        };

        duckFactory = new Thread()
        {
            @Override
            public void run()
            {
                while (!ended)
                {
                    try
                    {
                        Quacker tmp = null;
                        int delay = 2000 - (int)(1000*0.5f*timeModifier*diffModifier*Math.random());
                        int duckDice = 1 + (int)(Math.random()*20);
                        int positionY = 50 + (int)(Math.random()*600);
                        
                        if (duckDice > 18)
                        {
                            tmp = Quacker.createTank(that);
                            tmp.setBounds((tmp.isReverse()?-256:1200), positionY, 256, 256);
                        }
                        else if (duckDice > 15)
                        {
                            tmp = Quacker.createStealth(that);
                            tmp.setBounds((tmp.isReverse()?-128:1200), positionY, 128, 128);
                        }
                        else if (duckDice > 8)
                        {
                            tmp = Quacker.createSecondGen(that);
                            tmp.setBounds((tmp.isReverse()?-128:1200), positionY, 128, 128);
                        }
                        else if (duckDice > 2)
                        {
                            tmp = Quacker.createFirstGen(that);
                            tmp.setBounds((tmp.isReverse()?-128:1200), positionY, 128, 128);
                        }
                        else
                        {
                            tmp = Quacker.createCivilian(that);
                            tmp.setBounds((tmp.isReverse()?-128:1200), positionY, 128, 128);
                        }
                        
                        add(tmp, new Integer(1));
                        enemies.add(tmp);
                        tmp.start();
                        sleep((delay<1)?1:delay);
                    }
                    catch (InterruptedException e) {}
                }
            }
        };

        clock = new Thread()
        {
            @Override
            public void run()
            {
                while (!ended)
                {
                    try
                    {
                        sleep(1000);
                        time++;
                        clockDisplay.setText(makeClock(time));
                        clockDisplayShadow.setText(clockDisplay.getText());

                        if (time%5==0)
                        {
                            clockDisplay.setVisible(false);
                            timeModifier *= 1.1;
                            System.out.println("Modifier: " + timeModifier);
                            clockDisplay.setVisible(true);
                        }
                    }
                    catch (InterruptedException e) {}
                }
            }
        };

        ended = false;
        health = 10;
        points = 0;
        headCount = 0;
        casualties = 0;
        wpnPower = 1;
        timeModifier = 1f;
        diffModifier = difficulty.modifier;
        enemies = new ArrayList<>();
        barriers = new ArrayList<>();

        // Poniższa implementacja skrótów klawiszowych została zapożyczona od Kuby Bielickiego
        KeyStroke exitKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK);
        KeyStroke upgradeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0);
        KeyStroke hellNahKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_N, 0);
        
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(exitKeyStroke, "exit");
        getActionMap().put("exit", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(2137);
            }
        });
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(upgradeKeyStroke, "upgrade");
        getActionMap().put("upgrade", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (wpnBox.isVisible())
                {
                    points -= 100;
                    wpnPower *= 2;
                    wpnBox.setVisible(false);
                }
            }
        });
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(hellNahKeyStroke, "hellnah");
        getActionMap().put("hellnah", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (wpnBox.isVisible())
                    wpnBox.setVisible(false);
            }
        });
    }

    private void clockDisplaySetup()
    {
        clockLabel = new JLabel("TIME");
        clockLabel.setFont(Constants.TEXT_FONT.deriveFont(24f));
        clockLabel.setForeground(Constants.AC_COLOR);
        clockLabel.setVerticalAlignment(JLabel.TOP);
        clockLabel.setHorizontalAlignment(JLabel.CENTER);
        clockLabel.setBounds(500, 5, 200, 30);

        clockDisplay = new JLabel();
        clockDisplay.setText(makeClock(0));
        clockDisplay.setFont(Constants.FX_FONT.deriveFont(60f));
        clockDisplay.setForeground(Constants.AC_COLOR);
        clockDisplay.setVerticalAlignment(JLabel.CENTER);
        clockDisplay.setHorizontalAlignment(JLabel.CENTER);
        clockDisplay.setBounds(500, 15, 200, 80);

        clockDisplayShadow = new JLabel();
        clockDisplayShadow.setText(makeClock(0));
        clockDisplayShadow.setFont(Constants.FX_FONT.deriveFont(60f));
        clockDisplayShadow.setForeground(Constants.AC_COLOR.darker().darker().darker().darker());
        clockDisplayShadow.setVerticalAlignment(JLabel.CENTER);
        clockDisplayShadow.setHorizontalAlignment(JLabel.CENTER);
        clockDisplayShadow.setBounds(505, 20, 200, 80);
    }

    private void pointDisplaySetup()
    {
        pointLabel = new JLabel("POINTS");
        pointLabel.setFont(Constants.TEXT_FONT.deriveFont(24f));
        pointLabel.setForeground(Constants.AC_COLOR);
        pointLabel.setVerticalAlignment(JLabel.TOP);
        pointLabel.setHorizontalAlignment(JLabel.LEFT);
        pointLabel.setBounds(30, 5, 200, 30);

        pointDisplay = new JLabel();
        pointDisplay.setText("0");
        pointDisplay.setFont(Constants.FX_FONT.deriveFont(60f));
        pointDisplay.setForeground(Constants.AC_COLOR);
        pointDisplay.setVerticalAlignment(JLabel.CENTER);
        pointDisplay.setHorizontalAlignment(JLabel.LEFT);
        pointDisplay.setBounds(30, 15, 200, 80);

        pointDisplayShadow = new JLabel();
        pointDisplayShadow.setText("0");
        pointDisplayShadow.setFont(Constants.FX_FONT.deriveFont(60f));
        pointDisplayShadow.setForeground(Constants.AC_COLOR.darker().darker().darker().darker());
        pointDisplayShadow.setVerticalAlignment(JLabel.CENTER);
        pointDisplayShadow.setHorizontalAlignment(JLabel.LEFT);
        pointDisplayShadow.setBounds(35, 20, 200, 80);
    }

    private void healthDisplaySetup()
    {
        hpLabel = new JLabel("HEALTH");
        hpLabel.setFont(Constants.TEXT_FONT.deriveFont(24f));
        hpLabel.setForeground(Constants.AC_COLOR);
        hpLabel.setVerticalAlignment(JLabel.TOP);
        hpLabel.setHorizontalAlignment(JLabel.RIGHT);
        hpLabel.setBounds(950, 5, 200, 30);

        hpBar = new JProgressBar(0, 10);
        hpBar.setValue(health);
        hpBar.setForeground(Constants.FG_COLOR);
        hpBar.setBackground(Constants.BG_COLOR);
        hpBar.setBorderPainted(false);
        hpBar.setBounds(950, 45, 200, 40);
    }

    private void wpnBoxSetup()
    {
        wpnBox = new JLabel(Constants.UPGRADE_ICON);
        wpnBox.setVisible(false);
        wpnBox.setBounds(20, 95, 200, 100);
    }

    public void start()
    {
        clock.start();
        duckFactory.start();
        glitchFactory.start();
        thingDoer.start();
        glitchDoer.start();
        menu.playSound(Constants.GAME_THEME, true);
    }

    private void gameOver()
    {
        ended = true;
        menu.gameOver(new Score(difficulty, points, headCount, time, casualties));
    }

    public float getDiffModifier()
    { return diffModifier; }
    public float getTimeModifier()
    { return timeModifier; }
    public int getWpnPower()
    { return wpnPower; }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("Quack Hunt/ass/gamebg.png").getImage(), 0, 0, null);
    }

    private static String makeClock(int secs)
    {
        StringBuilder build = new StringBuilder();
        int minutes = secs/60;
        int seconds = secs%60;

        build.append((minutes>=10)?(minutes/10):0);
        build.append(minutes%10);
        build.append(':');
        build.append((seconds>=10)?(seconds/10):0);
        build.append(seconds%10);

        return build.toString();
    }
}