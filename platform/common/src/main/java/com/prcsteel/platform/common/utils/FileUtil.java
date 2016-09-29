package com.prcsteel.platform.common.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**   
 * @Title: FileUtil.java 
 * @Package com.prcsteel.common.utils 
 * @Description: 文件工具类，主要调用apache common io FileUtils 
 * @author Green.Ge   
 * @date 2015年7月15日 上午10:50:10 
 * @version V1.0   
 */
public class FileUtil {
	private static final Logger logger = Logger.getLogger(FileUtil.class);

	public static void saveFile(MultipartFile file, String destinationPath){
		try {
			if(!file.isEmpty()){
				FileUtils.writeByteArrayToFile(new File(destinationPath), file.getBytes());
			}
		} catch (IOException e) {
			logger.debug("saveFile", e);
		}
	}

	public static void saveFile(File file, String destinationPath){
		try {
			if(file.length()>0){
				FileUtils.copyFile(file, new File(destinationPath));
			}
		} catch (IOException e) {
			logger.debug("saveFile", e);
		}
	}
	

	/**
	 * 
	 * @brief 获取文件名前缀
	 * @author Green.Ge
	 * @param [in] 文件名称+后缀
	 * @return 文件前缀 即文件名称
	 */
	public static String getFilePrefix(String fileName) {
		if (StringUtils.isEmpty(fileName))
			return null;
		// System.out.println(fileName.substring(0,fileName.lastIndexOf(".")));
		return fileName.substring(0, fileName.replaceAll("\\\\", "/")
				.lastIndexOf("."));
	}

	/**
	 * 
	 * @brief 获取文件名后缀
	 * @author Green.Ge
	 * @param [in] 文件名称+后缀
	 * @return 文件后缀
	 */
	public static String getFileSuffix(String fileName) {
		if (StringUtils.isEmpty(fileName))
			return null;
		return fileName.substring(fileName.replaceAll("\\\\", "/").lastIndexOf(
				".") + 1);
	}

	/**
	 * 
	 * @brief 获取文件的文件夹路径
	 * @author Green.Ge
	 * @param [in] 文件全路径
	 * @return 文件的文件夹路径
	 */
	public static String getFilePath(String filePath) {
		if (StringUtils.isEmpty(filePath))
			return null;
		// System.out.println(fileName.substring(0,fileName.lastIndexOf(".")));

		return filePath.substring(0, filePath.replaceAll("\\\\", "/")
				.lastIndexOf("/") + 1);
	}

	/**
	 * 
	 * @brief 根据文件全路径获取文件名
	 * @author Green.Ge
	 * @param [in] 文件全路径
	 * @return 文件名
	 */
	public static String getFileNameAll(String filePath) {
		if (StringUtils.isEmpty(filePath))
			return null;
		// System.out.println(fileName.substring(fileName.lastIndexOf(".")));
		return filePath.substring(filePath.replaceAll("\\\\", "/").lastIndexOf(
				"/") + 1);

	}

	public static String getFileName(String fileName) {
		if (StringUtils.isEmpty(fileName))
			return null;
		// System.out.println(fileName.substring(fileName.lastIndexOf(".")));
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	public static String getFileName0(String filePath) {
		return getFileName(getFileNameAll(filePath));
	}

}
 