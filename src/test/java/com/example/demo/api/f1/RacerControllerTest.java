package com.example.demo.api.f1;

import ch.qos.logback.classic.pattern.DateConverter;
import com.example.demo.CommonOperations;
import com.example.demo.dto.f1.RacerDto;
import com.example.demo.dto.request.SignUpDto;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Date;

import static com.example.demo.CommonOperations.asJsonString;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class RacerControllerTest {
    private static DbSetupTracker dbSetupTracker = new DbSetupTracker();
    @Autowired
    MockMvc mockMvc;
    @Autowired
    DataSource dataSource;
    @BeforeEach
    void prepare(){
        final Operation insertDataF1Table =
                insertInto("tbl_nation")
                        .row()
                        .column("id", 1)
                        .column("name", "Viet Nam")
                        .end()


                        .build();
                insertInto("tbl_racer")
                        .row()
                        .column("id", 1)
                        .column("name", "Hieu")
                        .column("date_of_birth", "2001-10-14")
                        .column("bio", "Tieu su cua hieu")
                        .column("national", 1)
                        .end()


                        .build();
                insertInto("tbl_season")
                        .row()
                        .column("id", 1)
                        .column("name", "2022")
                        .end()

                        .build();
                insertInto("tbl_race_team")
                        .row()
                        .column("id", 1)
                        .column("name", "Ha Noi F1")
                        .column("power_unit", 100.0)
                        .column("description", "Chu thich doi dua ha noi f1")
                        .end()

                        .build();

                insertInto("tbl_season_raceteam")
                        .row()
                        .column("season_id", 1)
                        .column("race_team_id", 1)
                        .end().build();

                insertInto("tbl_racer_team")
                        .row()
                        .column("id", 1)
                        .column("racer_id", 1)
                        .column("race_team_id", 1)
                        .end().build();

                insertInto("tbl_confirmation_token")
                        .row()
                        .column("id", 1)
                        .column("user_id", 1)
                        .column("created_date", LocalDateTime.now())
                        .column("expire_date", LocalDateTime.now().plusMinutes(15))
                        .column("token", "testtoken")
                        .end().build();
        insertInto("tbl_city")
                .row()
                .column("id", 1)
                .column("name", "Ha Noi")
                .column("nation", 1)
                .end()
                .build();
        insertInto("tbl_race_course")
                .row()
                .column("id", 1)
                .column("name", "Ha Noi Stadium")
                .column("city", 1)
                .end()
                .build();
        insertInto("tbl_race_course")
                .row()
                .column("id", 1)
                .column("name", "Ha Noi Stadium")
                .column("laps", 7)
                .column("time", "2022-01-01 10:10:10")
                .column("race_course_id", 1)
                .column("season_id", 1)
                .end()
                .build();
        insertInto("tbl_result")
                .row()
                .column("id", 1)
                .column("ranking", 4)
                .column("status", 1)
                .column("finish_time", "2022-01-01 12:10:10")
                .column("racer_of_team_id", 1)
                .column("grand_prix_id", 1)
                .end()
                .build();
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
        final Operation insertConfirmationToken =
                insertInto("tbl_confirmation_token")
                        .row()
                        .column("id", 1)
                        .column("user_id", 1)
                        .column("created_date", LocalDateTime.now())
                        .column("expire_date", LocalDateTime.now().plusMinutes(15))
                        .column("token", "testtoken")
                        .end().build();
        Operation operation = sequenceOf(
                CommonOperations.DELETE_ALL_FOR_F1,
                insertRole,
                insertUser,
                insertUserRole,
                insertConfirmationToken,
                insertDataF1Table
        );
        Destination dest = new DataSourceDestination(dataSource);
        DbSetup dbSetup = new DbSetup(dest, operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @DisplayName("REGISTER: CASE 1: Username Password Email validated")
    void signUpCase1() throws Exception {
        RacerDto racerDto = RacerDto.builder()
                .name("Hieu")
                .national("Viet Nam")
                .dateOfBirth(new Date())
                .bio("asd")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/racer")
                        .content(asJsonString(racerDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
