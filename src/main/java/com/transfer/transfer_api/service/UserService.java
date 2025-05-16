package com.transfer.transfer_api.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.transfer.transfer_api.builder.UserQueryBuilder;
import com.transfer.transfer_api.dto.LoginRequest;
import com.transfer.transfer_api.dto.UserSearchRequest;
import com.transfer.transfer_api.entity.document.UserDocument;
import com.transfer.transfer_api.repository.elasticsearch.ElasticsearchUserRepository;
import com.transfer.transfer_api.security.CustomUserDetails;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService {

    private final ElasticsearchUserRepository elasticsearchUserRepository;

    private final ElasticsearchClient elasticsearchClient;

    public UserService(ElasticsearchUserRepository elasticsearchUserRepository, ElasticsearchClient elasticsearchClient) {
        this.elasticsearchUserRepository = elasticsearchUserRepository;
        this.elasticsearchClient = elasticsearchClient;
    }

//    нет необходимости кэшировать, так как это делается на уровне контроллера
//    @Cacheable()
    public SearchResponse<UserDocument> searchUsers(UserSearchRequest userSearchRequest) throws IOException {
        int from = userSearchRequest.getPage() * userSearchRequest.getSize();
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("user_document")
                .query(UserQueryBuilder.createUserSearchQuery(
                        userSearchRequest.getName(),
                        userSearchRequest.getEmail(),
                        userSearchRequest.getPhone(),
                        userSearchRequest.getDateOfBirth()))
                .from(from)
                .size(userSearchRequest.getSize())
                .build();

        return elasticsearchClient.search(searchRequest, UserDocument.class);
    }

    @Cacheable(value = "userByIdCache", key = "#id")
    public UserDetails loadUserById(Long id) {
        UserDocument user = elasticsearchUserRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден с id: " + id));
        return CustomUserDetails.create(user);
    }

    public Long authenticate(LoginRequest loginRequest) {
        String password = loginRequest.getPassword();
        if (password == null) {
            return null;
        }
        UserDocument user;
        String email = loginRequest.getEmail();
        if (email != null) {
            user = elasticsearchUserRepository.findByEmails(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден с email: " + email));
            if (user.getPassword().equals(password)) {
                return user.getId();
            }
        }

        String phone = loginRequest.getPhone();
        if (phone != null) {
            user = elasticsearchUserRepository.findByPhones(phone)
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден с phone: " + phone));
            if (user.getPassword().equals(password)) {
                return user.getId();
            }
        }
        return null;
    }

}
