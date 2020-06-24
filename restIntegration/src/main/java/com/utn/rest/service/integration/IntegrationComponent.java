package com.utn.rest.service.integration;

import com.utn.rest.model.LoginRequestDto;
import com.utn.rest.model.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class IntegrationComponent {

    private RestTemplate rest;
    private static String urlUser = "http://localhost:8080/backoffice/users/2";
    private static String urlString = "http://localhost:8080/backoffice/string";
    private static String loginUser = "http://localhost:8080/login";
    @PostConstruct
    private void init() {
        rest = new RestTemplateBuilder()
                .build();
    }

    public UserDto getUserByIdFromApi() {
        return rest.getForObject(urlUser,UserDto.class);
    }

    public String getStringFromApi() {
        return rest.getForObject(urlString,String.class);

    }

    public ResponseEntity loginUser(LoginRequestDto loginRequestDto){
        HttpEntity<LoginRequestDto> request = new HttpEntity<>(loginRequestDto);
        ResponseEntity response = rest.postForEntity(loginUser,request,String.class);
        HttpHeaders header = response.getHeaders();
        System.out.println(header.get("Authorization"));
        return response;
    }
}
