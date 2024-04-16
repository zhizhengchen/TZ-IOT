package infoauto.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {

   /* @Bean
    public CloseableHttpClient getHttpClient(){
        int timeout = 3000;
        CloseableHttpClient httpClient = HttpClients.createDefault();  //创建客户端请求
        RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
        return httpClient;
    }*/

}
