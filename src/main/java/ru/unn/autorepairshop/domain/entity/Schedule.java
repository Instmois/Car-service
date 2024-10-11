package ru.unn.autorepairshop.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column()
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime scheduleDate;

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

    @OneToOne(
            mappedBy = "schedule",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    private Appointment appointment;

}
