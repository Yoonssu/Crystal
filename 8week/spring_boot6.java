/*
 1. ORM
ORM은 Object Relational Mapping의 줄임말로 객체 관계 매핑을 의미한다.
자바와 같은 객체지향 언어에서 의미하는 객체(클래스)와 RDB(Relational Database)의 테이블을 자동으로 매핑하는 방법이다.
ORM을 이용하면 쿼리문 작성이 아닌 코드(메서드)로 데이터를 조작할 수 있다.

2. JPA
JPA(Java Persistence API)는 ORM 기술 표준으로 채택된 인터페이스 모음이며, ORM의 구체화된 자바 표준 스펙이다.

JPA의 메커니즘을 보면 내부적으로 JDBC를 사용한다.
JPA는 적절한 SQL을 생성하고 데이터베이스를 조작해서 객체를 자동 매핑하는 역할을 수행한다.
JPA 기반의 구현체는 대표적으로 3가지가 있다.
-하이버네이트
-이클립스 링크
-데이터 뉴클리어스

3. 하이버네이트
Spring Data JPA는 JPA를 편리하게 사용할 수 있도록  지원한다.
CRUD 처리에 필요한 인터페이스를 제공하며, 하이버네이트의 엔티티 매니저(EntityManager)를 직접 다루지 않고 리포지토리를 정의해 사용함으로써 스프링이 적합한 쿼리를 동적으로 생성하는 방식으로 데이터베이스를 조작한다.
이를 통해 자주 사용되는 기능을 더 쉽게 사용할 수 있게 구현한 라이브러리이다.

4. 리포지토리 인터페이스 설계
JpaRepository를 기반으로 더욱쉽게 DB를 사용할 수 있는 아키텍처를 제공한다.
JpaRepository를 상속하는 인터페이스를 생성하면 기존의 다양한 메서드를 손쉽게 활용할 수 있다.

리포지토리를 생성하기 위해서는 접근하려는 테이블과 매핑되는 엔티티에 대한 인터페이스를 생성하고, JpaRepository를 상속받으면 된다.
엔티티를 사용하기 위해서는 대상 엔티티를 Product로 설정하고 해당 엔티티의 @Id 필드 타입인 Long을 설정한다.


5. 리포지토리 메서드의 생성 규칙
FindBy: SQL문의 where 절 역할을 수행하는 구문이다. findBy 뒤에 엔티티의 필드값을 입력해서 사용한다.예) findByName(String name)

AND, OR: 조건을 여러 개 설정하기 위해 사용한다.예) findByNameAndEmail(String name, String email)

Like/NotLike: SQL문의 like와 동일한 기능을 수행하며, 특정 문자를 포함하는지 여부를 조건으로 추가한다. 비슷한 키워드로 Containing, Contains, isContaing이 있습니다.

StartsWith/StartingWith: 특정 키워드로 시작하는 문자열 조건을 설정한다.

EndsWith/EndingWith: 특정 키워드로 끝나는 문자열 조건을 설정한다.

IsNull/IsNotNull: 레코드 값이 Null이거나 Null이 아닌 값을 검색한다.

True/False: Boolean 타입의 레코드를 검색할 때 사용한다.

Before/After: 시간을 기준으로 값을 검색한다.

LessThan/GreaterThan: 특정 값(숫자)을 기준으로 대소 비교를 할 때 사용한다.

Between: 두 값(숫자) 사이의 데이터를 조회한다.



6. DAO 설계
DAO(Data Access Object)는 데이터베이스에 접근하기 위한 로직을 관리하기 위한 객체이다.
비즈니스 로직의 동작 과정에서 데이터를 조작하는 기능은 DAO 객체가 수행한다.
스프링 데이터 JPA에서 DAO의 개념은 리포지토리가 대체하고 있다.
규모가 작은 서비스에서는 DAO를 별도로 설계하지 않고 바로 서비스 레이어에서 데이터베이스에 접근해서 구현하기도 한다.



7. DAO 연동을 위한 컨트롤러와 서비스 설계
설계한 구성 요소들을 클라이언트의 요청과 연결하려면 컨트롤러와 서비스를 생성해야 한다.
먼저 DAO의 메서드를 호출하고 그 외 비즈니스 로직을 수행하는 서비스 레이어를 생성한 후 컨트롤러를 생성해야한다.



 */