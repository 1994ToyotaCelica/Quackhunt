import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.sound.sampled.*;

public class Quack extends JFrame
{
    // Settings
    private boolean playMusic = true;
    private boolean playSounds = true;
    private FloatControl volumeControl;
    private float masterVolume = 0.8f;

    private CardLayout layout = new CardLayout();
    private JPanel contentPanel;
    /*
        Main content panel on the frame.
        Holds:
            "title" - Title card
            "main" - Main menu
            "board" - Leaderboard
            "settings" - Settings menu
            "diff" - Difficulty chooser
            "game" - The actual game (constructed AFTER choosing the difficulty)
            "over" - Panel shown after game over
    */
    private JPanel titleCard;
    private JPanel menuMain;
    private JPanel leaderboard;
        JList<Score> board;
        ArrayList<Score> scores;
    private JPanel menuSettings;
    private JPanel diffChooser;
    private JPanel gameOver;
    private Game game;

    private Clip jukebox = null;
    private AudioInputStream musicStream = null;

    public Quack(boolean audio)
    {
        playSounds = audio;
        playMusic = audio;
        titleCardSetup();
        menuMainSetup();
        leaderboardSetup();
        menuSettingsSetup();
        diffChooserSetup();
        contentPanelSetup();

        frameSetup();
        soundSetup();
        setContentPane(contentPanel);
    }

    public void start()
    {
        ActionListener contentChanger = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changePane("main");
                playSound(Constants.MENU_THEME, true);
            }
        };

        Timer timer = new Timer(2000, contentChanger);
        timer.setRepeats(false);
        timer.start();
        setVisible(true);
    }

    public void gameOver(Score score)
    {
        gameOverSetup(score);
        contentPanel.add("over", gameOver);
        changePane("over");
        jukebox.close();
        contentPanel.remove(game);
        contentPanel.validate();
        game = null;
    }

    private void contentPanelSetup()
    {
        contentPanel = new JPanel();
        contentPanel.setLayout(layout);
        contentPanel.add("title", titleCard);
        contentPanel.add("main", menuMain);
        contentPanel.add("board", leaderboard);
        contentPanel.add("settings", menuSettings);
        contentPanel.add("diff", diffChooser);
    }

    private void titleCardSetup()
    {
        titleCard = new JPanel();
        titleCard.setBackground(new Color(0x131b2b));
        titleCard.setLayout(new BorderLayout());

        JLabel label = new JLabel();
        label.setIcon(new ImageIcon("Quack Hunt/ass/meme.png"));
        label.setText("made with java swing");
        label.setFont(FontLoader.loadFont("Quack Hunt/ass/meme.otf").deriveFont(32f));
        label.setForeground(Color.WHITE);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setIconTextGap(32);
        label.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        label.setAlignmentY(JPanel.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        titleCard.add(label);
    }

    private void menuMainSetup()
    {
        menuMain = new JPanel();
        menuMain.setLayout(new BoxLayout(menuMain, BoxLayout.Y_AXIS));
        menuMain.setBackground(Constants.BG_COLOR);

        JLabel title = new JLabel("QUACK HUNT 2");
        title.setFont(Constants.TITLE_FONT);
        title.setForeground(Constants.FG_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setAlignmentY(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(128,0,0,0));
        title.setFocusable(false);
        
        JLabel subtitle1 = new JLabel(" REVENGENCE ");
        subtitle1.setFont(Constants.SUBTITLE_FONT);
        subtitle1.setForeground(Constants.AC_COLOR);
        subtitle1.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle1.setAlignmentY(Component.TOP_ALIGNMENT);
        subtitle1.setBorder(new EmptyBorder(-20, 0, 40, 0));
        subtitle1.setFocusable(false);

        MenuButton startButton = new MenuButton("Start");
        startButton.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            { changePane("diff"); }
        });

        MenuButton boardButton = new MenuButton("Leaderboard");
        boardButton.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            { changePane("board"); }
        });

        MenuButton settingsButton = new MenuButton("Settings");
        settingsButton.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            { changePane("settings"); }
        });

        MenuButton exitButton = new MenuButton("Exit");
        exitButton.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e)
            {
                exitButton.setText("Embrace cowardice?");
                exitButton.setFont(Constants.TEXT_FONT.deriveFont(56f));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e)
            {
                exitButton.setText("Exit");
                exitButton.setFont(Constants.TEXT_FONT);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            { jukebox.close(); dispose(); System.exit(0); }
        });
        
        menuMain.add(title);
        menuMain.add(subtitle1);
        menuMain.add(startButton);
        menuMain.add(boardButton);
        menuMain.add(settingsButton);
        menuMain.add(exitButton);
    }

    private void diffChooserSetup()
    {
        Font smallerFont = Constants.TEXT_FONT.deriveFont(36f);
        Dimension smallerSize = new Dimension(400, 80);

        diffChooser = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("Quack Hunt/ass/frostedMenu.png").getImage(), 0, 0, null);
            }
        };
        diffChooser.setLayout(new BoxLayout(diffChooser, BoxLayout.Y_AXIS));
        diffChooser.setBorder(new EmptyBorder(128, 128, 128, 128));

        JLabel label = new JLabel("Choose a difficulty:");
        label.setFont(smallerFont);
        label.setForeground(Constants.FG_COLOR);
        label.setBackground(Constants.AC_COLOR);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        label.setMaximumSize(smallerSize);
        label.setPreferredSize(smallerSize);
        label.setOpaque(true);

        MenuButton gameJournalistMode = new MenuButton("Game Journalist");
        gameJournalistMode.setFont(smallerFont);
        gameJournalistMode.setMaximumSize(smallerSize);
        gameJournalistMode.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                gameJournalistMode.setText("No. Just no.");
                gameJournalistMode.setEnabled(false);
            }
        });
        
        MenuButton babyMode = new MenuButton("Baby");
        babyMode.setFont(smallerFont);
        babyMode.setMaximumSize(smallerSize);
        babyMode.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                gameSetup(Difficulty.BABY);
                changePane("game");
                game.start();
            }
        });
        
        MenuButton soldierMode = new MenuButton("Soldier");
        soldierMode.setFont(smallerFont);
        soldierMode.setMaximumSize(smallerSize);
        soldierMode.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                gameSetup(Difficulty.SOLDIER);
                changePane("game");
                game.start();
            }
        });
        
        MenuButton cyberCommandoMode = new MenuButton("Cyber Commando");
        cyberCommandoMode.setFont(smallerFont);
        cyberCommandoMode.setMaximumSize(smallerSize);
        cyberCommandoMode.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                gameSetup(Difficulty.CYBER_COMMANDO);
                changePane("game");
                game.start();
            }
        });
        
        MenuButton duckHunterMode = new MenuButton("D.U.C.K. Hunter");
        duckHunterMode.setFont(smallerFont);
        duckHunterMode.setMaximumSize(smallerSize);
        duckHunterMode.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                gameSetup(Difficulty.DUCK_HUNTER);
                changePane("game");
                game.start();
            }
        });

        MenuButton returnButton = new MenuButton("Cancel");
        returnButton.setFont(smallerFont);
        returnButton.setMaximumSize(smallerSize);
        returnButton.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            { changePane("main"); }

            @Override
            public void mouseEntered(MouseEvent e)
            { returnButton.setText("(not recommended)"); }

            @Override
            public void mouseExited(MouseEvent e)
            { returnButton.setText("Cancel"); }
        });

        diffChooser.add(label);
        diffChooser.add(gameJournalistMode);
        diffChooser.add(babyMode);
        diffChooser.add(soldierMode);
        diffChooser.add(cyberCommandoMode);
        diffChooser.add(duckHunterMode);
        diffChooser.add(Box.createVerticalStrut(80));
        diffChooser.add(returnButton);
    }

    private void gameOverSetup(Score score)
    {
        Font smallerFont = Constants.TEXT_FONT.deriveFont(36f);
        Dimension smallerSize = new Dimension(400, 80);

        gameOver = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("Quack Hunt/ass/frostedGame.png").getImage(), 0, 0, null);
            }
        };
        gameOver.setLayout(new BoxLayout(gameOver, BoxLayout.Y_AXIS));
        gameOver.setBorder(new EmptyBorder(128, 128, 128, 128));

        JLabel label1 = new JLabel("Game Over");
        label1.setFont(smallerFont);
        label1.setForeground(Constants.FG_COLOR);
        label1.setBackground(Constants.AC_COLOR);
        label1.setVerticalAlignment(JLabel.CENTER);
        label1.setHorizontalAlignment(JLabel.CENTER);
        label1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label1.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        label1.setMaximumSize(smallerSize);
        label1.setPreferredSize(smallerSize);
        label1.setOpaque(true);

        JPanel scorePackage = new JPanel(null);
        scorePackage.setMaximumSize(new Dimension(400, 500));
        scorePackage.setPreferredSize(new Dimension(400, 500));
        scorePackage.setBackground(Constants.BG_SHADE_COLOR);
        scorePackage.setOpaque(true);

        JTextArea scoreDisplay = new JTextArea(score.toString());
        scoreDisplay.setFont(smallerFont.deriveFont(24f));
        scoreDisplay.setForeground(Constants.FG_COLOR);
        scoreDisplay.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        scoreDisplay.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        scoreDisplay.setBounds(20, 20, 360, 164);
        scoreDisplay.setOpaque(false);
        scoreDisplay.setEditable(false);
        scorePackage.add(scoreDisplay);

        JTextArea nameArea = new JTextArea("Player Name");
        nameArea.setFont(smallerFont);
        nameArea.setForeground(Constants.BG_SHADE_COLOR);
        nameArea.setBackground(Constants.FG_COLOR);
        nameArea.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        nameArea.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        nameArea.setBounds(20, 184, 360, 36);
        scorePackage.add(nameArea);

        MenuButton addScore = new MenuButton("Add to Leaderboard");
        addScore.setFont(smallerFont);
        addScore.setMaximumSize(smallerSize);
        addScore.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                score.setPlayerName(nameArea.getText());
                scores.add(score);
                saveAllThisShit();
                addScore.setText("Score saved!");
                addScore.setEnabled(false);
            }
        });

        JLabel label2 = new JLabel("Try again?");
        label2.setFont(smallerFont);
        label2.setForeground(Constants.FG_COLOR);
        label2.setBackground(Constants.AC_COLOR);
        label2.setVerticalAlignment(JLabel.CENTER);
        label2.setHorizontalAlignment(JLabel.CENTER);
        label2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label2.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        label2.setMaximumSize(smallerSize);
        label2.setPreferredSize(smallerSize);
        label2.setOpaque(true);

        MenuButton replay = new MenuButton("Yes please");
        replay.setFont(smallerFont);
        replay.setMaximumSize(smallerSize);
        replay.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                changePane("diff");
            }
        });

        MenuButton backToMenu = new MenuButton("Hell nah");
        backToMenu.setFont(smallerFont);
        backToMenu.setMaximumSize(smallerSize);
        backToMenu.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            { backToMenu.setText("I got rekt"); }

            @Override
            public void mouseExited(MouseEvent e)
            { backToMenu.setText("Hell nah"); }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                changePane("main");
            }
        });

        gameOver.add(label1);
        gameOver.add(scorePackage);
        gameOver.add(addScore);
        gameOver.add(Box.createVerticalStrut(80));
        gameOver.add(label2);
        gameOver.add(replay);
        gameOver.add(backToMenu);
    }

    private void gameSetup(Difficulty difficulty)
    {
        game = new Game(difficulty, this);
        contentPanel.add("game", game);
    }

    private void leaderboardSetup()
    {
        if (Constants.SCORES_SAVE.exists())
            loadAllThisShit();
        else
            scores = new ArrayList<>();
        leaderboard = new JPanel();
        leaderboard.setBackground(Constants.BG_COLOR);
        leaderboard.setBorder(new EmptyBorder(128, 128, 128, 128));
        
        //board = new JList<Score>(Score.listConverterThatDoesNotReturnAnArrayOfObjectObjectsYesImLookingAtYouToArrayMethodYouStupidPieceOfCrap(scores));
        //board.setVisibleRowCount(10);

        // Hereby I lost the very last of my hope.
        JLabel shitpost = new JLabel(Constants.JLIST);

        MenuButton returnButton = new MenuButton("Return");
        returnButton.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            { changePane("main"); }
        });
        
        //leaderboard.add(board);
        leaderboard.add(shitpost);
        leaderboard.add(returnButton);
    }

    private void menuSettingsSetup()
    {
        menuSettings = new JPanel();
        menuSettings.setBorder(new EmptyBorder(128, 128, 0, 128));
        menuSettings.setBackground(Constants.BG_COLOR);

        JLabel masterLabel = new JLabel("Master volume");
        masterLabel.setFont(Constants.TEXT_FONT.deriveFont(36f));
        masterLabel.setForeground(Constants.FG_COLOR);
        masterLabel.setHorizontalAlignment(JLabel.CENTER);
        masterLabel.setVerticalAlignment(JLabel.BOTTOM);
        masterLabel.setPreferredSize(new Dimension(600, 64));

        JLabel volumeDisplay = new JLabel("80");
        volumeDisplay.setFont(Constants.FX_FONT);
        volumeDisplay.setForeground(Constants.AC_COLOR);
        volumeDisplay.setHorizontalAlignment(JLabel.CENTER);
        volumeDisplay.setVerticalAlignment(JLabel.TOP);
        volumeDisplay.setPreferredSize(new Dimension(128, 64));

        JSlider audioVolume = new JSlider();
        audioVolume.setBorder(new EmptyBorder(16, 0, 0, 0));
        audioVolume.setPreferredSize(new Dimension(400, 64));
        audioVolume.setFont(Constants.TEXT_FONT.deriveFont(24f));
        audioVolume.setForeground(Constants.FG_COLOR);
        audioVolume.setBackground(Constants.BG_COLOR);
        audioVolume.setMajorTickSpacing(10);
        audioVolume.setMinorTickSpacing(5);
        audioVolume.setLabelTable(audioVolume.createStandardLabels(50));
        audioVolume.setPaintTicks(true);
        audioVolume.setSnapToTicks(true);
        audioVolume.setPaintLabels(true);
        audioVolume.setValue(80);
        audioVolume.addChangeListener(new ChangeListener()
        {
            @Override public void stateChanged(ChangeEvent e)
            {
                int value = audioVolume.getValue();
                masterVolume = (float)value/100;
                volumeDisplay.setText((value<100?("0" + (value<10?"0":"")):"") + (value/5)*5);
                changeVolume(masterVolume);
            }
        });

        MenuButton soundButton = new MenuButton(playSounds?"Sound: YES":"Sound: NO");
        soundButton.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (playSounds)
                {
                    soundButton.setText("Sound: NO");
                    playSounds = false;
                }
                else
                {
                    soundButton.setText("Sound: YES");
                    playSounds = true;
                }
            }
        });

        MenuButton musicButton = new MenuButton(playMusic?"Music: YES":"Music: NO");
        musicButton.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (playMusic)
                {
                    playMusic = false;
                    musicButton.setText("Music: NO");
                    jukebox.close();
                }
                else
                {
                    playMusic = true;
                    musicButton.setText("Music: YES");
                    playSound(Constants.MENU_THEME, true);
                }
            }
        });

        MenuButton returnButton = new MenuButton("Return");
        returnButton.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            { changePane("main"); }
        });

        Component spacer = Box.createVerticalStrut(80);
        spacer.setPreferredSize(new Dimension(600, 190));

        menuSettings.add(masterLabel);
        menuSettings.add(audioVolume);
        menuSettings.add(volumeDisplay);
        menuSettings.add(soundButton);
        menuSettings.add(musicButton);
        menuSettings.add(spacer);
        menuSettings.add(returnButton);
    }

    private void frameSetup()
    {
        setAlwaysOnTop(true);
        setBackground(Color.BLACK);
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Quack Hunt 2");
        setIconImage(new ImageIcon("Quack Hunt/ass/icon.png").getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void soundSetup()
    {
        try
        {
            jukebox = AudioSystem.getClip();
        }
        catch (LineUnavailableException e)
        { System.out.println("ERR: Couldn't load audio!"); }
    }

    public void playSound(File fileToPlay, boolean loop)
    {
        if (playMusic)
        {
            try
            {
                musicStream = AudioSystem.getAudioInputStream(fileToPlay);
                if (jukebox.isOpen()) jukebox.close();
                jukebox.open(musicStream);
                changeVolume(masterVolume);
                if (loop) jukebox.loop(Clip.LOOP_CONTINUOUSLY);
            }
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
            { System.out.println("ERR: Couldn't load audio!"); }
        }
    }

    public void changeVolume(float volume)
    {
        if (volume > 1.0f || volume < 0.0f || !jukebox.isOpen()) return;
        volumeControl = (FloatControl)jukebox.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(20f * (float) Math.log10(volume));
    }

    public void changePane(String name)
    {
        layout.show(contentPanel, name);
    }

    private void saveAllThisShit()
    {
        try
        {
            FileOutputStream fallenCourier = new FileOutputStream(Constants.SCORES_SAVE);
            ObjectOutputStream fallenSidekick = new ObjectOutputStream(fallenCourier);
            fallenSidekick.writeObject(scores);
            fallenCourier.close();
            fallenSidekick.close();
        }
        catch (IOException e) {}
    }

    private void loadAllThisShit()
    {
        try
        {
            scores = null;
            FileInputStream risenCourier = new FileInputStream(Constants.SCORES_SAVE);
            ObjectInputStream risenSidekick = new ObjectInputStream(risenCourier);
            scores = (ArrayList<Score>) risenSidekick.readObject();
            risenCourier.close();
            risenSidekick.close();
        }
        catch (IOException | ClassNotFoundException e) {}
    }
}