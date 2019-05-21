package io.qdao.scanner.models;

import com.fasterxml.jackson.annotation.*;
import io.qdao.scanner.utils.Utils;
import io.qdao.scanner.interceptors.TransfersLogEntityListener;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transfers_log")
@EntityListeners(TransfersLogEntityListener.class)
public class TransfersLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String addressFrom;

    @Column
    private String addressTo;

    @Column
    private BigDecimal value;

    @Column
    private String hash;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATA_ISO_8601_PATTERN)
    private Date createdAt = new Date();

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.DATA_ISO_8601_PATTERN)
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_uid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uid")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("contract_uid")
    private Contract contract;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "transfersLog", cascade = CascadeType.ALL)
    @JoinColumn(name = "loan_uid", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uid")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("loan_uid")
    private Loan loan;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
