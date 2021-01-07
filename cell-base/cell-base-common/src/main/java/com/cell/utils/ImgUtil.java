package com.cell.utils;

import com.idrsolutions.image.png.PngCompressor;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-05-18 17:01
 */
public class ImgUtil
{

    public static OutputStream reduceJpeg(FileInputStream inputStream) throws IOException
    {
//        int[] results = getImgWidthHeight(inputStream);
//        int widthdist = results[0];
//        int heightdist = results[1];
//        // 开始读取文件并进行压缩
//        BufferedImage src = null;
//        try
//        {
//            src = ImageIO.read(inputStream);
//        } catch (IOException e)
//        {
//            throw new IOException("imageIo读取文件失败:" + e.getMessage());
//        }
//        // 构造一个类型为预定义图像类型之一的 BufferedImage
//        BufferedImage tag = new BufferedImage(widthdist, heightdist, BufferedImage.TYPE_INT_RGB);
//        // 这边是压缩的模式设置
//        tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);
//        OutputStream out = new ByteArrayOutputStream();
//        //将图片按JPEG压缩，保存到out中
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        try
//        {
//            encoder.encode(tag);
//        } catch (IOException e)
//        {
//            throw new IOException("jpeg编码失败:" + e.getMessage());
//        }
//
//        return out;
        return null;
    }

    public static void compressPng(InputStream inputStream, OutputStream outputStream) throws IOException
    {
        PngCompressor.compress(inputStream, outputStream);
    }

    /**
     * 指定图片宽度和高度和压缩比例对图片进行压缩
     *
     * @param imgsrc  源图片地址
     * @param imgdist 目标图片地址
     */
//    public static void reduceImg(String imgsrc, String imgdist)
//    {
//        try
//        {
//            File srcfile = new File(imgsrc);
//            // 检查图片文件是否存在
//            if (!srcfile.exists())
//            {
//                System.out.println("文件不存在");
//            }
//            int[] results = getImgWidthHeight(srcfile);
//
//            int widthdist = results[0];
//            int heightdist = results[1];
//            // 开始读取文件并进行压缩
//            BufferedImage src = ImageIO.read(srcfile);
//
//            // 构造一个类型为预定义图像类型之一的 BufferedImage
//            BufferedImage tag = new BufferedImage(widthdist, heightdist, BufferedImage.TYPE_INT_RGB);
//
//            // 这边是压缩的模式设置
//            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);
//
//            //创建文件输出流
//            FileOutputStream out = new FileOutputStream(imgdist);
//            //将图片按JPEG压缩，保存到out中
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//            encoder.encode(tag);
//            //关闭文件输出流
//            out.close();
//        } catch (Exception ef)
//        {
//            ef.printStackTrace();
//        }
//    }

    /**
     * 获取图片宽度和高度
     *
     * @param file 图片路径
     * @return 返回图片的宽度
     */
    public static int[] getImgWidthHeight(File file)
    {
        InputStream is = null;
        BufferedImage src = null;
        int result[] = {0, 0};
        try
        {
            // 获得文件输入流
            is = new FileInputStream(file);
            // 从流里将图片写入缓冲图片区
            src = ImageIO.read(is);
            // 得到源图片宽
            result[0] = src.getWidth(null);
            // 得到源图片高
            result[1] = src.getHeight(null);
            is.close();  //关闭输入流
        } catch (Exception ef)
        {
            ef.printStackTrace();
        }

        return result;
    }

    public static int[] getImgWidthHeight(FileInputStream is)
    {
        BufferedImage src = null;
        int result[] = {0, 0};
        try
        {
            // 获得文件输入流
            // 从流里将图片写入缓冲图片区
            src = ImageIO.read(is);
            // 得到源图片宽
            result[0] = src.getWidth(null);
            // 得到源图片高
            result[1] = src.getHeight(null);
            is.close();  //关闭输入流
        } catch (Exception ef)
        {
            ef.printStackTrace();
        }

        return result;
    }
//
//    public static void main(String[] args)
//    {
//        String originFilePath = "/Users/joker/Desktop/a.jpg";
//        String orgiinOutPutPath = "/Users/joker/Desktop/b.jpg";
//        File srcfile = new File(originFilePath);
//        File distfile = new File(orgiinOutPutPath);
//
//        System.out.println("压缩前图片大小：" + srcfile.length());
//        reduceImg(originFilePath, orgiinOutPutPath);
//        System.out.println("压缩后图片大小：" + distfile.length());
//    }

    public static boolean compressPic(String srcFilePath, String descFilePath) throws IOException
    {
        File file = null;
        BufferedImage src = null;
        FileOutputStream out = null;
        // 指定写图片的方式为 jpg
        ImageWriter imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam imgWriteParams = new ImageWriteParam(null);
        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
        imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
        // 这里指定压缩的程度，参数qality是取值0~1范围内，
        imgWriteParams.setCompressionQuality((float) 1);
        imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
        ColorModel colorModel = ImageIO.read(new File(srcFilePath)).getColorModel();// ColorModel.getRGBdefault();

        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
                colorModel, colorModel.createCompatibleSampleModel(16, 16)));
        try
        {
            if (StringUtils.isEmpty(srcFilePath))
            {
                return false;
            } else
            {
                file = new File(srcFilePath);
                System.out.println(file.length());
                src = ImageIO.read(file);
                out = new FileOutputStream(descFilePath);
                imgWrier.reset();
                // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
                // OutputStream构造
                imgWrier.setOutput(ImageIO.createImageOutputStream(out));
                // 调用write方法，就可以向输入流写图片
                imgWrier.write(null, new IIOImage(src, null, null),
                        imgWriteParams);
                out.flush();
                out.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
