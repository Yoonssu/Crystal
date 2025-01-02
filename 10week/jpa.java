/*
 1. JPQL
- JPA Query Language으로 JPA에서 사용할 수 있는 쿼리를 의미한다.
- SQL문과의 차이점은 SQL은 테이블이나 칼럼의 이름을 사용하지만 JPQL은 매핑된 엔티티의 이름과 필드를 사용한다.







2. 쿼리 메서드


2.1. 쿼리 메서드 생성
- 쿼리 메서드는 동작을 결정하는 주제(Subject)와 검색 및 정렬 조건 지정하는 영역인 서술어(Predicate)로 구분한다.
- 리포지토리는 JpaRepository를 상속받는 것만으로도 다양한 CRUD 메서드를 제공 하지만 기본 메서드들은 식별자 기반    으로 생성되므로 쿼리 메서드와 같이 별도의 메서드를 정의해서 사용한다.

- 쿼리 주제 : ‘find…By’, ‘exists…By’, ‘get…By’와 같은 키워드
- 서술어의 시작 : ‘By’




2.1.1. 키워드 예시 
1) 조회 키워드

1. exists...By  : 특정 데이터가 존재하는지 확인하는 키워드이다. 
boolean existsByNumber(Long number);


2. count...By : 조회 쿼리 결과로 나온 레코드의 개수를 리턴한다.
long countByName(String name);


3. delete...By, remove...By : 삭제 쿼리를 수행한다. 리턴 타입이 없거나 삭제한 횟수를 리턴한다.
void deleteByNumber(Long number);
long removeByName(String name);


4. ...First<Number>..., ...Top<Number>... : 조회된 결괏값의 개수를 제한하는 키워드이다.
( 한 번에 여러 건을 조회할 때 사용되며, 단 건으로 조회하기 위해서는 <Number>를 생략하면 된다.)
List<Product> findFirst5ByName(String name);
List<Product> findTop10ByName(String name);







2) 조건자 키워드
1. Is : 값의 일치를 조건으로 사용하는 키워드이다. 
2. (Is)Not : 값의 불일치를 조건으로 사용하는 키워드이다.
3. (Is)Null, (Is)NotNull : 값이 null인지 검사하는 키워드이다.
4. (Is)True, (Is)False : boolean 타입으로 지정된 칼럼값을 확인하는 키워드이다. 
5. And, Or : 여러 조건을 묶을 때 사용한다.
6. (Is)GreaterThan, (Is)LessThan, (Is)Between : 숫자나 datetime 칼럼을 대상으로 한 비교 연산에 사용할 수 있는 키워드이다.
7. (Is)StartingWith(==StartsWith), (Is)EndingWith(==EndsWith), (Is)Containing(==Contains), (Is)Like : 칼럼값에서 일부 일치 여부를 확인하는 키워드이다.
















3. 정렬과 페이징 처리


3.1. 정렬 처리

1. OrderBy
List<Product> findByNameOrderByNumberAsc(String name);
List<Product> findByNameOrderByNumberDesc(String name);






2. 우선순위 기준으로 작성

List<Product> findByNameOrderByPriceAscStockDesc(String name);






3. 매개변수 활용

List<Product> findByName(String name, Sort sort);






4. Sort 객체 활용

productRepository.findByName("펜",Sort.by(Order.asc("price")));
productRepository.findByName("펜",Sort.by(Order.asc("price"),Orderdesc("stock")));






3.2. 페이징 처리
페이징 : 데이터베이스의 레코드를 개수로 나눠 페이지를 구분하는 것을 의미한다.
- JPA에서는 페이징 처리를 위해 Page와 Pageable을 사용한다.






// 페이징 처리를 위한 쿼리 메서드 예시
Page<Product> findByName(String name, Pageable pageable);


// 페이징 쿼리 메서드를 호출하는 방법
Page<Product> productPage = productRepository.findByName("펜", PageRequest.of(0, 2));


// Page 객체의 데이터 출력
Page<Product> productPage = productRepository.findByName("펜", PageRequest.of(0, 2));
System.out.println(productPage.getContent());






3.2.1. PageRequest
- Pageable 파라미터를 전달하기 위해 PageRequest 클래스를 사용한다.
- PageRequest는 of 메서드를 통해 PageRequest 객체를 생성한다.




※ of 메서드
of 메서드	매개변수 설정	비고
of(int page, int size)	페이지 번호(0부터 시작), 페이지당 데이터 개수	데이터를 정렬하지 않음
of(int page, int size, Sort)	페이지 번호, 페이지당 데이터 개수, 정렬	sort에 의해 정렬
of(int page, int size, Direction, String... properties)	페이지 번호, 페이지당 데이터 개수, 정렬 방향, 속성(칼럼)	Sort.by(direction,properties)에 의해 정렬




















4. @Query 어노테이션 사용하기
- 데이터 베이스에서 값을 가져올 때 @Query 어노테이션을 사용해 직접 JPQL을 작성할  수 있다. 


@Query("SELECT p FROM Product p WHERE p.name = :name")
List<Product> findByNameParam(@Param("name") String name);


-> @Query를 사용하면 엔티티 타입이 아니라 원하는 칼럼의 값만 추출할 수 있다.


















5. QueryDSL 적용하기


5.1. QueryDSL
- QueryDSL은 정적 타입을 이용해 SQL과 같은 쿼리를 생성할 수 있도록 지원하는 프레임워크다. 
-> @Query를 사용하면 직접 문자열을 입력하기 때문에 컴파일 시점에 에러를 잡지 못하고 런타임 에러가 발생할 수 있다. 이러한 문제점을 해결하기 위해서 QueryDSL을 사용한다. 


5.2.QueryDSL의 장점
-  IDE가 제공하는 코드 자동 완성 기능을 사용할 수 있다.
-  문법적으로 잘못된 쿼리를 허용하지 않는다. 따라서 정상적으로 활용된 QueryDSL은 문법 오류를 발생 시키지 않는다.
- 고정된 SQL 쿼리를 작성하지 않기 때문에 동적으로 쿼리를 생성할 수 있다.
-  코드로 작성하므로 가독성 및 생산성이 향상된다.
-  도메인 타입과 프로퍼티를 안전하게 참조할 수 있다.





 */