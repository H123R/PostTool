package com.jax.PostTool.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class FormItem {

    public enum FormType {
        FILE,
        STRING,
        ;
    }

    @Getter
    private FormType formType;

    @Getter
    @Setter
    private String value;

}
