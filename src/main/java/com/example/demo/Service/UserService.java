package com.example.demo.Service;

import com.example.demo.Exception.RoleNotFoundException;
import com.example.demo.Exception.UserNotAuthorizedException;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Exception.UserProfileNotFoundException;
import com.example.demo.Model.*;
import com.example.demo.Repo.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final RoleRepository roleRepository;

    private final AdultRepo adultRepo;

    private final ChildRepo childRepo;

    private final FamilyRepo familyRepo;

    private final FriendRepo friendRepo;

    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository, RoleRepository roleRepository,
                       AdultRepo adultRepo, ChildRepo childRepo, FamilyRepo familyRepo, FriendRepo friendRepo) {
        this.adultRepo = adultRepo;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.roleRepository = roleRepository;
        this.childRepo = childRepo;
        this.familyRepo = familyRepo;
        this.friendRepo = friendRepo;
    }

    public UserProfile getUserProfileById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return user.getUserProfile();
    }

    public UserProfile getUserProfileByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return user.getUserProfile();
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    public Integer getUserNumberOfAdultsById(Long id) {
        return adultRepo.findByUserId(id).size();
    }

    public Integer getUserNumberOfFamiliesById(Long id) {
        return familyRepo.findByUserId(id).size();
    }

    public Integer getUserNumberOfChildrenById(Long id) {
        return childRepo.findByUserId(id).size();
    }

    public UserProfile updateUserProfile(UserProfile newUserProfile, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        boolean isUser = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_USER);
        if (!isUser) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }

        return userProfileRepository.findById(user.getUserProfile().getId())
                .map(userProfile -> {
                    userProfile.setBio(newUserProfile.getBio());
                    userProfile.setLocation(newUserProfile.getLocation());
                    userProfile.setGender(newUserProfile.getGender());
                    userProfile.setMaritalStatus(newUserProfile.getMaritalStatus());
                    userProfile.setBirthdate(newUserProfile.getBirthdate());
                    return userProfileRepository.save(userProfile);
                })
                .orElseThrow(() -> new UserProfileNotFoundException(id));
    }
    public User updateRolesUser(HashMap<String, Boolean> roles, Long id, Long userID) {
        User callerUser = this.userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException(userID));

        boolean isAdmin = callerUser.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
        );
        if (!isAdmin) {
            throw new UserNotAuthorizedException(String.format(callerUser.getUsername()));
        }

        User user = this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        Set<Role> roleSet = new HashSet<>();
        System.out.println(roles);
        if (roles.get("isUser")) {
            Role role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_USER));
            roleSet.add(role);
        }
        if (roles.get("isModerator")){
            Role role = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_MODERATOR));
            roleSet.add(role);
        }
        if (roles.get("isAdmin")){
            Role role = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_ADMIN));
            roleSet.add(role);
        }
        user.setRoles(roleSet);
        return userRepository.save(user);
    }
    public Integer getUserByNrOfAdults(Long id){
        return adultRepo.findByUserId(id).size();
    }
    public Integer getUserByNrOfChildren(Long id){
        return childRepo.findByUserId(id).size();
    }
    public Integer getUserByNrOfFamilies(Long id){
        return familyRepo.findByUserId(id).size();
    }
    public Integer getUserByNrOfFriends(Long id){
        return friendRepo.findByUserId(id).size();
    }
    public List<User> searchUsersByUsername(String username) {
        return this.userRepository.findTop20BySearchTerm(username);
    }
}
