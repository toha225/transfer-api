package com.transfer.transfer_api.service;

import com.transfer.transfer_api.entity.Account;
import com.transfer.transfer_api.exception.InsufficientFundsException;
import com.transfer.transfer_api.repository.jpa.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        logger.info("Начало перевода с {} на {} на сумму: {}", fromUserId, toUserId, amount);
        if (fromUserId.equals(toUserId)) {
            logger.error("Ошибка перевода: отправитель и получатель совпадают (fromUserId={}, toUserId={})", fromUserId,
                    toUserId);
            throw new IllegalArgumentException("Отправитель и получатель не могут совпадать");
        }
        Account sender = accountRepository.findByUserIdWithLock(fromUserId)
                .orElseThrow(() -> new EntityNotFoundException("Счет отправителя не найден"));
        Account receiver = accountRepository.findByUserIdWithLock(toUserId)
                .orElseThrow(() -> new EntityNotFoundException("Счет получателя не найден"));

        logger.debug("Найденные счета: баланс отправителя={} и баланс получателя={}", sender.getBalance(),
                receiver.getBalance());

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств");
        }
        logger.info("Списание {} со счета отправителя (userId={})", amount, fromUserId);
        sender.setBalance(sender.getBalance().subtract(amount));
        logger.info("Зачисление {} на счет получателя (userId={})", amount, toUserId);
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);
        logger.info("Операция перевода успешно завершена. Новый баланс отправителя={} и баланс получателя={}",
                sender.getBalance(), receiver.getBalance());
    }

}

