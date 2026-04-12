package com.it.Mujakjung_be.gobal.admin.controller;

import com.it.Mujakjung_be.gobal.admin.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class TravelController {

    private final TravelService service;
}
