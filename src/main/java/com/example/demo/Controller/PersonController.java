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


    @GetMapping("/adult")
    public List<AdultDTO> getPeople(){
        return service.getAdultLimit();
    }

    @GetMapping("/adult/{personId}")
    public Adult getAdultWithId(@PathVariable("personId") Long personId){
        System.out.println("Got here");
        System.out.println(personId);
        return service.getAdultID(personId);
    }
    @GetMapping("/child/avg")
    public int getAverageAge(){return service.averageChildAge();}
    @GetMapping("/friends")
    public List<Friend> getFriends(){return service.getFriendLimit();}
    @PostMapping("/family")
    public void addFamily(@RequestBody Family family) throws SQLException, ClassNotFoundException {
        service.addFamily(family);
    }
    @PostMapping("/family/mass")
    public void addFamilies(@RequestBody List<Family> family) throws SQLException, ClassNotFoundException {
        service.addFamily(family);
    }
    @GetMapping("/child")
    public List<ChildDTO> getChildren(){return service.getChildrenLimit();}
    @PostMapping("/child/{famid}/family")
    public void addChild(@RequestBody Child c,@PathVariable Long famid){

        service.addChild(c,famid);
    }
    @PostMapping("/child/mass/{famid}")
    public void addChildren(@RequestBody List<Child> c,@PathVariable Long famid) throws SQLException, ClassNotFoundException {
        for(Child ch:c)
        {
            service.addChild(ch, famid);
        }
    }
    @GetMapping("/family")
    public List<FamilyDTO> getFamilies(){return service.getFamilyLimit();}
    @GetMapping("/family/{nr}")
    public List<Family> getFamiliesNR(@PathVariable("nr") Integer nr){return service.getFamiliesNR(nr);}
    @PostMapping("/friend/{p1}/{p2}")
    public void addFriends(@PathVariable Long p1,@PathVariable Long p2)
    {
        service.addFriend(p1,p2);
    }
    //@GetMapping("/family")
    public Family getFamilyWithID(@RequestBody Long id){return service.getFamilyWithID(id);}
    @GetMapping("/child/{id}")
    public Child getChildWithID(@PathVariable("id") Long id){return service.getChildWithId(id);}
    @PostMapping("/adult")
    public void addPerson(@RequestBody Adult p){
        service.addAdult(p);
    }
    @PostMapping("/adult/mass")
    public void addPersonArr(@RequestBody List<Adult> p){
        service.addAdult(p);
    }
    @DeleteMapping("/adult/{id}")
    public void removePerson(@PathVariable("id") Long id)
    {
        service.removeAdult(id);

    }
    @PutMapping("/adult/address")
    public void updateAddressAdult(@RequestBody Long id, @RequestBody String  newAd)
    {
        service.updateAdult(id,newAd);
    }
    @PutMapping("/child/address")
    public void updateAddressChild(@RequestBody Long id, @RequestBody String  newAd)
    {
        service.updateChild(id,newAd);
    }
    @PostMapping("/family/child/{id}/{cid}")
    public void addChildToFam(@PathVariable("id") Long id,@PathVariable("cid") Long cid)
    {
        service.addChildToFamily(id,cid);
    }
    @DeleteMapping("/child/{id}")
    public void removeChild(@PathVariable("id") Long id){
        service.removeChild(id);
    }

    @DeleteMapping("/family/{id}")
    public void removeFamily(@PathVariable("id") Long id){
        service.removeFamily(id);
    }

    @DeleteMapping("/friend/{id}")
    public void removeFriend(@PathVariable("id") Long id){
        service.removeFriend(id);
    }
    @GetMapping("/family/avg")
    public int getFamAvg(){return service.averageChildCount();}
}