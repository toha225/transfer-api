package com.transfer.transfer_api.builder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.time.LocalDate;

public class UserQueryBuilder {

    public static Query createUserSearchQuery(String name, String email, String phone, LocalDate dateOfBirth) {
        return Query.of(q -> q.bool(b -> {
            if (name != null && !name.isEmpty()) {
                b.must(mu -> mu.prefix(p -> p.field("name").value(name)));
            }
            if (email != null && !email.isEmpty()) {
                b.must(mu -> mu.term(t -> t.field("emails").value(email)));
            }
            if (phone != null && !phone.isEmpty()) {
                b.must(mu -> mu.term(t -> t.field("phones").value(phone)));
            }
            if (dateOfBirth != null) {
                b.must(mu -> mu.range(r -> r.date(d -> d.field("dateOfBirth").gt(dateOfBirth.toString()))));
            }
            return b;
        }));
    }

}
