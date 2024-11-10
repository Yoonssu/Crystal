/*


1장  스프링 부트란?


1. 스프링 프레임워크

-  자바를 위한 오픈소스 경량급 애플리케이션 프레임워크로 엔터프라이즈급 애플리케이션을 개발하기 위한 다양한 기능을 제공, 쉽게 사용할 수 있도록 돕는 도구이다.





1.1 제어역전

- (IoC, Inversion of Control)



- 스프링 프레임 워크는 제어역전 기반이다.

- 지금까지의 프로그램을 사용자가 제어하는 구조였다면 제어역전은 제어의 흐름을 사용자가 컨트롤 하지 않고 위임한 특별한 객체에 모든 것을 맡기는 것 이다.

- 이 때문에 의존성 주입(DI; Dependency Injection), 관점 지향 프로그래밍(AOP; Aspect-Oriented Programming) 등이 가능하다.

-  객체 생성 및 연결을 외부 요소에 맡겨 결합도를 낮추고, 코드 유연성을 높인다.





1.2. 의존성 주입 

- (DI, Dependency Injection)



- 제어 역전의 한 방법이다. 

- 의존성 주입 :  객체가 서로 의존하는 관계가 되게 의존성을 주입하는 것

-  IOC에서의 의존성 주입 :  각 클래스 사이에 필요로 하는 의존관계를 빈 설정 정보를 바탕으로 컨테이너가 자동으로 연결해 주는 것





+  의존성을 주입받는 방법





 1.생성자를 통한 의존성 주입



   => 스프링 공식 문서에서 권장



   WHY?  레퍼런스 객체 없이는 객체를 초기화할 수 없게 설계할 수 있기 때문이다.



 @Autowired라는 어노테이션을 통해 의존성을 주입가능. 
@RestController
public class DIContoller {
    MyService myService;	

    
    @Autowired
    public DIContoller(MyService myService) {
        this.myService = myService;
    }

	
@getmapping(“/di/hello”)
    public String getHello() {
return myService.getHello();
    }

}




2. 필드 객체 선언을 통한 의존성 주입

@RestController
public class FieldInjectionController {

    @Autowired
    private MyService myService;

}






 3. setter 메서드를 통한 의존성 주입

@RestController
public class SetterInjectionController {
    MyService myService;
    
    @Autowired
    public void setMyService(MyService myService) {
        this.myService = myService;
    }
}












1.3  스프링 프레임 워크 vs 스프링 부트

- 스프링 프레임워크 :  자바 애플리케이션 개발을 위한 커다란 프레임워크이며 다양한 기능을 제공한다

- 스프링 부트 :   스프링 프레임워크 기반 애플리케이션 개발을 간편하게 만들어주는 도구이며, 필요한 모듈들을 추가하다 보면 설정이 복잡해지는 문제를 해결하며, 빠른 개발과 구성의 간편함을 제공한다.









1.4 의존성 관리



- 스프링 부트에서는 스프링 프레임워크의 개발에 필요한 각 모듈의 의존성을 직접 설정하는 불편함을 해소하기 위해 다양한 의존성을 제공한다.

spring-boot-starter-web: 스프링 MVC를 사용하는 RESTful 애플리케이션을 만들기 위한 의존성이다. 기본으로 내장 톰캣(Tomcat)이 포함돼 있어 jar 형식으로 실행 가능하다.
spring-boot-starter-test: JUnit Jupiter, Mockito 등의 테스트용 라이브러리를 포함한다.
spring-boot-starter-jdbc: HikariCP 커넥션 풀을 활용한 JDBC 기능을 제공한다.
spring-boot-starter-security: 스프링 시큐리티(인증, 권한, 인가 등) 기능을 제한다.
spring-boot-starter-data-jpa: 하이버네이트를 활용한 JPA 기능을 제공한다.
spring-boot-starter-cache: 스프링 프레임워크의 캐시 기능을 지원한다.




- > 스프링 부트의 각 웹 애플리케이션에는 내장 WAS가 존재하며 웹 애플리케이션을 개발할 때 가장 기본이 되는 의존성인 ‘spring-boot-starter-web’의 경우 톰캣을 내장한다.

-> 스프링 부트는 톰캣이 자동 설정한다.















2장  개발에 앞서 알면 좋은 기초 지식


1. 마이크로서비스 아키텍처

- 포털 사이트의 많은 기능들을 단일 서비스로 구성하면 서버 업데이트나 유지보수에 어려움이 있다. 

- 이를 해결하기 위해 마이크로서비스 아키텍처가 제안되었고, 이는 서비스를 작은 단위로 나누어 구성하는 아키텍처를 의미한다. 







2. 스프링부트의 동작 방식





- 스프링 부트에서 spring-boot-starter-web 모듈을 사용하면 기본적으로 톰캣을 사용하는 스프링 MVC 구조를 기반으로 동작한다.




 

- 서블릿 :  클라이언트의 요청을 처리하고 결과를 반환하는 자바 웹 프로그래밍 기술이다.

- > 서블릿 컨테이너에서 관리하며 서블릿 컨테이너에는 톰캣이 있다.



- 서블릿 컨테이너

서블릿 객체를 생성, 초기화, 호출, 종료하는 생명주기를 관리한다.
서블릿 객체는 싱글톤 패턴으로 관리한다.
멀티 스레딩을 지원한다.
 

1) 동작 과정

DispatcherServlet으로 요청(HttpServletRequest)이 들어오면, 핸들러 매핑(Handler Mapping)을 통해 요청 URI에 매핑된 핸들러를 찾는다. 여기서 핸들러는 컨트롤러(Controller)를 의미한다.
그런 다음 핸들러 어댑터(HandlerAdapter)로 컨트롤러를 호출한다.
핸들러 어댑터로부터 컨트롤러의 응답이 돌아오면 ModelAndView로 응답을 가공하여 반환한다.
뷰 형식으로 리턴하는 컨트롤러를 사용할 때는 뷰 리졸버(View Resolver)를 통해 뷰(View)를 받아 반환한다. 핸들러 매핑은 요청 정보를 기준으로 어떤 컨트롤러를 사용할지 선정하는 인터페이스이다. 





3. 레이어드 아키텍처

- 애플리케이션의 컴포넌트를 유사 관심사를 기준으로 레이어로 묶어 수평적으로 구성한 구조를 의미한다.


프레젠테이션 계층 (유저 인터페이스(UI) 계층) : 클라이언트로부터 데이터와 함께 요청을 받고 처리 결과를 응답으로 전달하는 역할. 
비즈니스 계층 (서비스(Service) 계층) : 핵심 비즈니스 로직을 구현하는 영역으로 트랜잭션 처리나 유효성 검사 등의 작업도 수행.
데이터 접근 계층 : 데이터베이스에 접근해야 하는 작업을 수행.  Spring Data JPA에서는 DAO 역할을 리포지토리가 수행하기 때문에 리포지토리로 대체할 수 있다.








4. REST(Representational State Transfer) API

-  대중적으로 가장 많이 사용되는 애플리케이션 인터페이스이다.

- 주고받는 자원에 이름을 규정하고 URI에 명시해 HTTP 메서드(GET, POST, PUT, DELETE)를 통해 해당 자원의 상태를 주고받는 것을 의미한다.



1) REST API

-  REST 아키텍처를 따르는 시스템/애플리케이션 인터페이스이다.







2)  REST 특징



1. 유니폼 인터페이스

- 일관된 인터페이스를 의미한다. 

 

2. 무상태성

- 서버가 상태 정보를 별도로 저장하거나 관리하지 않는다는 것을 의미한다.

 

3. 캐시 가능성

- REST에서는 HTTP 표준을 그대로 활용함으로써 HTTP의 캐싱 기능을 적용할 수 있다.  서버의 트랜잭션 부하를 줄이고 효율성을 높일 수 있다.



4. 레이어 시스템

- REST 서버는 네트워크 상에서 다수의 계층으로 구성될 수 있다.



5. 클라이언트-서버 아키텍처

- REST 서버는 API를 제공하고, 클라이언트는 사용자 정보를 관리하는 구조로 설계된다. 





3) REST의 URI 설계 규칙



1. URI의 마지막에는 ‘/’를 포함하지 않는다.

2. 언더바(_)는 사용하지 않는다. 대신 하이픈(-)을 이용한다.

3. URL에는 행위(동사)가 아닌 결과(명사)를 포함한다.

4. URL은 소문자로 작성해야한다.

5. 파일의 확장자는 URI에 포함하지 않는다.









3장  개발 환경 구성




1. 자바 JDK 설치



https://www.oracle.com/java/technologies/downloads/


Download the Latest Java LTS Free

Subscribe to Java SE and get the most comprehensive Java support available, with 24/7 global access to the experts.

www.oracle.com



2. '고급 시스템 설정' => '시스템 속성' => '환경 변수' 클릭해서  jdk 사용을 위한 환경 변수를 추가한다.





3. cmd에서 자바 설치 확인 (명령어 : java --version)





4. Intellij 설치 



https://www.jetbrains.com/ko-kr/idea/download/#section=windows


최고의 Java 및 Kotlin IDE인 IntelliJ IDEA를 다운로드하세요



www.jetbrains.com















4장 스프링 부트 애플리케이션 개발하기




1. 프로젝트 생성



- IntelliJ  IDEA Ultimate 버전에 내장된 Spring Initializr를 사용하여 프로젝트 생성 가능하다.

- 스프링 공식 사이트에서도 프로젝트 생성이 가능하다.





2. 빌드 관리 도구

- JVM, WAS

- 프로젝트를 인식하고 실행할 수 있게 작성한 소스코드와 프로젝트에 사용된 파일들을 빌드하는 도구이다.

- 빌드 관리 도구를 이용해서 라이브러리 간 버전 호환성 문제를 해결할 수 있다. 





3. pom.xml(Project Object Model) 

- Maven 프로젝트의 기본 설정 파일이다. 

- 이 파일에는 프로젝트 구성, 의존성, 플러그인, 목표 빌드 프로파일 등의 프로젝트와 관련된 모든 정보가 포함된다.





4. 메이븐

- 자바 기반 프로젝트를 빌드하고 관리하는데 사용하는 도구이다.

- pom.xml 파일에 필요한 라이브러리를 추가하면 해당 라이브러리에 필요한 라이브러리까지 같이 내려받아 관리한다.







5. 문장 출력



1) 컨트롤러 작성하기

- 컨트롤러는 사용자 요청처리의 핵심 역할로 MVC 패턴에서 "Controller" 부분을 차지한다.



package com.example.hello.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}


컨트롤러에서 수행되는 주요 작업

사용자의 요청을 받음.
요청에 맞는 모델(Model)의 메서드나 기능을 호출.
모델의 반환 값을 처리하여 사용자에게 표시할 결과를 생성.
결과를 뷰에 전달하여 사용자에게 웹 페이지를 표시.




6. 애플리케이션 실행




7. 웹 브라우저를 통한 동작 테스트






 */