
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
import org.springframework.data.domain.Page;
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
    @PersistenceContext
    EntityManager entityManager;
    public List<Adult> getAdultPage(int page)
    {

        Pageable pageable= PageRequest.of(page,50,Sort.by("adultID"));
        return adultRepo.findAll(pageable).getContent();
    }

    public List<Child> getChildPage(int page)
    {

        Pageable pageable= PageRequest.of(page,50,Sort.by("childID"));
        return childRepo.findAll(pageable).getContent();
    }
    public List<Family> getFamilyPage(int page)
    {

        Pageable pageable= PageRequest.of(page,10,Sort.by("famID"));
        List<Family> alist= familyRepo.findAll(pageable).getContent();
        List<FamilyDTO> retlist=new ArrayList<>();
        for(Family a: alist)
        {
            retlist.add(new FamilyDTO(a.getMom(),a.getDad(),a.getHomeAddress()));
        }
        return alist;
    }
    public List<Friend> getFriendPage(int page)
    {

        Pageable pageable= PageRequest.of(page,50,Sort.by("id"));
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

    public void updateAdult(Long id, Adult adN) {
        Adult ad = adultRepo.findById(id).get();
        ad.setAddress(adN.getAddress());
        ad.setAge(adN.getAge());
        ad.setAname(adN.getAname());
        ad.setBirthdate(adN.getBirthdate());
        ad.setEyeColor(adN.getEyeColor());
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
    public void updateFamily(Long id, Family family)
    {
        Family f=familyRepo.findById(id).get();
        f.setDad(family.getDad());
        f.setMom(family.getMom());
        f.setHomeAddress(family.getHomeAddress());
        f.setNrOfMembers(family.getNrOfMembers());
        familyRepo.save(f);

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

    public void updateChild(Long p, Child s,Long famid) {
        Child ad = childRepo.findById(p).get();
        Family f= familyRepo.findById(famid).get();
        ad.setFamily(f);
        ad.setName(s.name);
        ad.setAge(s.getAge());
        ad.setAddress(s.getAddress());
        ad.setBirthdate(s.getBirthdate());
        ad.setEyeColor(s.getEyeColor());
        childRepo.save(ad);
    }

    public void addChildToFamily(Long id, Long cid) {
        Family f = familyRepo.findById(id).get();
        Child c = childRepo.findById(cid).get();
        f.addChild(c);
        f.setNrOfMembers(f.getChildren().size()+2);
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
    public Long countAdults()
    {
        TypedQuery<Long> query = entityManager.createQuery("SELECT Count(*) FROM Adult e", Long.class);
        return query.getSingleResult();

    }
    public Long countAdultsAge(int age)
    {
        TypedQuery<Long> query = entityManager.createQuery("SELECT Count(*) FROM Adult e where e.age=:age", Long.class);
        query.setParameter("age",age);
        return query.getSingleResult();

    }
    public Long countChildren()
    {
        TypedQuery<Long> query = entityManager.createQuery("SELECT Count(*) FROM Child e", Long.class);
        return query.getSingleResult();

    }
    public Long countFamilies()
    {
        TypedQuery<Long> query = entityManager.createQuery("SELECT Count(*) FROM Family e", Long.class);
        return query.getSingleResult();

    }
    public Long countFriends()
    {
        TypedQuery<Long> query = entityManager.createQuery("SELECT Count(*) FROM Friend e", Long.class);
        return query.getSingleResult();

    }
    public Friend getOneFriend(Long id)
    {
        return friendRepo.findById(id).get();
    }
    public List<Adult> getAdultsByAge(int age,int page)
    {
        Pageable pageable = PageRequest.of(page, 50, Sort.by("adultID"));
        return adultRepo.findByAge(age, pageable);
    }
    public List<Adult> testAddAdult()
    {
        TypedQuery<Adult> query = entityManager.createQuery("SELECT e FROM Adult e WHERE e.age=99999", Adult.class);
        System.out.println(query.getResultList().size());
        return query.getResultList();

    }
}