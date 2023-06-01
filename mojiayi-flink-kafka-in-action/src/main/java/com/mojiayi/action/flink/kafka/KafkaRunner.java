package com.mojiayi.action.flink.kafka;

import com.mojiayi.action.flink.kafka.config.KafkaConfiguration;
import com.mojiayi.action.flink.kafka.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author mojiayi
 */
@Component
@Slf4j
public class KafkaRunner implements ApplicationRunner {
    @Autowired
    private KafkaConfiguration kafkaConfiguration;

    @Autowired
    @Qualifier("hiveDruidDataSource")
    private DataSource druidDataSource;

    @Override
    public void run(ApplicationArguments args) {
        if (log.isDebugEnabled()) {
            log.debug("kafka config={}", GsonUtil.toJson(kafkaConfiguration));
        }
        try {
            // 设置streaming运行环境
            final StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();

            // 设置kafka连接信息
            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getBootstrapServers());

            receiveAndSaveData(kafkaConfiguration.getAgentgioactvTopics(), kafkaConfiguration.getAgentgioactvInsertSql(), streamEnv, properties);
            receiveAndSaveData(kafkaConfiguration.getAgentgioCstmTopics(), kafkaConfiguration.getAgentgioCstmInsertSql(), streamEnv, properties);
            receiveAndSaveData(kafkaConfiguration.getAgentgioclickTopics(), kafkaConfiguration.getAgentgioclickInsertSql(), streamEnv, properties);

            // execute the streaming pipeline
            streamEnv.execute("FlinkKafkaStream");
        } catch (Exception e) {
            log.info("接收kafka消息出现异常", e);
        }
    }

    private void receiveAndSaveData(String topic, String insertSql, StreamExecutionEnvironment streamEnv, Properties properties) {
        // 在Flink设置Kafka消费者
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), properties);
        //Setup to receive only new messages
        kafkaConsumer.setStartFromLatest();

        //Create the data stream
        DataStream<String> dataStream = streamEnv.addSource(kafkaConsumer);


        //Convert each record to an Object
        dataStream.map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String rawMessage) {
                log.info("topic={},message={}", topic, rawMessage);
//                saveDataToHive(topic, rawMessage, insertSql);
                return new Tuple2<>(rawMessage, 0);
            }
        });
    }

    private void saveDataToHive(String topic, String rawMessage, String insertSql) {
        try {
            PreparedStatement preparedStatement = druidDataSource.getConnection().prepareStatement(insertSql);
        } catch (SQLException e) {
            log.info("保存数据到hive出现异常,topic={}", topic);
        }
    }
}
