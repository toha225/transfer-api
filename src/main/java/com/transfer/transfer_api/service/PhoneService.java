package com.transfer.transfer_api.service;

import com.transfer.transfer_api.entity.PhoneData;
import com.transfer.transfer_api.entity.User;
import com.transfer.transfer_api.entity.document.UserDocument;
import com.transfer.transfer_api.repository.elasticsearch.ElasticsearchUserRepository;
import com.transfer.transfer_api.repository.jpa.PhoneDataRepository;
import com.transfer.transfer_api.repository.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PhoneService {

    private final ElasticsearchUserRepository elasticsearchUserRepository;

    private final PhoneDataRepository phoneRepository;

    private final UserRepository userRepository;

    public PhoneService(ElasticsearchUserRepository elasticsearchUserRepository, PhoneDataRepository phoneRepository,
            UserRepository userRepository) {
        this.elasticsearchUserRepository = elasticsearchUserRepository;
        this.phoneRepository = phoneRepository;
        this.userRepository = userRepository;
    }

    public void updatePhone(Long userId, String oldPhone, String newPhone) {
        if (elasticsearchUserRepository.existsByPhones(newPhone)) {
            throw new RuntimeException("Телефон занят!");
        }

        PhoneData phoneData = phoneRepository.findByUserIdAndPhone(userId, oldPhone)
                .orElseThrow(() -> new RuntimeException("Телефон не найден"));
        phoneData.setPhone(newPhone);
        phoneRepository.save(phoneData);

        UserDocument userDocument = elasticsearchUserRepository.findByPhones(oldPhone)
                .orElseThrow(() -> new RuntimeException("Email не найден"));

        List<String> updatedPhones = userDocument.getPhones();
        updatedPhones.remove(oldPhone);
        updatedPhones.add(newPhone);
        elasticsearchUserRepository.save(userDocument);
    }

    public void addPhone(Long id, String newPhone) {
        if (elasticsearchUserRepository.existsByPhones(newPhone)) {
            throw new RuntimeException("Телефон занят!");
        }
        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(newPhone);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        phoneData.setUser(user);
        phoneRepository.save(phoneData);

        UserDocument userDocument = elasticsearchUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        List<String> phones = userDocument.getPhones();
        phones.add(newPhone);
        elasticsearchUserRepository.save(userDocument);
    }

    public void removePhone(Long id, String phone) {

        UserDocument userDocument = elasticsearchUserRepository.findByIdAndPhones(id, phone)
                .orElseThrow(() -> new RuntimeException("Телефон не найден"));
        List<String> phones = userDocument.getPhones();

        if (phones.size() < 2) {
            throw new RuntimeException("Удаление запрещено, не может быть меньше одного Телефон");
        }

        List<PhoneData> listPhone = phoneRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("Телефон не найден"));
        for (PhoneData phoneData : listPhone) {
            if (phoneData.getPhone().equals(phone)) {
                phoneRepository.delete(phoneData);
            }
        }

        phones.remove(phone);
        elasticsearchUserRepository.save(userDocument);
    }

}
