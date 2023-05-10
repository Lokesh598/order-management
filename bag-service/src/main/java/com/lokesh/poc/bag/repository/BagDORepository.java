package com.lokesh.poc.bag.repository;

import com.lokesh.poc.bag.dataobject.response.BagDO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagDORepository extends ReactiveMongoRepository<BagDO, String> {
}
