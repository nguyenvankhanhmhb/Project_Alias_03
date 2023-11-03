package com.example.ProjectAlias.Provider;


import com.example.ProjectAlias.Entity.UserEntity;
import com.example.ProjectAlias.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomeAuthenProvider implements AuthenticationProvider {

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserEntity user = userRepository.findByEmail(username);

        if(user!=null){
            if(passwordEncoder.matches(password, user.getPassword())){
                List<GrantedAuthority> role = new ArrayList<>();
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ADMIN");
                role.add(grantedAuthority);

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(password,username,role);

                return token;

            }

        }else {


        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
