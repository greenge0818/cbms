package com.prcsteel.platform.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Excel帮助类
 * @author zhoukun
 */
@Service
public class ExcelUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	/**
	 * 生成Excel，返回Excel文件流
	 * @param titles 标题
	 * @param datas 数据
	 * @return
	 */
	public static InputStream generateExcel(String[] titles,List<Object[]> datas) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		
		// 设置表头
		HSSFRow row = sheet.createRow(0);
		CellStyle headStyle = workbook.createCellStyle(); 
		HSSFFont headerFont = workbook.createFont();    //标题字体
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setFontHeightInPoints((short) 11);
        headStyle.setFont(headerFont);
		for (int i = 0; i < titles.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(headStyle);
		}
		
		// 设置数据
		for (int i=0;i<datas.size();i++) {
			Object[] rowDatas = datas.get(i);
			HSSFRow dataRow = sheet.createRow(i+1);
			for (int j = 0; j < rowDatas.length; j++) {
				if(rowDatas[j] == null){
					continue;
				}
				dataRow.createCell(j).setCellValue(rowDatas[j].toString());
			}
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			workbook.write(bos);
			return new ByteArrayInputStream(bos.toByteArray());
		} catch (IOException e) {
			throw new BusinessException(Constant.EXCEPTIONCODE_SYSTEM, "Generate excel output stream failed.");
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				logger.error("Can't close excel output stream.",e);
			}
		}
	}
	
}
