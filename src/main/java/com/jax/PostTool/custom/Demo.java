package com.jax.PostTool.custom;

import com.jax.PostTool.core.*;
import com.jax.PostTool.core.scheduling.Loop;
import org.junit.Test;

public class Demo {

    @Test
    public void get() {
        RequestTemplate.get("/service/get",
                        Headers.newHeader()
                                .header("X-Auth", "A-#{token}"),
                        Params.newParams()
                                .param("name", "jax"))
                .request();
    }

    @Test
    public void postJsonByObject() {
        RequestTemplate.post("/service/postJson",
                Headers.newHeader()
                        .header("X-Auth", "A-#{token}"),
                Body.newBody().addString("name", "jax")
                        .addObj("info",
                        new BodyObject()
                                .addInt("age", 20)
                                .addDate("birthday", "2024-10-01"))
                )
                .request();
    }

    @Test
    public void postJsonByJsonStr() {
        RequestTemplate.post("/service/postJson",
                Headers.newHeader()
                        .header("X-Auth", "A-#{token}"),
                Body.newBody("{\n" +
                        "  \"name\": \"jax\",\n" +
                        "  \"info\": {\n" +
                        "    \"age\": 20,\n" +
                        "    \"birthday\": \"2024-10-01T00:00:00+08:00\"\n" +
                        "  }\n" +
                        "}"))
                .request();
    }

    @Test
    public void postForm() {
        RequestTemplate.post("/service/postForm",
                Headers.newHeader()
                        .header("X-Auth", "A-#{token}"),
                Form.newForm()
                        .file("csvFile", "test.csv")
                        .form("user", "jax")
                ).request();
    }

    @Test
    public void download() {
        RequestTemplate.get("/service/download",
                Headers.newHeader()
                        .header("X-Auth", "A-#{token}"),
                Params.newParams()
                        .param("fileName", "testFile.txt")
        ).requestAndDownload("response.txt");
    }

    @Test
    public void loop() {
        Loop.loop(() -> get(), 100);
    }

}
