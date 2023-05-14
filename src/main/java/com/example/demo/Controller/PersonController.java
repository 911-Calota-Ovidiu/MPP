package com.example.demo.Controller;

import com.example.demo.Repo.*;
import com.example.demo.Model.*;
import com.example.demo.Security.JWT.JwtUtils;
import com.example.demo.Service.*;
import com.example.demo.Model.*;
import com.example.demo.Repo.FriendRepo;
import com.example.demo.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "https://main--mppateveryone.netlify.app/"}, allowCredentials = "true")
@RestController
@RequestMapping("/api")
@Validated
public class PersonController {

    @Autowired
    private Service service;
    private final UserService userService;

    private final JwtUtils jwtUtils;

    public PersonController(UserService userService,JwtUtils jwtUtils) {
        this.userService=userService;
        this.jwtUtils=jwtUtils;
    }


    // ADULT REQUESTS
    @GetMapping("/adult/page/{page}")
    public List<Adult> getPeople(@PathVariable int page) {
        return service.getAdultPage(page);
    }

    @GetMapping("/adult/{personId}")
    public Adult getAdultWithId(@PathVariable("personId") Long personId) {
        return service.getAdultID(personId);
    }

    @PostMapping("/adult")
    public void addPerson(@RequestBody Adult p,@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        service.addAdult(p,user.getId());
    }

    @PostMapping("/adult/mass")
    public void addPersonArr(@RequestBody List<Adult> p) {
        service.addAdult(p);
    }

    @DeleteMapping("/adult/{id}")
    public void removePerson(@PathVariable("id") Long id,@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        service.removeAdult(id,user.getId());

    }

    @PutMapping("/adult/{id}")
    public void updateAddressAdult(@PathVariable Long id, @RequestBody Adult ad,@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        service.updateAdult(id, ad,user.getId());
    }
    @GetMapping("/adult/count")
    public Long countAdults()
    {
        return service.countAdults();
    }

    @GetMapping("/adult/age/{age}/page/{page}")
    public List<Adult> getAdultsByAge(@PathVariable int age, @PathVariable int page)
    {
        return service.getAdultsByAge(age,page);
    }
    @GetMapping("/adult/age/{age}")
    public Long getAdultsAgeCount(@PathVariable int age)
    {
        return service.countAdultsAge(age);
    }


    // CHILD REQUESTS
    @GetMapping("/child/avg")
    public Double getAverageAge() {
        return service.averageChildAge();
    }

    @GetMapping("/child/page/{page}")
    public List<Child> getChildren(@PathVariable int page) {
        return service.getChildPage(page);
    }

    @PostMapping("/child/{famid}/family")
    public void addChild(@RequestBody Child c, @PathVariable Long famid,@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        service.addChild(c, famid,user.getId());
    }


    @GetMapping("/child/{id}")
    public Child getChildWithID(@PathVariable("id") Long id) {
        return service.getChildWithId(id);
    }

    @PutMapping("/child/{id}/{famID}")
    public void updateAddressChild(@PathVariable Long id, @RequestBody Child newchild,@PathVariable Long famID,@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        service.updateChild(id, newchild,famID,user.getId());
    }

    @DeleteMapping("/child/{id}")
    public void removeChild(@PathVariable("id") Long id,@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        service.removeChild(id,user.getId());
    }

    @GetMapping("/child/count")
    public Long countChildren()
    {
        return service.countChildren();
    }



    // FAMILY REQUESTS
    @PostMapping("/family")
    public void addFamily(@RequestBody Family family,@RequestHeader("Authorization") String token) throws SQLException, ClassNotFoundException {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        service.addFamily(family,user.getId());
    }

    @PostMapping("/family/mass")
    public void addFamilies(@RequestBody List<Family> family) throws SQLException, ClassNotFoundException {
        service.addFamily(family);
    }

    @GetMapping("/family/page/{page}")
    public List<Family> getFamilies(@PathVariable int page) {
        return service.getFamilyPage(page);
    }


    @GetMapping("/family/filter/{nr}")
    public List<Family> getFamiliesNR(@PathVariable("nr") Integer nr) {
        return service.getFamiliesNR(nr);
    }

    @GetMapping("/family/{id}")
    public Family getFamilyWithID(@PathVariable Long id) {
        return service.getFamilyWithID(id);
    }

    @PostMapping("/family/child/{id}/{cid}")
    public void addChildToFam(@PathVariable("id") Long id, @PathVariable("cid") Long cid,@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        service.addChildToFamily(id, cid,user.getId());
    }

    @DeleteMapping("/family/{id}")
    public void removeFamily(@PathVariable("id") Long id,@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        service.removeFamily(id,user.getId());
    }


    @GetMapping("/family/avg")
    public int getFamAvg() {
        return service.averageChildCount();
    }

    @GetMapping("/family/count")
    public Long countFamilies()
    {
        return service.countFamilies();
    }
    @PutMapping("/family/{id}")
    public void updateFamily(@RequestBody Family family,@PathVariable Long id,@RequestHeader("Authorization") String token){
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        service.updateFamily(id,family,user.getId());
    }



    // FRIEND REQUESTS
    @GetMapping("/friends/page/{page}")
    public List<Friend> getFriends(@PathVariable int page) {
        return service.getFriendPage(page);
    }

    @PostMapping("/friends/{p1}/{p2}")
    public void addFriends(@PathVariable Long p1, @PathVariable Long p2,@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        service.addFriend(p1, p2,user.getId());
    }

    @DeleteMapping("/friends/{id}")
    public void removeFriend(@PathVariable Long id,@RequestHeader("Authorization") String token) {
        String username = this.jwtUtils.getUserNameFromJwtToken(token);
        User user = this.userService.getUserByUsername(username);
        service.removeFriend(id,user.getId());
    }

    @GetMapping("/friends/count")
    public Long countFriends()
    {
        return service.countFriends();
    }

    @GetMapping("/friend/{id}")
    public Friend getOneFriend(@PathVariable Long id){return service.getOneFriend(id);}

    @RequestMapping("/")
    public String mainpage()
    {
        return "Works?!?!?!";
    }




    //other requests

    @GetMapping("get-entities-per-page")
    Integer getPageSize() {
        return service.getPageSize();
    }

    @PostMapping("/modify-entities-per-page/{entitiesPerPage}")
    void setPageSize(@PathVariable int entitiesPerPage)
    {
        this.service.setPageSize(entitiesPerPage);
    }
}
