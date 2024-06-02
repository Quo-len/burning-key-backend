package com.example.burningkey.token.repository;

import com.example.burningkey.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

   /* @Query(value = """
      select t from Token t inner join User u
      on t.user.userId = u.userId 
      where u.userId = :id and (t.expired = false or t.revoked = false)
      """)
    List<Token> findAllValidTokenByUser(Integer id);*/

    Optional<Token> findByToken(String token);

}