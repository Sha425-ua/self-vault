package com.selfvault.server.repository;

import com.selfvault.server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);

    @Query("SELECT u.salt FROM UserEntity u WHERE u.username = :username")
    Optional<String> findSaltByUsername(@Param("username") String username);

    boolean existsByUsernameAndAuthHash(String username, String authHash);
}
