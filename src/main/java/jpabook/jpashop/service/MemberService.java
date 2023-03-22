package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service                 // 어노테이션에 기본적으로 Component가 포함되어 있으므로 ComponentScan 대상 ─▶ 자동으로 Spring Bean 에 등록
@Transactional           // ─▶ JPA의 모든 데이터 변경이나 로직들은 Transaction 안에서 실행되어야 한다.
@RequiredArgsConstructor // ─▶ final 선언된 Fleid 만 생성자를 만들어줌.
public class MemberService { // Ctrl + Shift + T : Test 생성

    // @Autowired
    // └─▶ Spring Bean 에 등록된 MemberRepository 를 주입 : Field Injection
    // └─▶ 변경 불가
    private final MemberRepository memberRepository;

    /*
    public void setMemberRepository(MemberRepository memberRepository) {
     └─▶ App Loading 시점에 세팅완료 : 그 이후 set을 통해 설정하는건 좋지않음
        this.memberRepository = memberRepository;
    }

    public MemberService(MemberRepository memberRepository) {
       └─▶ 생성자가 하나일 경우 자동으로 주입한다. | final : Complie 시점에 체크 가능
        this.memberRepository = memberRepository;
    }
    */

    /**
     * 회원 가입
     */
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        // Exception
    }

    // 회원 전체조회
    @Transactional(readOnly = true)
    // └─▶ 읽기에서 readOnly 옵션을 달아주면 JPA가 읽기전용으로 성능을 최적화함
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
