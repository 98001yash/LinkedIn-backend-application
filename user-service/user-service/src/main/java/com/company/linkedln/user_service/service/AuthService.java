package com.company.linkedln.user_service.service;


import com.company.linkedln.user_service.Repository.UserRepository;
import com.company.linkedln.user_service.dto.LoginRequestDto;
import com.company.linkedln.user_service.dto.SignupRequestDto;
import com.company.linkedln.user_service.dto.UserDto;
import com.company.linkedln.user_service.entities.User;
import com.company.linkedln.user_service.exceptions.BadRequestException;
import com.company.linkedln.user_service.exceptions.ResourceNotFoundException;
import com.company.linkedln.user_service.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDto signUp(SignupRequestDto signupRequestDto) {
        boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());
        if(exists){
            throw new BadRequestException("User already exists, cannot signup again");
        }
        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(PasswordUtils.hashPassword(signupRequestDto.getPassword()));


        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public String login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(()->new ResourceNotFoundException("User not found with email: "+loginRequestDto.getEmail()));

        boolean isPasswordMatch = PasswordUtils.checkPassword(loginRequestDto.getPassword(),user.getPassword());

        if(!isPasswordMatch){
            throw new BadRequestException("incorrect password");
        }
        return jwtService.generateAccessToken(user);
    }
}
