package com.utn.rest.service;

import com.utn.rest.model.LoginRequestDto;
import com.utn.rest.model.UserDto;
import com.utn.rest.service.integration.IntegrationComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class IntegrationService {

    @Autowired
    IntegrationComponent integrationComponent;

    /*public Pet getPet() {
        return integrationComponent.getPetsFromApi();
    }*/
    public UserDto getUserByIdFromApi(){
        return integrationComponent.getUserByIdFromApi();
    }
    public String getStringFromApi(){
        return integrationComponent.getStringFromApi();
    }

    public ResponseEntity login(LoginRequestDto loginRequestDto) {
        return integrationComponent.loginUser(loginRequestDto);
    }
}
