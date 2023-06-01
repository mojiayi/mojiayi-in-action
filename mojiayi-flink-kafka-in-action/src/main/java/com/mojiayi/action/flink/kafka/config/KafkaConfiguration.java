package com.mojiayi.action.flink.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author guangri.li
 * @since 2023/5/31 15:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfiguration {
    private String type;
    private String bootstrapServers;
    private String consumerSaslJaasConfig;
    private String consumerSaslMechanism;
    private String consumerSecurityProtocol;
    private String agentgioCstmTopics;
    private String agentgioCstmGroupId;
    private String agentgioCstmInsertSql;
    private String agentgioclickTopics;
    private String agentgioclickGroupId;
    private String agentgioclickInsertSql;
    private String agentgioactvTopics;
    private String agentgioactvGroupId;
    private String agentgioactvInsertSql;
}
