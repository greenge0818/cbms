package com.prcsteel.platform.common.service;

import com.prcsteel.platform.common.model.FileInfo;

import java.io.InputStream;

/**
 * 
 * @author zhoukun
 *
 */
public interface FileService {

	/**
	 * 读取文件的基本信息
	 * @param fileId  文件标识，如果实现是基于文件系统的实现，其中可以带路径的（如果有）
	 * @return
	 */
	FileInfo getFileInfo(String fileId);
	
	/**
	 * 获取文件的绝对下载地址
	 * 这个地址可能会在一段时间后失效
	 * @param fileId 文件标识，如果实现是基于文件系统的实现，其中可以带路径的（如果有）
	 * @return 文件的下载地址
	 */
	String getFileAbsolutelyUrl(String fileId);
	
	/**
	 * 保存一个文件
	 * @param stream
	 * @param filePathAndName 文件存储的相对路径，路径分隔符为'/'
	 * @return 文件的唯一标识fileId，如果fileName中有路径，则返回的fileId也会有路
	 */
	String saveFile(InputStream stream, String filePathAndName);
	
	/**
	 * 保存一个文件
	 * @param stream
	 * @param filePathAndName 文件存储的相对路径，路径分隔符为'/'
	 * @param highQuality 是不是高质量保存图片
	 * @author tuxianming
	 * @date 20160623
	 * @return 文件的唯一标识fileId，如果fileName中有路径，则返回的fileId也会有路
	 */
	String saveFile(InputStream stream, String filePathAndName, boolean highQuality);
	
	/**
	 * 获取文件数据
	 * @param fileId 文件标识，如果实现是基于文件系统的实现，其中可以带路径的（如果有）
	 * @return
	 */
	InputStream getFileData(String fileId);
	
	/**
	 * 删除文件
	 * @param fileId 文件标识，如果实现是基于文件系统的实现，其中可以带路径的（如果有）
	 */
	void removeFile(String fileId);
}
