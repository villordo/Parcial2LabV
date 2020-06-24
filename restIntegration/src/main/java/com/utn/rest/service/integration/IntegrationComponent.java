package com.utn.rest.service.integration;

import com.utn.rest.model.DateDto;
import com.utn.rest.model.InvoiceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.List;

@Slf4j
@Component
public class IntegrationComponent {

    private RestTemplate rest;
    private static String url = "http://localhost:8080/backoffice/invoices/";
    @PostConstruct
    private void init() {
        rest = new RestTemplateBuilder()
                .build();
    }


    public ResponseEntity<List<InvoiceDto>> getInvoicesByDate(String date) {
        url = url+date;
        ResponseEntity<List<InvoiceDto>> response = rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<InvoiceDto>>() {});
        return response;
    }
}
