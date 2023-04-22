package com.example.demo.Controller;

import com.example.demo.Repo.*;
import com.example.demo.Model.*;
import com.example.demo.Service.*;
import com.example.demo.Model.*;
import com.example.demo.Repo.FriendRepo;
import com.example.demo.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
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
        return service.adultFirstPage();
    }

    @GetMapping("/adult/{personId}")
    public Adult getAdultWithId(@PathVariable("personId") Long personId){
        return service.getAdultID(personId);
    }
    @GetMapping("/child/avg")
    public int getAverageAge(){return service.averageChildAge();}
    @GetMapping("/friends")
    public List<FriendDTO> getFriends(){return service.getDTOFriends();}
    @PostMapping("/family")
    public void addFamily(@RequestBody Family family) throws SQLException, ClassNotFoundException {
        service.addFamily(family);
    }
    @PostMapping("/family/mass")
    public void addFamilies(@RequestBody List<Family> family) throws SQLException, ClassNotFoundException {
        service.addFamily(family);
    }
    @GetMapping("/child")
    public List<ChildDTO> getChildren(){return service.getDTOchildren();}
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
    public List<FamilyDTO> getFamilies(){return service.getDTOfamilies();}
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
    public void updateAddress(@RequestBody Long id, @RequestBody String  newAd)
    {
        service.updateAdult(id,newAd);
    }

    @PostMapping("/family/child/{id}/{cid}")
    public void addChildToFam(@PathVariable("id") Long id,@PathVariable("cid") Long cid)
    {
        service.addChildToFamily(id,cid);
    }
    @GetMapping("/adult/next")
    public List<AdultDTO> getNextAdultPage()
    {
        return service.gettenadultsNEXT();
    }
    @GetMapping("/adult/prev")
    public List<AdultDTO> getPrevAdultPage()
    {
        return service.gettenadultsPREV();
    }

    @GetMapping("/child/next/page")
    public List<ChildDTO> getNextChildPage()
    {
        return service.gettenchildrenNEXT();
    }
    @GetMapping("/child/prev/page")
    public List<ChildDTO> getPrevChildPage()
    {
        return service.gettenchildrenPREV();
    }

    @GetMapping("/family/next")
    public List<FamilyDTO> getNextFamilyPage()
    {
        return service.gettenfamiliesNEXT();
    }
    @GetMapping("/family/prev")
    public List<FamilyDTO> getPrevFamilyPage()
    {
        return service.gettenfamiliesPREV();
    }

    @GetMapping("/friend/next")
    public List<Friend> getNextFriendPage()
    {
        return service.gettenfriendsNEXT();
    }
    @GetMapping("/friend/prev")
    public List<Friend> getPrevFriendPage()
    {
        return service.gettenfriendsPREV();
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
}