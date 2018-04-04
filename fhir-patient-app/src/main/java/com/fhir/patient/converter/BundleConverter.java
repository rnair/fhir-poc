/**
 * 
 */
package com.fhir.patient.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import org.hl7.fhir.dstu3.model.Bundle;
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
public class BundleConverter extends FhirJsonHttpMessageConverter<Bundle>
{

    public BundleConverter(FhirContext fhirContext)
    {
        super(fhirContext);
    }

    @Override
    protected boolean supports(Class<?> clazz)
    {
        return Bundle.class.isAssignableFrom(clazz);
    }

    @Override
    protected MediaType getDefaultContentType(Bundle bundle)
    {
        return mediaType;
    }

    @Override
    protected Bundle readInternal(Class<? extends Bundle> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException
    {
        Bundle bundle;
        try
        {
            Reader reader = new InputStreamReader(inputMessage.getBody());
            bundle = getParser().parseResource(Bundle.class, reader);
        }
        catch (Exception ex)
        {
            throw new HttpMessageNotReadableException("Could not read bundle json message: " + ex.getMessage(), ex);
        }
        return bundle;
    }

    @Override
    protected void writeInternal(Bundle bundle, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException
    {
        OutputStream outputStream = outputMessage.getBody();
        String body = getParser().encodeResourceToString(bundle);
        outputStream.write(body.getBytes());
        outputStream.close();
    }

}
