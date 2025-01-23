/*

1. 유효성 검사사

유효성 검사(validation) 혹은 데이터 검증이란 애플리케이션의 비즈니스 로직 수행 중
각 계층에서 다른 계층으로 전달되는 데이터가 올바르게 전달되는지 사전 검증하는 과정이다. 
자바에서 가장 중요하게 생각해야 할 유효성 검사의 일례로는 NullPointException 예외가 있다.


2. 유효성 검사를 위한 대표적인 어노테이션

- 문자열 검증

@Null : null만 허용.
@NotNull : null 허용 x. "", " "는 허용.
@NotEmpty : null, "" 허용 x.
NotBlank : null, "", " " 허용 x
최댓값 / 최솟값 검증

BigDecimal, BigInteger, int, long 타입 등 지원.
@DecimalMax(value = "$numberString") : $numberString보다 작은 값을 허용.
@DecimalMin(value = "$numberString") : $numberString보다 큰 값을 허용.
@Min(value = $number) : $number 이상의 값 허용.
@Max(value = $number) : $number 이하의 값 허용.
값의 범위 검증

BigDecimal, BigIntteger, int, long 등의 타입 지원.
@Positive : 양수 허용.
@PositiveOrZero : 0을 포함한 양수 허용.
@Negative : 음수 허용.
@NegativeOrZero : 0을 포함한 음수 허용.
시간에 대한 검증

Date, LocalDate, LocalDateTime 등의 타입 지원.
@Future : 현재보다 미래의 날짜를 허용.
@FutureOrPresent : 현재를 포함한 미래의 날짜 허용.
@Past : 현재보다 과거의 날짜를 허용.
@PastOrPresent : 현재를 포함한 과거의 날짜 허용.
이메일 검증

@Email : 이메일 형식을 검사. ""는 허용.
자릿수 범위 검증

BigDecimal, BigInteger, int, long 등의 타입을 지원.
@Digits(integer = $number1, fraction = $number2) : $numbe1의 정수 자릿수와 $number2의 소수 자릿수를 허용.
Boolean 검증

@AssertTrue : true인지 체크. null은 체크 X.
@AssertFalse : false인지 체크. null값은 체크 X.
문자열 길이 검증

@Size(min = $number1, max = $number2) : $number1 이상 $number2 이하의 범위를 허용.
정규식 검증

@Pattern(regexp = "$expression") : 정규식을 검사. 정규식은 자바의 java.util.regex.Pattern 패키지의 컨벤션을 따름.





3. 예외처리

예외(exception)란 입력 값의 처리 불능이나 잘못된 참조값 등으로 애플리케이션의 정상적인 동작을 방해하는 요소로,
이는 개발자가 미리 코드를 통해 예방할 수 있는 부분이다.


에러(error)는 주로 자바의 가상머신에서 발생시키는 것으로 메모리 부족(OutOrMemory), 
스택 오버플로(StackOverFlow) 등의 애플리케이션 코드에서 처리가 거의 불가능한 부분을 일컫는다.
에러는 발생 시점의 처리가 어렵기 때문에 미리 애플리케이션 코드를 살펴보며 문제를 원천적으로 예방해야 한다.



4. 예외처리 방법

- 예외 복구
예외 상황을 파악하여 문제를 해결하는 방법. (try-catch)
예외 상황이 발생하면 애플리케이션은 여러 catch를 순차적으로 순회하며 매칭되는 예외 처리 동작을 수행한다.

- 예외 처리 회피
예외가 발생한 메서드를 호출한 곳으로 에러 처리를 전가하는 방법. (throw(s))
예외 발생 시점에서 바로 처리하는 것이 아닌, 예외 발생 메서드를 호출한 곳으로 (catch문 내부에서) throw new XXXException 혹은 throws XXXException을 통해 예외 발생을 넘기는 방법이다.

- 예외 전환
예외 발생 시 해당 예외 타입을 적합한 예외 타입으로 변환하여 전달하는 방법.
try-catch 방식에서 catch 부에 throw를 적합한 커스텀 예외 타입의 객체로 전달한다.


Checked Exception
반드시 예외 처리 필요
확인 시점 : 컴파일 단계
대표적인 예외 클래스 : IOException, SQLException
Uncheked Exception
명시적 처리를 강제하지 않음
실행 중 단계
RuntimeException, NullPointerException, IllegalAragumentException, IndexOutOfBoundException, SystemException



5. 액추에이터

스프링 부트 액추에이터는 HTTP 엔드포인트나 JMX(Java Management Extensions)를 활용하여
애플리케이션을 모니터링하고 관리할 수 있는 기능을 제공한다.


6. 앤드포인트
액추에이터의 엔드포인트는 애플리케이션의 모니터링을 사용하는 경로이다.

액추에이터를 활성화하면 기본적으로 URL 경로 뒤에 "/actuator"를 추가하고 뒤에 추가 경로를 적어 상세 내역에 접근할 수 있으며,
/actuator가 아닌 다른 명칭의 경로 지정 방법은 application.properties 파일에서 다음과 같이 코드를 추가하는 것으로 지정할 수 있다.



  auditevents	  호출된 Audit 이벤트 정보를 표시 (AuditEventRepository 빈 필요)
  beans	  애플리케이션의 모든 스프링 빈 리스트 표시
  caches	  사용 가능 캐시 표시
  conditions	  자동 구성 조건 내역 생성
  configprops	  @ConfigurationProperties의 속성 리스트 표시
  env	  애플리케이션의 사용 가능한 환경 속성 표시
  health	  애플리케이션의 상태 정보 표시
  httptrace	  최근 100건의 요청 기록 표시 (HttpTraceRepository 빈 필요)
  info	  애플리케이션의 정보 표시
  intergrationgraph	  스프링 통합 그래프 표시 (spring-integration-core 모듈 의존성 추가 필요)
  loggers	  애플리케이션의 로거 구성 표시 및 수정
  metrics	  애플리케이션의 매트릭 정보 표시
  mappings	  @RequestMapping의 모든 매핑 정보 표시
  quartz	  Quartz 스케줄러 작업에 대한 정보 표시
  scheduledtasks	  애플리케이션의 예약된 작업 표시
  sessions	  스프링 세션 저장소에서 사용자의 세션 검색 및 삭제
  (스프링 세션을 사용하는 서블릿 기반 웹 애플리케이션 필요)
  shutdown	  애플리케이션을 정상적으로 종료 가능 (기본값은 비활성화 상태)
  startup	  애플리케이션 시작 시 수집된 시작 단계 데이터 표시
  (BufferingApplicationStartup으로 구성된 스프링 애플리케이션 필요)
  threaddump	  스레드 덤프 수행


  
  heapdump	  힙 덤프 파일 반환
  (HotSpot VM 상에서 hprof 포맷 파일이 반환되며, OpenJ9 JVM 상에서 PHD 파일 반환)
  jolokia	  Jolokia가 클래스패스에 있을 때 HTTP를 통해 JMX 빈을 표시
  (jolokia-core 모듈 의존성 추가 필요 / WebFlux에서 사용 불가)
  logfile	  logging.file.name 또는 logging.file.path 속성이 설정돼 있는 경우 로그 파일 내용 반환
  Prometheus	  Promtheus 서버에서 스크랩할 수 있는 형식으로 메트릭 표시
  (micrometer-registry-prometheus 묘듈 의존성 추가 필요)






 */