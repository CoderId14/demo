package com.example.demo;


import com.example.demo.Repository.UserRepo;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaTest {


    @Autowired
    private UserRepo userRepo;

    @Test
    @DisplayName("SAVE USER SUCCESS")
    public void saveUserSuccess(){
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().roleName("ROLE_USER").build());
        User user = User.builder()
                .username("user1")
                .password("user1")
                .email("user1@gmail.com")
                .isActive(true)
                .roles(roles)
                .build();
        userRepo.save(user);

        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }
}
