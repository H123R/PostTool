package com.jax.PostTool.core.util;


import java.util.ArrayList;
import java.util.List;

/**
 * @author jax
 * @date 2021/10/9 23:26
 */
public class ExcelUtils {

    public static List<String> getExcelArray(int count, int rowNum) {
        if (count > 26) {
            throw new RuntimeException("暂不支持26个以上");
        }
        List<String> res = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            res.add("" + (char)(i + 'A') + rowNum);
        }
        return res;
    }

    public static List<List<String>> splitCsvDataFile(String input) {
        String lineSplitReg = "((\\n)(?=(\"[0-9]+\")))";
        String columnSplitReg = ",(?=[\",])";
        List<List<String>> resList = new ArrayList<>();
        List<String> lineList = StringUtils.splitString(input, lineSplitReg);

        for (String line : lineList) {
            List<String> columnList = StringUtils.splitString(line, columnSplitReg);
            List<String> res = new ArrayList<>();
            for (String column : columnList) {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(column)) {
                    int startIndex = 0;
                    int endIndex = column.length();
                    if (column.charAt(startIndex) == '"') {
                        // 如果第一个字符是双引号，需删除
                        startIndex ++;
                    }
                    if (column.charAt(endIndex - 1) == '"') {
                        endIndex --;
                    }
                    res.add(column.substring(startIndex, endIndex));
                } else {
                    res.add(column);
                }
            }
            resList.add(res);
        }
        return resList;
    }


}
