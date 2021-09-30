package com.encircle360.oss.receiptfox.service;

import org.springframework.stereotype.Service;

import com.encircle360.oss.receiptfox.repository.TemplateMappingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateMappingService {

    private final TemplateMappingRepository templateMappingRepository;

}
