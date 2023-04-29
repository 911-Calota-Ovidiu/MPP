
package com.example.demo.Service;

import com.example.demo.Model.*;
import com.example.demo.Repo.AdultRepo;
import com.example.demo.Repo.ChildRepo;
import com.example.demo.Repo.FamilyRepo;
import com.example.demo.Repo.FriendRepo;
import com.example.demo.Model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    AdultRepo adultRepo;
    @Autowired
    ChildRepo childRepo;
    @Autowired
    FamilyRepo familyRepo;
    @Autowired
    FriendRepo friendRepo;
    Long startadult= 0L,startchild=0L,startfamily=0L,startfriend=0L;
    @PersistenceContext
    EntityManager entityManager;
    public List<Adult> getAdultPage(int page)
    {

        Pageable pageable= PageRequest.of(page,10,Sort.by("adultID"));
        List<Adult> alist= adultRepo.findAll(pageable).getContent();
        List<AdultDTO> retlist=new ArrayList<>();
        for(Adult a: alist)
        {
            retlist.add(new AdultDTO(a.getName(),a.getAddress(),a.getAge()));
        }
        return alist;
    }

    public List<ChildDTO> getChildPage(int page)
    {

        Pageable pageable= PageRequest.of(page,10,Sort.by("childID"));
        List<Child> alist= childRepo.findAll(pageable).getContent();
        List<ChildDTO> retlist=new ArrayList<>();
        for(Child a: alist)
        {
            retlist.add(new ChildDTO(a.name,a.getAddress(),a.getFamily().getId()));
        }
        return retlist;
    }
    public List<FamilyDTO> getFamilyPage(int page)
    {

        Pageable pageable= PageRequest.of(page,10,Sort.by("famID"));
        List<Family> alist= familyRepo.findAll(pageable).getContent();
        List<FamilyDTO> retlist=new ArrayList<>();
        for(Family a: alist)
        {
            retlist.add(new FamilyDTO(a.getMom(),a.getDad(),a.getHomeAddress()));
        }
        return retlist;
    }
    public List<Friend> getFriendPage(int page)
    {

        Pageable pageable= PageRequest.of(page,10,Sort.by("id"));
        return friendRepo.findAll(pageable).getContent();
    }


    public void removeFamily(Long id)
    {
        familyRepo.deleteById(id);
    }


    public void addAdult(Adult a) {
        adultRepo.save(a);
    }

    public void addAdult(List<Adult> a) {
        adultRepo.saveAll(a);
    }

    public void removeAdult(Long id) {
        adultRepo.deleteById(id);
    }

    public void updateAdult(Long id, String newAd) {
        Adult ad = adultRepo.findById(id).get();
        ad.setAddress(newAd);
        adultRepo.save(ad);
    }

    public AdultRepo getAdults() {
        return adultRepo;
    }

    public List<Family> getFamiliesNR(Integer nr) {
        List<Family> returnList = new ArrayList<>();
        for (Family f : familyRepo.findAll()) {
            if (f.getChildren().size() > nr) {
                returnList.add(f);
            }
        }
        return returnList;
    }

    public void addChild(Child a, Long famid) {
        Family f = familyRepo.findById(famid).get();
        a.setFamily(f);
        childRepo.save(a);

    }

    public void addChild(List<Child> a) {
        childRepo.saveAll(a);

    }

    public void removeChild(Long id) {
        childRepo.deleteById(id);
        friendRepo.deleteById(id);
    }

    public void updateChild(Long p, String s) {
        Child ad = childRepo.findById(p).get();
        ad.setAddress(s);
        childRepo.save(ad);
    }

    public void addChildToFamily(Long id, Long cid) {
        Family f = familyRepo.findById(id).get();
        Child c = childRepo.findById(cid).get();
        f.addChild(c);
        familyRepo.save(f);
    }

    public List<ChildDTO> getDTOchildren() {
        List<ChildDTO> returnList = new ArrayList<>();

        List<Child> cs = childRepo.findAll().stream().toList();
        for (Child c : cs) {
            returnList.add(new ChildDTO(c.name, c.getAddress(), null));
        }
        List<Family> fams = familyRepo.findAll().stream().toList();
        for (Family fam : fams) {
            for (Child c : fam.getChildren()) {
                for (ChildDTO cdto : returnList) {
                    if (c.name == cdto.getName()) cdto.setFamid(fam.getId());
                }
            }
        }
        return returnList;
    }

    public List<AdultDTO> getDTOadults() {
        List<AdultDTO> returnList = new ArrayList<>();
        for (Adult f : adultRepo.findAll()) {
            returnList.add(new AdultDTO(f.getName(), f.getAddress(), f.getAge()));
        }
        return returnList;
    }
    public void removeFriend(Long id)
    {
        friendRepo.deleteById(id);
    }
    public Adult getAdultWithID(Long id) {
        return adultRepo.findById(id).get();
    }

    public Family getFamilyWithID(Long id) {
        return familyRepo.findById(id).get();
    }

    public Child getChildWithId(Long id) {
        return childRepo.findById(id).get();
    }

    public List<FamilyDTO> getDTOfamilies() {
        List<FamilyDTO> returnList = new ArrayList<>();
        for (Family f : familyRepo.findAll()) {
            FamilyDTO fam = new FamilyDTO(f.getMom(), f.getDad(), f.getHomeAddress());
            returnList.add(fam);
        }
        return returnList;
    }

    public ChildRepo getChildren() {
        return childRepo;
    }


    public void addFriend(Long id1, Long id2) {
        Child c = childRepo.findById(id1).get();
        Child c1 = childRepo.findById(id2).get();
        Friend f = new Friend();
        f.setC1(c);
        f.setC2(c1);
        friendRepo.save(f);
    }

    public List<FriendDTO> getDTOFriends() {
        List<FriendDTO> returnList = new ArrayList<>();
        List<Friend> frs = friendRepo.findAll().stream().toList();
        for (Friend f : frs) {
            FriendDTO fdto = new FriendDTO(f.getPerson());
            fdto.addFriends(f.getFriends());
            returnList.add(fdto);
        }
        return returnList;
    }


    public void addFamily(Family a) {
        familyRepo.save(a);
    }

    public void addFamily(List<Family> a) {
        familyRepo.saveAll(a);
    }

    public FamilyRepo getFamilies() {
        return familyRepo;
    }

    public Adult getAdultID(Long id) {
        return adultRepo.findById(id).get();
    }
    public int averageChildCount()
    {
        int count=0,nrfam=0;
        for(Family f: familyRepo.findAll())
        {
            nrfam++;
            count+=f.getNrOfMembers();
        }
        return count/nrfam;
    }
    public Double averageChildAge() {
        /*int age = 0, sum = 0;
        List<Child> children = childRepo.findAll();
        for (Child c : children) {
            age += c.getAge();
            sum++;
        }*/

        TypedQuery<Double> query = entityManager.createQuery("SELECT AVG(e.age) FROM Child e", Double.class);
        return query.getSingleResult();
    }
}