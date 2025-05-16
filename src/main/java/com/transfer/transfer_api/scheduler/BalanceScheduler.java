package com.transfer.transfer_api.scheduler;

import com.transfer.transfer_api.repository.jpa.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BalanceScheduler {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AccountRepository accountRepository;

    public BalanceScheduler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Scheduled(fixedRate = 30000)
    public void updateAccountBalances() {
        try {
            accountRepository.updateAccountBalance();
            logger.trace("Баланс обновлен: {}", System.currentTimeMillis());
        } catch (Exception e) {
            logger.error("Ошибка при обновлении баланса: {}", e.getMessage());
        }
    }
}

