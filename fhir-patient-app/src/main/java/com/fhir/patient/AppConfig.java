package com.fhir.patient;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import com.fhir.patient.converter.BundleConverter;
import com.fhir.patient.converter.CarePlanConverter;
import com.fhir.patient.converter.ObservationConverter;
import com.fhir.patient.converter.OperationOutcomeConverter;
import com.fhir.patient.converter.PatientConverter;

import ca.uhn.fhir.context.FhirContext;

/**
 * @author rakeshnair
 *
 */
@Configuration
public class AppConfig
{
    @Autowired
    private FhirContext fhirContext;

    @Bean
    public HttpMessageConverters customConverters()
    {
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new PatientConverter(fhirContext));
        converters.add(new OperationOutcomeConverter(fhirContext));
        converters.add(new ObservationConverter(fhirContext));
        converters.add(new BundleConverter(fhirContext));
        converters.add(new CarePlanConverter(fhirContext));
        return new HttpMessageConverters(true, converters);
    }
}
