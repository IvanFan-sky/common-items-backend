package com.common.core.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Description Excel工具类，提供Excel导入导出功能
 * @Date 2025/1/7 18:30
 * @Author SparkFan
 */
@Slf4j
public class ExcelUtils {

    /**
     * 导出Excel到响应流
     *
     * @param response  HTTP响应
     * @param data      导出数据
     * @param clazz     数据类型
     * @param fileName  文件名
     */
    public static <T> void exportExcel(HttpServletResponse response, List<T> data, Class<T> clazz, String fileName) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 防止中文乱码
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
            
            EasyExcel.write(response.getOutputStream(), clazz)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("数据")
                    .doWrite(data);
                    
            log.info("Excel导出成功，文件名：{}，数据量：{}", fileName, data.size());
        } catch (IOException e) {
            log.error("Excel导出失败", e);
            throw new RuntimeException("Excel导出失败", e);
        }
    }

    /**
     * 从上传文件读取Excel数据
     *
     * @param file  上传文件
     * @param clazz 数据类型
     * @return 读取的数据列表
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> clazz) {
        return importExcel(file, clazz, 1, null);
    }

    /**
     * 从上传文件读取Excel数据
     *
     * @param file      上传文件
     * @param clazz     数据类型
     * @param headRowNumber 标题行号
     * @param listener  读取监听器
     * @return 读取的数据列表
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> clazz, Integer headRowNumber, ReadListener<T> listener) {
        try {
            if (listener == null) {
                listener = new DefaultExcelListener<>();
            }
            
            EasyExcel.read(file.getInputStream(), clazz, listener)
                    .headRowNumber(headRowNumber)
                    .sheet()
                    .doRead();
                    
            if (listener instanceof DefaultExcelListener) {
                List<T> data = ((DefaultExcelListener<T>) listener).getData();
                log.info("Excel导入成功，文件名：{}，数据量：{}", file.getOriginalFilename(), data.size());
                return data;
            }
            
            return ListUtils.newArrayList();
        } catch (IOException e) {
            log.error("Excel导入失败", e);
            throw new RuntimeException("Excel导入失败", e);
        }
    }

    /**
     * 验证Excel文件格式
     *
     * @param file 上传文件
     * @return 是否为Excel文件
     */
    public static boolean isExcelFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }
        
        return fileName.toLowerCase().endsWith(".xlsx") || fileName.toLowerCase().endsWith(".xls");
    }

    /**
     * 验证文件大小
     *
     * @param file    上传文件
     * @param maxSize 最大大小（字节）
     * @return 是否在允许范围内
     */
    public static boolean isValidFileSize(MultipartFile file, long maxSize) {
        return file != null && file.getSize() <= maxSize;
    }

    /**
     * 默认Excel读取监听器
     */
    @Slf4j
    public static class DefaultExcelListener<T> implements ReadListener<T> {
        
        /**
         * 每隔5条存储数据库，实际使用中可以100条，然后清理list，方便内存回收
         */
        private static final int BATCH_COUNT = 100;
        
        /**
         * 缓存的数据
         */
        private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        
        /**
         * 所有数据
         */
        private List<T> allData = ListUtils.newArrayList();

        /**
         * 这个每一条数据解析都会来调用
         */
        @Override
        public void invoke(T data, AnalysisContext context) {
            log.debug("解析到一条数据：{}", data);
            cachedDataList.add(data);
            allData.add(data);
            
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (cachedDataList.size() >= BATCH_COUNT) {
                saveData();
                // 存储完成清理 list
                cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            }
        }

        /**
         * 所有数据解析完成了都会来调用
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 这里也要保存数据，确保最后遗留的数据也存储到数据库
            saveData();
            log.info("所有数据解析完成！总数量：{}", allData.size());
        }

        /**
         * 加上存储数据库
         */
        private void saveData() {
            log.info("{}条数据，开始存储数据库！", cachedDataList.size());
            // 这里可以做数据库存储操作
            log.info("存储数据库成功！");
        }
        
        /**
         * 获取所有数据
         */
        public List<T> getData() {
            return allData;
        }
    }
} 