/*
 
1. RestTemplate
RestTemplate이란 HTTP 서버와의 통신을 단순화한 스프링에서 제공하는 템플릿으로,

RESTful 원칙을 따르는 서비스를 편리하게 만들 수 있게 도와준다.

기본적으로 동기 방식으로 지원하며, AsyncRestTemplate을 이용하면 비동기 방식으로 활용할 수 있다.

단 RestTemplate은 현업에선 주로 사용되지만 지원 중단(deprecated)된 상태이므로 향후 대체하게 될 WebClient 방식도 함께 숙지해두어야 한다.

 

2. RestTemplate의 특징

HTTP 프로토콜 메서드에 맞는 여러 메서드 제공
RESTful 형식을 갖춤
HTTP 요청으로 JSON, XML, 문자열 등 다양한 형식의 응답을 받을 수 있음
블로킹(blocking) I/O 기반의 동기 방식
다른 API 호출 시 HTTP 헤더에 다양한 값 설정 가능


3. 동작 원리

(1) 우선 Application에서 시작하며, Application은 개발자가 작성하는 코드부를 의미한다. 개발자는 코드에서 REST API에의 요청을 위해 RestTemplate을 선언하고 URI와 HTTP 메서드, Body 등을 설정한다.
(2) RestTemplate은 요청을 위해 HttpMessageConverter를 통해 RequestEntity를 요청 메세지로 변환한다. RequestEntity란 자바 객체를 request body에 담을 메세지 형태(JSON 등)를 의미한다.
(3) RestTemplate은 ClientHttpRequestFactory에서 ClientHttpRequest 객체를 만들어 (2)에서 만든 메세지를 전달한다.
(4) (3)에서 만들어진 ClientHttpRequest 객체가 실질적으로 HTTP 통신의 요청을 수행하며, 외부 API로 요청을 보낸다.
(5) 요청에 대한 응답을 외부에서 받게 되면, RestTemplate은 ResponseErrorHandler로 오류를 확인한다.
(6) ClientHttpResponse에서 응답 데이터를 받으며, 오류가 있을 시 처리한다.
(7) 응답 데이터가 정상이라면 HttpMessageConverter에 응답 데이터를 전달하여 자바 객체로 변환한다.
(8) 변환된 자바 객체를 Application에 전달한다.



4. WebClient란

일반적으로 실제 운영환경의 애플리케이션은 정식 버전의 스프링 부트 버전보다 낮으므로 RestTemplate을 주로 사용한다.
하지만 스프링이 최신 버전으로 넘어오며 RestTemplate의 지원이 중단되고 WebClient 사용을 권고하고 있으므로, 
빈번히 사용되고 있는 RestTemplate과 WebClient를 모두 알고 있는 것이 중요하다.

Spring WebFlux는 HTTP 요청을 수행하는 클라이언트로 WebClient를 제공한다. WebClient는 리액터(Reactor) 기반의 API인데, 이때 리액터란 Reactive Programming으로 함수형 프로그래밍과 흡사한 스트림 특징의 방식이며 비동기 형식의 사용이 가능해진다.


5. WebClient의 특징

논블로킹(Non-Blocking) I/O 지원
리액티브 스트림(Reactive Streams)의 백 프레셔(Back Pressure) 지원
적은 하드웨어 리소스로 동시성 지원
함수형 API 지원
동기, 비동기 상호작용 지원
스트리밍 지원


6. WebClient 구현
package com.springboot.rest.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientService {

    public String getName() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:9090")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri("/api/v1/crud-api")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getNameWithPathVariable() {
        WebClient webClient = WebClient.create("http://loaclhost:9090");

        ResponseEntity<String> responseEntity = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/crud-api/{name}").build("Flature"))
                .retrieve().toEntity(String.class).block();

        return responseEntity.getBody();
    }

    public String getNameWithParameter() {
        WebClient webClient = WebClient.create("http://localhost:9090");

        return webClient.get().uri(uriBuilder -> uriBuilder.path("/api/v1/crud-api")
                    .queryParam("name", "Flature")
                    .build())
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode().equals(HttpStatus.OK))
                        return clientResponse.bodyToMono(String.class);
                    else
                        return clientResponse.createException().flatMap(Mono::error);

                    })
                .block();
    }
}


7. post 요청방식
public ResponseEntity<MemberDto> postWithParamAndBody() {
    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:9090")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    MemberDto memberDto = MemberDto.builder()
            .name("flature!!")
            .email("flature@gmail.com")
            .organization("Around Hub Studio")
            .build();

    return webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path(("/api/v1/crud-api"))
                .queryParam("name", "Flature")
                .queryParam("email", "flature@wikibooks.co.kr")
                .queryParam("organization", "Wikibooks")
                .build())
            .bodyValue(memberDto)
            .retrieve()
            .toEntity(MemberDto.class)
            .block();
}

public ResponseEntity<MemberDto> postWithHeader() {
    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:9090")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    MemberDto memberDto = MemberDto.builder()
            .name("flature!!")
            .email("flature@gmail.com")
            .organization("Around Hub Studio")
            .build();

    return webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path("api/v1/crud-api/add-header").build())
            .bodyValue(memberDto)
            .header("my-header", "Wikibooks API")
            .retrieve()
            .toEntity(MemberDto.class)
            .block();
}
 
 */