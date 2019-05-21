package io.qdao.scanner.controllers.v1;

import io.qdao.scanner.dto.ContractResponseDto;
import io.qdao.scanner.dto.LoanResponseDto;
import io.qdao.scanner.dto.RatesResponseDto;
import io.qdao.scanner.exchanges.RatesProvider;
import io.qdao.scanner.services.ContractService;
import io.qdao.scanner.services.LoansService;
import io.qdao.scanner.types.FiatCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RestApiCalculatorController {

    @Autowired
    private RatesProvider ratesProvider;

    @Autowired
    private LoansService loansService;

    @Autowired
    private ContractService contractService;

    @GetMapping("/rates")
    public RatesResponseDto getActualRates() {
        return ratesProvider.fullDto();
    }

    @GetMapping("/{currency}/contract")
    public ContractResponseDto getContractInfo(@PathVariable("currency") FiatCurrency fiatCurrency) throws Exception {
        return contractService.getActualContract(fiatCurrency);
    }

    @GetMapping("/{currency}/loans/actual")
    public List<LoanResponseDto> getCurrentLoans(@PathVariable("currency") FiatCurrency fiatCurrency) {
        return loansService.getActualLoansByCurrency(fiatCurrency);
    }

    @GetMapping("/{currency}/loans/all")
    public List<LoanResponseDto> getAllLoans(@PathVariable("currency") FiatCurrency fiatCurrency) {
        return loansService.getLoansByCurrency(fiatCurrency);
    }


}
