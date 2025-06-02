package com.board.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @GetMapping("favicon.ico")
    public void returnNoFavicon() {
    }
}
