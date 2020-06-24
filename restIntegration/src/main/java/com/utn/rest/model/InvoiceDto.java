package com.utn.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceDto {
    private Integer callCount;
    private String lineNumber;
    private Date dateEmission;
    private Date dateExpiration;
    private float priceTotal;
}