package com.xyz.influxdb2.springbootinfluxdb2;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.HealthCheck;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
class SpringBootInfluxdb2ApplicationTests {

    @Autowired
    InfluxDBClient influxDBClient;

    @Test
    void contextLoads() {
    }

    @Test
    void testConnect() {
        assertSame(influxDBClient.health().getStatus(), HealthCheck.StatusEnum.PASS);
    }

    @Test
    void testWrite() {
        Map<String, String> tagMap = new HashMap<>();
        tagMap.put("tag1", "aaa");
        Map<String, Object> fieldsMap = new HashMap<>();
        fieldsMap.put("field1", "aaa");
        Point point = Point.measurement("test")
                .addTags(tagMap)
                .addFields(fieldsMap);
        WriteApi writeApi = influxDBClient.getWriteApi();
        writeApi.writePoint("db-test", "yunnex", point);
        writeApi.flush();
        writeApi.close();
    }

    @Test
    void testQuery() {
        List<FluxTable> query = influxDBClient.getQueryApi().query("from(bucket: \"db-test\") |> range(start: -1h)");
        System.out.println(query.size());
    }

}
