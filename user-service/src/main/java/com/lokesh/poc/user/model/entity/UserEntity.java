package com.lokesh.poc.user.model.entity;

import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
@CompoundIndex(name = "user_unique_idx", def = "{'userId':1}", unique = true)
public class UserEntity {
    @Id
    private String id;
    private Integer userId;
    private String emailId;

    //user address
}
