package com.ivanfang.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@SpringBootTest
public class MyTest {

    @Test
    public void testPath() throws FileNotFoundException {
        String filePath = ResourceUtils.getURL("classpath:").getPath();
        System.out.println("method 1 = " + filePath);

        String absolutePath = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        System.out.println("method 2 = " + absolutePath);
    }

    @Test
    public void integerToString() {
        Integer i = 10;
        String str = i + "";
        System.out.println("str = " + str);
    }

    @Test
    public void parseTest() {
        String str = null;
        Integer i = Integer.parseInt(str);
        System.out.println("i = " + i);
    }

}
