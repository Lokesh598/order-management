package com.lokesh.poc.bag.model.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "bagproduct")
public class BagProductEntity {
    @Id
    private String id;
    private String bagId;
    private String itemId;
    private Date lastModified;
    private int quantity;

}
