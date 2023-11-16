package com.zhangz.springbootdemocloudalibabaorder.mq;

import com.zhangz.springbootdemocloudalibabaorder.config.MQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**  接收 MQ消息服务类
 * @author 100451
 */
@Slf4j
@Component
public class MqReceiveService {

    /**
     * 接收订单下单后 发送用户通知的消息
     * 当队列不存在时自动创建并且自动绑定exchange
     * @param message 消息体
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(MQConfig.ORDER_QUEUE), exchange = @Exchange(MQConfig.ORDER_EXCHANGE), key = MQConfig.ORDER_RUTEKEY)})
    public void orderNotion(String message) {
        log.info("接收到消息体|{}", message);
    }
}
