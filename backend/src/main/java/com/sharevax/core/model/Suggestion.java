package com.sharevax.core.model;

import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@ToString
@Table(name = "suggestion")
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "supplier_approval_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus supplierStatus = ApprovalStatus.PENDING;

    @Column(name = "demander_approval_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus demanderStatus = ApprovalStatus.PENDING;

    @OneToOne
    private Supply supply;
    @OneToOne
    private Demand demand;

    @Column(name = "quantity", nullable = false)
    private BigInteger quantity;

    public enum ApprovalStatus {
        APPROVED,
        DENIED,
        PENDING
    }

}
