package com.example.demo.api.auth;

import com.example.demo.CommonOperations;
import com.example.demo.api.auth.request.ConfirmationRequest;
import com.example.demo.api.auth.request.LoginRequest;
import com.example.demo.api.auth.request.SignUpRequest;
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

import static com.example.demo.CommonOperations.asJsonString;
import static org.hamcrest.CoreMatchers.is;

import javax.sql.DataSource;

import java.time.LocalDateTime;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {
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
    @DisplayName("REGISTER: CASE 1: Username Password Email validated")
    void signUpCase1() throws Exception {
        SignUpRequest USER_1_SIGNUP = SignUpRequest.builder()
                .username("test2")
                .password("test2")
                .email("test2@gmail.com")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content(asJsonString(USER_1_SIGNUP))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseData.username", is("test2")));
    }
    @Test
    @DisplayName("REGISTER: CASE 2: Username Password Email empty")
    void signUpCase2() throws Exception {
        SignUpRequest USER_1_SIGNUP = SignUpRequest.builder()
                .username("")
                .password("")
                .email("")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content(asJsonString(USER_1_SIGNUP))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("REGISTER: CASE 3: Username Password Email invalid")
    void signUpCase3() throws Exception {
        SignUpRequest USER_1_SIGNUP = SignUpRequest.builder()
                .username("!#@$%^")
                .password("!@#$%")
                .email("!@#$%^%@gmail.com")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content(asJsonString(USER_1_SIGNUP))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("REGISTER: CASE 4: Username invalid, Password Email valid")
    void signUpCase4() throws Exception {
        SignUpRequest USER_1_SIGNUP = SignUpRequest.builder()
                .username("!#@$%^")
                .password("test1")
                .email("test1@gmail.com")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content(asJsonString(USER_1_SIGNUP))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("REGISTER: CASE 5: Password invalid, Username Email valid")
    void signUpCase5() throws Exception {
        SignUpRequest USER_1_SIGNUP = SignUpRequest.builder()
                .username("test1")
                .password("#@@#%")
                .email("test1@gmail.com")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content(asJsonString(USER_1_SIGNUP))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("REGISTER: CASE 6: Email invalid, Username Password valid")
    void signUpCase6() throws Exception {
        SignUpRequest USER_1_SIGNUP = SignUpRequest.builder()
                .username("test1")
                .password("test1")
                .email("!@#$#@gmail.com")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content(asJsonString(USER_1_SIGNUP))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
    @Test
    @DisplayName("LOGIN: CASE 1: Username, Password valid and user exist in database")
    void loginCase1() throws Exception {

        LoginRequest USER_1_LOGIN = LoginRequest.builder()
                .username("test1")
                .password("test1")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(asJsonString(USER_1_LOGIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
    @DisplayName("LOGIN: CASE 2: Username, Password empty")
    void loginCase2() throws Exception {

        LoginRequest USER_1_LOGIN = LoginRequest.builder()
                .username("")
                .password("")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(asJsonString(USER_1_LOGIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("LOGIN: CASE 3: Username, Password invalid with sensitive character")
    void loginCase3() throws Exception {

        LoginRequest USER_1_LOGIN = LoginRequest.builder()
                .username("*%*&^*&_")
                .password("*%*&^*&_")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(asJsonString(USER_1_LOGIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("LOGIN: CASE 4: Username invalid")
    void loginCase4() throws Exception {

        LoginRequest USER_1_LOGIN = LoginRequest.builder()
                .username("*%*&^*&_")
                .password("test1")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(asJsonString(USER_1_LOGIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("LOGIN: CASE 5: Password invalid with sensitive character")
    void loginCase5() throws Exception {

        LoginRequest USER_1_LOGIN = LoginRequest.builder()
                .username("test1")
                .password("*%*&^*&_")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .content(asJsonString(USER_1_LOGIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("CONFIRM: CASE 1: Token valid")
    void confirmCase1() throws Exception {
        ConfirmationRequest token = ConfirmationRequest.builder()
                .token("testtoken")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register/confirm")
                        .content(asJsonString(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("CONFIRM: CASE 2: Token invalid")
    void confirmCase2() throws Exception {
        ConfirmationRequest token = ConfirmationRequest.builder()
                .token("invalidTestToken")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register/confirm")
                        .content(asJsonString(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }



}