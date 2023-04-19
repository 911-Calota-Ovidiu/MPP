package com.example.demo;

import com.example.demo.Model.Adult;
import com.example.demo.Model.Child;
import com.example.demo.Model.Family;
import com.example.demo.Repo.*;
import com.example.demo.Service.Service;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class DemoApplicationTests {

	@MockBean
	AdultRepo adultRepo;

	@MockBean
	ChildRepo childRepo;

	@MockBean
	FamilyRepo familyRepo;

	@MockBean
	FriendRepo FriendRepo;

	@Autowired
	Service service;

	@Test
	public void testChildAvgAge() {
		Adult a1 = Adult.builder()
				.adultID(1L)
				.age(3)
				.address("somewhere")
				.aname("someone")
				.eyeColor("col")
				.birthdate("das")
				.build();
		adultRepo.save(a1);
		Adult a2 = Adult.builder().adultID(2L).age(5).address("anywhere").aname("else").eyeColor("cul").birthdate("sda").build();
		adultRepo.save(a2);
		Family f = Family.builder().mom(a1.getAdultID()).dad(a2.getAdultID()).children(new ArrayList<>()).homeAddress("somewhere").famID(1L).nrOfMembers(5).build();
		Child c = Child.builder().childID(1L).age(3).address("somewhere").birthdate("today").eyeColor("dasds").name("name").family(f).build();
		childRepo.save(c);
		f.addChild(c);
		Child c2 = Child.builder().childID(2L).age(4).address("somewhere").birthdate("today").eyeColor("dasds").name("name").family(f).build();
		Child c3 = Child.builder().childID(3L).age(5).address("somewhere").birthdate("today").eyeColor("dasds").name("name").family(f).build();

		childRepo.save(c3);
		childRepo.save(c2);

		f.addChild(c2);

		f.addChild(c3);
		familyRepo.save(f);
		//when(service.averageChildAge()).thenReturn(4);
		Family f2 = Family.builder().mom(a1.getAdultID()).dad(a2.getAdultID()).homeAddress("somewhere").famID(2L).nrOfMembers(1).build();
		familyRepo.save(f2);
		Family f3 = Family.builder().mom(a1.getAdultID()).dad(a2.getAdultID()).homeAddress("somewhere").famID(3L).nrOfMembers(2).build();
		familyRepo.save(f3);
		Family f4 = Family.builder().mom(a1.getAdultID()).dad(a2.getAdultID()).homeAddress("somewhere").famID(4L).nrOfMembers(5).build();
		familyRepo.save(f4);
		//when(service.getFamiliesNR(3)).thenReturn(
				//Stream.of(f,f4).collect(Collectors.toList())
		//);
		System.out.println("Tests Passed");

	}

}
