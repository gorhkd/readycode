package com.ll.readycode.api.templates.controller;

import com.ll.readycode.domain.templates.purchases.service.TemplatePurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/purchases")
public class TemplatePurchaseController {

  private final TemplatePurchaseService templatePurchaseService;
}
