package io.qdao.scanner.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RabbitmqConfiguration {

    private static final boolean DEFAULT_QUEUE_DURABLE = false;

    public static final String RATES_QUEUE = "rates.queue";

    public static final String FIAT_QUEUE = "fiat.queue";

    public static final String RATES_FANOUT = "fanout.rates";

    public static final String FIAT_RATES_FANOUT = "fanout.fiats";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void consruct () {
        rabbitTemplate.setExchange(RATES_FANOUT);
    }

    @Bean
    Queue rateQueue() {
        return new Queue(RATES_QUEUE, DEFAULT_QUEUE_DURABLE);
    }

    @Bean
    Queue fiatsQueue() {
        return new Queue(FIAT_QUEUE, DEFAULT_QUEUE_DURABLE);
    }

    @Bean
    FanoutExchange ratesExchange() {
        return new FanoutExchange(RATES_FANOUT);
    }

    @Bean
    FanoutExchange fiatsExchange() {
        return new FanoutExchange(FIAT_RATES_FANOUT);
    }

    @Bean
    public Binding repositoryChange() {
        return BindingBuilder.bind(rateQueue()).to(ratesExchange());
    }

    @Bean
    public Binding fiatsChange() {
        return BindingBuilder.bind(fiatsQueue()).to(fiatsExchange());
    }
}
