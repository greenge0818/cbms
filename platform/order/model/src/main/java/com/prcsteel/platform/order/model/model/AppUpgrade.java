package com.prcsteel.platform.order.model.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppUpgrade{

	boolean upgrade;// upgrade 是否升级，升级：true，不升级：false
	String name;// 应用名称
	int versionCode;// 版本号
	int versionCode2;//ios版本号
	String upgradeDesc;// 升级描述
	String path;// 下载地址
	boolean forcedUpgrade;// forcedUpgrade 是否强制升级，强制升级：true，不强制升级：false
	String size;// 应用大小

//	Logger logger = LoggerFactory.getLogger(this.getClass());
	public AppUpgrade() {
		Properties prop = new Properties();
		InputStream in = this.getClass().getResourceAsStream("/app-upgrade.properties");
		
		try {
			prop.load(in);
//			upgrade = prop.getProperty("upgrade");
			name = prop.getProperty("name");
			versionCode = Integer.parseInt(prop.getProperty("versionCode"));
			versionCode2 = Integer.parseInt(prop.getProperty("versionCode2"));
			upgradeDesc = prop.getProperty("upgradeDesc");
			path = prop.getProperty("path");
			forcedUpgrade = Boolean.parseBoolean(prop.getProperty("forcedUpgrade"));
			size = prop.getProperty("size");
		} catch (IOException e) {
//			logger.error("生成安卓更新信息出错:"+e.getMessage());
		}
	}
	public boolean isUpgrade() {
		return upgrade;
	}
	public void setUpgrade(boolean upgrade) {
		this.upgrade = upgrade;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getUpgradeDesc() {
		return upgradeDesc;
	}
	public void setUpgradeDesc(String upgradeDesc) {
		this.upgradeDesc = upgradeDesc;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isForcedUpgrade() {
		return forcedUpgrade;
	}
	public void setForcedUpgrade(boolean forcedUpgrade) {
		this.forcedUpgrade = forcedUpgrade;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getVersionCode2() {
		return versionCode2;
	}
	public void setVersionCode2(int versionCode2) {
		this.versionCode2 = versionCode2;
	}
	
}
