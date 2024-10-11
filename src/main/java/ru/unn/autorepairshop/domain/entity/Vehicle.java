package ru.unn.autorepairshop.domain.entity;

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
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long id;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "model")
    private String model;

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


    //todo проверить, как будет работать при удалении
    @OneToMany(
            mappedBy = "vehicle",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Appointment> appointments;

}
