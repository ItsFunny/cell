package com.cell.tool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-04 09:01
 */
public class BannerUtil
{
    public static void create(File srcImgFile, File destAsciiImgFile)
    {
        final String base = "@#&$%*o!;.";
        String result = "";
        try
        {
            BufferedImage bufferedImage = ImageIO.read(srcImgFile);
            for (int i = 0; i < bufferedImage.getHeight(); i += 32)
            {
                for (int j = 0; j < bufferedImage.getWidth(); j += 8)
                {
                    int pixel = bufferedImage.getRGB(j, i); // 下面三行代码将一个数字转换为RGB数字
                    int red = (pixel & 0xff0000) >> 16;
                    int green = (pixel & 0xff00) >> 8;
                    int blue = (pixel & 0xff);
                    float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
                    int index = Math.round(gray * (base.length() + 1) / 255);
                    result += index >= base.length() ? " " : String.valueOf(base.charAt(index));
                }
                result += "\r\n";
            }
            FileWriter fileWriter = new FileWriter(destAsciiImgFile);
            fileWriter.write(result);
            fileWriter.flush();
            fileWriter.close();
//            System.out.print(result);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void create(String srcImgFile, String destAsciiImgFile)
    {
        create(new File(srcImgFile), new File(destAsciiImgFile));
    }

    public static void main(String[] args)
    {
        create("/Users/joker/Java/cell/cell-node/cell-node-spring/src/main/java/com/cell/tool/banner.txt", "/Users/joker/Java/cell/cell-node/cell-node-spring/src/main/java/com/cell/tool/a.txt");
    }
}
