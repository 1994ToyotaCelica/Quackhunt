import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class FontLoader
{
    public static Font loadFont(String pathname)
    {
        try
        {
            return Font.createFont(Font.TRUETYPE_FONT, new File(pathname));
        } catch (FontFormatException | IOException e) { System.out.println("ERR: Couldn't load " + pathname); }

        return null;
    }
}