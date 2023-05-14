
package com.example.demo.Service;

import com.example.demo.Exception.UserNotAuthorizedException;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Model.*;
import com.example.demo.Repo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        Pageable pageable= PageRequest.of(page,entitiesPerPage,Sort.by("id"));
        return friendRepo.findAll(pageable).getContent();
    }


    public void removeFamily(Long id,Long userid)
    {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        familyRepo.deleteById(id);
    }


    public void addAdult(Adult a,Long id) {
        User user=userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        a.setUser(user);
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        adultRepo.save(a);

    }

    public void addAdult(List<Adult> a) {
        adultRepo.saveAll(a);
    }

    public void removeAdult(Long id,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        User addedBy=adultRepo.findById(id).get().getUser();

        if (!userOrModOrAdmin&& !Objects.equals(addedBy.getId(), userid)) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        adultRepo.deleteById(id);
    }

    public void updateAdult(Long id, Adult adN,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        Adult ad = adultRepo.findById(id).get();
        ad.setAddress(adN.getAddress());
        ad.setAge(adN.getAge());
        ad.setAname(adN.getAname());
        ad.setBirthdate(adN.getBirthdate());
        ad.setEyeColor(adN.getEyeColor());
        adultRepo.save(ad);
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
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        Family f=familyRepo.findById(id).get();
        f.setDad(family.getDad());
        f.setMom(family.getMom());
        f.setHomeAddress(family.getHomeAddress());
        f.setNrOfMembers(family.getNrOfMembers());
        familyRepo.save(f);

    }
    public void addChild(Child a, Long famid,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        Family f = familyRepo.findById(famid).get();
        a.setFamily(f);
        a.setUser(user);
        childRepo.save(a);

    }

    public void addChild(List<Child> a) {
        childRepo.saveAll(a);

    }

    public void removeChild(Long id,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        childRepo.deleteById(id);
        friendRepo.deleteById(id);
    }

    public void updateChild(Long p, Child s,Long famid,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
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

    public void addChildToFamily(Long id, Long cid,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
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
    public void removeFriend(Long id,Long userid)
    {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
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


    public void addFriend(Long id1, Long id2, Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        Child c = childRepo.findById(id1).get();
        Child c1 = childRepo.findById(id2).get();
        Friend f = new Friend();
        f.setC1(c);
        f.setC2(c1);
        f.setUser(user);
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


    public void addFamily(Family a,Long userid) {
        User user=userRepository.findById(userid).orElseThrow(() -> new UserNotFoundException(userid));
        boolean userOrModOrAdmin = user.getRoles().stream().anyMatch((role) ->
                role.getName() == ERole.ROLE_ADMIN
                        || role.getName() == ERole.ROLE_MODERATOR
                        || role.getName() == ERole.ROLE_USER
        );
        if (!userOrModOrAdmin) {
            throw new UserNotAuthorizedException(String.format(user.getUsername()));
        }
        a.setUser(user);
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
        return friendRepo.findById(id).get();
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