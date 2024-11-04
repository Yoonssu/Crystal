/*
 1. AOP Aspect Oriented Programming
- 관점지향 프로그래밍
- 관점을 기준으로 다양한 기능을 분리하여 보는 프로그래밍
- 관점지향 프로그래밍은 객체지향 프로그래밍을 보완하기 위해 쓰인다. 기존 객체(Object) 지향은 목적에 따라 클래스를 만들고 객체를 만들었다.

1) 단점 :  핵심 비즈니스 로직이든, 부가 기능의 로직이든 하나의 객체로 분리하는데 그침		이 기능들을 어떻게 바라보고 나눠쓸지에 대한 정의가 부족함

2) AOP가 필요한 상황
- 컴파일 시점 적용 : .java 파일을 컴파일할 때 부가기능을 넣어서 .class 파일로 컴파일 해주는 것
- 클래스 로딩 시점 적용 : 클래스로더에 .class 파일을 올리는 시점에 바이트 코드 조작해서 부가기능 로직 추가하는 방식
- 런타임 시점 적용 : 프록시를 통해 부가 기능을 적용하는 방식


=> spring AOP는 런타임 시점에 적용하는 방식을 사용한다.
WHY? > 다른 시점에서는 별도의 컴파일러와 클래스로더 조작기를 사용해야함. 번거로움 


3) 주요 개념
aspect : 흩어진 관심사를 모듈화 한 것, 부가기능을 모듈화 함
target : aspect를 적용하는 곳 – 클래스, 메서드
advice : 실질적인 부가기능을 담은 구현체
jointPoint : advice가 적용될 위치
pointcut : jointPoint의 살세한 스펙을 정의한 것

4) 특징
- 프록시 패턴 기반의 aop 구현체 -> 접근 제어 및 부가기능을 추가하기 위해서
- 스프링 빈에만 aop 적용가능


5) 적용
- 공통 관심 사항 vs 핵심 관심 사항 분리
- 원하는 곳에 공통 관심 사항 적용
- 핵심 관심 사항을 깔끔하게 유지할 수 있다.

6) @AOP
- 스프링 @AOP를 사용하기 위해서는 의존성 추가해야함

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>


- @Aspect 어노테이션을 붙여서 이 클래스가 Aspect를 나타내는 클래스라는 것을 명시하고 @Component를 붙여 스프링 빈으로 등록한다.
- 
@Component
@Aspect
public class PerfAspect 
{@Around("execution(* com.saelobi..*.EventService.*(..))")
public Object logPerf(ProceedingJoinPoint pjp) throws Throwable
{long begin = System.currentTimeMillis(); 
Object retVal = pjp.proceed(); // 메서드 호출 자체를 감쌈 System.out.println(System.currentTimeMillis() - begin); 
return retVal; }}

 */