import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class Quacker extends JPanel
{
    private Thread mover;
    private Thread animator;
    private Hitbox bodyBox;
    private Hitbox critBox;
    private JLabel sprite;

    private ImageIcon frame1;
    private ImageIcon frame2;
    
    private float modifier;
    private EnemyType type;
    private boolean reverse;
    private int healthPoints;
    private int speedPerTick;
    private boolean dead;
    private boolean running;

    public Quacker(Game target)
    {
        setLayout(null);
        setOpaque(false);
        bodyBox = new Hitbox();
        critBox = new Hitbox();
        sprite = new JLabel();
        dead = false;
        running = true;
        reverse = Math.random() > 0.5;
        modifier = target.getDiffModifier() * target.getTimeModifier();

        bodyBox.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                healthPoints -= target.getWpnPower();
                if (healthPoints <= 0)
                    dead = true;
            }
        });
        critBox.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                healthPoints -= 2*target.getWpnPower();
                if (healthPoints <= 0)  dead = true;
            }
        });

        mover = new Thread()
        {
            @Override
            public void run()
            {
                while (!mover.isInterrupted())
                {
                    try { sleep(10); }
                    catch (InterruptedException e) {}
                    setLocation(getX()-(int)((reverse?-1:1)*speedPerTick*modifier), getY());
                    if (getX() < -100 - getWidth() || getX() > 1300 + getWidth())
                        running = false;
                    if (healthPoints <= 0)
                    {
                        dead = true;
                        running = false;
                    }
                    if (!running)
                    {
                        setVisible(false);
                        animator.interrupt();
                        mover.interrupt();
                    }
                }
            }
        };

        animator = new Thread()
        {
            @Override
            public void run()
            {
                while (!animator.isInterrupted())
                {
                    try
                    {   // Manualne obejście logiki. Szybsze (?)
                        sleep(250);
                        sprite.setIcon(frame2);
                        sleep(250);
                        sprite.setIcon(frame1);
                    }
                    catch (InterruptedException e) {}
                }
            }
        };
    }
    
    public void start()
    {
        mover.start();
        animator.start();
    }

    public EnemyType getType()
    { return type; }

    public int getHealthPoints()
    { return healthPoints; }

    public int getSpeedPerTick()
    { return speedPerTick; }

    public boolean isDead()
    { return dead; }

    public boolean isRunning()
    { return running; }

    public boolean isReverse()
    { return reverse; }

    public static Quacker createFirstGen(Game target)
    {
        Quacker tmp = new Quacker(target);
        tmp.setSize(128, 128);
        tmp.type = EnemyType.BASIC;
        tmp.sprite.setBounds(0, 0, 128, 128);
        tmp.bodyBox.setBounds(48, 29, 80, 80);
        tmp.critBox.setBounds(0, 45, 48, 48);
        tmp.healthPoints = 4;
        tmp.speedPerTick = 2;

        if (tmp.reverse)
        {
            tmp.frame1 = Constants.GEN1_SPRITE1R;
            tmp.frame2 = Constants.GEN1_SPRITE2R;
        }
        else
        {
            tmp.frame1 = Constants.GEN1_SPRITE1;
            tmp.frame2 = Constants.GEN1_SPRITE2;
        }
        
        tmp.sprite.setIcon(tmp.frame1);
        tmp.add(tmp.sprite);
        tmp.add(tmp.critBox);
        tmp.add(tmp.bodyBox);

        return tmp;
    }


    public static Quacker createSecondGen(Game target)
    {
        Quacker tmp = new Quacker(target);
        tmp.setSize(128, 128);
        tmp.type = EnemyType.ADVANCED;
        tmp.sprite.setBounds(0, 0, 128, 128);
        tmp.critBox.setBounds(0, 45, 48, 48);
        tmp.bodyBox.setBounds(48, 29, 80, 80);
        tmp.healthPoints = 8;
        tmp.speedPerTick = 3;

        if (tmp.reverse)
        {
            tmp.frame1 = Constants.GEN2_SPRITE1R;
            tmp.frame2 = Constants.GEN2_SPRITE2R;
        }
        else
        {
            tmp.frame1 = Constants.GEN2_SPRITE1;
            tmp.frame2 = Constants.GEN2_SPRITE2;
        }
        
        tmp.sprite.setIcon(tmp.frame1);
        tmp.add(tmp.sprite);
        tmp.add(tmp.critBox);
        tmp.add(tmp.bodyBox);

        return tmp;
    }

    public static Quacker createStealth(Game target)
    {
        Quacker tmp = new Quacker(target);
        tmp.setSize(128, 128);
        tmp.type = EnemyType.STEALTH;
        tmp.sprite.setBounds(0, 0, 128, 128);
        tmp.bodyBox.setBounds(0, 45, 128, 48);
        tmp.healthPoints = 2;
        tmp.speedPerTick = 6;

        if (tmp.reverse)
        {
            tmp.frame1 = Constants.STEALTH_SPRITE1R;
            tmp.frame2 = Constants.STEALTH_SPRITE2R;
        }
        else
        {
            tmp.frame1 = Constants.STEALTH_SPRITE1;
            tmp.frame2 = Constants.STEALTH_SPRITE2;
        }
        
        tmp.sprite.setIcon(tmp.frame1);
        tmp.add(tmp.sprite);
        tmp.add(tmp.critBox);
        tmp.add(tmp.bodyBox);

        return tmp;
    }

    public static Quacker createTank(Game target)
    {
        Quacker tmp = new Quacker(target);
        tmp.setSize(256, 256);
        tmp.type = EnemyType.TANK;
        tmp.sprite.setBounds(0, 0, 256, 256);
        tmp.critBox.setBounds(72, 160, 184, 72);
        tmp.bodyBox.setBounds(0, 32, 256, 128);
        tmp.healthPoints = 20;
        tmp.speedPerTick = 1;

        if (tmp.reverse)
        {
            tmp.frame1 = Constants.TANK_SPRITE1R;
            tmp.frame2 = Constants.TANK_SPRITE2R;
        }
        else
        {
            tmp.frame1 = Constants.TANK_SPRITE1;
            tmp.frame2 = Constants.TANK_SPRITE2;
        }
        
        tmp.sprite.setIcon(tmp.frame1);
        tmp.add(tmp.sprite);
        tmp.add(tmp.critBox);
        tmp.add(tmp.bodyBox);

        return tmp;
    }

    public static Quacker createCivilian(Game target)
    {
        Quacker tmp = new Quacker(target);
        tmp.setSize(128, 128);
        tmp.type = EnemyType.CIVILIAN;
        tmp.sprite.setBounds(0, 0, 128, 128);
        tmp.bodyBox.setBounds(48, 29, 80, 80);
        tmp.critBox.setBounds(0, 45, 48, 48);
        tmp.healthPoints = 4;
        tmp.speedPerTick = 1;

        if (tmp.reverse)
        {
            tmp.frame1 = Constants.CIVILIAN_SPRITE1R;
            tmp.frame2 = Constants.CIVILIAN_SPRITE2R;
        }
        else
        {
            tmp.frame1 = Constants.CIVILIAN_SPRITE1;
            tmp.frame2 = Constants.CIVILIAN_SPRITE2;
        }
        
        tmp.sprite.setIcon(tmp.frame1);
        tmp.add(tmp.sprite);
        tmp.add(tmp.critBox);
        tmp.add(tmp.bodyBox);

        return tmp;
    }
}