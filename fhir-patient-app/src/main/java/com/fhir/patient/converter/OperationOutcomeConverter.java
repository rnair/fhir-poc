package com.fhir.patient.converter;

import java.io.IOException;
import java.io.OutputStream;

import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import ca.uhn.fhir.context.FhirContext;

public class OperationOutcomeConverter extends FhirJsonHttpMessageConverter<OperationOutcome>
{

    public OperationOutcomeConverter(FhirContext fhirContext)
    {
        super(fhirContext);
    }

    @Override
    protected boolean supports(Class<?> clazz)
    {
        return OperationOutcome.class.isAssignableFrom(clazz);
    }

    @Override
    protected MediaType getDefaultContentType(OperationOutcome oo)
    {
        return mediaType;
    }

    @Override
    protected OperationOutcome readInternal(Class<? extends OperationOutcome> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException
    {
        System.out.println("Inside readInternal of operation outcome!!");
        return null;
    }

    @Override
    protected void writeInternal(OperationOutcome t, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException
    {
        OutputStream outputStream = outputMessage.getBody();
        String body = getParser().encodeResourceToString(t);
        outputStream.write(body.getBytes());
        outputStream.close();
    }

}
