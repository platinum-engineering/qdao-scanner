package io.qdao.scanner.interceptors;

import io.qdao.scanner.components.SocketConnector;
import io.qdao.scanner.configs.RabbitmqConfiguration;
import io.qdao.scanner.dto.ContractResponseDto;
import io.qdao.scanner.dto.LoanResponseDto;
import io.qdao.scanner.dto.RatesResponseDto;
import io.qdao.scanner.exchanges.RatesProvider;
import io.qdao.scanner.services.ContractService;
import io.qdao.scanner.services.LoansService;
import io.qdao.scanner.types.FiatCurrency;
import io.qdao.scanner.utils.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RatesInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoansService loansService;

    @Autowired
    private SocketConnector connector;

    @Autowired
    private ContractService contractService;

    @Autowired
    private RatesProvider ratesProvider;

    @RabbitListener(queues = {RabbitmqConfiguration.RATES_QUEUE, RabbitmqConfiguration.RATES_QUEUE})
    void ratesChangeEvent(String json) {
        final RatesResponseDto dto = ratesProvider.fullDto();
        connector.notifyAll(Topics.TOPIC_RATE, dto);

        for (FiatCurrency fiatCurrency : FiatCurrency.values()) {
            notifyActualLoansInformation(fiatCurrency);
            notifyActualContractInformation(fiatCurrency);
        }
    }

    private void notifyActualLoansInformation(FiatCurrency fiatCurrency) {
        final List<LoanResponseDto> loans = loansService.getLoansByCurrency(fiatCurrency);
        for (LoanResponseDto loan : loans) {
            try {
                connector.notifyAll(Topics.loansTopic(fiatCurrency), loan);
            } catch (Throwable e) {
                logger.warn(e.getMessage());
            }
        }
    }

    private void notifyActualContractInformation(FiatCurrency fiatCurrency) {
        try {
            final ContractResponseDto response = contractService.getActualContract(fiatCurrency);
            connector.notifyAll(Topics.contractTopic(fiatCurrency), response);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }
}
