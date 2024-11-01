package com.divyajyoti.user_management.services;

import com.divyajyoti.user_management.dtos.ResponseStatusDto;
import com.divyajyoti.user_management.dtos.UserDto;
import com.divyajyoti.user_management.entities.UserEntity;
import com.divyajyoti.user_management.repositories.UserEntityRepository;
import com.divyajyoti.user_management.rests.exceptions.GenericRestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Slf4j
@Service
public class UserManagementService {

    private final UserEntityRepository userEntityRepository;

    @Autowired
    public UserManagementService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @CachePut(value = "expense-sharing-user-management", key = "(#userData.name + '-' + #userData.contact).toUpperCase()")
    public ResponseStatusDto registerNewUser(UserDto userData) {
        Optional<UserEntity> optionalUser;
        try {
            optionalUser = userEntityRepository.findByContact(userData.getContact());
        } catch (Exception e) {
            throw new GenericRestException("DATABASE ERROR, PLEASE TRY LATER!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (optionalUser.isPresent())
            throw new GenericRestException("USER ALREADY EXISTS WITH GIVEN CONTACT NO!", HttpStatus.BAD_REQUEST);
        UserEntity newUser = new UserEntity();
        newUser.setContact(userData.getContact());
        newUser.setName(userData.getName());
        newUser.setEmail(userData.getEmail());
        UserEntity savedUser;
        try {
            savedUser = userEntityRepository.save(newUser);
        } catch (Exception e) {
            throw new GenericRestException("ERROR WHILE SAVING INFO INTO DATABASE!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseStatusDto("USER CREATION SUCCESSFUL", savedUser);
    }

    @CachePut(value = "expense-sharing-user-management", key = "(#userData.name + '-' + #userData.contact).toUpperCase()")
    public ResponseStatusDto updateUserDetails(UserDto userData, BigInteger id) {
        Optional<UserEntity> optionalUser;
        try {
            optionalUser = userEntityRepository.findById(id);
        } catch (Exception e) {
            log.error("DB_FETCH_ERR: {}", e.getMessage());
            throw new GenericRestException("DATABASE ERROR, PLEASE TRY LATER!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (optionalUser.isEmpty())
            throw new GenericRestException("USER DOES NOT EXIST", HttpStatus.BAD_REQUEST);
        UserEntity newUser = new UserEntity();
        newUser.setId(id);
        newUser.setContact(userData.getContact());
        newUser.setName(userData.getName());
        newUser.setEmail(userData.getEmail());
        UserEntity savedUser;
        try {
            savedUser = userEntityRepository.save(newUser);
        } catch (Exception e) {
            throw new GenericRestException("ERROR WHILE UPDATING INFO INTO DATABASE!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseStatusDto("USER DETAILS UPDATED", savedUser);
    }

    public ResponseStatusDto getUserDetails(BigInteger id) {
        Optional<UserEntity> optionalUser;
        try{
            optionalUser = userEntityRepository.findById(id);
        } catch (Exception e){
            log.error("DATABASE ERROR WHILE GETTING USER DATA: {}", e.getMessage());
            throw new GenericRestException("SERVER ERROR WHILE GETTING USER DATA: {}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(optionalUser.isEmpty())
            throw new GenericRestException("USER DOES NOT EXIST: {}", HttpStatus.NOT_FOUND);
        UserEntity foundUserEntity = optionalUser.get();
        UserDto userDto = new UserDto(foundUserEntity.getName(),
                foundUserEntity.getContact(), foundUserEntity.getEmail());
        return new ResponseStatusDto("SUCCESS", userDto);
    }

}
