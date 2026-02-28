package com.example.config;

import com.example.client.DishClient;
import com.example.client.OrderClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {
    @Bean
    public OrderClient orderClient(RestClient.Builder builder) {
        // 使用服务名 "Order-Service"，Spring Cloud LoadBalancer 会自动解析 IP
        return createHttpProxy(builder, "http://Order-Service", OrderClient.class);
    }

    // 2. 定义 DishClient 的 Bean
    @Bean
    public DishClient dishClient(RestClient.Builder builder) {
        // 使用服务名 "Resource-Service" (假设 DishClient 属于 Resource 服务)
        return createHttpProxy(builder, "http://Resource-Service", DishClient.class);
    }

    /**
     * 通用的代理对象创建工具方法
     */
    private <T> T createHttpProxy(RestClient.Builder builder, String baseUrl, Class<T> clientClass) {
        RestClient restClient = builder.baseUrl(baseUrl).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(clientClass);
    }
}
