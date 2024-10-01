// 3. 회원 관리 예제





// 1) 웹 애플리케이션 기본 구조






// -> 컨트롤러: 웹 MVC의 컨트롤러 역할이다.

// -> 서비스: 핵심 비즈니스 로직 구현한다.

// -> 리포지토리: 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리한다.

// -> 도메인: 비즈니스 도메인 객체, 예) 회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 관리된다.







// 2) 회원 관리 예제의 비즈니스 요구사항 정리

// - 회원 아이디, 회원 이름

// - 회원 등록, 조회 기능  











// 3) 클래스 의존 관계














// 4) 회원 객체

//  package hello.hellospring.domain;
 
//      public class Member {
//          private Long id;
//          private String name;
//      public Long getId() {
//          return id;
//     }
//      public void setId(Long id) {
//          this.id = id;
//     }
//     public String getName() {
//          return name;
//     }
//      public void setName(String name) {
//          this.name = name;
//      }
//  }


// - Member 클래스에서는 id와 name을 설정한다.





// 5) 회원 리포지토리 인터페이스

// - 회원을 등록하기 위한 리포지토리 작성

//  package hello.hellospring.repository;
 
//  import hello.hellospring.domain.Member;
//  import java.util.List;
//  import java.util.Optional;
 
//  public interface MemberRepository {
//      Member save(Member member);
//      Optional<Member> findById(Long id);
//      Optional<Member> findByName(String name);
//      List<Member> findAll();
//  }








// 6) 회원 리포지토리 메모리 

// - 회원을 조회하기 위한 리포지토리 작성

// package hello.hellospring.repository;

//  import hello.hellospring.domain.Member;
//  import java.util.*;
//  /**
//  * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
//  */
 
//  public class MemoryMemberRepository implements MemberRepository {
//     private static Map<Long, Member> store = new HashMap<>();
//     private static long sequence = 0L;
    
//     @Override
//      public Member save(Member member) {
//             member.setId(++sequence);
//             store.put(member.getId(), member);
//      		return member;
//         }
        
//     @Override
//          public Optional<Member> findById(Long id) {
//          return Optional.ofNullable(store.get(id));
//     }
    
//     @Override
//          public List<Member> findAll() {
//          return new ArrayList<>(store.values());
//     }
    
//     @Override
//          public Optional<Member> findByName(String name) {
//          return store.values().stream()
//                 .filter(member -> member.getName().equals(name))
//                 .findAny();
//     }
    
//      public void clearStore() {
//             store.clear();
//         }
//  }




// 7) 회원 서비스 개발

//  package hello.hellospring.service;
// import hello.hellospring.domain.Member;
// import hello.hellospring.repository.MemberRepository;
// import java.util.List;
// import java.util.Optional;

//  public class MemberService {
//      private final MemberRepository memberRepository = new 
//     MemoryMemberRepository();
//     /**
//      * 
// 회원가입
//      */
//      public Long join(Member member) {
//          validateDuplicateMember(member); //중복 회원 검증
//          memberRepository.save(member);
//          return member.getId();
//         }
//      private void validateDuplicateMember(Member member) {
//             memberRepository.findByName(member.getName())
//                     .ifPresent(m -> {
//      		throw new IllegalStateException("이미 존재하는 회원입니다.");
//                     });
//         }
//         /**
//          * 
//     전체 회원 조회
//          */
//      public List<Member> findMembers() {
//          return memberRepository.findAll();
//             }
//      public Optional<Member> findOne(Long memberId) {
//          return memberRepository.findById(memberId);
//             }
//  }






// 7) 테스트 코드 작성

// - 코드를 작성한 후에는 올바르게 실행되는지 테스트코드를 작성하여 확인한다. 



// 7.1) 회원 리포지토리 테스트 코드 작성

//  package hello.hellospring.repository;
//  import hello.hellospring.domain.Member;
//  import org.junit.jupiter.api.AfterEach;
//  import org.junit.jupiter.api.Test;
//  import java.util.List;
//  import java.util.Optional;
//  import static org.assertj.core.api.Assertions.*;
 
//  class MemoryMemberRepositoryTest {
//  	MemoryMemberRepository repository = new MemoryMemberRepository();
//     @AfterEach
//      public void afterEach() {
//             repository.clearStore();
//         }
        
        
//         @Test
//      public void save() {
//          //given
//          Member member = new Member();
//                 member.setName("spring");
//          //when
//                 repository.save(member);
//          //then
//          Member result = repository.findById(member.getId()).get();
//          assertThat(result).isEqualTo(member);
//       }
      
//         @Test
//      public void findByName() {
//          //given
//          Member member1 = new Member();
//                 member1.setName("spring1");
//                 repository.save(member1);
//          Member member2 = new Member();
//                 member2.setName("spring2");
//                 repository.save(member2);
//          //when
//          Member result = repository.findByName("spring1").get();
//          //then
//          assertThat(result).isEqualTo(member1);
//         }
        
//         @Test
//      public void findAll() {
//          //given
//          Member member1 = new Member();
//                 member1.setName("spring1");
//                 repository.save(member1);
//          Member member2 = new Member();
//                 member2.setName("spring2");
//                 repository.save(member2);
//          //when
//          List<Member> result = repository.findAll();
//          //then
//          assertThat(result.size()).isEqualTo(2);
//         }
//  }


// - @AfterEach : 테스트를 실행할 때마다 DB에 테스트의 결과가 남아있는데, 이럴 경우 다음 테스트가 실패할 가능성이 있다. @AfterEach를 사용하면 DB에 저장된 데이터를 각 테스트가 종료될 때마다 삭제할 수 있다.





// 7.2) 회원 서비스 테스트 

// package hello.hellospring.service;
//  import hello.hellospring.domain.Member;
//  import hello.hellospring.repository.MemoryMemberRepository;
//  import org.junit.jupiter.api.BeforeEach;
//  import org.junit.jupiter.api.Test;
//  import static org.assertj.core.api.Assertions.*;
//  import static org.junit.jupiter.api.Assertions.*;
 
 
//  class MemberServiceTest {
//      MemberService memberService;
//      MemoryMemberRepository memberRepository;
     
//     @BeforeEach
//     public void beforeEach() {
//                 memberRepository = new MemoryMemberRepository();
//                 memberService = new MemberService(memberRepository);
//             }
            
//     @AfterEach
//     public void afterEach() {
//                 memberRepository.clearStore();
//     }
    
    
//    	 @Test
//      public void 회원가입() throws Exception {
//          //Given
//          Member member = new Member();
//                 member.setName("hello");
//          //When
//          Long saveId = memberService.join(member);
//          //Then
//          Member findMember = memberRepository.findById(saveId).get();
//          assertEquals(member.getName(), findMember.getName());
//     }
    
//     @Test
//  	public void 중복_회원_예외() throws Exception {
//          //Given
//          Member member1 = new Member();
//                 member1.setName("spring");
//          Member member2 = new Member();
//                 member2.setName("spring");
//          //When
//                 memberService.join(member1);
//          IllegalStateException e = assertThrows(IllegalStateException.class,
//                         () -> memberService.join(member2));//예외가 발생해야 한다.
//          assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//             }
//  }


// - @BeforeEach : 각 테스트 전에 호출되어 테스트가 서로 영향이 없도록 항상 새로운 객체를 생성하고, 의존 관계도 새로 맺어준다.

// */