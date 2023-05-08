package com.lokesh.poc.bag.repository;

import com.ctc.wstx.dtd.ModelNode;
import com.lokesh.poc.bag.dto.BagItemDto;
import com.lokesh.poc.bag.model.entity.BagItemEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface BagItemRepository extends ReactiveMongoRepository<BagItemEntity, String> {
    @Query("{bagId:?0}")
    Flux<BagItemDto> findByBagId(String bagId);
}
