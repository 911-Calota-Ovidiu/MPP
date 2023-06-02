
package com.example.demo.Service;

import com.example.demo.Exception.UserNotAuthorizedException;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Model.*;
import com.example.demo.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Autowired
    UserRepository userRepository;

    int entitiesPerPage=50;


    public List<Adult> getAdultPage(int page)
    {

        Pageable pageable= PageRequest.of(page,entitiesPerPage,Sort.by("adultID"));
        return adultRepo.findAll(pageable).getContent();
    }

    public List<Child> getChildPage(int page)
    {

        Pageable pageable= PageRequest.of(page,entitiesPerPage,Sort.by("childID"));
        return childRepo.findAll(pageable).getContent();
    }
    public List<Family> getFamilyPage(int page)
    {

        Pageable pageable= PageRequest.of(page,entitiesPerPage,Sort.by("famID"));
        return familyRepo.findAll(pageable).getContent();
    }
    public List<Friend> getFriendPage(int page)
    {

        Pageable pageable= PageRequest.of(page,entitiesPerPage,Sort.by("id"));
        return friendRepo.findAll(pageable).getContent();
    }


    public void removeFamily(Long id,Long userid)
    {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        if (!isModOrAdmin(user)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        familyRepo.deleteById(id);
    }


    public void addAdult(Adult a,Long id) {
        User user=userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        a.setUser(user);
        if (!isModOrAdmin(user)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        adultRepo.save(a);

    }

    public void addAdult(List<Adult> a) {
        adultRepo.saveAll(a);
    }
    boolean isModOrAdmin(User user)
    {
        return user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
    }
    public void removeAdult(Long id,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        Optional<Adult> ad=adultRepo.findById(id);
        Adult addedBy = new Adult();
        if(ad.isPresent())
            addedBy=ad.get();

        if (!isModOrAdmin(user)&& !Objects.equals(addedBy.getId(), userid)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        adultRepo.deleteById(id);
    }

    public void updateAdult(Long id, Adult adN,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        if (!isModOrAdmin(user)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        Optional<Adult> a = adultRepo.findById(id);
        if (a.isPresent()){
            Adult ad=a.get();
            ad.setAddress(adN.getAddress());
            ad.setAge(adN.getAge());
            ad.setAname(adN.getAname());
            ad.setBirthdate(adN.getBirthdate());
            ad.setEyeColor(adN.getEyeColor());
            adultRepo.save(ad);
        }
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
    public void updateFamily(Long id, Family family,Long userid)
    {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        if (!isModOrAdmin(user)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        Optional<Family> fa=familyRepo.findById(id);
        if (fa.isPresent()) {
            Family f=fa.get();
            f.setDad(family.getDad());
            f.setMom(family.getMom());
            f.setHomeAddress(family.getHomeAddress());
            f.setNrOfMembers(family.getNrOfMembers());
            familyRepo.save(f);
        }

    }
    public void addChild(Child a, Long famid,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        if (!isModOrAdmin(user)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        Optional<Family> fa = familyRepo.findById(famid);
        if(fa.isPresent()) {
            Family f=fa.get();
            a.setFamily(f);
            a.setUser(user);
            childRepo.save(a);
        }
    }

    public void removeChild(Long id,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        if (!isModOrAdmin(user)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        childRepo.deleteById(id);
        friendRepo.deleteById(id);
    }

    public void updateChild(Long p, Child s,Long famid,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        if (!isModOrAdmin(user)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        Child ad=new Child();
        Family f=new Family();
        Optional<Child> c = childRepo.findById(p);
        if(c.isPresent())
            ad=c.get();
        Optional<Family> fa= familyRepo.findById(famid);
        if(fa.isPresent())
        {
           f=fa.get();
        }
        ad.setFamily(f);
        ad.setName(s.name);
        ad.setAge(s.getAge());
        ad.setAddress(s.getAddress());
        ad.setBirthdate(s.getBirthdate());
        ad.setEyeColor(s.getEyeColor());
        childRepo.save(ad);
    }

    public void addChildToFamily(Long id, Long cid,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        if (!isModOrAdmin(user)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        Child c=new Child();
        Family f=new Family();
        Optional<Child> ad = childRepo.findById(cid);
        if(ad.isPresent())
            c=ad.get();
        Optional<Family> fa= familyRepo.findById(id);
        if(fa.isPresent())
        {
            f=fa.get();
        }
        f.addChild(c);
        f.setNrOfMembers(f.getChildren().size()+2);
        familyRepo.save(f);
    }

    public void removeFriend(Long id,Long userid)
    {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        if (!isModOrAdmin(user)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        friendRepo.deleteById(id);
    }
    public Family getFamilyWithID(Long id) {
        Family f=new Family();
        Optional<Family> fa= familyRepo.findById(id);
        if(fa.isPresent())
        {
            f=fa.get();
        }
        return f;
    }

    public Child getChildWithId(Long id) {
        Child ad=new Child();
        Optional<Child> c = childRepo.findById(id);
        if(c.isPresent())
            ad=c.get();
        return ad;
    }


    public void addFriend(Long id1, Long id2, Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        if (!isModOrAdmin(user)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        Child c1=new Child(),c2=new Child();
        Optional<Child> co1 = childRepo.findById(id1);
        if(co1.isPresent())
            c1=co1.get();
        Optional<Child> co2 = childRepo.findById(id2);
        if(co2.isPresent())
            c2=co2.get();
        Friend f = new Friend();
        f.setC1(c1);
        f.setC2(c2);
        f.setUser(user);
        friendRepo.save(f);
    }


    public void addFamily(Family a,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        if (!isModOrAdmin(user)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        a.setUser(user);
        familyRepo.save(a);
    }

    public void addFamily(List<Family> a) {
        familyRepo.saveAll(a);
    }


    public Adult getAdultID(Long id) {
        Optional<Adult> ad=adultRepo.findById(id);
        return ad.orElse(null);
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

        return childRepo.averageChildAge();
    }
    public Long countAdults()
    {

        return adultRepo.count();

    }
    public Long countAdultsAge(int age)
    {
        return (long) adultRepo.getAdultsByAge(age).size();

    }
    public Long countChildren()
    {
        return childRepo.count();

    }
    public Long countFamilies()
    {
        return familyRepo.count();

    }
    public Long countFriends()
    {
        return friendRepo.count();
    }
    public Friend getOneFriend(Long id)
    {
        Optional<Friend> f=friendRepo.findById(id);
        return f.orElse(null);
    }
    public List<Adult> getAdultsByAge(int age,int page)
    {
        Pageable pageable = PageRequest.of(page, entitiesPerPage, Sort.by("adultID"));
        return adultRepo.findByAge(age, pageable);
    }
    public void setPageSize(int ent)
    {
        this.entitiesPerPage=ent;
    }
    public int getPageSize()
    {
        return entitiesPerPage;
    }
}