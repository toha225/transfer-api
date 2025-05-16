package com.transfer.transfer_api.service;

import com.transfer.transfer_api.entity.EmailData;
import com.transfer.transfer_api.entity.User;
import com.transfer.transfer_api.entity.document.UserDocument;
import com.transfer.transfer_api.repository.elasticsearch.ElasticsearchUserRepository;
import com.transfer.transfer_api.repository.jpa.EmailDataRepository;
import com.transfer.transfer_api.repository.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmailService {

    private final ElasticsearchUserRepository elasticsearchUserRepository;

    private final EmailDataRepository emailRepository;

    private final UserRepository userRepository;

    public EmailService(ElasticsearchUserRepository elasticsearchUserRepository, EmailDataRepository emailRepository,
            UserRepository userRepository) {
        this.elasticsearchUserRepository = elasticsearchUserRepository;
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
    }

    public void updateEmail(Long userId, String oldEmail, String newEmail) {
        if (elasticsearchUserRepository.existsByEmails(newEmail)) {
            throw new RuntimeException("Email занят!");
        }

        EmailData emailData = emailRepository.findByUserIdAndEmail(userId, oldEmail)
                .orElseThrow(() -> new RuntimeException("Email не найден"));
        emailData.setEmail(newEmail);
        emailRepository.save(emailData);

        UserDocument userDocument = elasticsearchUserRepository.findByEmails(oldEmail)
                .orElseThrow(() -> new RuntimeException("Email не найден"));

        List<String> updatedEmails = userDocument.getEmails();
        updatedEmails.remove(oldEmail);
        updatedEmails.add(newEmail);
        elasticsearchUserRepository.save(userDocument);
    }

    public void addEmail(Long id, String newEmail) {
        if (elasticsearchUserRepository.existsByEmails(newEmail)) {
            throw new RuntimeException("Email занят!");
        }
        EmailData emailData = new EmailData();
        emailData.setEmail(newEmail);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        emailData.setUser(user);
        emailRepository.save(emailData);

        UserDocument userDocument = elasticsearchUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        List<String> updatedEmails = userDocument.getEmails();
        updatedEmails.add(newEmail);
        elasticsearchUserRepository.save(userDocument);
    }

    public void removeEmail(Long id, String email) {
        UserDocument userDocument = elasticsearchUserRepository.findByIdAndEmails(id, email)
                .orElseThrow(() -> new RuntimeException("Email не найден"));
        List<String> updatedEmails = userDocument.getEmails();

        if (updatedEmails.size() < 2) {
            throw new RuntimeException("Удаление запрещено, не может быть меньше одного Email");
        }

        List<EmailData> emails = emailRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("Email не найден"));
        for (EmailData emailData : emails) {
            if (emailData.getEmail().equals(email)) {
                emailRepository.delete(emailData);
            }
        }

        updatedEmails.remove(email);
        elasticsearchUserRepository.save(userDocument);
    }

}
