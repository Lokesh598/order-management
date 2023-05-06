package com.lokesh.poc.bag.model.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "bag")
public class BagEntity {
}
