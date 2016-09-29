package com.prcsteel.platform.smartmatch.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;

/**
 * 资源模板工具类
 * 
 * @author peanut
 * @date 创建时间：2015年11月27日 下午3:38:33
 */
public class TempletUtils {
	private static Sheet sheet;
	private static Row row;
	/**
	 * 取得固定的模板列头
	 * @param  ins 模板文件路径
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static List<String> getTempletColumns(InputStream ins) throws FileNotFoundException, IOException{
		HSSFWorkbook wb=new HSSFWorkbook(ins);
		return getExcelTitleColumns(wb);
	}

	/**
	 * 读取Excel表格列头的内容
	 * 
	 * @param wb excel对象
	 * @return String 列头内容的数组
	 */
	public static List<String> getExcelTitleColumns(Workbook wb) {
		sheet = wb.getSheetAt(0); 
		row = sheet.getRow(0);
		if(row==null){
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "模板文件首行格式不对,请与管理员确认!");
		}
		// 总列数
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		List<String> title = new ArrayList<String>();
		for (int i = 0; i < colNum; i++) {
			title.add(getCellFormatValue(row.getCell((short) i)));
		}
		return title;
	}

	/**
	 * 读取Excel数据内容
	 * 
	 * @param wb  
	 * @return 包含数据的列表
	 */
	public static List<String> getExcelContent(Workbook wb) {
		List<String> content = new ArrayList<String>();
		
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		//总列数
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为列头的标题
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			if(row!=null){
				StringBuffer sb = new StringBuffer();
				for(int j=0;j<colNum;j++){
					String value=getCellFormatValue(row.getCell((short) j)).trim();
					if(StringUtils.isBlank(value)){
						//行数rowNum失真,判断其第一列数据为空时,数据行结束
						if(j==0){
							break;
						}else{
							value=" ";
						}
					}else{
						value=value.replaceAll("\\s*","");
					}
					// 每个单元格的数据内容用";;;"分割开,以便后面用";;;"分组,对数据进行匹配
					sb.append(value);
					sb.append(Constant.EXCEL_TEMPLET_FIEL_CONTENT_SEPARATOR);
				}
				content.add(sb.toString());
			}
		}
		return content;
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	private static String getCellFormatValue(Cell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
			case HSSFCell.CELL_TYPE_NUMERIC:
			case HSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式

					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);
				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					Double  o = cell.getNumericCellValue();
					String str = o.toString();
					boolean isNum = str.matches("[0-9]+");
					if(isNum){
						cellvalue=String.valueOf(o.intValue());
					}else{
						cellvalue=String.valueOf(o);
					}
				}
				break;
			}
			// 如果当前Cell的Type为STRING
			case HSSFCell.CELL_TYPE_STRING:
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString().trim();
				break;
			// 默认的Cell值
			default:
				cellvalue = "";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}
}
