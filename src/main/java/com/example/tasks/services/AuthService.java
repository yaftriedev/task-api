package com.example.tasks.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.tasks.config.JwtManager;
import com.example.tasks.dto.UserDTO;
import com.example.tasks.dto.UserLoginDTO;
import com.example.tasks.dto.UserRegisterDTO;
import com.example.tasks.model.User;
import com.example.tasks.repository.UserRepository;

@Service 
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    
    public UserDTO register(UserRegisterDTO userRegisterDTO) {
        
        String name = userRegisterDTO.getName();
        String email = userRegisterDTO.getEmail();

        if (!isValidEmail(email)) {
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "The email don't have a correct format"
            );
        }

        if (name == null || name.equals("")) {
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "The name musn't be empty"
            );            
        }

        if (userRepository.findByName(name) != null) {
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "This name are register"
            );
        }

        if (userRepository.findByEmail(email) != null) {
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "This email are register"
            );
        }

        String passwordHashed = passwordEncoder.encode(userRegisterDTO.getPassword());

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPasswordHashed(passwordHashed);

        userRepository.save(user);

        return UserDTO.fromEntity(user);
    }

    public String login(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmail(userLoginDTO.getEmail())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "The email or the password are incorrect"
            ));
        
        if ( !passwordEncoder.matches( userLoginDTO.getPassword(), user.getPasswordHashed() ) ) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "The email or the password are incorrect"
            );
        }

        return JwtManager.createToken(
            UserDTO.fromEntity(user)
        );
    }

}
