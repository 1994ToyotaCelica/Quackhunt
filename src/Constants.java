import java.awt.Color;
import java.awt.Font;
import java.io.File;

import javax.swing.ImageIcon;

class Constants
{
    static final Color FG_COLOR = new Color(0xfafafa);
    static final Color BG_COLOR = new Color(0x202020);
    static final Color BG_SHADE_COLOR = new Color(BG_COLOR.getRGB() - 0x020202);
    static final Color AC_COLOR = new Color(0xff0055);

    private static final Font FONT1 = FontLoader.loadFont("Quack Hunt/ass/font1.ttf");
    static final Font TITLE_FONT = FONT1.deriveFont(128f);

    private static final Font FONT2 = FontLoader.loadFont("Quack Hunt/ass/font2.otf");
    static final Font SUBTITLE_FONT = FONT2.deriveFont(72f);

    private static final Font FONT3 = FontLoader.loadFont("Quack Hunt/ass/font3.otf");
    static final Font TEXT_FONT = FONT3.deriveFont(64f);

    private static final Font FONT4 = FontLoader.loadFont("Quack Hunt/ass/font4.ttf");
    static final Font FX_FONT = FONT4.deriveFont(80f);
    
    static final File MENU_THEME = new File("Quack Hunt/ass/title.wav");
    static final File GAME_THEME = new File("Quack Hunt/ass/game.wav");

    static final ImageIcon GEN1_SPRITE1 = new ImageIcon("Quack Hunt/ass/mk1_1.png");
    static final ImageIcon GEN1_SPRITE2 = new ImageIcon("Quack Hunt/ass/mk1_2.png");

    static final ImageIcon GEN2_SPRITE1 = new ImageIcon("Quack Hunt/ass/mk2_1.png");
    static final ImageIcon GEN2_SPRITE2 = new ImageIcon("Quack Hunt/ass/mk2_2.png");

    static final ImageIcon STEALTH_SPRITE1 = new ImageIcon("Quack Hunt/ass/stealth_1.png");
    static final ImageIcon STEALTH_SPRITE2 = new ImageIcon("Quack Hunt/ass/stealth_2.png");

    static final ImageIcon TANK_SPRITE1 = new ImageIcon("Quack Hunt/ass/tank_1.png");
    static final ImageIcon TANK_SPRITE2 = new ImageIcon("Quack Hunt/ass/tank_2.png");

    static final ImageIcon CIVILIAN_SPRITE1 = new ImageIcon("Quack Hunt/ass/civilian_1.png");
    static final ImageIcon CIVILIAN_SPRITE2 = new ImageIcon("Quack Hunt/ass/civilian_2.png");

    static final ImageIcon GEN1_SPRITE1R = new MirroredImageIcon("Quack Hunt/ass/mk1_1.png");
    static final ImageIcon GEN1_SPRITE2R = new MirroredImageIcon("Quack Hunt/ass/mk1_2.png");

    static final ImageIcon GEN2_SPRITE1R = new MirroredImageIcon("Quack Hunt/ass/mk2_1.png");
    static final ImageIcon GEN2_SPRITE2R = new MirroredImageIcon("Quack Hunt/ass/mk2_2.png");

    static final ImageIcon STEALTH_SPRITE1R = new MirroredImageIcon("Quack Hunt/ass/stealth_1.png");
    static final ImageIcon STEALTH_SPRITE2R = new MirroredImageIcon("Quack Hunt/ass/stealth_2.png");

    static final ImageIcon TANK_SPRITE1R = new MirroredImageIcon("Quack Hunt/ass/tank_1.png");
    static final ImageIcon TANK_SPRITE2R = new MirroredImageIcon("Quack Hunt/ass/tank_2.png");

    static final ImageIcon CIVILIAN_SPRITE1R = new MirroredImageIcon("Quack Hunt/ass/civilian_1.png");
    static final ImageIcon CIVILIAN_SPRITE2R = new MirroredImageIcon("Quack Hunt/ass/civilian_2.png");

    static final ImageIcon BARRIER_SPRITE = new ImageIcon("Quack Hunt/ass/popup.png");
    static final ImageIcon UPGRADE_ICON = new ImageIcon("Quack Hunt/ass/upgrade.png");

    static final ImageIcon JLIST = new ImageIcon("Quack Hunt/ass/jlist.jpg");

    static final File SCORES_SAVE = new File("Quack Hunt/ser/scores.ser");
}