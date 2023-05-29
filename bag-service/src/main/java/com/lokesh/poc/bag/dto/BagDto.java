package com.lokesh.poc.bag.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BagDto {
    private String bagId;
    private String userId;
    private LocalDate created;
    private LocalDate lastModified;
}