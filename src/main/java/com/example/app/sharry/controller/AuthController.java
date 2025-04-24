package com.example.app.sharry.controller;


import com.example.app.sharry.dao.UserRepository;
import com.example.app.sharry.dto.LoginDto;
import com.example.app.sharry.model.User;
import com.example.app.sharry.responses.ApiResponse;
import com.example.app.sharry.service.CustomUserDetailsService;
import com.example.app.sharry.service.UserCrudService;
import com.example.app.sharry.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sharry")
public class AuthController {

    @Autowired
    private UserCrudService userCrudService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private ApiResponse apiResponse;
    @Autowired
    private LoginDto loginDto;
    @Autowired
    private UserRepository userRepository;



    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> register(@RequestBody User user) throws Exception
    {
        User newUser = userCrudService.createNewUser(user);

        newUser.setPassword(null);
        apiResponse.setMessage("User created!");
        apiResponse.setData(newUser);

        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.CREATED);
    }

    @PostMapping(value = "/authenticate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> authenticateUser(@RequestBody User user)
    {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        authenticationManager.authenticate(auth);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        User LoggedInUser = userRepository.findByUsername(user.getUsername());

        loginDto.setJwt(jwt);
        loginDto.setUsername(user.getUsername());
        loginDto.setUserid(LoggedInUser.getId());

        apiResponse.setMessage("Auth Token!");
        apiResponse.setData(loginDto);

        return new ResponseEntity<>(apiResponse.getBodyResponse(),HttpStatus.OK);
    }
}