package com.selfvault.server.repository;

import com.selfvault.server.entity.SecretEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SecretRepository extends JpaRepository<SecretEntity, Long> {
    boolean existsByUsernameAndTitle(String username, String title);

    @Transactional
    void deleteByUsernameAndTitle(String username, String title);
}
