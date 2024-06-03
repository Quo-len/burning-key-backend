package com.example.burningkey.user_statistics.api.controller;

import com.example.burningkey.user_statistics.service.UserStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user-statistics")
@CrossOrigin // cross domain tomcat's port 8080 and react's 3000
public class UserStatisticController {

    @Autowired
    private UserStatisticService userStatisticService;

  //  @GetMapping
  //  public ResponseEntity<List<UserStatisticDto>>



}
