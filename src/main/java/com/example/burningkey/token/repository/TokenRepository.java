package com.example.burningkey.token.repository;

import com.example.burningkey.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = " select t from Token t  inner join t.user u  where u.id = :id and (t.expired = false or t.revoked = false) ")
    List<Token> findAllValidTokenByUser(Long id);

    @Query("delete from Token t where t.expired = true")
    void deleteExpired();

    Optional<Token> findByToken(String token);

}