import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {

    private final String m_sDirectory;
    private final BufferedImage m_Image;
    private String m_sWritePath;
    private String m_sFileName;

    public ImageProcessor (String dir)
    {
        this.m_sDirectory = dir;
        this.m_Image = GetImage(m_sDirectory);
    }

    public BufferedImage GetImage(String path) {
        BufferedImage img = null;
        File f = null;

        try {
            f = new File(path);
            img = ImageIO.read(f);
            this.m_sWritePath = f.getParent();
            this.m_sFileName = f.getName().replaceFirst("[.][^.]+$", "");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    public void ProcessImage()
    {
        if( m_Image != null ) {
            Integer width = m_Image.getWidth();
            Integer height = m_Image.getHeight();

            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {

                    int p = m_Image.getRGB(x, y);

                    int a = ( p >> 24) & 0xff;
                    int r = ( p >> 16) & 0xff;
                    int g = ( p >> 8 ) & 0xff;
                    int b = p & 0xff;

                    int avg = (r + g + b) / 3;

                    p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                    m_Image.setRGB(x, y, p);
                }
            }
        }
    }

    public void WriteImage()
    {
        try {
            File f = new File(m_sWritePath + "\\" + m_sFileName + "_gray.jpg");
            ImageIO.write(m_Image, "jpg", f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
