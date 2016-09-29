package com.prcsteel.platform.common.utils;

import com.prcsteel.platform.common.constants.Constant;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Rolyer on 2015/10/20.
 */
public class ImageUtil {

    /**
     * 图片压缩
     *
     * @param inputStream 文件流
     * @param maxWidth    最大宽度
     * @param maxHeight   最大高度
     * @param highQuality 是否保持清晰度
     * @return
     */
    public static BufferedImage compress(InputStream inputStream, float maxWidth, float maxHeight, boolean highQuality) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            // 关闭流
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            
            int newWidth = image.getWidth();
            int newHeight = image.getHeight();
            if(!highQuality){
	            // 获得缩放的比例
	            double ratio = 1.0;
	            // 判断如果高、宽都不大于设定值，则不处理
	            if (image.getHeight() > maxHeight || image.getWidth() > maxWidth) {
	                double ratio1 = maxHeight / image.getHeight();
	                double ratio2 = maxWidth / image.getWidth();
	
	                ratio = ratio1 < ratio2 ? ratio1 : ratio2;
	            }
	            // 计算新的图面宽度和高度
	            newWidth = (int) (image.getWidth() * ratio);
	            newHeight = (int) (image.getHeight() * ratio);
            }
            BufferedImage bufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);

            return bufferedImage;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * 图片压缩
     *
     * @param inputStream 文件流
     * @param maxWidth    最大宽度
     * @param maxHeight   最大高度
     * @return
     */
    public static BufferedImage compress(InputStream inputStream, float maxWidth, float maxHeight) {
    	return compress(inputStream, maxWidth, maxHeight, false);
    }
    
    /**
     * 文件流转换
     *
     * @param bufferedImage 图片流
     * @param formatName    格式名：jpg,png,gif,bmp...，默认值jpg
     * @return
     */
    public static InputStream convertBufferedImageToInputStream(BufferedImage bufferedImage, String formatName) {
        if (formatName == null) {
            formatName = "jpg";
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, formatName, baos);
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 图片判断
     * @param suffix 扩展名
     * @return
     */
    public static boolean isImage(String suffix) {
        if (StringUtils.isBlank(suffix)) {
            return false;
        }

        if (Constant.IMAGE_SUFFIX.contains(suffix.toLowerCase())) {
            return true;
        }

        return false;
    }
}