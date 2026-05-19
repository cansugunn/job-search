package com.jobsearch.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  @Value("${job-search.rabbitmq.queue}")
  private String queueName;

  @Value("${job-search.rabbitmq.exchange}")
  private String exchangeName;

  @Value("${job-search.rabbitmq.routing-key}")
  private String routingKey;

  @Bean
  public Queue newJobPostingsQueue() {
    return QueueBuilder.durable(queueName).build();
  }

  @Bean
  public DirectExchange jobSearchExchange() {
    return new DirectExchange(exchangeName);
  }

  @Bean
  public Binding binding(Queue newJobPostingsQueue, DirectExchange jobSearchExchange) {
    return BindingBuilder.bind(newJobPostingsQueue).to(jobSearchExchange).with(routingKey);
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter());
    return template;
  }
}
