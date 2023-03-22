package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // ──▶ 순수 단위테스트가 아닌 Spring Integration 을 통해 JUnit4 을 실행
@SpringBootTest              // └─▶ SpringBoot를 띄운상태에서 Spirng 컨테이너 안에서 Test 수행! JPA가 DB까지 접근하는 것을 확인하도록 해보자!
@Transactional               // JPA와 다르게 Spring은 Commit 이 아닌 Rollback 을 수행한다. 따라서 Test 에선 Insert 쿼리가 실행되지 않음.
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
//    @Rollback(value = false) // DB에 Data 들어간걸 확인하고 싶을 때
    public void regist() throws Exception { // Test ─▶ given 이 주어졌을 때 when 을 하면 then 이 되는걸 검증
        //given
        Member member = new Member();
        member.setName("Yoon");

        //when
        Long saveId = memberService.join(member);

        //then
//        em.flush(); // 영속성 컨텍스트에 있는 변경이나 등록내역을 DB에 직접 반영
        assertEquals(member, memberRepository.findOne(saveId)); // 결과가 같으면 회원가입 성공
        // └─▶ JPA 에서 같은 Transactional 안에서 Entity 즉, PK 값이 같으면 영속성 컨텍스트에서 하나로 관리한다.
    }

    @Test(expected = IllegalStateException.class) // try catch 적용
    public void duplicated_user_exception() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("Yoon");

        Member member2 = new Member();
        member2.setName("Yoon");

        //when
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생해야 한다!!

        /*
        try {
            memberService.join(member2); // 예외가 발생해야 한다!!
        } catch (IllegalStateException  e) {
            return;
        }*/
        //then
        fail("예외 발생!");
    }
}