package com.selfvault.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "secrets")
@NoArgsConstructor
public class SecretsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "encrypted_data", nullable = false)
    private String encryptedData;

    @Column(nullable = false)
    private String iv;
}
