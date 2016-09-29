package com.prcsteel.platform.order.web.support;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.prcsteel.platform.common.utils.PageData;
import com.prcsteel.platform.common.utils.Tools;

/**
 * 导入到EXCEL
 * Created by kongbinheng on 2015/8/4.
 */
public class ObjectExcelView extends AbstractExcelView {

    @Override
    public void buildExcelDocument(Map<String, Object> model,
                                   HSSFWorkbook workbook, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        Date date = new Date();
        String filename = Tools.dateToStr(date, "yyyyMMddHHmmss");
        HSSFSheet sheet;
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
        Object data = model.get("data") != null ? model.get("data") : null;
        if (data != null) {
            List<Map<String, Object>> listSheet = (List<Map<String, Object>>) data;
            for (Integer i = 0; i < listSheet.size(); i++) {
                sheet = workbook.createSheet("sheet" + (i + 1));
                insertData(listSheet.get(i), workbook, sheet);
            }
        }
        else {
            sheet = workbook.createSheet("sheet1");
            insertData(model, workbook, sheet);
        }

    }

    private void insertData(Map<String, Object> model, HSSFWorkbook workbook, HSSFSheet sheet) {
        HSSFCell cell;
        List<String> titles = (List<String>) model.get("titles");
        List<String> merges = (List<String>) model.get("merges");
        int len = titles.size();
        HSSFCellStyle headerStyle = workbook.createCellStyle(); //标题样式
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFFont headerFont = workbook.createFont();    //标题字体
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setFontHeightInPoints((short) 11);
        headerStyle.setFont(headerFont);
        short width = 20, height = 25 * 20;
        sheet.setDefaultColumnWidth(width);
        for (int i = 0; i < len; i++) { //设置标题
            String title = titles.get(i);
            cell = getCell(sheet, 0, i);
            cell.setCellStyle(headerStyle);
            setText(cell, title);
        }
        sheet.getRow(0).setHeight(height);

        HSSFCellStyle contentStyle = workbook.createCellStyle(); //内容样式
        contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        List<PageData> varList = (List<PageData>) model.get("varList");
        
        int varCount = varList.size();
        for (int i = 0; i < varCount; i++) {
            PageData vpd = varList.get(i);
            for (int j = 0; j < len; j++) {
                String varstr = vpd.getString("var" + (j + 1)) != null ? vpd.getString("var" + (j + 1)) : "";
                cell = getCell(sheet, i + 1, j);
                cell.setCellStyle(contentStyle);
                
                setText(cell, varstr);
            }
        }
        
        //add by tuxianming 20160622
        //添加样式, 给指定的单元格设置样式
        List<ObjectExcelStyle> styles = (List<ObjectExcelStyle>) model.get("styles");
        if(styles!=null){
        	for (int i=0; i<styles.size(); i++) {
        		ObjectExcelStyle style = styles.get(i);
        		
        		cell = getCell(sheet, style.getY(),style.getX()-1);
        		HSSFCellStyle cellStyle = workbook.createCellStyle();
        		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        		
        		//设置字体颜色
        		if(ObjectExcelStyle.StyleType.FONT_COLOR.equals(style.getType())){
        			Font font=workbook.createFont();
        			font.setColor((short)style.getValue()); //HSSFColor.VIOLET.index 
        			cellStyle.setFont(font);
        		}
        		/*
        		 else if(ObjectExcelStyle.StyleType.OTHER)
        		 	// set other style ....
        		 */
        		
        		cell.setCellStyle(cellStyle);
			} 
        }
        
        //end tuxianming
        
        if(merges!=null){
            for (int i = 0; i < merges.size(); i++) { //合并单元格
                String[] merge = merges.get(i).split(",");
                if(merge.length==4){
                    sheet.addMergedRegion(new CellRangeAddress(Integer.parseInt(merge[0]),Integer.parseInt(merge[1]),Integer.parseInt(merge[2]),Integer.parseInt(merge[3])));
                }
            }
        }
        
    }
}
