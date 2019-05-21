package io.qdao.scanner.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ContractResponseDto implements Serializable {

    private Long uid;

    private String address;

    private String name;

    private String symbol;

    private BigDecimal totalSupply;

    private BigDecimal totalIssuedTokens;

    private BigDecimal totalCollateralized;

    private BigDecimal totalCollateralizedUsd;

    private Integer decimals;

    private Double collateralIndex;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(BigDecimal totalSupply) {
        this.totalSupply = totalSupply;
    }

    public BigDecimal getTotalCollateralized() {
        return totalCollateralized;
    }

    public void setTotalCollateralized(BigDecimal totalCollateralized) {
        this.totalCollateralized = totalCollateralized;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public Double getCollateralIndex() {
        return collateralIndex;
    }

    public void setCollateralIndex(Double collateralIndex) {
        this.collateralIndex = collateralIndex;
    }

    public BigDecimal getTotalIssuedTokens() {
        return totalIssuedTokens;
    }

    public void setTotalIssuedTokens(BigDecimal totalIssuedTokens) {
        this.totalIssuedTokens = totalIssuedTokens;
    }

    public BigDecimal getTotalCollateralizedUsd() {
        return totalCollateralizedUsd;
    }

    public void setTotalCollateralizedUsd(BigDecimal totalCollateralizedUsd) {
        this.totalCollateralizedUsd = totalCollateralizedUsd;
    }
}
