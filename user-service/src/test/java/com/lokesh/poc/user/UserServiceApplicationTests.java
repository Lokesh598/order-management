package com.lokesh.poc.user;

import com.lokesh.poc.user.controller.UserController;
import com.lokesh.poc.user.dataobject.UserNameRequest;
import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.repository.UserRepository;
import com.lokesh.poc.user.service.UserService;
import com.lokesh.poc.user.service.impl.UserServiceImpl;
import com.lokesh.poc.user.utils.EntityDtoUtil;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

//import static reactor.core.publisher.Mono.when;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@WebFluxTest(UserController.class)

public class UserServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;
	// we dont want to interact with actual database, so we only mock service layer
	//@InjectMocks //it will not create actual object
	@MockBean
	private UserServiceImpl userService;

	@Test
	public void addUserTest() {
		Mono<UserDto> userDtoMono = Mono.just(new UserDto("1", "1", "Lokesh", "lokesh@gmail.com", LocalDate.now()));

		when(userService.addNewUser(userDtoMono)).thenReturn(userDtoMono);

		webTestClient.post().uri("/api/user/v1/signup")
				.body(Mono.just(userDtoMono), UserDto.class)
				.exchange()
				.expectBody();
	}
	@Test
	public void getUserByEmailTest() {
		Mono<UserDto> userDtoMono = Mono.just(new UserDto("1", "1", "Lokesh", "lokesh@gmail.com", LocalDate.now()));
		when(userService.findUserByEmailId("lokesh@gmail.com")).thenReturn(userDtoMono);

		Flux<UserDto> responseBody = webTestClient.get().uri("/api/user/v1/login/lokesh@gmail.com")
				.exchange()
				.expectStatus().isOk()
				.returnResult(UserDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(p->p.getEmailId().equals("lokesh@gmail.com"))
				.verifyComplete();
	}

	@Test
	public void updateUserInfoTest() {
		Mono<UserDto> userDtoMono = Mono.just(new UserDto("1", "1", "Lokesh", "lokesh@gmail.com", LocalDate.now()));
		when(userService.updateUserInfo("lokesh@gmail.com", new UserNameRequest("harish"))).thenReturn(userDtoMono);

		webTestClient.put().uri("/api/user/v1/update/lokesh@gmail.com")
				.body(Mono.just(userDtoMono),UserDto.class)
				.exchange()
				.expectStatus().isOk();
	}
	@Test
	public void deleteUserInfoTest() {
		Mono<UserDto> userDtoMono = Mono.just(new UserDto("1", "1", "Lokesh", "lokesh@gmail.com", LocalDate.now()));
		when(userService.deleteUserInfo("lokesh@gmail.com")).thenReturn(userDtoMono);

		webTestClient.delete().uri("/api/user/v1/delete/lokesh@gmail.com")
				.exchange()
				.expectStatus().isOk()
				.returnResult(UserDto.class)
				.getResponseBody();
	}
}