package com.transfer.transfer_api.repository.jpa;

import com.transfer.transfer_api.entity.Account;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.user.id = :userId")
    Optional<Account> findByUserIdWithLock(@Param("userId") Long userId);

    // Для планировщика – обновление баланса
    @Query("SELECT update_account_balance()")
    void updateAccountBalance();
}
