package com.lokesh.poc.user.model.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder(toBuilder = true)
@JsonRootName(value = "user")
@JsonPropertyOrder({"id", "userId", "userName", "emailId"})
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
@CompoundIndex(name = "user_unique_idx", def = "{'userId':1}", unique = true)
public class UserEntity {
    @Id
    private String id;
    private String userId;
    private String userName;
    private String emailId;
    private LocalDate createdDate;
    //user address
}
