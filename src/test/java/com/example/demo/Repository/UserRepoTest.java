package com.example.demo.Repository;

import com.example.demo.CommonOperations;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import java.util.Optional;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserRepoTest {
    private static DbSetupTracker dbSetupTracker = new DbSetupTracker();
    @Autowired
    DataSource dataSource;
    @Autowired
    UserRepo userRepo;

    @BeforeEach
    void prepare(){
        final Operation insertRole =
                insertInto("tbl_role")
                        .row()
                            .column("id", 1)
                            .column("role_name", "ROLE_USER")
                        .end()


                        .build();

        final Operation insertUser =
                insertInto("tbl_user")
                        .row()
                            .column("id", 1)
                            .column("avatar", "")
                            .column("email", "test1@gmail.com")
                            .column("is_active", 1)
                            .column("name", "test1")
                            .column("password", "$2a$10$g2LNKcrRhx6aKDp1OELyVeITVsfYe5msrBT1jiVtDPaiAOhanDEuq")
                            .column("username", "test1")
                        .end()

                        .build();

        final Operation insertUserRole =
                insertInto("tbl_user_role")
                        .row()
                            .column("user_id", 1)
                            .column("role_id", 1)
                        .end().build();
        Operation operation = sequenceOf(
                CommonOperations.DELETE_ALL,
                insertRole,
                insertUser,
                insertUserRole
        );
        Destination dest = new DataSourceDestination(dataSource);
        DbSetup dbSetup = new DbSetup(dest, operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @DisplayName("Case 1: User had existed in database")
    void findByUsernameCase1() {
        assertEquals(userRepo.findByUsername("test1").get().getUsername(),"test1");
    }
    @Test
    @DisplayName("Case 2: Bad Request, User not exist in database")
    void findByUsernameCase2() {
       assertEquals(userRepo.findByUsername("test2"), Optional.empty());
    }

}