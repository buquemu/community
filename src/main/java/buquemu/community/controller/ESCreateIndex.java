package buquemu.community.controller;

import buquemu.community.service.ESCreateIndexService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class ESCreateIndex {

    @Autowired
    private ESCreateIndexService cis;
    //请求创建索引

    @GetMapping("/createIndex")
    //    准备一个可以连接es的client
    public void createIndex() throws JsonProcessingException {
        cis.createIndex();
    }
}
