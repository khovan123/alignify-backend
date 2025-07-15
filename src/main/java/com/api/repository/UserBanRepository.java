package com.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.api.model.UserBan;

@Repository
public interface UserBanRepository extends MongoRepository<UserBan, String> {

  List<UserBan> findAllByRoleId(String roleId);

  Page<UserBan> findAllByRoleId(String roleId, Pageable pageable);

}
