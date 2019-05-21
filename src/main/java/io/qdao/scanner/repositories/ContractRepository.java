package io.qdao.scanner.repositories;

import io.qdao.scanner.models.Contract;
import io.qdao.scanner.types.FiatCurrency;
import io.qdao.scanner.web3j.NetworkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    Optional<Contract> findByAddress(String address);

    Optional<Contract> findByAddressAndFiatCurrencyAndNetworkType(String address, FiatCurrency fiatCurrency, NetworkType networkType);

    default Contract getContract(String address) {
        return findByAddress(address)
                .orElse(makeContractByAddress(address, FiatCurrency.USD, NetworkType.MAINNET));
    }

    default Contract getContract(String address, FiatCurrency fiatCurrency, NetworkType networkType) {
        return findByAddressAndFiatCurrencyAndNetworkType(address, fiatCurrency, networkType)
                .orElse(makeContractByAddress(address, fiatCurrency, networkType));
    }

    default Contract makeContractByAddress(String address, FiatCurrency fiatCurrency, NetworkType networkType) {
        final Contract c = new Contract();
        c.setAddress(address);
        c.setFiatCurrency(fiatCurrency);
        c.setNetworkType(networkType);
        return c;
    }
}
