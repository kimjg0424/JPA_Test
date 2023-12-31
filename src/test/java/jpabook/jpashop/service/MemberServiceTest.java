package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired
	MemberService memberService;

	@Autowired
	MemberRepository memberRepository;


	@Test
	@DisplayName("회원가입")
	public void join() throws Exception {
		//Given
		Member member = new Member();
		member.setName("kim");
		//When
		Long saveId = memberService.join(member);
		Member resultMember = memberRepository.findOne(saveId);
		//Then
		assertThat(member).isEqualTo(resultMember);
	}

	@DisplayName("중복회원예외")
	@Test
	void duplicateMemberException() throws Exception{
	    //Given
		Member member1 = new Member();
		member1.setName("kim");
		Member member2 = new Member();
		member2.setName("kim");
		//When
	    memberService.join(member1);
	    //Then
		assertThatThrownBy(() ->memberService.join(member2))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("이미 존재하는 회원입니다.");
	}





}