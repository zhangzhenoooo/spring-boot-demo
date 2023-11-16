package com.zhangz.springbootdemocloudalibabaorder;

import com.zhangz.springbootdemocloudalibabaorder.config.MQConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MQTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Test
    public void sentOrderNotion(){
        rabbitTemplate.convertAndSend(MQConfig.ORDER_EXCHANGE, MQConfig.ORDER_RUTEKEY,"订单【12312412345】已经下单啦");
    }
}
