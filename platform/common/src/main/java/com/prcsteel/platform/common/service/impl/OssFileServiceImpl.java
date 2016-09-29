package com.prcsteel.platform.common.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.utils.ImageUtil;
import com.prcsteel.platform.common.model.FileInfo;
import com.prcsteel.platform.common.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * 
 * @author zhoukun
 */
public class OssFileServiceImpl implements FileService {

	private static final Logger logger = LoggerFactory.getLogger(OssFileServiceImpl.class);
	
	private static final String USER_META_FILE_NAME = "FileName";
	
	private static final String FILE_SEPARATOR = "/";
	
	@Value("${aliyun.oss.accessKey}")
	private String accessKeyId;
	
	@Value("${aliyun.oss.accessKeySecret}")
	private String accessKeySecret;
	
	@Value("${aliyun.oss.endPoint}")
	private String endPoint;
	
	@Value("${aliyun.oss.bucketName}")
	private String bucketName;
	
	@Value("${aliyun.oss.downloadUrlAvailableTimeInSecond}")
	private int downloadUrlAvailableTimeInSecond;

	@Value("${image.maxWidth}")
	private String imageMaxWidth;

	@Value("${image.maxHeight}")
	private String imageMaxHeight;
	
	private OSSClient client;
	
	@PostConstruct
	private void init(){
		client = new OSSClient(endPoint, accessKeyId, accessKeySecret);
	}
	
	@Override
	public FileInfo getFileInfo(String fileId) {
		FileInfo info = new FileInfo();
		info.setFileId(fileId);
		OSSObject obj = this.client.getObject(bucketName, fileId);
		ObjectMetadata meta = obj.getObjectMetadata();
		info.setFileSize(meta.getContentLength());
		info.setFileName(meta.getUserMetadata().get(USER_META_FILE_NAME));
		return info;
	}

	@Override
	public String getFileAbsolutelyUrl(String fileId) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.SECOND, downloadUrlAvailableTimeInSecond);
		URL url = client.generatePresignedUrl(bucketName, fileId, c.getTime());
		String urlStr = url.toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		logger.info("download url generated,expiration at:{},url: {}",sdf.format(c.getTime()),urlStr);
		return urlStr;
	}
	
	@Override
	public String saveFile(InputStream stream, String filePathAndName) {
		return saveFile(stream, filePathAndName, false);
	}
	
	@Override
	public String saveFile(InputStream stream, String filePathAndName, boolean highQuality) {
		String suffix = FileUtil.getFileSuffix(filePathAndName);
		// 处理图片
		if(ImageUtil.isImage(suffix)) {
			BufferedImage bufferedImage = ImageUtil.compress(stream, Integer.valueOf(imageMaxWidth), Integer.valueOf(imageMaxHeight), highQuality);
			stream = ImageUtil.convertBufferedImageToInputStream(bufferedImage, suffix);
		}

		String key = UUID.randomUUID().toString();
		try{
			if(filePathAndName.split(FILE_SEPARATOR).length > 1){
				key = filePathAndName.substring(0,filePathAndName.lastIndexOf(FILE_SEPARATOR))+ FILE_SEPARATOR + key;
			}
			key += "." + FileUtil.getFileSuffix(filePathAndName);
			// OSS文件不支持以/开头
			if(key.startsWith(FILE_SEPARATOR)){
				key = key.substring(1);
			}
			ObjectMetadata meta = new ObjectMetadata();
			meta.addUserMetadata(USER_META_FILE_NAME, filePathAndName);
			client.putObject(bucketName, key, stream, meta);
			logger.info("new file saved,bucket:{}, fileName:{}, fileKey: {}",bucketName,filePathAndName,key);
		}catch(Exception e){
			logger.error("save file to oss failed", e);
		}finally{
			try {
				stream.close();
			} catch (IOException e) {
				logger.error("can't close input stream,but file is upload to oss successfully",e);
			}
		}
		return key;
	}

	@Override
	public InputStream getFileData(String fileId) {
		OSSObject obj = client.getObject(bucketName, fileId);
		InputStream stream = obj.getObjectContent();
		logger.info("read data from oss,bucketName: {},fileid: {}",bucketName,fileId);
		return stream;
	}

	@Override
	public void removeFile(String fileId) {
		client.deleteObject(bucketName, fileId);
		logger.info("file deleted from oss,bucketName: {},fileId: {}",bucketName,fileId);
	}

}
