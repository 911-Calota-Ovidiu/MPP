package com.example.demo.Controller;

import com.example.demo.Exception.UserNotAuthorizedException;
import com.example.demo.Model.ERole;
import com.example.demo.Model.User;
import com.example.demo.Model.UserProfile;
import com.example.demo.Security.JWT.JwtUtils;
import com.example.demo.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.SystemUtils;

import javax.validation.Valid;
import java.io.File;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:4200", "https://main--mppateveryone.netlify.app"})
@RestController
@RequestMapping("/api")
@Validated
public class UserController {
    private final UserService userService;

    private final JwtUtils jwtUtils;

    UserController(UserService userService, JwtUtils jwtUtils) {this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/user-profile-id/{id}")
    UserProfile getProfileById(@PathVariable Long id) {
        return userService.getUserProfileById(id);
    }

    @GetMapping("/user-profile-username/{username}")
    UserProfile getProfileByUsername(@PathVariable String username) {
        return userService.getUserProfileByUsername(username);
    }

    @GetMapping("/user/{username}")
    User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/user-number-adults/{id}")
    Integer getUserNumberOfAdultsById(@PathVariable Long id) {
        return userService.getUserByNrOfAdults(id);
    }

    @GetMapping("/user-number-children/{id}")
    Integer getUserNumberOfChildrenById(@PathVariable Long id) {
        return userService.getUserByNrOfChildren(id);
    }

    @GetMapping("/user-number-families/{id}")
    Integer getUserNumberOfFamiliesById(@PathVariable Long id) {
        return userService.getUserByNrOfFamilies(id);
    }
    @GetMapping("/user-number-friends/{id}")
    Integer getUserNumberOfFriendsById(@PathVariable Long id) {
        return userService.getUserByNrOfFriends(id);
    }
    @GetMapping("/user-search")
    List<User> getPilotsByName(@RequestParam(required = false) String username) {
        return this.userService.searchUsersByUsername(username);
    }

    @PutMapping("/user-profile/{id}")
    UserProfile updateUserProfile(@Valid @RequestBody UserProfile newUserProfile,
                                  @PathVariable Long id) {
        System.out.println(id);
        return userService.updateUserProfile(newUserProfile, id);
    }

    @PutMapping("/user-roles/{id}")
    User updateUser(@Valid @RequestBody HashMap<String, Boolean> roles,
                    @PathVariable Long id,
                    @RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);

        return userService.updateRolesUser(roles, id, user.getId());
    }
    @DeleteMapping("/admin/delete/all")
    public ResponseEntity<?> deleteAllRecords(@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );
        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        return executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/deleteAll.sql");
    }
    @PostMapping("/admin/add/adults")
    public ResponseEntity<?> addAdultsBulk(@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );
        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        return executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/adults.sql");
    }
    @DeleteMapping("/admin/delete/adults")
    public ResponseEntity<?> deleteAdultsBulk(@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        boolean isAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );
        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        return executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/deleteAdults.sql");
    }
    public ResponseEntity<?> executeLinuxCommand(String command) {
        if (!SystemUtils.IS_OS_LINUX) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new Object() {
                public String getMessage() {return "Server OS is not Linux";}
            });
        }

        try {
            File workingDirectory = new File("/home/ubuntu/MPP/src/sql_scripts");
            ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", command);
            pb.directory(workingDirectory);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            int exitCode = process.waitFor();
            System.out.println("Process exited with code " + exitCode);

            if (exitCode == 0) {
                return ResponseEntity.ok(new Object() {
                    public String getMessage() {
                        return "Successful command";
                    }
                });
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Object() {
                    public String getMessage() {return "OS could not properly execute the command";}
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new Object() {
                public String getMessage() {return "OS related error";}
            });
        }
    }
}
