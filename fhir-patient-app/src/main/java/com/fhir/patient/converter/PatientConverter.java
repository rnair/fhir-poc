package com.fhir.patient.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import org.hl7.fhir.dstu3.model.Patient;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import ca.uhn.fhir.context.FhirContext;

/**
 * @author rakeshnair
 *
 */
public class PatientConverter extends FhirJsonHttpMessageConverter<Patient>
{

    public PatientConverter(FhirContext fhirContext)
    {
        super(fhirContext);
    }

    @Override
    protected boolean supports(Class<?> clazz)
    {
        return Patient.class.isAssignableFrom(clazz);
    }

    @Override
    protected MediaType getDefaultContentType(Patient patient)
    {
        return mediaType;
    }

    @Override
    protected Patient readInternal(Class<? extends Patient> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException
    {
        Patient patient;
        try
        {
            Reader reader = new InputStreamReader(inputMessage.getBody());
            patient = getParser().parseResource(Patient.class, reader);
        }
        catch (Exception ex)
        {
            throw new HttpMessageNotReadableException("Could not read patient json message: " + ex.getMessage(), ex);
        }
        return patient;
    }

    @Override
    protected void writeInternal(Patient patient, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException
    {
        OutputStream outputStream = outputMessage.getBody();
        String body = getParser().encodeResourceToString(patient);
        outputStream.write(body.getBytes());
        outputStream.close();
    }
}
