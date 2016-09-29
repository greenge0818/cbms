package com.prcsteel.platform.common.model;

import java.io.Serializable;

/**
 * 文件基本信息
 * @author zhoukun
 *
 */
public class FileInfo implements Serializable {

	private static final long serialVersionUID = 4813650127203443484L;

	String fileName;
	
	String fileId;
	
	long fileSize;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
