package ru.unn.autorepairshop.domain.entity;

import ru.unn.autorepairshop.domain.enums.ServiceStatus;
import ru.unn.autorepairshop.domain.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long id;

    @Column(name = "service_type")
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column(name = "service_status")
    @Enumerated(EnumType.STRING)
    private ServiceStatus serviceStatus;

    @OneToMany(
            mappedBy = "service",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<PartOrder> partOrders;

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
