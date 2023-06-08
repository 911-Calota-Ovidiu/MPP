package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Model.UserProfile;
import com.example.demo.Security.JWT.JwtUtils;
import com.example.demo.Service.UserService;
import org.apache.commons.lang3.SystemUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:4200", "https://main--mppateveryone.netlify.app"})
@RestController
@RequestMapping("/api")
@Validated
public class UserController {
    private final UserService userService;
    @Value("${openai.api.key}")
    private String key;

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
    @GetMapping("/user/gpt")
    public String gptCall() throws IOException {
        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        System.out.println(key);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer "+key);

        JSONObject data = new JSONObject();
        data.put("model", "text-davinci-003");
        data.put("prompt", "Write an example of a bio in the first person. Max 255 characters");
        data.put("max_tokens", 4000);
        data.put("temperature", 1.0);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());
        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        System.out.println(new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text"));
        return "{\"text\":\""+  new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text").replaceAll("\n","")+"\"}";
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
    public ResponseEntity<?> deleteAllRecords() {
        return executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/deleteAll.sql");
    }
    @PostMapping("/admin/add/adults")
    public ResponseEntity<?> addAdultsBulk() {
        return executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/adults.sql");
    }
    @DeleteMapping("/admin/delete/adults")
    public ResponseEntity<?> deleteAdultsBulk() {
        return executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/deleteAdults.sql");
    }


    @PostMapping("/admin/add/children")
    public ResponseEntity<?> addChildrenBulk() {
        return executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/children.sql");
    }
    @DeleteMapping("/admin/delete/children")
    public ResponseEntity<?> deleteChildrenBulk() {
        return executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/deleteChildren.sql");
    }


    @PostMapping("/admin/add/families")
    public ResponseEntity<?> addFamiliesBulk() {
        return executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/families.sql");
    }
    @DeleteMapping("/admin/delete/families")
    public ResponseEntity<?> deleteFamiliesBulk() {
        return executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/deleteFmilies.sql");
    }
    @PostMapping("/admin/add/friends")
    public ResponseEntity<?> addFriendsBulk() {
        executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/friend.sql");
        executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/first_friend.sql");
        return executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/second_friend.sql");

    }
    @DeleteMapping("/admin/delete/friends")
    public ResponseEntity<?> deleteFriendsBulk() {
        return executeLinuxCommand("psql -U postgres -d MPP -f /home/ubuntu/MPP/src/sql_scripts/deleteFriends.sql");
    }
    public ResponseEntity<?> executeLinuxCommand(String command) {
        if (!SystemUtils.IS_OS_LINUX) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new Object() {
                public String getMessage() {return "Server OS is not Linux";}
            });
        }

        try {
            File workingDirectory = new File("/home/ubuntu/MPP/src/sql_scripts");
            ProcessBuilder pb = new ProcessBuilder("/bin/dash", "-c", command);
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
