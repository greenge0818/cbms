package com.prcsteel.platform.common.service.impl;

import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.utils.ImageUtil;
import com.prcsteel.platform.common.model.FileInfo;
import com.prcsteel.platform.common.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

/**
 * 
 * @author zhoukun
 *
 */
public class LocalFileServiceImpl implements FileService {

	private static final Logger logger = LoggerFactory.getLogger(LocalFileServiceImpl.class);
	
	@Value("${prcsteel.file.local.savePath}")
	private String localFileSavePath;
	
	@Value("${prcsteel.file.local.fileDownloadPrefixUrl}")
	private String fileDownloadPrefixUrl;
	
	private static final String FILE_ID_SEPARATOR = "/";

	@Value("${image.maxWidth}")
	private String imageMaxWidth;

	@Value("${image.maxHeight}")
	private String imageMaxHeight;

	@Override
	public FileInfo getFileInfo(String fileId) {
		String[] names = fileId.split(FILE_ID_SEPARATOR);
		String fileAbsPath = combinePath(localFileSavePath, fileId);
		fileAbsPath = fileAbsPath.replace(FILE_ID_SEPARATOR, File.separator);
		File f = new File(fileAbsPath);
		FileInfo fi = new FileInfo();
		fi.setFileId(fileId);
		fi.setFileName(names[names.length-1]);
		fi.setFileSize(f.length());
		return fi;
	}

	@Override
	public String getFileAbsolutelyUrl(String fileId) {
		return combinePath(fileDownloadPrefixUrl,fileId);
	}

	@Override
	public String saveFile(InputStream stream, String filePathAndName) {
		return saveFile(stream, filePathAndName, false);
	}
	
	@Override
	public String saveFile(InputStream stream, String filePathAndName, boolean highQuality) {
		localFileSavePath = localFileSavePath.replace(FILE_ID_SEPARATOR, File.separator);
		filePathAndName = filePathAndName.replace(FILE_ID_SEPARATOR, File.separator);
		File f = new File(combinePath(localFileSavePath, filePathAndName));
		String suffix = FileUtil.getFileSuffix(filePathAndName);
		if(f.exists()){
			if(filePathAndName.contains(".")){
				filePathAndName = filePathAndName.substring(0,filePathAndName.lastIndexOf(".") + 1);
			}
			filePathAndName += UUID.randomUUID().toString() + "." + suffix;
			f = new File(combinePath(localFileSavePath, filePathAndName));
		}

		// 处理图片
		if(ImageUtil.isImage(suffix)) {
			BufferedImage bufferedImage = ImageUtil.compress(stream, Integer.valueOf(imageMaxWidth), Integer.valueOf(imageMaxHeight), highQuality);
			stream = ImageUtil.convertBufferedImageToInputStream(bufferedImage, suffix);
		}

		String fileAbsPath = combinePath(localFileSavePath, filePathAndName);
		FileOutputStream out = null;
		try {
			File dir = new File(f.getParent());
			if(!dir.exists()){
				dir.mkdirs();
			}
			f.createNewFile();
			out = new FileOutputStream(f);
			int count = 0;
			byte[] buffer = new byte[1024];
			do{
				count = stream.read(buffer, 0, buffer.length);
				if(count > 0){
					out.write(buffer,0,count);
				}
			}while(count > 0);
			logger.info("new file saved at: {}",fileAbsPath);
		} catch (IOException e) {
			logger.error("create local file failed: " + fileAbsPath,e);
			throw new RuntimeException(e.getMessage());
		}finally {
			try{
				if(out != null){
					out.close();
				}
				if(stream != null){
					stream.close();
				}
			}catch(Exception e){}
		}
		
		return filePathAndName;
	}

	@Override
	public InputStream getFileData(String fileId) {
		try {
			String fileAbsPath = combinePath(localFileSavePath, fileId);
			fileAbsPath = fileAbsPath.replace(FILE_ID_SEPARATOR, File.separator);
			FileInputStream inputStream = new FileInputStream(fileAbsPath);
			logger.info("get file data:{}",fileAbsPath);
			return inputStream;
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void removeFile(String fileId) {
		String fileAbsPath = combinePath(localFileSavePath, fileId);
		fileAbsPath = fileAbsPath.replace(FILE_ID_SEPARATOR, File.separator);
		File fi = new File(fileAbsPath);
		if(fi.exists()){
			fi.delete();
		}
		logger.info("file deleted: {}",fileAbsPath);
	}

	
	private String combinePath(String prefixPath,String fileId){
		if(fileId.startsWith("/")){
			fileId = fileId.substring(1);
		}
		if(prefixPath.endsWith("/")){
			prefixPath = prefixPath.substring(0, prefixPath.length()-1);
		}
		return prefixPath + File.separator + fileId;
	}

}
