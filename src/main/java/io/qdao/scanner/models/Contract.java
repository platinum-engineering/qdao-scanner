package io.qdao.scanner.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.qdao.scanner.interceptors.ContractEntityListener;
import io.qdao.scanner.types.FiatCurrency;
import io.qdao.scanner.utils.Utils;
import io.qdao.scanner.web3j.NetworkType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contracts")
@EntityListeners(ContractEntityListener.class)
public class Contract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String address;

    @Column
    private String name;

    @Column
    private String symbol;

    @Column
    private BigDecimal totalSupply;

    @Column
    private Integer decimals;

    @Column(columnDefinition = "fiat_currency")
    @Type(type = "io.qdao.scanner.types.usertypes.FiatCurrencyType")
    private FiatCurrency fiatCurrency = FiatCurrency.USD;

    @Column(columnDefinition = "fiat_currency")
    @Type(type = "io.qdao.scanner.types.usertypes.NetworkTypeType")
    private NetworkType networkType = NetworkType.MAINNET;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATA_ISO_8601_PATTERN)
    private Date createdAt = new Date();

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATA_ISO_8601_PATTERN)
    private Date updatedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contract")
    @JsonIgnore
    private Set<TransfersLog> transfers = new HashSet<>();

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

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
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

    public Set<TransfersLog> getTransfers() {
        return transfers;
    }

    public void setTransfers(Set<TransfersLog> transfers) {
        this.transfers = transfers;
    }

    public FiatCurrency getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(FiatCurrency fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }
}
