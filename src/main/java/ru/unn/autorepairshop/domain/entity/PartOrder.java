package ru.unn.autorepairshop.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.unn.autorepairshop.domain.enums.PartName;
import ru.unn.autorepairshop.domain.enums.PartOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "part_order")
public class PartOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_order_id")
    private Long id;

    @Column(name = "part_name")
    @Enumerated(EnumType.STRING)
    private PartName partName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PartOrderStatus status;

    @Column(name = "order_date")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime orderDate;

    @Column(name = "delivery_date")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deliveryDate;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "appointment_id",
            nullable = false,
            updatable = false
    )
    private Appointment appointment;

}
