package com.example.demo.api.user;


import com.example.demo.CommonOperations;
import com.example.demo.dto.request.ChangePasswordDto;
import com.example.demo.dto.request.ForgotPasswordDto;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;
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

import static com.example.demo.CommonOperations.asJsonString;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    private static DbSetupTracker dbSetupTracker = new DbSetupTracker();
    @Autowired
    MockMvc mockMvc;
    @Autowired
    DataSource dataSource;
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
                CommonOperations.DELETE_ALL,
                insertRole,
                insertUser,
                insertUserRole,
                insertConfirmationToken
        );
        Destination dest = new DataSourceDestination(dataSource);
        DbSetup dbSetup = new DbSetup(dest, operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @DisplayName("Get Email: CASE 1: username valid")
    void getEmailWithUsernameCase1() throws Exception {

        ForgotPasswordDto forgotPasswordDto = ForgotPasswordDto.builder()
                .usernameOrEmail("test1")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/forgot-password")
                                .content(asJsonString(forgotPasswordDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.email", is("test1@gmail.com")));
    }
    @Test
    @DisplayName("Get Email: CASE 2: username invalid")
    void getEmailWithUsernameCase2() throws Exception {

        ForgotPasswordDto forgotPasswordDto = ForgotPasswordDto.builder()
                .usernameOrEmail("invalidTest1")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/forgot-password")
                        .content(asJsonString(forgotPasswordDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get User from Token: CASE 1: token valid")
    void getUserFromTokenCase1() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/forgot-password")
                        .param("token", "testtoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("test1")));
    }
    @Test
    @DisplayName("Get User from Token: CASE 2: token invalid")
    void getUserFromTokenCase2() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/forgot-password")
                        .param("token", "invalidtesttoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Change password (Forgot Password): CASE 1: Username Password Token valid")
    void changePasswordCase1() throws Exception {
        ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
                .usernameOrEmail("test1")
                .password("newTest1")
                .token("testtoken")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/change-password")
                        .content(asJsonString(changePasswordDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("Change password (Forgot Password): CASE 2: Username Password Token invalid")
    void changePasswordCase2() throws Exception {
        ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
                .usernameOrEmail("invalidtest1")
                .password("newTest1")
                .token("invalidtesttoken")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/change-password")
                        .content(asJsonString(changePasswordDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Change password (Forgot Password): CASE 3: Username invalid, Password Token valid")
    void changePasswordCase3() throws Exception {
        ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
                .usernameOrEmail("invalidtest1")
                .password("newTest1")
                .token("testtoken")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/change-password")
                        .content(asJsonString(changePasswordDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
