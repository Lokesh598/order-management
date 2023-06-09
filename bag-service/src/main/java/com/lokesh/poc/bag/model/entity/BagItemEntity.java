package com.lokesh.poc.bag.model.entity;


import com.lokesh.poc.bag.dto.ItemDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "bag-item")
public class BagItemEntity {
    @Id
    private String id;
    private String bagId;
    private String userId;
//    private String itemId;
//
//    private int qty;
    private List<ItemDto> itemDto;
    //bag can have multiple items
//    private List<ItemEntity> item;
    private LocalDate lastModified;
}
