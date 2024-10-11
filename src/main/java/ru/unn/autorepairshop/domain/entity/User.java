package ru.unn.autorepairshop.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "created_at")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private AuthData authData;

    @OneToMany(
            mappedBy = "client",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Vehicle> vehicles;

    @OneToMany(
            mappedBy = "client",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Appointment> appointments;

}
