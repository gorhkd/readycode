package com.ll.readycode.domain.templates.purchases.service;

import com.ll.readycode.domain.templates.purchases.repository.TemplatePurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TemplatePurchaseService {

  private final TemplatePurchaseRepository templatePurchaseRepository;
}
