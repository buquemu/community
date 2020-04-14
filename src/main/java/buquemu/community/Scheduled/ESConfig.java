package buquemu.community.Scheduled;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
@Component
@Configuration
public class ESConfig {
//    准备一个可以连接es的client
    @Bean
    public TransportClient initTransportClient() throws Exception {
//        setting 可以指定集群的配置，例如名称elasticsearch  配置默认值
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
//        socket 连接tcp ip编码复杂  大流套小流  配置一个连接的节点地址
        InetSocketTransportAddress address =
                new InetSocketTransportAddress(InetAddress.getByName("59.110.166.150"), 9300);
//        提供连接节点
        client.addTransportAddress(address);
        return client;
    }
}
