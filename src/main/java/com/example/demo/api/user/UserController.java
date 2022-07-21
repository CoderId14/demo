package com.example.demo.api.user;


import com.example.demo.Service.impl.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private UserService userService;

    @DeleteMapping()
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id){
        log.info("Delete user id= "+id);
        userService.delete(id);
        return ResponseEntity.ok("Delete user id = " +id);
    }
}
