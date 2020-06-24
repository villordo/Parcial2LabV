package com.utn.rest.service;

import com.utn.rest.model.DateDto;
import com.utn.rest.service.integration.IntegrationComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class IntegrationService {

    @Autowired
    IntegrationComponent integrationComponent;


    public ResponseEntity getInvoicesByDate(String date) {
        return integrationComponent.getInvoicesByDate(date);
    }
}
