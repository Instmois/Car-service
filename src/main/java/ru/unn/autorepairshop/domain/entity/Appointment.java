package ru.unn.autorepairshop.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "appointment_date")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime appointmentDate;

    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "client_id",
            nullable = false,
            updatable = false
    )
    private User client;

    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "vehicle_id",
            nullable = false,
            updatable = false
    )
    private User vehicleId;

    @OneToMany(
            mappedBy = "appointment",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Service> services;

}
