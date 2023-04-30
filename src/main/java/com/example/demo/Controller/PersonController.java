package com.example.demo.Controller;

import com.example.demo.Repo.*;
import com.example.demo.Model.*;
import com.example.demo.Service.*;
import com.example.demo.Model.*;
import com.example.demo.Repo.FriendRepo;
import com.example.demo.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.List;

@RestController
public class PersonController {
    @Autowired
    private Service service;

    Connection con = null;


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
    public void addPerson(@RequestBody Adult p) {
        service.addAdult(p);
    }

    @PostMapping("/adult/mass")
    public void addPersonArr(@RequestBody List<Adult> p) {
        service.addAdult(p);
    }

    @DeleteMapping("/adult/{id}")
    public void removePerson(@PathVariable("id") Long id) {
        service.removeAdult(id);

    }

    @PutMapping("/adult/{id}")
    public void updateAddressAdult(@PathVariable Long id, @RequestBody Adult ad) {
        service.updateAdult(id, ad);
    }
    @GetMapping("/adult/count")
    public Long countAdults()
    {
        return service.countAdults();
    }
    @GetMapping("/adult/test/add")
    public List<Adult> testAdd()
    {
        return service.testAddAdult();
    }



    // CHILD REQUESTS
    @GetMapping("/child/avg")
    public Double getAverageAge() {
        return service.averageChildAge();
    }

    @GetMapping("/child/page/{page}")
    public List<ChildDTO> getChildren(@PathVariable int page) {
        return service.getChildPage(page);
    }

    @PostMapping("/child/{famid}/family")
    public void addChild(@RequestBody Child c, @PathVariable Long famid) {

        service.addChild(c, famid);
    }

    @PostMapping("/child/mass/{famid}")
    public void addChildren(@RequestBody List<Child> c, @PathVariable Long famid) throws SQLException, ClassNotFoundException {
        for (Child ch : c) {
            service.addChild(ch, famid);
        }
    }

    @GetMapping("/child/{id}")
    public Child getChildWithID(@PathVariable("id") Long id) {
        return service.getChildWithId(id);
    }

    @PutMapping("/child/address")
    public void updateAddressChild(@RequestBody Long id, @RequestBody String newAd) {
        service.updateChild(id, newAd);
    }

    @DeleteMapping("/child/{id}")
    public void removeChild(@PathVariable("id") Long id) {
        service.removeChild(id);
    }


    // FAMILY REQUESTS
    @PostMapping("/family")
    public void addFamily(@RequestBody Family family) throws SQLException, ClassNotFoundException {
        service.addFamily(family);
    }

    @PostMapping("/family/mass")
    public void addFamilies(@RequestBody List<Family> family) throws SQLException, ClassNotFoundException {
        service.addFamily(family);
    }

    @GetMapping("/family/page/{page}")
    public List<FamilyDTO> getFamilies(@PathVariable int page) {
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
    public void addChildToFam(@PathVariable("id") Long id, @PathVariable("cid") Long cid) {
        service.addChildToFamily(id, cid);
    }

    @DeleteMapping("/family/{id}")
    public void removeFamily(@PathVariable("id") Long id) {
        service.removeFamily(id);
    }


    @GetMapping("/family/avg")
    public int getFamAvg() {
        return service.averageChildCount();
    }


    // FRIEND REQUESTS
    @GetMapping("/friends/page/{page}")
    public List<Friend> getFriends(@PathVariable int page) {
        return service.getFriendPage(page);
    }

    @PostMapping("/friends/{p1}/{p2}")
    public void addFriends(@PathVariable Long p1, @PathVariable Long p2) {
        service.addFriend(p1, p2);
    }

    @DeleteMapping("/friend/{id}")
    public void removeFriend(@PathVariable("id") Long id) {
        service.removeFriend(id);
    }




    @RequestMapping("/")
    public String mainpage()
    {
        return "Works?!?!?!";
    }
}
