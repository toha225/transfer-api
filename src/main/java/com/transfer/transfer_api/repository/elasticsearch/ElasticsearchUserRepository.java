package com.transfer.transfer_api.repository.elasticsearch;

import com.transfer.transfer_api.entity.document.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ElasticsearchUserRepository extends ElasticsearchRepository<UserDocument, Long> {

    Optional<UserDocument> findByEmails(String email);

    Optional<UserDocument> findByPhones(String phone);

    boolean existsByEmails(String email);

    boolean existsByPhones(String phone);

    Optional<UserDocument> findByIdAndEmails(Long id, String email);

    Optional<UserDocument> findByIdAndPhones(Long id, String phone);

}
