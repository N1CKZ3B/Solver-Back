package eci.ieti.safezone.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping("/status")
    public String getStatus() {
        return "The app is running and functional :) Yeiii Tonny Tonny Chopper";
    }
}
