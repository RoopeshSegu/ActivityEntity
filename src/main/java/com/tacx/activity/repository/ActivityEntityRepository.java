package com.tacx.activity.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tacx.activity.domain.ActivityEntity;

public interface ActivityEntityRepository extends MongoRepository<ActivityEntity, String>{

}
