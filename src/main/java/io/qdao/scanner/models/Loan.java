package io.qdao.scanner.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.qdao.scanner.utils.Utils;
import io.qdao.scanner.exchanges.CryptoCurrency;
import io.qdao.scanner.interceptors.LoanEntityListener;
import io.qdao.scanner.types.LoanStatus;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "loans")
@EntityListeners(LoanEntityListener.class)
public class Loan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(columnDefinition = "loan_status")
    @Type(type = "io.qdao.scanner.types.usertypes.LoanStatusType")
    private LoanStatus status;

    @Column(columnDefinition = "currency")
    @Type(type = "io.qdao.scanner.types.usertypes.CurrencyType")
    private CryptoCurrency collateralizedCurrency;

    @Column
    private String collateralizedAddress;

    @Column
    private BigDecimal collateralizedValue;

    @Column
    private BigDecimal collateralizedRate;

    @Column
    private Double collateralizedStartIndex;

    @Column
    private String ethAddress;

    @Column
    private BigDecimal tokenValue;

    @Column
    private Double liquidationIndex;

    @Column
    private BigDecimal liquidationRate;

    @Column
    private BigDecimal liquidationFee;

    @Column
    private BigDecimal differenceToCustomer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private TransfersLog transfersLog;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATA_ISO_8601_PATTERN)
    private Date createdAt = new Date();

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATA_ISO_8601_PATTERN)
    private Date updatedAt;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public CryptoCurrency getCollateralizedCurrency() {
        return collateralizedCurrency;
    }

    public void setCollateralizedCurrency(CryptoCurrency collateralizedCurrency) {
        this.collateralizedCurrency = collateralizedCurrency;
    }

    public String getCollateralizedAddress() {
        return collateralizedAddress;
    }

    public void setCollateralizedAddress(String collateralizedAddress) {
        this.collateralizedAddress = collateralizedAddress;
    }

    public BigDecimal getCollateralizedValue() {
        return collateralizedValue;
    }

    public void setCollateralizedValue(BigDecimal collateralizedValue) {
        this.collateralizedValue = collateralizedValue;
    }

    public BigDecimal getCollateralizedRate() {
        return collateralizedRate;
    }

    public void setCollateralizedRate(BigDecimal collateralizedRate) {
        this.collateralizedRate = collateralizedRate;
    }

    public Double getCollateralizedStartIndex() {
        return collateralizedStartIndex;
    }

    public void setCollateralizedStartIndex(Double collateralizedStartIndex) {
        this.collateralizedStartIndex = collateralizedStartIndex;
    }

    public String getEthAddress() {
        return ethAddress;
    }

    public void setEthAddress(String ethAddress) {
        this.ethAddress = ethAddress;
    }

    public BigDecimal getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(BigDecimal tokenValue) {
        this.tokenValue = tokenValue;
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

    public BigDecimal getDifferenceToCustomer() {
        return differenceToCustomer;
    }

    public void setDifferenceToCustomer(BigDecimal differenceToCustomer) {
        this.differenceToCustomer = differenceToCustomer;
    }

    public TransfersLog getTransfersLog() {
        return transfersLog;
    }

    public void setTransfersLog(TransfersLog transfersLog) {
        this.transfersLog = transfersLog;
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
}
