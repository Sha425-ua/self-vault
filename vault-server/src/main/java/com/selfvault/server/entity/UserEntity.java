package com.selfvault.server.entity;

import com.selfvault.domain.model.RegisterRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "auth_hash", nullable = false)
    private String authHash;

    @Column(nullable = false)
    private String salt;

    public static UserEntity fromDto(RegisterRequestDto dto) {
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.username());
        entity.setAuthHash(dto.authHash());
        entity.setSalt(dto.salt());
        return entity;
    }
}