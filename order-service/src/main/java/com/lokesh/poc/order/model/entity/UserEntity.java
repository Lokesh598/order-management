package com.lokesh.poc.order.model.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Getter
@Setter
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
