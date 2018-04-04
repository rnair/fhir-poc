/**
 * 
 */
package com.fhir.patient.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import org.hl7.fhir.dstu3.model.Observation;
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
public class ObservationConverter extends FhirJsonHttpMessageConverter<Observation>
{

    public ObservationConverter(FhirContext fhirContext)
    {
        super(fhirContext);
    }

    @Override
    protected boolean supports(Class<?> clazz)
    {
        return Observation.class.isAssignableFrom(clazz);
    }

    @Override
    protected MediaType getDefaultContentType(Observation obs)
    {
        return mediaType;
    }

    @Override
    protected Observation readInternal(Class<? extends Observation> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException
    {
        Observation obs;
        try
        {
            Reader reader = new InputStreamReader(inputMessage.getBody());
            obs = getParser().parseResource(Observation.class, reader);
        }
        catch (Exception ex)
        {
            throw new HttpMessageNotReadableException("Could not read obs json message: " + ex.getMessage(), ex);
        }
        return obs;
    }

    @Override
    protected void writeInternal(Observation obs, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException
    {
        OutputStream outputStream = outputMessage.getBody();
        String body = getParser().encodeResourceToString(obs);
        outputStream.write(body.getBytes());
        outputStream.close();
    }
}
