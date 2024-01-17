package com.prox.passgpt.service.impl;

import com.prox.passgpt.model.Proxy;
import com.prox.passgpt.service.ProxyService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.util.List;

@Service
public class ProxyServiceImpl implements ProxyService {

    private List<Proxy> proxies;
    private int currentIndexProxy = 0;
    @PostConstruct
    public void init(){
        proxies = List.of(new Proxy("user49354:HDGVndA6wY:23.157.216.45:49354"));
    }
    @Override
    public Proxy getProxy(){
        if(proxies.isEmpty()) throw new RuntimeException("List proxy is null");
        if(currentIndexProxy < 0 || currentIndexProxy>= proxies.size()){
            currentIndexProxy = 0;
        }
        return proxies.get(currentIndexProxy++);
    }
    private ClientHttpConnector clientHttpConnector() {
        Proxy gProxy = getProxy();
        HttpClient httpClient = HttpClient.create()
                .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                        .host("23.157.216.44")
                        .port(39060)
                        .username(gProxy.getUsername())
                        .password(appName -> gProxy.getPassword())
                );
        return new ReactorClientHttpConnector(httpClient);
    }
}
