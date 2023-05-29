package com.lokesh.poc.bag.model.entity;

import lombok.*;
import org.bouncycastle.asn1.cms.TimeStampAndCRL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "bag")
public class BagEntity {
    @Id
    private String bagId;
    private String userId;
    private LocalDate created;
    private LocalDate lastModified;
}
