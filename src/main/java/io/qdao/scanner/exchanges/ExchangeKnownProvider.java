package io.qdao.scanner.exchanges;

import com.google.gson.Gson;
import io.qdao.scanner.configs.RabbitmqConfiguration;
import io.qdao.scanner.dto.ExchangeRateDto;
import io.qdao.scanner.services.ExchangesService;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.logging.Logger;

abstract class ExchangeKnownProvider implements ExchangeProvider {

    protected final Logger logger = Logger.getLogger(this.getName());

    private final Exchange exchange;

    private Double price = null;

    private Double volume = null;

    @Autowired
    private ExchangesService exchangesService;

    @Autowired
    private Gson gson;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    <T extends Exchange> ExchangeKnownProvider(Class<T> exchangeClass) {
        exchange = ExchangeFactory.INSTANCE.createExchange(exchangeClass);

        final Thread thread = new Thread(this::refresh);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public Double getVolume() {
        return volume;
    }

    @Override
    public void disconnect() {
    }

    @Override
    public synchronized void refresh() {
        try {
            updateData(exchange);
        } catch (Throwable e) {
            logger.warning(e.getLocalizedMessage());
        }

        try {
            rabbitTemplate.setExchange(RabbitmqConfiguration.RATES_FANOUT);
            final ExchangeRateDto dto = exchangesService.exc2dto(getName(), getPrice(), getLOrder());
            final String message = gson.toJson(dto);
            rabbitTemplate.convertAndSend(message);
        } catch (Throwable e) {
            logger.warning(e.getLocalizedMessage());
        }
    }

    protected abstract void updateData(Exchange exchange) throws IOException ;

    protected void setPrice(Double price) {
        this.price = price;
    }

    protected void setVolume(Double volume) {
        this.volume = volume;
    }
}
