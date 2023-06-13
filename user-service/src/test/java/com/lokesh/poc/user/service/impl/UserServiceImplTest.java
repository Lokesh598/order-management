package com.lokesh.poc.user.service.impl;

import com.lokesh.poc.user.data.UserData;
import com.lokesh.poc.user.dataobject.UserNameRequest;
import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.model.entity.UserEntity;
import com.lokesh.poc.user.repository.UserRepository;
import com.lokesh.poc.user.utils.EntityDtoUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
//import static reactor.core.publisher.Mono.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    private String TEST_EMAIL = "test@gmail.com";
    @Test
    void addNewUser() {
        UserDto userDto = UserData.userRequest();
        Mono<UserDto> userDtoMono = Mono.just(userDto);

        UserEntity userEntity = EntityDtoUtil.dtoToEntity(userDto);
        Mono<UserEntity> userEntityMono = Mono.just(userDto).map(EntityDtoUtil::dtoToEntity);

        when(userRepository.findByEmailId(userDto.getEmailId())).thenReturn(Mono.empty());
        when(userRepository.insert(userEntity)).thenReturn(userEntityMono);
        Mono<UserDto> result = userService.addNewUser(userDtoMono);
        StepVerifier.create(result)
                .consumeNextWith(newUser -> {
                    assertEquals(userDto.getId(), newUser.getId());
                    assertEquals(userDto.getUserId(), newUser.getUserId());
                    assertEquals(userDto.getUserName(), newUser.getUserName());
                    assertEquals(userDto.getEmailId(), newUser.getEmailId());
                    assertEquals(userDto.getCreatedDate(), newUser.getCreatedDate());
//                    newUser.getUserName().equals(userDto.getUserName()) && newUser.getEmailId().equals(userDto.getEmailId())
                });
//                .verifyComplete();
    }

    @Test
    void shouldFindUserByEmailId() {
        Mono<UserDto> userDto = Mono.just(UserDto.builder()
                .id("1")
                .userId("1")
                .userName("Lokesh")
                .emailId("test@gmail.com")
                .createdDate(LocalDate.now())
                .build());
        when(userRepository.findByEmailId(TEST_EMAIL)).thenReturn(userDto);
        Mono<UserDto> userMono = userService.findUserByEmailId(TEST_EMAIL);
        StepVerifier
                .create(userMono)
                .consumeNextWith(newUser -> {
                    assertEquals(newUser.getEmailId(), TEST_EMAIL);
                })
                .verifyComplete();

    }

    @Test
    @DisplayName("Update user info with valid email id and return updated user")
    void testUpdateUserInfo() {
        String emailId = "amit@gmail.com";
        UserNameRequest userNameRequest = new UserNameRequest("Lokesh");
        UserDto userDto = UserData.userRequest();
        Mono<UserDto> userDtoMono = Mono.just(userDto);
        Mono<UserEntity> userEntityMono = userDtoMono.map(EntityDtoUtil::dtoToEntity);

        UserDto expectedUser = new UserDto();

        expectedUser.setId(userDto.getId());
        expectedUser.setUserId(userDto.getUserId());
        expectedUser.setUserName(userNameRequest.getUserName());
        expectedUser.setEmailId(userDto.getEmailId());
        expectedUser.setCreatedDate(userDto.getCreatedDate());
        Mono<UserDto> user = Mono.just(expectedUser);

        UserEntity userEntity = EntityDtoUtil.dtoToEntity(expectedUser);

        when(userRepository.findByEmailId(emailId)).thenReturn(Mono.just(userDto));
        when(userRepository.save(userEntity)).thenReturn(Mono.just(userEntity));

        Mono<UserDto> result = userService.updateUserInfo(emailId, userNameRequest);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(userDto1 -> userDto1.getEmailId().equals(emailId) &&
                        userDto1.getUserName().equals(userNameRequest.getUserName()))
                .verifyComplete();
    }

    @Test
    void deleteUserInfo() {
        UserDto userDto = UserData.userRequest();

        String emailId = userDto.getEmailId();

        when(userRepository.findByEmailId(emailId)).thenReturn(Mono.just(userDto));
        when(userRepository.deleteById(userDto.getId())).thenReturn(Mono.empty());

        Mono<UserDto> result = userService.deleteUserInfo(emailId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(deletedUserDto ->
                        deletedUserDto.getEmailId().equals(userDto.getEmailId()) &&
                                deletedUserDto.getUserName().equals(userDto.getUserName()))
                .verifyComplete();

        verify(userRepository).deleteById(userDto.getId());
    }
}