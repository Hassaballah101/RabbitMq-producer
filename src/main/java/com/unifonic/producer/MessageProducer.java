package com.unifonic.producer;

import com.savoirtech.logging.slf4j.json.LoggerFactory;
import com.savoirtech.logging.slf4j.json.logger.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Hassaballah created on 20-Mar-2024
 */
@Component
public class MessageProducer implements ApplicationRunner {

  private final static Logger logger = LoggerFactory.getLogger(MessageProducer.class);

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private RabbitMQConfig rabbitMQConfig;

  public void sendMessage() {

    logger.info().message("Sending to the Queue...")
        .log();

    int counter = 0;
    String messageBody;
    Message message;

    // Continuously send the SMS message to the smsQueue
    while (true) {
      messageBody = "Message " + counter;
      message = new Message(messageBody.getBytes());

      logger.info().message("Sending messages to the queue...")
          .field("messageBody", messageBody)
          .field("counter", counter)
          .log();

      try {
        rabbitTemplate.send("myqueue", message);
        counter++;

        Thread.sleep(500); // Wait for half a sec
      } catch (Exception exception) {
        logger.error().message("Exception happened while sending message to queue")
            .stack()
            .exception("exception", exception)
            .log();
        counter--;
      }
    }
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    sendMessage();
  }
}
