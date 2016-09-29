package com.prcsteel.platform.order.web.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.prcsteel.platform.common.utils.PageData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;


/**
 * 从EXCEL导入到数据库
 * Created by kongbinheng on 2015/8/4.
 */
public class ObjectExcelRead {

    private static Logger logger = LogManager.getLogger(ObjectExcelRead.class);

    /**
     * @param filepath 文件路径
     * @param filename 文件名
     * @param startrow 开始行号
     * @param startcol 开始列号
     * @param sheetnum sheet
     * @return list
     */
    public static List<Object> readExcel(String filepath, String filename, int startrow, int startcol, int sheetnum) {
        List<Object> varList = new ArrayList<Object>();
//        try {
//            File file = new File(filepath, filename);
//            FileInputStream fi = new FileInputStream(file);
//            varList = readExcelFile(fi, startrow, startcol, sheetnum);
//        } catch (Exception e) {
//            logger.error("读取excel出错：", e);
//        }
        return varList;
    }

    /**
     *
     * @param startrow
     * @param startcol
     * @param sheetnum
     * @return
     */
    public static List<Object> readExcel(MultipartFile mFile, int startrow, int startcol, int sheetnum) {
        return readExcelFile(mFile, startrow, startcol, sheetnum);
    }

    /**

     * @param startrow 开始行号
     * @param startcol 开始列号
     * @param sheetnum sheet
     * @return
     */
    private static List<Object> readExcelFile(MultipartFile mFile, int startrow, int startcol, int sheetnum){
        List<Object> varList = new ArrayList<Object>();
        //03,07兼容处理
        Workbook wb = null;
        try {

            wb = new HSSFWorkbook(mFile.getInputStream());
        } catch (Exception ex) {
            try {
                wb =new XSSFWorkbook(mFile.getInputStream());
            }catch (Exception exp){

            }
        }
        Sheet sheet = wb.getSheetAt(sheetnum);
        int rowNum = sheet.getLastRowNum() + 1;                    //取得最后一行的行号
        for (int i = startrow; i < rowNum; i++) {                    //行循环开始
            PageData varpd = new PageData();
            Row row = sheet.getRow(i);                            //行
            int cellNum = row.getLastCellNum();                    //每行的最后一个单元格位置
            for (int j = startcol; j < cellNum; j++) {                //列循环开始
                Cell cell = row.getCell(Short.parseShort(j + ""));
                String cellValue = null;
                if (null != cell) {
                    switch (cell.getCellType()) {                    // 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
                        case 0:
                            cellValue = String.valueOf((int) cell.getNumericCellValue());
                            break;
                        case 1:
                            cellValue = cell.getStringCellValue();
                            break;
                        case 2:
                            cellValue = cell.getNumericCellValue() + "";
                            // cellValue = String.valueOf(cell.getDateCellValue());
                            break;
                        case 3:
                            cellValue = "";
                            break;
                        case 4:
                            cellValue = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case 5:
                            cellValue = String.valueOf(cell.getErrorCellValue());
                            break;
                    }
                } else {
                    cellValue = "";
                }
                varpd.put("var" + j, cellValue);
            }
            varList.add(varpd);
        }
        return varList;
    }
}
