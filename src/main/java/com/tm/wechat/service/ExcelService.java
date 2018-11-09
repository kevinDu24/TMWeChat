package com.tm.wechat.service;

import com.tm.wechat.annotation.ExcelTitle;
import com.tm.wechat.dto.excel.ExcelDto;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 导出excel service
 * Created by qiaohao on 2017/3/7.
 */
@Service
public class ExcelService {


    public void exportList(String title, List dataset, OutputStream out) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
        XSSFWorkbook workbook=new XSSFWorkbook();
        XSSFSheet sheet=workbook.createSheet(title);
        sheet.setDefaultColumnWidth(15);
        //Field[] fields=dataset.get(0).getClass().getDeclaredFields();
        if(dataset.size() < 1){
            return;
        }
        Method[] methods= dataset.get(0).getClass().getDeclaredMethods();
        List<Method> getterMethods=new ArrayList<Method>();
        List<ExcelDto> excelDtos = new ArrayList<>();
        for(Method method:methods){
            if(method.getName().contains("get") && method.isAnnotationPresent(ExcelTitle.class)){
                getterMethods.add(method);
                ExcelDto excelDto = new ExcelDto();
                excelDto.setMethod(method);
                excelDto.setSort(method.getAnnotation(ExcelTitle.class).sort());
                excelDtos.add(excelDto);
            }
        }
        Collections.sort(excelDtos, new Comparator<ExcelDto>() {
            @Override
            public int compare(ExcelDto o1, ExcelDto o2) {
                if(o1.getSort() > o2.getSort())
                    return 1;
                else
                    return -1;
            }
        });

        int index = 0;
        XSSFRow row=sheet.createRow(index);
        for(int i=0;i<excelDtos.size();i++){
            XSSFCell cell=row.createCell(i);
            XSSFRichTextString text=new XSSFRichTextString(excelDtos.get(i).getMethod().getAnnotation(ExcelTitle.class).value());
            cell.setCellValue(text);
        }
        for(Object obj:dataset){
            index++;
            row=sheet.createRow(index);
            for(int i=0;i<excelDtos.size();i++){
                XSSFCell cellData =row.createCell(i);
                XSSFRichTextString textData = null;
                //String value=new String(getterMethods.get(i).invoke(obj, null) +"");
                String value=new String(excelDtos.get(i).getMethod().invoke(obj, null) +"");
                if(!value.equals("null")){
                    textData=new XSSFRichTextString(value);
                }else{
                    textData=new XSSFRichTextString(" ");
                }
                cellData.setCellValue(textData);
            }
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void exportList(String title, List dataset,Class clazz, OutputStream out) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
        XSSFWorkbook workbook=new XSSFWorkbook();
        XSSFSheet sheet=workbook.createSheet(title);
        sheet.setDefaultColumnWidth(15);
        //Field[] fields=dataset.get(0).getClass().getDeclaredFields();
        if(dataset.size() < 1){
            Method[] methods= clazz.getDeclaredMethods();
            List<ExcelDto> excelDtos = new ArrayList<>();
            for(Method method:methods){
                if(method.getName().contains("get") && method.isAnnotationPresent(ExcelTitle.class)){
                    ExcelDto excelDto = new ExcelDto();
                    excelDto.setMethod(method);
                    excelDto.setSort(method.getAnnotation(ExcelTitle.class).sort());
                    excelDtos.add(excelDto);
                }
            }
            Collections.sort(excelDtos, new Comparator<ExcelDto>() {
                @Override
                public int compare(ExcelDto o1, ExcelDto o2) {
                    if(o1.getSort() > o2.getSort())
                        return 1;
                    else
                        return -1;
                }
            });
            XSSFRow row=sheet.createRow(0);
            for(int i=0;i<excelDtos.size();i++){
                XSSFCell cell=row.createCell(i);
                XSSFRichTextString text=new XSSFRichTextString(excelDtos.get(i).getMethod().getAnnotation(ExcelTitle.class).value());
                cell.setCellValue(text);
            }
            try {
                workbook.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        Method[] methods= dataset.get(0).getClass().getDeclaredMethods();
        List<Method> getterMethods=new ArrayList<Method>();
        List<ExcelDto> excelDtos = new ArrayList<>();
        for(Method method:methods){
            if(method.getName().contains("get") && method.isAnnotationPresent(ExcelTitle.class)){
                getterMethods.add(method);
                ExcelDto excelDto = new ExcelDto();
                excelDto.setMethod(method);
                excelDto.setSort(method.getAnnotation(ExcelTitle.class).sort());
                excelDtos.add(excelDto);
            }
        }
        Collections.sort(excelDtos, new Comparator<ExcelDto>() {
            @Override
            public int compare(ExcelDto o1, ExcelDto o2) {
                if(o1.getSort() > o2.getSort())
                    return 1;
                else
                    return -1;
            }
        });

        int index = 0;
        XSSFRow row=sheet.createRow(index);
        for(int i=0;i<excelDtos.size();i++){
            XSSFCell cell=row.createCell(i);
            XSSFRichTextString text=new XSSFRichTextString(excelDtos.get(i).getMethod().getAnnotation(ExcelTitle.class).value());
            cell.setCellValue(text);
        }
        for(Object obj:dataset){
            index++;
            row=sheet.createRow(index);
            for(int i=0;i<excelDtos.size();i++){
                XSSFCell cellData =row.createCell(i);
                XSSFRichTextString textData = null;
                //String value=new String(getterMethods.get(i).invoke(obj, null) +"");
                String value=new String(excelDtos.get(i).getMethod().invoke(obj, null) +"");
                if(!value.equals("null")){
                    textData=new XSSFRichTextString(value);
                }else{
                    textData=new XSSFRichTextString(" ");
                }
                cellData.setCellValue(textData);
            }
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
