package me.thebutlah.valuenoise;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ryan on 1/16/16.
 */
public class Main {
    public static void main(String[] args) {
        ValueNoise noise = new ValueNoise(1,null,10_00);
        BufferedImage img = new BufferedImage(10_00,10_00, BufferedImage.TYPE_BYTE_GRAY);
        for (int y=0;y<img.getHeight();y++) {
            for (int x=0;x<img.getWidth();x++) {
                int height = (int) Math.max(Math.min(128*(1+noise.generateValueNoise2D(x/25.0,y/25.0,2)), 255), 0);
                //System.out.println(height);
                int rgb = new Color(height, height, height).getRGB();
                img.setRGB(x,y,rgb);
            }
        }
        try {
            File output = new File("img.png");
            ImageIO.write(img, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(e.hashCode());
        }
    }
}
