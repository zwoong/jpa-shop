package jpabook.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired MemberService memberService;
  @Autowired MemberRepository memberRepository;

  @Test
  public void 회원가입() throws Exception {
    // given
    Member member = new Member();
    member.setName("Choi");

    // when
    Long saveId = memberService.join(member);

    // then
    assertEquals(member, memberRepository.findOne(saveId));
  }

  @Test
  public void 중복_회원_예외() throws Exception {
    // given
    Member member1 = new Member();
    member1.setName("Choi");

    Member member2 = new Member();
    member2.setName("Choi");

    // when
    memberService.join(member1);
    try {
      memberService.join(member2);
    } catch (IllegalStateException e) {
      return;
    }

    // then
    fail("예외가 발생해야 한다.");
  }
}