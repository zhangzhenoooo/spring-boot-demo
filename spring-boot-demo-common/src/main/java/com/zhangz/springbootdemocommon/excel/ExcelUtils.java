package com.zhangz.springbootdemocommon.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.sun.istack.NotNull;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static void export(@NotNull Collection<T> collection, Class clazz, String exportPath) throws Exception {
        if (null == collection) {
            throw new Exception("excel export collect can not be null !");
        }
        ExcelWriter excelWriter = EasyExcel.write(exportPath, clazz).build();
        WriteSheet sheet = EasyExcel.writerSheet(0).head(clazz).build();
        excelWriter.write(collection, sheet);
        excelWriter.finish();
    }

    public static void export(Collection<T> collection, Class clazz, HttpServletResponse response, String fileName) throws Exception {
        try {
            if (null == collection) {
                throw new Exception("excel export collect can not be null !");
            }
            
            String name = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            // 文件名，编码格式
            name = new String(name.getBytes("UTF-8"), "ISO-8859-1");
            // 文件格式
            response.setHeader("Content-disposition", "attachment;filename=" + name + ".xlsx");
            // 使用EasyExcel工具写excel
            EasyExcel.write(response.getOutputStream(), clazz).sheet("0").doWrite(collection);

        } catch (Exception e) {
            throw new Exception("excel export error  ", e);
        }
    }

    public static List<Map<Integer, String>> readExcel(String abpath) {
        List<Map<Integer, String>> list = new ArrayList<>();
        EasyExcel.read(abpath, new PageReadListener<Map<Integer, String>>(list::addAll)).sheet().doRead();
        return list;
    }
}
