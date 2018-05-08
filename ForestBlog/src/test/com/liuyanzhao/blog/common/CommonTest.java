package com.liuyanzhao.blog.common;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：〈常规测试〉
 *
 * @author liyaohua
 * create on 2018/5/8
 * @version 1.0
 */
public class CommonTest {

    private static final Logger log = LoggerFactory.getLogger(CommonTest.class);

    @Test
    public void test1(){
        log.info("info");
        log.error("error");
        log.warn("1111");
    }
}
