package com.transfer.transfer_api.initializer;

import com.transfer.transfer_api.entity.EmailData;
import com.transfer.transfer_api.entity.PhoneData;
import com.transfer.transfer_api.entity.User;
import com.transfer.transfer_api.entity.document.UserDocument;
import com.transfer.transfer_api.repository.jpa.EmailDataRepository;
import com.transfer.transfer_api.repository.jpa.PhoneDataRepository;
import com.transfer.transfer_api.repository.jpa.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ElasticSearchDataInitializer implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ElasticsearchOperations elasticsearchOperations;

    private final UserRepository userRepository;

    private final EmailDataRepository emailDataRepository;

    private final PhoneDataRepository phoneDataRepository;

    public ElasticSearchDataInitializer(ElasticsearchOperations elasticsearchOperations,
            UserRepository userRepository, EmailDataRepository emailDataRepository,
            PhoneDataRepository phoneDataRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.userRepository = userRepository;
        this.emailDataRepository = emailDataRepository;
        this.phoneDataRepository = phoneDataRepository;
    }

    @Override
    public void run(String... args) {
        IndexOperations indexOps = elasticsearchOperations.indexOps(UserDocument.class);
        if (!indexOps.exists()) {
            indexOps.create();
            indexOps.putMapping(indexOps.createMapping(UserDocument.class));
            logger.info("Индекс 'user_document' создан.");
        }

        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            logger.debug("Данные из БД не найдены, пропускаем инициализацию Elasticsearch.");
            return;
        }

        Map<Long, List<String>> emailMap = emailDataRepository.findAll().stream()
                .collect(Collectors.groupingBy(email -> email.getUser().getId(),
                        Collectors.mapping(EmailData::getEmail, Collectors.toList())
                ));

        Map<Long, List<String>> phoneMap = phoneDataRepository.findAll().stream()
                .collect(Collectors.groupingBy(hone -> hone.getUser().getId(),
                        Collectors.mapping(PhoneData::getPhone, Collectors.toList())
                ));

        List<UserDocument> documents = users.stream()
                .map(user -> convertToUserDocument(user,
                        emailMap.getOrDefault(user.getId(), Collections.emptyList()),
                        phoneMap.getOrDefault(user.getId(), Collections.emptyList())
                ))
                .collect(Collectors.toList());

        elasticsearchOperations.save(documents, IndexCoordinates.of("user_document"));
        logger.info("Данные из БД загружены в Elasticsearch.");

    }

    private UserDocument convertToUserDocument(User user, List<String> emails, List<String> phones) {
        UserDocument doc = new UserDocument();
        doc.setId(user.getId());
        doc.setName(user.getName());
        doc.setDateOfBirth(user.getDateOfBirth());
        doc.setPassword(user.getPassword());
        doc.setEmails(emails);
        doc.setPhones(phones);

        return doc;
    }

}
