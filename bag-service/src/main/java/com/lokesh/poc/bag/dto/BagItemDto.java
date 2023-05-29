package com.lokesh.poc.bag.dto;

import com.lokesh.poc.bag.model.entity.BagEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BagItemDto {
    private String id;
    private String bagId;
    private String userId;
//    private String itemId;
//    private int qty;
    private List<ItemDto> itemDto;
    private LocalDate lastModified;

}
