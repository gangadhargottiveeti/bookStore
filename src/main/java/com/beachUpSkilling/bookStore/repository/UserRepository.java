package com.beachUpSkilling.bookStore.repository;

import com.beachUpSkilling.bookStore.model.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, Long> {

    UserInfo findByUsername(String username);
    UserInfo findFirstById(Long id);

}
