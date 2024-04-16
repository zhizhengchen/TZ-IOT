import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @description
 * @Author Administrator
 * @Date 2024/3/7 11:42
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        queue.put(1);
        queue.put(2);
        queue.put(3);
        queue.put(4);
        Integer poll = queue.poll();
        System.out.println(poll);
    }


}
