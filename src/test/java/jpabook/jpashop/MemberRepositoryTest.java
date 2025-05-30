package jpabook.jpashop;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import jpabook.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {

  @Autowired MemberRepository memberRepository;

  @Test
  @Transactional
  @Rollback(false)
  public void testMember() throws Exception {
    // given
    Member member = new Member();
    member.setName("memberA");

    // when
    Long savedId = memberRepository.save(member);
    Member findMember = memberRepository.find(savedId);

    // then
    assertThat(findMember.getId()).isEqualTo(member.getId());
    assertThat(findMember.getName()).isEqualTo(member.getName());
    assertThat(findMember).isEqualTo(member);
    System.out.println("findMember == member: " + (findMember == member));
  }
}