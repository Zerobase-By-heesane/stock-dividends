package com.zero.stock.service;

import com.zero.stock.model.Auth;
import com.zero.stock.persist.entity.MemberEntity;
import com.zero.stock.persist.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return this.memberRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("not found username -> " + username)
        );
    }

    public MemberEntity register(Auth.SignUp signUp){
        if(this.memberRepository.existsByUsername(signUp.getUsername())){
            throw new RuntimeException("already exists username -> " + signUp.getUsername());
        }

        signUp.setPassword(this.passwordEncoder.encode(signUp.getPassword()));
        return this.memberRepository.save(signUp.toEntity());
    }

    public MemberEntity authentication(Auth.SignIn signIn){
        return null;
    }

    public MemberEntity authenticate(Auth.SignIn signIn){
        MemberEntity user = memberRepository.findByUsername(signIn.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("not found username -> " + signIn.getUsername())
        );


        if(this.passwordEncoder.matches(this.passwordEncoder.encode(signIn.getPassword()), user.getPassword())){
            throw new RuntimeException("password not matched");
        }
        return user;
    }
}
