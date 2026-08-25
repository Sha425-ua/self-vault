package com.selfvault.server.repository;

import com.selfvault.server.entity.SecretEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecretRepository extends JpaRepository<SecretEntity, Long> {
    boolean existsByUsernameAndTitle(String username, String title);

    @Transactional
    void deleteByUsernameAndTitle(String username, String title);

    @Query("SELECT s.title FROM SecretEntity s WHERE s.username = :username")
    List<String> getTitlesByUsername(@Param("username") String username);

    @Query("SELECT s.encryptedData FROM SecretEntity s WHERE s.username = :username AND s.title = :title")
    Optional<String> getEncryptedData(@Param("username") String username, @Param("title") String title);
}
