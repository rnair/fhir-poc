package com.fhir.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ca.uhn.fhir.context.FhirContext;

@SpringBootApplication
public class PatientFhirApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(PatientFhirApplication.class, args);
    }

    @Bean
    public FhirContext fhirContext()
    {
        FhirContext fhirContext = FhirContext.forDstu3();
        return fhirContext;
    }
}
