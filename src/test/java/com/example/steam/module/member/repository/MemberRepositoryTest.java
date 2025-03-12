package com.example.steam.module.member.repository;

import com.example.steam.module.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;
    @Test
    void deleteTest(){
        Member member = Member.builder()
                .email("email")
                .nickname("nickName")
                .password("password")
                .shoppingCart(null)
                //.deleted(false)
                .build();
        member = memberRepository.save(member);

        member = memberRepository.findById(member.getId()).get();
        System.out.println(member.getId() + " " + member.getDeleted());
        em.flush();
        em.clear();
        memberRepository.delete(member);
        em.flush();
    }
}