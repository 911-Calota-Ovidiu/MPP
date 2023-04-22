
package com.example.demo.Service;

import com.example.demo.Model.*;
import com.example.demo.Repo.AdultRepo;
import com.example.demo.Repo.ChildRepo;
import com.example.demo.Repo.FamilyRepo;
import com.example.demo.Repo.FriendRepo;
import com.example.demo.Model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

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
    public List<AdultDTO> gettenadultsNEXT()
    {
        List<AdultDTO> rList=new ArrayList<>();
        Long index=0L;
        for(Long i=startadult;i<startadult+10;i++)
        {
            index=i;
            if(i+1>adultRepo.findAll().size()) break;
            Adult a=adultRepo.findById(i).get();
            rList.add(new AdultDTO(a.getName(),a.getAddress(),a.getAge()));
        }
        if(index+1>familyRepo.findAll().size()) {startfamily=0L; return null;}
        startadult+=10;
        return rList;
    }
    public List<AdultDTO> adultFirstPage(){
        startadult=0L;
        return gettenadultsNEXT();
    }
    public List<ChildDTO> gettenchildrenNEXT()
    {
        Long index=0L;
        List<ChildDTO> rList=new ArrayList<>();
        for(Long i=startchild;i<startchild+10;i++)
        {
            index=i;
            if(i+1>childRepo.findAll().size()) break;
            Child c=childRepo.findById(i).get();
            rList.add(new ChildDTO(c.name,c.getAddress(),c.getFamily().getId()));
        }
        if(index+1>familyRepo.findAll().size()) {startfamily=0L; return null;}
        startchild+=10;
        return rList;
    }
    public List<FamilyDTO> gettenfamiliesNEXT()
    {
        List<FamilyDTO> rList=new ArrayList<>();
        Long index=0L;
        for(Long i=startfamily;i<startfamily+10;i++)
        {
            index=i;
            if(i+1>familyRepo.findAll().size()) break;
            Family f=familyRepo.findById(i).get();
            rList.add(new FamilyDTO(f.getMom(),f.getDad(),f.getHomeAddress()));

        }
        startfamily+=10;
        if(index+1>familyRepo.findAll().size()) {startfamily=0L; return null;}
        return rList;
    }
    public List<Friend> gettenfriendsNEXT()
    {
        List<Friend> rList=new ArrayList<>();
        Long index=0L;
        for(Long i=startfriend;i<startfriend+10;i++)
        {
            index=i;
            if(i+1>friendRepo.findAll().size()) break;
            rList.add(friendRepo.findById(i).get());
        }
        if(index+1>familyRepo.findAll().size()) {startfamily=0L; return null;}
        startfriend+=10;
        return rList;
    }

    public List<AdultDTO> gettenadultsPREV()
    {
        List<AdultDTO> rList=new ArrayList<>();
        startadult-=20;
        if(startadult<0){startadult=0L;}
        for(Long i=startadult;i<startadult+10;i++)
        {
            if(i+1>adultRepo.findAll().size()) break;
            Adult a=adultRepo.findById(i).get();
            rList.add(new AdultDTO(a.getName(),a.getAddress(),a.getAge()));
        }

        return rList;
    }
    public List<ChildDTO> gettenchildrenPREV()
    {
        List<ChildDTO> rList=new ArrayList<>();
        startchild-=20;
        if(startchild<0){startchild=0L; return null;}
        for(Long i=startchild;i<startchild+10;i++)
        {
            if(i+1>childRepo.findAll().size()) break;
            Child c=childRepo.findById(i).get();
            rList.add(new ChildDTO(c.name,c.getAddress(),c.getFamily().getId()));
        }

        return rList;
    }
    public void removeFamily(Long id)
    {
        familyRepo.deleteById(id);
    }
    public List<FamilyDTO> gettenfamiliesPREV()
    {
        List<FamilyDTO> rList=new ArrayList<>();
        startfamily-=20;
        if(startfamily<0){startfamily=0L; return null;}
        for(Long i=startfamily;i<startfamily+10;i++)
        {
            if(i+1>familyRepo.findAll().size()) break;
            Family c=familyRepo.findById(i).get();
            rList.add(new FamilyDTO(c.getMom(),c.getDad(),c.getHomeAddress()));
        }
        return rList;
    }
    public List<Friend> gettenfriendsPREV()
    {
        List<Friend> rList=new ArrayList<>();
        startfriend-=20;
        if(startfriend<0){startfriend=0L; return null;}
        for(Long i=startfriend;i<startfriend+10;i++)
        {
            if(i+1>friendRepo.findAll().size()) break;
            Friend c=friendRepo.findById(i).get();
            rList.add(c);
        }
        return rList;
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

    public void updateChild(Pair<Long, String> p) {
        Child ad = childRepo.findById(p.first).get();
        ad.setAddress(p.second);
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

    public int averageChildAge() {
        int age = 0, sum = 0;
        List<Child> children = childRepo.findAll();
        for (Child c : children) {
            age += c.getAge();
            sum++;
        }
        return age / sum;
    }
}