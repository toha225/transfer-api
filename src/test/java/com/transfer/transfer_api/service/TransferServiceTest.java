package com.transfer.transfer_api.service;

import com.transfer.transfer_api.entity.Account;
import com.transfer.transfer_api.entity.User;
import com.transfer.transfer_api.exception.InsufficientFundsException;
import com.transfer.transfer_api.repository.jpa.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void transfer_successful() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("50.00");

        Account sender = new Account();
        User userFrom = new User();
        userFrom.setId(fromUserId);
        sender.setUser(userFrom);
        sender.setBalance(new BigDecimal("100.00"));

        Account receiver = new Account();
        User userTo = new User();
        userTo.setId(toUserId);
        receiver.setUser(userTo);
        receiver.setBalance(new BigDecimal("200.00"));

        when(accountRepository.findByUserIdWithLock(fromUserId)).thenReturn(Optional.of(sender));
        when(accountRepository.findByUserIdWithLock(toUserId)).thenReturn(Optional.of(receiver));

        transferService.transfer(fromUserId, toUserId, amount);

        assertEquals(new BigDecimal("50.00"), sender.getBalance());
        assertEquals(new BigDecimal("250.00"), receiver.getBalance());
        verify(accountRepository, times(1)).save(sender);
        verify(accountRepository, times(1)).save(receiver);
    }

    @Test
    public void transfer_insufficientFunds() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("150.00");

        Account sender = new Account();
        User userFrom = new User();
        userFrom.setId(fromUserId);
        sender.setUser(userFrom);
        sender.setBalance(new BigDecimal("100.00"));

        Account receiver = new Account();
        User userTo = new User();
        userTo.setId(toUserId);
        receiver.setUser(userTo);
        receiver.setBalance(new BigDecimal("200.00"));

        when(accountRepository.findByUserIdWithLock(fromUserId)).thenReturn(Optional.of(sender));
        when(accountRepository.findByUserIdWithLock(toUserId)).thenReturn(Optional.of(receiver));

        assertThrows(InsufficientFundsException.class, () ->
                transferService.transfer(fromUserId, toUserId, amount)
        );
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void transfer_whenFromAndToAccountsAreTheSame() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("50.00");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                transferService.transfer(userId, userId, amount)
        );
        assertEquals("Отправитель и получатель не могут совпадать", ex.getMessage());
    }

    @Test
    public void transfer_senderNotFound() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("50.00");

        when(accountRepository.findByUserIdWithLock(fromUserId)).thenReturn(Optional.empty());
        when(accountRepository.findByUserIdWithLock(toUserId)).thenReturn(Optional.of(new Account()));

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                transferService.transfer(fromUserId, toUserId, amount)
        );
        assertTrue(ex.getMessage().contains("Счет отправителя не найден"));
    }

    @Test
    public void transfer_receiverNotFound() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("50.00");

        when(accountRepository.findByUserIdWithLock(fromUserId)).thenReturn(Optional.of(new Account()));
        when(accountRepository.findByUserIdWithLock(toUserId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                transferService.transfer(fromUserId, toUserId, amount)
        );
        assertTrue(ex.getMessage().contains("Счет получателя не найден"));
    }
}
