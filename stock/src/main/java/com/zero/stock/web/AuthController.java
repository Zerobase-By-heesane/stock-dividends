package com.zero.stock.web;

import com.zero.stock.model.Auth;
import com.zero.stock.persist.entity.MemberEntity;
import com.zero.stock.security.TokenProvider;
import com.zero.stock.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp signupRequest) {
        return ResponseEntity.ok(this.memberService.register(signupRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody Auth.SignIn signInRequest) {
        MemberEntity memberEntity = this.memberService.authenticate(signInRequest);
        log.info("user login : " + memberEntity.getUsername());
        return ResponseEntity.ok(this.tokenProvider.generateToken(memberEntity.getUsername(), memberEntity.getRoles()));
    }

}
