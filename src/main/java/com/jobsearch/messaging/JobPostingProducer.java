package com.jobsearch.messaging;

import com.jobsearch.data.event.NewJobPostingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobPostingProducer {

  private final RabbitTemplate rabbitTemplate;

  @Value("${job-search.rabbitmq.exchange}")
  private String exchange;

  @Value("${job-search.rabbitmq.routing-key}")
  private String routingKey;

  public void publishNewJobPosting(NewJobPostingEvent newJobPostingEvent) {
    rabbitTemplate.convertAndSend(exchange, routingKey, newJobPostingEvent);
    log.info("Published new job posting to queue: jobId={}, title={}",
             newJobPostingEvent.jobId(),
             newJobPostingEvent.title());
  }
}
