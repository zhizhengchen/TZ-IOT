package infoauto.util;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//存放下发给中机中联工单的队列
public class IotQueueUtil {
    private static BlockingQueue<JSONObject> queue = new LinkedBlockingQueue<>();
    private static BlockingQueue<JSONObject> queue2 = new LinkedBlockingQueue<>();

    //往队列放数据
    public static void addToQueue(JSONObject jsonData) {
        try {
            queue.put(jsonData);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("数据添加到队列时发生中断异常：" + e.getMessage());
        }
    }

    //从队列中获取数据
    public static JSONObject getFromQueue() {
        JSONObject data = null;
        try {
            data = queue.poll();
            System.out.println("从队列中获取到的数据：" + data);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            System.err.println("从队列中获取数据时发生中断异常：" + e.getMessage());
        }
        return data;
    }

    //往队列放数据
    public static void addToQueue2(JSONObject jsonData) {
        try {
            queue2.put(jsonData);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("数据添加到队列时发生中断异常：" + e.getMessage());
        }
    }

    //从队列中获取数据
    public static JSONObject getFromQueue2() {
        JSONObject data = null;
        try {
            data = queue2.take();
            System.out.println("从队列中获取到的数据：" + data);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("从队列中获取数据时发生中断异常：" + e.getMessage());
        }
        return data;
    }

}
