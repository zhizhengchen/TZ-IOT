package infoauto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

/**
 * 异步线程池
 * @author cs
 *
 */
/**
 * 异步线程池
 * @author cs
 *
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    private CallerRunsPolicy callerRunsPolicy = new CallerRunsPolicy(); // 线程池对拒绝任务(无线程可用)的处理策略

    private String threadNamePrefix = "Thread-";

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1); // 将核心线程数设置为1
        executor.setMaxPoolSize(1); // 将最大线程数设置为1
        executor.setQueueCapacity(999); // 将队列容量设置为1，使得新任务一直等待直到当前任务执行完成
        executor.setKeepAliveSeconds(100); // 由于只有一个线程，空闲时间设置为0
        executor.setRejectedExecutionHandler(callerRunsPolicy);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();
        return executor;
    }

}
