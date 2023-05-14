package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Model.UserProfile;
import com.example.demo.Security.JWT.JwtUtils;
import com.example.demo.Service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:4200", "https://superb-cupcake-f2ff38.netlify.app"})
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
}
