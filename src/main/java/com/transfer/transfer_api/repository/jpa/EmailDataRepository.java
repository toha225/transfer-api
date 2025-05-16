package com.transfer.transfer_api.repository.jpa;

import com.transfer.transfer_api.entity.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, Long> {

    Optional<EmailData> findByEmail(String email);

    Optional<EmailData> findByUserIdAndEmail(Long userId, String email);

    boolean existsByEmail(String email);

    Optional<List<EmailData>> findByUserId(Long id);

}
