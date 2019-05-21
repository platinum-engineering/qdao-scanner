package io.qdao.scanner.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.qdao.scanner.utils.Utils;
import io.qdao.scanner.exchanges.CryptoCurrency;
import io.qdao.scanner.types.FiatCurrency;
import io.qdao.scanner.types.LoanStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LoanResponseDto implements Serializable {

    private Long uid;

    private String ethAddress;

    private BigDecimal tokensIssued;

    private LoanStatus loanStatus;

    private CryptoCurrency currency;

    private String ensureAddress;

    private BigDecimal ensureAmount;

    private BigDecimal ensureRate;

    private Double startIndex;

    private Double currentIndex;

    private Double liquidationIndex;

    private BigDecimal liquidationRate;

    private BigDecimal liquidationFee;

    private BigDecimal differentBeforeLiquidation;

    private FiatCurrency fiatCurrency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATA_ISO_8601_PATTERN)
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATA_ISO_8601_PATTERN)
    private Date updatedAt;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getEthAddress() {
        return ethAddress;
    }

    public void setEthAddress(String ethAddress) {
        this.ethAddress = ethAddress;
    }

    public BigDecimal getTokensIssued() {
        return tokensIssued;
    }

    public void setTokensIssued(BigDecimal tokensIssued) {
        this.tokensIssued = tokensIssued;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public CryptoCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(CryptoCurrency currency) {
        this.currency = currency;
    }

    public String getEnsureAddress() {
        return ensureAddress;
    }

    public void setEnsureAddress(String ensureAddress) {
        this.ensureAddress = ensureAddress;
    }

    public BigDecimal getEnsureAmount() {
        return ensureAmount;
    }

    public void setEnsureAmount(BigDecimal ensureAmount) {
        this.ensureAmount = ensureAmount;
    }

    public BigDecimal getEnsureRate() {
        return ensureRate;
    }

    public void setEnsureRate(BigDecimal ensureRate) {
        this.ensureRate = ensureRate;
    }

    public Double getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Double startIndex) {
        this.startIndex = startIndex;
    }

    public Double getLiquidationIndex() {
        return liquidationIndex;
    }

    public void setLiquidationIndex(Double liquidationIndex) {
        this.liquidationIndex = liquidationIndex;
    }

    public BigDecimal getLiquidationRate() {
        return liquidationRate;
    }

    public void setLiquidationRate(BigDecimal liquidationRate) {
        this.liquidationRate = liquidationRate;
    }

    public BigDecimal getLiquidationFee() {
        return liquidationFee;
    }

    public void setLiquidationFee(BigDecimal liquidationFee) {
        this.liquidationFee = liquidationFee;
    }

    public BigDecimal getDifferentBeforeLiquidation() {
        return differentBeforeLiquidation;
    }

    public void setDifferentBeforeLiquidation(BigDecimal differentBeforeLiquidation) {
        this.differentBeforeLiquidation = differentBeforeLiquidation;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(Double currentIndex) {
        this.currentIndex = currentIndex;
    }

    public FiatCurrency getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(FiatCurrency fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }
}
