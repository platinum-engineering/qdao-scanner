package io.qdao.scanner.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

public class ContractTotalDto implements Serializable {

    private String contactAddress;

    private String contractSymbol;

    private String contractName;

    private BigDecimal totalSupply;

    private BigDecimal totalTradingVolume;

    private BigInteger decimals;

    private BigInteger holders;

    private Set<CurrencyItemDto> collateralizedItems;

    private Set<CurrencyItemDto> currentRates;

    private Double totalCollateralizedUSD;

    private Double collateralIndexPercent;

    private BigInteger transactionsCount;

    private Date receivedAt = new Date();

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContractSymbol() {
        return contractSymbol;
    }

    public void setContractSymbol(String contractSymbol) {
        this.contractSymbol = contractSymbol;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public BigDecimal getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(BigDecimal totalSupply) {
        this.totalSupply = totalSupply;
    }

    public BigDecimal getTotalTradingVolume() {
        return totalTradingVolume;
    }

    public void setTotalTradingVolume(BigDecimal totalTradingVolume) {
        this.totalTradingVolume = totalTradingVolume;
    }

    public BigInteger getDecimals() {
        return decimals;
    }

    public void setDecimals(BigInteger decimals) {
        this.decimals = decimals;
    }

    public BigInteger getHolders() {
        return holders;
    }

    public void setHolders(BigInteger holders) {
        this.holders = holders;
    }

    public Set<CurrencyItemDto> getCollateralizedItems() {
        return collateralizedItems;
    }

    public void setCollateralizedItems(Set<CurrencyItemDto> collateralizedItems) {
        this.collateralizedItems = collateralizedItems;
    }

    public Double getTotalCollateralizedUSD() {
        return totalCollateralizedUSD;
    }

    public void setTotalCollateralizedUSD(Double totalCollateralizedUSD) {
        this.totalCollateralizedUSD = totalCollateralizedUSD;
    }

    public Double getCollateralIndexPercent() {
        return collateralIndexPercent;
    }

    public void setCollateralIndexPercent(Double collateralIndexPercent) {
        this.collateralIndexPercent = collateralIndexPercent;
    }

    public BigInteger getTransactionsCount() {
        return transactionsCount;
    }

    public void setTransactionsCount(BigInteger transactionsCount) {
        this.transactionsCount = transactionsCount;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Set<CurrencyItemDto> getCurrentRates() {
        return currentRates;
    }

    public void setCurrentRates(Set<CurrencyItemDto> currentRates) {
        this.currentRates = currentRates;
    }
}
