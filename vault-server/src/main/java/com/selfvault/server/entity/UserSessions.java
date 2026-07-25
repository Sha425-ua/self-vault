package com.selfvault.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_sessions")
@Setter
@Getter
@NoArgsConstructor
public class UserSessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "device_info", nullable = false)
    private String deviceInfo;

    @Column(name = "expire_at", nullable = false)
    private Long expireAt;
}
