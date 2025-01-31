/*
 1. 보안 용어 이해
 
1.1 인증 (Authentication)
- 사용자가 누구인지 확인하는 단계이다. 
- 확인 후, 서버는 유저에게 토큰을 전달하고 유저는 원하는 리소스에 접근할 수 있다.
 
1.2. 인가 (Authorization)
-  인증을 통해 검증된 사용자가 애플리케이션 내부의 리소스에 접근할 때 사용자가 해당 리소스에 접근할 권리가 있는지를 확인하는 과정
 
1.3. 접근 주체 (principal)
- 애플리케이션을 사용하는 주체
 

 

 

 

2. 스프링 시큐리티
-   서블릿 필터(Servlet Filter)를 기반으로 동작한다.

 
1) 필터 체인 (FilterChain)
- 서블릿 컨테이너에서 관리하며, 클라이언트에서 요청을 보내면 URI을 확인해서 필터와 서블릿을 매핑한다.
 
 
2) DelegatingFilterProxy
- 서블릿 컨테이너의 생명주기와 스프링 애플리케이션 컨텍스트 사이에서 다리 역할을 수행하는 필터 구현체이다.
- 스프링 시큐리티는 사용하고자 하는 필터체인을 서블릿 컨테이너의 필터 사이에서 동작시키기 위해 사용한다.
- 서블릿 필터를 구현하고 있으며, 역할을 위임할 필터체인 프록시(FilterChainProxy)를 내부에 가지고 있다.
 
2.1) 필터체인 프록시(FilterChainProxy)
- 스프링 시큐리티에서 제공하는 필터이다.
- 보안 필터체인(SecurityFilterChain)을 통해 많은 보안 필터(Security Filter)를 사용할 수 있다.
 
2.1.1) 보안 필터체인
- List 형식으로 담을 수 있어 URI 패턴에 따라 특정 보안필터 체인을 선택해서 사용할 수 있다.
- WebSecurityConfigurerAdapter 클래스를 상속받아 설정할 수 있다. 
 

 

 

 

 


 

 
수행 과정
1. 클라이언트로부터 요청을 받으면 서블릿 필터에서 SecurityFilterChain으로 작업이 위임되고 그중 UsernamePasswordAuthenticationFilter에서 인증을 처리
2. AuthenticationFilter는 요청 객체(HttpServletRequest)에서 username과 password를 추출해서 토큰 생성
3. AuthenticationManager에게 토큰 전달. AuthenticationManager는 인터페이스이며, 일반적으로 사용되는 구현체는 ProviderManager.
4. ProviderManager는 인증을 위해 AuthenticationProvider로 토큰 전달
5. AuthenticationProvider는 토큰 정보를 UserDetailsService에 전달
6. UserDetailsService는 전달받은 정보를 통해 DB에서 일치하는 사용자를 찾아 UserDetails 객체 생성
7. 생성된 객체는 AuthenticationProvider로 전달되며, 해당 Provider에서 인증을 수행하고 성공하면 ProviderManager로 권한을 담은 토큰을 전달
8. ProviderManager는 검증된 토큰을 AuthenticationFilter로 전달
9. AuthenticationFilter는 검증된 토큰을 SecurityContextHilder에 있는 SecurityContext에 저장
 

 

 

 

 

3. JWT
- JSON Web Token
- 정보를 JSON 형태로 안전하게 전송하기 위한 토큰이다.
- URL로 이용할 수 있는 문자열로만 구성되어 있으며 디지털 서명이 적용되어 있어 신뢰할 수 있다.
 
 
3.1. JWT 구조
 

1) 헤더(Header)  
- 검증과 관련된 내용을 담고 있으며 해싱 알고리즘과, 토큰의 타입을 지정한다.
 
2) 내용(Payload)
- 토큰에 담긴 정보를 포함한다.
- 정보에 포함된 속성을 클레임이라고 하며 3가지가 있다. 
 
2.1) 클레임
 
- 등록된 클레임(Registered Claims) : 이미 이름이 정해져 있는 클레임
iss : JWT의 발급자(Issuer) 주체를 나타낸다. iss의 값은 문자열이나 URI를 포함하는 대소문자를 구분하는 문자열이다.
sub :  JWT의 제목(Subject)이다.
aud : JWT의 수신인(Audience)이다. JWT를 처리하려는 각 주체는 해당 값으로 자신을 식별해야 한다. 요청을 처리하는 주체가 'aud' 값으로 자신을 식별하지 않으면 JWT는 거부된다.
exp : JWT의 만료시간(Expiration)이다. 시간은 NumericDate 형식으로 지정해야 한다.
nbf : 'Not Before'를 의미한다.
iat : JWT가 발급된 시간(Issued at)이다.
jti : JWT의 식별자(JWT ID)이다. 주로 중복 처리를 방지하기 위해 사용된다.
 
- 공개 클레임(Public Claims) : 키 값을 마음대로 정의할 수 있는 클레임
- 비공개 클레임(Private Claims) : 통신 간에 상호 합의되고 등록된 클레임과 공개된 클레임이 아닌 클레임
 

 

 
3) 서명(Signature)
 
- 인코딩된 헤더, 인코딩된 내용, 비밀키, 헤더의 알고리즘 속성값을 가져와 생성된다.
HMACSHA256(
    base64UrlEncode(header) + "." +
    base64UrlEncode(payload),
    secret
)
 

 

 

 

 
4. 스프링 시큐리티와 JWT 적용
 

 

1) 스프링 시큐리티와 JWT 의존성을 추가한다. 
<dependencies>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt</artifactId>
      <version>0.9.1</version>
    </dependency>

  </dependencies>
 

 

 

2) UserDetails, UserDetailsService 구현
- 이 엔티티는 토큰을 생성할 때 토큰의 정보로 사용될 정보와 권한 정보를 갖게 된다.
 

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class User implements UserDetails {

    private static final long serialVersionUID = 6014984039564979072L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String uid;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.uid;
    }

    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
 

 

 

 

 

3) 엔티티 조회하는 기능 구현을 위한 리포지토리와 서비스 구현
UserRepository 구현
public interface UserRepository extends JpaRepository<User, Long> {
    User getByUid(String uid);
}
 

UserDetailsServiceImpl 구현
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("[loadUserByUsername] loadUserByUsername 수행. username : {}", username);
        return userRepository.getByUid(username).
            orElseThrow(() -> new RuntimeException("유저를 찾을 수 없음"));
    }
}
 
 
4) JwtTokenProvider 구현
- JWT 토큰을 생성하는 JwtTokenProvider를 구현
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private static final String KEY_ROLES = "roles";
    private static final long VALIDATE_TIME = 1 * 60 * 60 * 1000L; // 1시간

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화 시작");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화 완료");
    }

    // 토큰 생성 매서드
    public String generateToken(String username, List<String> roles) {

        LOGGER.info("[generateToken] 토큰 생성 시작");

        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles); // key value로 저장

        var now = new Date();
        var expiredDate = new Date(now.getTime() + VALIDATE_TIME);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 비밀키
                .compact();

        LOGGER.info("[generateToken] 토큰 생성 완료");

        return token;
    }

    public Authentication getAuthentication(String jwt) {

        LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 시작");

        UserDetails userDetails = memberServiceImpl.loadUserByUsername(getUsername(jwt));

        LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails UserName : {}"
                    , userDetails.getUsername());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        LOGGER.info("[getUsername] 토큰 기반 회원 구별 정보 추출");

        // 토큰이 유효한지 확인
        String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();

        LOGGER.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);

        return info;
    }

    public String resolveToken(HttpServletRequest request) {
        LOGGER.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String token) {

        LOGGER.info("[validateToken] 토큰 유효 체크 시작");

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            //토큰 만료 시간이 현재보다 이전인지 아닌지 만료 여부 확인
            return !claims.getBody().getExpiration().before(new Date());

            // 파싱하는 과정에서 토큰 만료 시간이 지날 수 있다, 만료된 토큰을 확인할 때에
        } catch (Exception e) {
            LOGGER.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false; // 토큰이 빈 문자열이면 false
        }
    }
}
 

 

- createToken() 메서드에서는 JWT 토큰의 내용에 값을 넣기 위해 Claims 객체를 생성한다.
- getAuthentication() 메서드는 필터에서 인증이 성공했을 때 SecurityContextHolder에 저장할 Authentication을 생성하는 역할을 한다.
 

- getUsername() 메서드는 JWT 토큰에서 회원 구별 정보를 추출하는 메서드이다.
- resolveToken() 메서드는 HttpServletRequest를 파라미터로 받아 헤더 값으로 전달된 'X-AUTH-TOKEN' 값을 리턴한다.
- validateToken() 메서드는 토큰을 전달받아 클레임의 유효기간을 체크하고 boolean 타입의 값을 리턴한다.
 

 

 

5) JwtTokenProvider 구현
- JWT 토큰으로 인증하고 SecurityContextHolder에 추가하는 필터를 설정할 수 있다.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest,
        HttpServletResponse servletResponse,
        FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(servletRequest);
        LOGGER.info("[doFilterInternal] token 값 추출 완료. token : {}", token);

        LOGGER.info("[doFilterInternal] token 값 유효성 체크 시작");
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LOGGER.info("[doFilterInternal] token 값 유효성 체크 완료");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
- doFilterInternal() 메서드에서 JwtTokenProvider를 통해 servletRequest에서 토큰을 추출하고, 토큰에 대한 유효성을 검사한다.
 

 

6) SecurityConfiguration 구현
-  WebSecurityConfigureAdapter를 상속받는 Configuration 클래스를 구현하여 스프링 시큐리티를 설정한다. 
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable()

            .csrf().disable()

            .sessionManagement()
            .sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)

            .and()
            .authorizeRequests(
            .antMatchers("/sign-api/sign-in", "/sign-api/sign-up",
                "/sign-api/exception").permitAll()
            .antMatchers(HttpMethod.GET, "/product/**").permitAll()

            .antMatchers("**exception**").permitAll()

            .anyRequest().hasRole("ADMIN")

            .and()
            .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
            .and()
            .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())

            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
            "/swagger-ui.html", "/webjars/**", "/swagger/**", "/sign-api/exception");
    }
}
 

 

- 스프링 시큐리티 설정은 대부분 HttpSecurity를 통해 진행한다.
- SecurityConfiguration은 configure() 메서드와 HttpSecurity 파라미터를 받은 configure() 메서드로 구성되어 있다.
 
6.1) configure() 메서드
httpBasic().disable() : UI를 사용하는 것을 기본값으로 가진 시큐리티 설정을 비활성화한다.
csrf().disable()
CSRF는 Cross-Site Request Forgery의 줄임말로 '사이트 간 요청 위조'를 의미한다.
csrf() 메서드는 기본적으로 CSRF 토큰을 발급해서 클라이언트로부터 요청을 받을 때마다 토큰을 검증하는 방식으로 동작한다.
sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
REST API 기반 어플리케이션의 동작 방식을 설정한다.
진행 중인 프로젝트에서는 JWT 토큰으로 인증을 처리하며, 세션을 사용하지 않기 때문에 STATELESS로 설정한다.
authorizeRequest()
어플리케이션에 들어오는 요청에 대한 사용 권한을 체크한다.
antMatchers() 메서드는 antPatter을 통해 권한을 설정하는 역할이다.
exceptionHandling().accessDeniedHandler()
권한을 확인하는 과정에서 통과하지 못하는 예외가 발생할 경우 예외를 전달한다.
exceptionHandling().authenticationEntryPoint()
인증 과정에서 예외가 발생할 경우 예외를 전달한다.
 

 
7) 커스텀 AccessDeniedHandler, AuthenticationEntryPoint 구현
 

- 인증과 인가 과정의 예외 상황에서 사용한다.
 

7.1.) CustomAccessDeniedHandler
- 액세스 권한이 없는 리소스에 접근할 경우 발생하는 AccessDeniedException를 파라미터로 가져온다.
 

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException exception) throws IOException {
        LOGGER.info("[handle] 접근이 막혔을 경우 경로 리다이렉트");
        response.sendRedirect("/sign-api/exception");
    }
}
 

 

 

 

7. 2.) CustomAuthenticationEntryPoint
-   AuthenticationEntryPoint 인터페이스의 구현체이며, commence() 메서드를 오버라이딩한다.
 

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException ex) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LOGGER.info("[commence] 인증 실패로 response.sendError 발생");

        EntryPointErrorResponse entryPointErrorResponse = new EntryPointErrorResponse();
        entryPointErrorResponse.setMsg("인증이 실패하였습니다.");

        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(entryPointErrorResponse));
    }
}
 

 

 

 

8) 회원가입
- signUp() 메서드를 통해 회원가입을 구현한다.
public interface SignService {
    SignUpResultDto signUp(String id, String password, String name, String role);

    SignInResultDto signIn(String id, String password) throws RuntimeException;
}
@Service
public class SignServiceImpl implements SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

    public UserRepository userRepository;
    public TokenProvider tokenProvider;
    public PasswordEncoder passwordEncoder;

    @Autowired
    public SignServiceImpl(UserRepository userRepository, TokenProvider tokenProvider, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    SignUpResultDto signUp(String id, String password, String name, String role) {
        LOGGER.info("[getSignUpResult] 회원 가입 정보 전달");

        User user;

        if (role.equalsIgnoreCase("admin")) {
            user = User.builder()
                .uid(id)
                .name(name)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singletonList("ROLE_ADMIN"))
                .build();
        } else {
            user = User.builder()
                .uid(id)
                .name(name)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        }

        User savedUser = userRepository.save(user);
        SignUpResultDto signUpResultDto = new SignInResultDto();

        LOGGER.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");

        if(!savedUser.getName().isEmpty()) {
            LOGGER.info("[getSignUpResult] 정상 처리 완료");
            setSuccessResult(signUpResultDto);
        } else {
            LOGGER.info("[getSignUpResult] 실패 처리 완료");
               setFailResult(SignUpResultDto);
        }
        return signUpResultDto;
    }


    @Override
    public SignInResultDto signIn(String id, String password) throws RuntimeException {

        LOGGER.info("[getSignInResult] signDataHandler로 회원 정보 요청");
        User user = userRepository.getByUid(id);
        LOGGER.info("[getSignInResult] Id : {}", id);

        LOGGER.info("[getSignInResult] 패스워드 비교 수행");
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
        LOGGER.info("[getSignInResult] 패스워드 일치");

        LOGGER.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResultDto signInResultDto = SignInResultDto.builder()
            .token(tokenProvider.createToken(String.valueOf(user.getUid()),
                                             user.getRoles()))
            .build();

        LOGGER.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        setSuccessResult(signInResultDto);

        return signInResultDto;            
    }

    private void setSuccessResult(SignUpResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    private void setFailResult(SignUpResultDto result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommomResponse.FAIL.getMsg());
    }
}
@Configuration
public class PasswordEncoderConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
 

 

 

 

 

9) 로그인
- signIn() 메서드를 통해 로그인을 구현한다.
- 입력된 id를 통해 User 엔티티를 가져오고, 입력된 password와 PasswordEncoder를 사용해 DB에 저장된 password가 일치하는지 확인한다. 
- 일치하면 JwtTokenProvider를 통해 id와 role 값을 전달해서 토큰을 생성한 후 Response에 담아 전달한다.
 

package com.springboot.security.common;

@Getter
@Setter
public enum CommonResponse {

    SUCCESS(0, "Success"), FAIL(-1, "FAIL");

    int code;
    String msg;
}
@RestController
@RequestMapping("/sign-api")
public class SignController {

    private final Logger LOGGER = LoggerFactory.getLogger(SignController.class);
    private final SignService signService;

    @Autowired
    public SignController(SignService signService) {
        this.signService = signService;
    }

    @PostMapping(value = "/sign-in")
    public SignInResultDto signIn(
        @ApiParam(value = "ID", required=true) @RequestParam String id,
        @ApiParam(value = "Password", required=true) @RequestParam String password)
        throws RuntimeException {

        LOGGER.info("[signIn] 로그인을 시도하고 있습니다. id : {}, pw : ****", id);
        SignInResultDto signInResultDto = signService.signIn(id, password);

        if (signInResultDto.getCode() == 0) {
            LOGGER.info("[signIn] 정상적으로 로그인되었습니다. id : {}, token : {}",
                       id, signInResultDto.getToken());
        }

        return signInResultDto;
    }

    @PostMapping(value = "/sign-up")
    public SignUpResultDto signUp(
        @ApiParam(value = "ID", required=true) @RequestParam String id,
        @ApiParam(value = "비밀번호", required=true) @RequestParam String password,
        @ApiParam(value = "이름", required=true) @RequestParam String name,
        @ApiParam(value = "권한", required=true) @RequestParam String role) {

        LOGGER.info("[signUp] 회원가입을 합니다. id : {}, password : ****, name : {}, role : {}",
                   id, name, role);

        SignUpResultDto signUpResultDto = signService.signUp(id, password, name, role);
        LOGGER.info("[signUp] 회원가입을 완료했습니다. id : {}" , id);
        return signUpResultDto;
    }

    @GetMapping(value="/exception")
    public void exceptionTest() throws RuntimeException {
        throw new RuntimeException("접근이 금지되었습니다");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(RuntimeException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        //responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        LOGGER.error("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "에러 발생");

        return new ResponseEntity<>(map, responseHeaders, httpStatus);     
    }
}
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpResultDto {

    private boolean success;

    private int code;

    private String msg;
}
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResultDto {

    private String token;

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String token) {
        super(success, code, msg);
        this.token = token;
    }
}
 
 */