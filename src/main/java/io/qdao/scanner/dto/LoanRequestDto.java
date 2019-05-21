package io.qdao.scanner.dto;

import java.math.BigDecimal;

public class LoanRequestDto {

    private Long uid;

    private String transferHash;

    private String address;

    private String loanTxh;

    private BigDecimal value;

    private BigDecimal rate;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getTransferHash() {
        return transferHash;
    }

    public void setTransferHash(String transferHash) {
        this.transferHash = transferHash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getLoanTxh() {
        return loanTxh;
    }

    public void setLoanTxh(String loanTxh) {
        this.loanTxh = loanTxh;
    }
}
