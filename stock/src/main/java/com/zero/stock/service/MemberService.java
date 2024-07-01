package com.zero.stock.service;

import com.zero.stock.exception.impl.AlreadyExistUserException;
import com.zero.stock.exception.impl.NotMatchPasswordException;
import com.zero.stock.exception.impl.UsernameNotFoundException;
import com.zero.stock.model.Auth;
import com.zero.stock.persist.entity.MemberEntity;
import com.zero.stock.persist.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) {
      return this.memberRepository.findByUsername(username).orElseThrow(UsernameNotFoundException::new);
    }

    public MemberEntity register(Auth.SignUp signUp){
        if(this.memberRepository.existsByUsername(signUp.getUsername())){
            throw new AlreadyExistUserException();
        }

        signUp.setPassword(this.passwordEncoder.encode(signUp.getPassword()));
        return this.memberRepository.save(signUp.toEntity());
    }

    public MemberEntity authentication(Auth.SignIn signIn){
        return null;
    }

    public MemberEntity authenticate(Auth.SignIn signIn){
        log.info("username : " + signIn.getUsername());
        log.info("password : " + signIn.getPassword());
        MemberEntity user = memberRepository.findByUsername(signIn.getUsername()).orElseThrow(UsernameNotFoundException::new);
        boolean matches = this.passwordEncoder.matches(signIn.getPassword(), user.getPassword());
        log.info("matches : " + matches);

        log.info("signIn Password : " + signIn.getPassword());
        log.info("password : " + user.getPassword());


        if(!this.passwordEncoder.matches(signIn.getPassword(), user.getPassword())){
            throw new NotMatchPasswordException();
        }
        return user;
    }
}
