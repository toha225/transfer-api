package com.transfer.transfer_api.repository.jpa;

import com.transfer.transfer_api.entity.EmailData;
import com.transfer.transfer_api.entity.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {

    Optional<PhoneData> findByPhone(String phone);
    // Поиск телефона у конкретного пользователя
    Optional<PhoneData> findByUserIdAndPhone(Long userId, String phone);

    // Проверка, существует ли уже такой телефон
    boolean existsByPhone(String phone);

    Optional<List<PhoneData>> findByUserId(Long id);
}
