package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // @PersistenceContext = @Autowired
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
        // 1. persist -> 영속성 Context에 "member"객체 주입
        // 2. Transation 이 Commit 되는 시점에 DB에 insert 쿼리 날림 :: JPA
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
        // 1. JPA find() 메소드 | 단건 조회 Param : Type, PK
    }

    public List<Member> findAll() {
        // Ctrl + Alt + N : 인라인 변수 단축키
        // JPQL : Entity 객체 대상으로 쿼리
        //  SQL : Table 대상으로 쿼리 실행
        return em.createQuery("select m from Member m", Member.class) 
                .getResultList();
    }

    public List<Member> findByName(String name) {
        // Parameter Binding "name"
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
