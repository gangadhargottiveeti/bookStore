package com.beachUpSkilling.bookStore.repository;

import com.beachUpSkilling.bookStore.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql(scripts = {"classpath:InsertUserForTest.sql"})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnTestUserDataWhenFindByUserNameMethodIsCalled() {
        UserInfo user = userRepository.findByUsername("user");

        assertThat(user).isNotNull();
    }

    @Test
    void shouldReturnAllUsersWhenFindAllMethodIsCalled() {
        Iterable<UserInfo> allUsers = userRepository.findAll();
        long totalUsersCount = StreamSupport.stream(allUsers.spliterator(), false).count();

        assertEquals(1, totalUsersCount);
    }
}