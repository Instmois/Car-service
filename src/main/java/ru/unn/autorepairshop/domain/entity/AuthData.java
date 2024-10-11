package ru.unn.autorepairshop.domain.entity;

import ru.unn.autorepairshop.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "auth_data")
public class AuthData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_data_id")
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ToString.Exclude
    @OneToOne(orphanRemoval = true, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

}
