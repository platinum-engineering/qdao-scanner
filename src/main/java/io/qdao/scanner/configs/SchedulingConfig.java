package io.qdao.scanner.configs;

import io.qdao.scanner.utils.Utils;
import io.qdao.scanner.components.SocketConnector;
import io.qdao.scanner.dto.StringContentDto;
import io.qdao.scanner.exchanges.ExchangeProvider;
import io.qdao.scanner.exchanges.RatesProvider;
import io.qdao.scanner.utils.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.util.HtmlUtils;

import java.util.Set;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    private static final long TAME_SCHEDULE_RATE = 1000L;

    private static final long TICKET_SCHEDULE_RATE = 5000L;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private SocketConnector connector;

    @Autowired
    private Set<ExchangeProvider> providers;

    @Autowired
    private RatesProvider ratesProvider;

    /**
     * Use this method for ping application connection, also for get information about
     */
    @Scheduled(fixedRate = TAME_SCHEDULE_RATE)
    public void reportCurrentTime() {
        final String currentTime = Utils.getCurrentFormattedTime();
        final StringContentDto dto = new StringContentDto(HtmlUtils.htmlEscape(currentTime));
        connector.notifyAll(Topics.TOPIC_TIME, dto);
    }

    /**
     * TODO: Replace to Web-socket connection (Or any exchange protocols), if exchange have web-socket documentation.
     */
    @Scheduled(fixedRate = TICKET_SCHEDULE_RATE)
    public void reportExchangesTicket() {
        taskExecutor.execute(() -> {
            for (ExchangeProvider provider : providers) {
                provider.refresh();
            }
        });
    }

    /**
     * TODO: Replace to Fiat currency exchange web-socket. (Or any exchange protocols)
     */
    @Scheduled(fixedRate = TICKET_SCHEDULE_RATE)
    public void reportCoinMarketCapTicket() {
        taskExecutor.execute(() -> {
            ratesProvider.update();
        });
    }
}
