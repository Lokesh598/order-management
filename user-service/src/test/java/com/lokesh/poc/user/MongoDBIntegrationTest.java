package com.lokesh.poc.user;

import com.lokesh.poc.user.dto.UserDto;
import com.lokesh.poc.user.service.UserService;
import com.lokesh.poc.user.util.ReadJson;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.lokesh.poc.user.constants.UserServiceConstants.USER_COLLECTION;
import static org.junit.jupiter.api.Assertions.assertEquals;



//@DataMongoTest
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MongoDBIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private UserService userService;
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @BeforeEach
    void setup() throws Exception {
    }

    @Test
    public void testAddUser() {
        UserDto userDto = ReadJson.read("userDetails.json", UserDto.class);
        reactiveMongoTemplate.insert(userDto, USER_COLLECTION).block();

        webTestClient.post().uri("/api/user/v1/signup")
                .body(Mono.just(userDto), UserDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }
    @Test
    public void testGetUserByEmailId() {
        UserDto userDto = ReadJson.read("userDetails.json", UserDto.class);
        reactiveMongoTemplate.insert(userDto, USER_COLLECTION).block();

        webTestClient.get().uri("/api/user/v1/login/amit@gmail.com")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(userDto1 ->
                        assertEquals("amit@gmail.com", userDto.getEmailId())
                );
    }

    @Test
    public void testUpdateUserInfo() {
        Query query = new Query();
        UserDto userDto = ReadJson.read("userDetails.json", UserDto.class);
        Update update = Update.update("userName", userDto.getUserName());

        Mono<UpdateResult> resultMono = reactiveMongoTemplate.updateFirst(query, update, UserDto.class, USER_COLLECTION);

        resultMono.block();

        webTestClient.put().uri("/api/user/v1/update/lokesh@gmail.com")
                .body(Mono.just(resultMono),UserDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testDeleteUserInfo() {
        Query query = new Query(Criteria.where("emailId").is("lokesh@gmail.com"));

        Mono<DeleteResult> resultMono = reactiveMongoTemplate.remove(query, UserDto.class, USER_COLLECTION);

        resultMono.subscribe(result -> {
            long deletedCount = result.getDeletedCount();
            System.out.println("Deleted " + deletedCount + " documents");
        });

//        webTestClient.delete().uri("/api/user/v1/delete/lokesh@gmail.com")
//                .exchange()
//                .expectStatus().isOk()
//                .returnResult(Void.class);
    }
}

