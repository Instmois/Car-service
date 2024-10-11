package ru.unn.autorepairshop.domain.entity;

import ru.unn.autorepairshop.domain.enums.PartName;
import ru.unn.autorepairshop.domain.enums.PartOrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "part_orders")
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

    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "service_id",
            nullable = false,
            updatable = false
    )
    private Service service;

}
