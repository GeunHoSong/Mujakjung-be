package com.it.Mujakjung_be.gobal.admin.service;

import com.it.Mujakjung_be.gobal.admin.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository repository;
}
