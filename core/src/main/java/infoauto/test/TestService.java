package infoauto.test;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Created by InfoAuto.
 *
 * @author : zhuangweizhong
 * @create 2023/5/22 9:40
 */
@Service
@Slf4j
public class TestService {
    /**
     * 向指定ip分发内容
     */

    public void postcxzk(List<String> listip, JSONObject o) throws IOException {

        CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.createDefault();

        try{
           //遍历并向指定的ip发送请求
           listip.stream().forEach(ip->{
               httpAsyncClient.start();
               final HttpPost httpPost = new HttpPost(ip+"/getApplication2");
               httpPost.setHeader("Connection","close");
               StringEntity stringEntity = new StringEntity(o.toString(), "utf-8");
               //将参数设置到请求对象中
               httpPost.setEntity(stringEntity);
               //设置content-Type
               httpPost.setHeader("Content-Type","application/json");

               Future<HttpResponse> future = httpAsyncClient.execute(httpPost, null);
               HttpResponse response = null;
               try {
                   response = future.get(5000, TimeUnit.MILLISECONDS);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               } catch (ExecutionException e) {
                   e.printStackTrace();
                   System.out.println("连接超时");

               } catch (TimeoutException e) {
                   System.out.println(ip+"连接失败");
               }
           });
       }finally {
           httpAsyncClient.close();
       }

    }

}
