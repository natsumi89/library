package com.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationController {
    @GetMapping("/to-reservation-page")
    public String toReservationPage() {
        return "reservation";
    }
}
