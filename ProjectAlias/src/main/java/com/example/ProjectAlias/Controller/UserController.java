package com.example.ProjectAlias.Controller;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/sign")
    public ResponseEntity<?> signin (@RequestParam String email, @RequestParam String password ){


                SecretKey key =  Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("Kiểm tra: " + secretString);


        UsernamePasswordAuthenticationToken authen = new UsernamePasswordAuthenticationToken(email,password);
        authenticationManager.authenticate(authen);

        return new ResponseEntity<>("Hello Sign in", HttpStatus.OK);
    }
}
