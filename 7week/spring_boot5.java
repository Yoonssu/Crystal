/*
 1. GET API 만들기

GET API는 웹 애플리케이션 서버에서 값을 가져올 때 사용하는 API이다.
컨트롤러 클래스에 @RestController, @RequestMapping 설정


2.@RequestMapping으로 구현하기

@RequestMapping 어노테이션을 별다른 설정없이 선언하면 HTTP의 모든 요청을 받는다. 
GET 형식의 요청을 받기 위한 method 요소 값을 RequestMethod.GET 로 설정해 준다.


3. @PathVariable을 활용한 GET 메서드 구현

웹 통신의 기본 목적은 데이터를 주고 받는 것이기에 대부분 매개변수를 받는 메서드를 작성한다.
매개변수를 받을 때 자주 쓰이는 방법 중 하나인 URL에 값을 담아 전달되는 요청을 처리하는 방법.


4. @RequestParam을 활용한 GET 메서드 구현

쿼리 형식으로 값을 전달할 때 사용한다.
URL 에서 ?를 기준으로 우측에 {키}={값} 형태로 구성된 요청을 전송하는 방법.



5. DTO 객체를 활용한 GET 메서드 구현
 
->dto

Data Transfer Object의 약자로, 다른 레이어 간의 데이터 교환에 활용된다. 
간략하게 설명 하자면 각 클래스 및 인터페이스를 호출하면서 전달하는 매개변수로 사용되는 데이터 객체이다.
DTO에는 private 필드, Getter, Setter만 존재한다.
DTO와 VO의 역할은 서로 엄밀하게 구분하지 않고 사용할 때가 많다.


6. POST API 만들기

POST API는 웹 애플리케이션을 통해 데이터베이스에 리소스를 저장할 때 사용되는 API 이다.
POST API에서는 저장하고자 하는 리소스나 값을 HTTP 바디(body)에 담아 서버에 전달한다.
URI 가 GET API에 비해 간단하다.



7. @RequestBody를 활용한 POST 메서드 구현
 
일반적으로 POST 형식의 요청은 클라이언트가 서버에 리소스를 저장하는 데 사용한다. 그러므로 클라이언트 의 요청 트래픽에 값이 포함되어 있다. 즉, POST 요청에서는 리소스를 담기 위해 HTTP Body에 값을 넣어 전송한다.

Body 영역에 작성되는 값은 일정한 형태를 취기에 일반적으로 JSON(JavaScript Object Notation) 형식으로 전송된다.


8. PUT API 만들기
 
PUT API는 웹 애플리케이션 서버를 통해 데이터베이스 같은 저장소에 존재하는 리소스 값을 업데이트하는 데 사용한다. POST API와 비교하면 요청을 받아 실제 데이터베이스에 반영하는 과정(서비스 로직)에서 차이가 있지만 컨트롤러 클래스를 구현하는 방법은 POST API와 거의 동일하다. 리소스를 서버에 전달하기 위해 HTTP Body를 활용해야 하기 때문이다.

9. ResponseEntity를 활용한 PUT 메서드 구현
 
HttpEntity는 다음과 같이 헤더(Header)와 Body로 구성된 HTTP 요청과 응답을 구성하는 역할을 수행한다.

RequestEntity와 ResponseEntity는 HttpEntity를 상속받아 구현한 클래스이다.
ResponseEntity는 서버에 들어온 요청에 대해 응답 데이터를 구성해서 전달




 */