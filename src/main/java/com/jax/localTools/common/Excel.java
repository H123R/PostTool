package com.jax.localTools.common;

import com.jax.PostTool.core.util.ExcelUtils;
import com.jax.PostTool.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

@Slf4j
public class Excel {

    private static final String PLACEHOLDER = "{X}";

    /**
     * 批量将Excel表格数据转成SQL的、符合excel函数的拼接表达式
     */
    @Test
    public void parseConcatenateString() {
        String input = "INSERT INTO tableX(name) VALUES('{X}')";
        String res = parseConcatenateString(input, null, null, null);
        log.info(res);
    }

    private String parseConcatenateString(String input,
                                          String placeHolder,
                                          Integer count,
                                          Integer row) {
        // String temp = input.replace("\"", "\"\"");
        String temp = input;

        if (org.apache.commons.lang3.StringUtils.isBlank(placeHolder)) {
            placeHolder = PLACEHOLDER;
        }
        if (null == count) {
            count = StringUtils.countSubString(input, placeHolder);
        }
        if (null == row) {
            row = 2;
        }

        List<String> excelArray = ExcelUtils.getExcelArray(count, row);
        for (int i = 0; i < excelArray.size(); i++) {
            String excelArrayStr = concatWithSingleQuote("\"," + excelArray.get(i) + ",\"");
            String placeHolderQuote = concatWithSingleQuote(placeHolder);
            temp = StringUtils.replaceFirst(temp, placeHolderQuote, excelArrayStr);
        }

        return "=CONCATENATE(\"" + temp + "\")";
    }

    /**
     * 给字符串包一对单引号
     */
    private String concatWithSingleQuote(String str) {
        return "'" + str + "'";
    }

}
