package com.huawei.hicloud.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName MongoCriteriaTest
 * @Description TODO
 * @Author itw_yanjy
 * @Date 2019/3/30 21:32
 * @Version 1.0
 */
public class MongoCriteriaTest {

    private static final Logger logger = LoggerFactory.getLogger(MongoCriteriaTest.class);

    @Test
    public void test01() throws Exception {

        ClassPathResource resource = new ClassPathResource("MongoCri.txt");

        InputStream inputStream = resource.getInputStream();
        String content = IOUtils.toString(inputStream);
        logger.info("Content is: {}.", content);

        JSONObject criteriaJson = JSON.parseObject(content);

        Set<Map.Entry<String, Object>> entries = criteriaJson.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            logger.info("Entry key: {}, value: {}.", entry.getKey(), entry.getValue());
        }
    }


    public static Map<String, Object> mapHandler(Map<String, Object> mapCriteria) {
        Set<Map.Entry<String, Object>> entries = mapCriteria.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            Object value = entry.getValue();
            logger.info("Entry key: {}, value: {}.", key, value);
            if ("$and".equals(key)) {
                listHandler((List<Map<String, Object>>)value, "and");
            } else if ("$or".equals(key)) {
                listHandler((List<Map<String, Object>>)value, "or");
            } else if ("$in".equals(key)) {

            } else {

            }
        }

        return null;
    }

    public static Map<String, Object> listHandler(List<Map<String, Object>> listCriteria, String opt) {

        return null;
    }

}
