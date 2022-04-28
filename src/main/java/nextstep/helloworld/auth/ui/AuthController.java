package nextstep.helloworld.auth.ui;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nextstep.helloworld.auth.application.AuthService;
import nextstep.helloworld.auth.application.AuthorizationException;
import nextstep.helloworld.auth.dto.MemberResponse;
import nextstep.helloworld.auth.dto.TokenRequest;
import nextstep.helloworld.auth.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {
    private static final String SESSION_KEY = "USER";
    private AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * ex) request sample
     * <p>
     * POST /login/session HTTP/1.1
     * content-type: application/x-www-form-urlencoded; charset=ISO-8859-1
     * host: localhost:55477
     * <p>
     * email=email@email.com&password=1234
     */
    @PostMapping("/login/session")
    public ResponseEntity sessionLogin(@RequestParam("email") String email,
                                       @RequestParam("password") String password,
                                       HttpServletRequest request) {
        // TODO: email과 password 값 추출하기

        if (authService.checkInvalidLogin(email, password)) {
            throw new AuthorizationException();
        }

        // TODO: Session에 인증 정보 저장 (key: SESSION_KEY, value: email값)
        final HttpSession session = request.getSession();
        session.setAttribute(SESSION_KEY, email);

        return ResponseEntity.ok().build();
    }

    /**
     * ex) request sample
     * <p>
     * GET /members/me HTTP/1.1
     * cookie: JSESSIONID=E7263AC9557EF658C888F02EEF840A19
     * accept: application/json
     */
    @GetMapping("/members/me")
    public ResponseEntity findMyInfo(HttpServletRequest request) {
        // TODO: Session을 통해 인증 정보 조회하기 (key: SESSION_KEY)
        final HttpSession session = request.getSession();
        String email = (String) session.getAttribute(SESSION_KEY);
        System.out.println("#####email" + email);
        MemberResponse member = authService.findMember(email);
        return ResponseEntity.ok().body(member);
    }

    /**
     * ex) request sample
     * <p>
     * POST /login/token HTTP/1.1
     * accept: application/json
     * content-type: application/json; charset=UTF-8
     * <p>
     * {
     * "email": "email@email.com",
     * "password": "1234"
     * }
     */
    @PostMapping("/login/token")
    public ResponseEntity tokenLogin(@RequestBody TokenRequest tokenRequest) {
        // TODO: TokenRequest 값을 메서드 파라미터로 받아오기 (hint: @RequestBody)
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }

    /**
     * ex) request sample
     * <p>
     * GET /members/you HTTP/1.1
     * authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MTAzNzY2NzIsImV4cCI6MTYxMDM4MDI3Mn0.Gy4g5RwK1Nr7bKT1TOFS4Da6wxWh8l97gmMQDgF8c1E
     * accept: application/json
     */
    @GetMapping("/members/you")
    public ResponseEntity findYourInfo(HttpServletRequest request) {
        // TODO: authorization 헤더의 Bearer 값을 추출하기
        final String bearer = request.getHeader("authorization");
        final String token = bearer.split(" ")[1];

        MemberResponse member = authService.findMemberByToken(token);
        return ResponseEntity.ok().body(member);
    }
}
