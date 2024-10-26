package com.jax.localTools.common;

import org.junit.Test;

public class SQL {

    @Test
    public void parsePrepareSQL() {
        String prepareSQL = "SELECT * FROM tableX WHERE age = ? AND username = ? ";
        String paramsSQL = "20(Integer), jax(String)";

        String[] sqlSplit = prepareSQL.split("\\?");
        String[] paramsSplit = paramsSQL.split(",");

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paramsSplit.length; i++) {
            builder.append(sqlSplit[i]);
            builder.append("'");

            String params = paramsSplit[i];
            params = params.substring(0, params.lastIndexOf("("));
            builder.append(params.trim());

            builder.append("'");
        }
        if (paramsSplit.length < sqlSplit.length) {
            builder.append(sqlSplit[sqlSplit.length - 1]);
        }
        System.out.println(builder);

    }

}
