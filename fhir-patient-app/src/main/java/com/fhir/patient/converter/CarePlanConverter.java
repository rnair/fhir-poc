/**
 * 
 */
package com.fhir.patient.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import org.hl7.fhir.dstu3.model.CarePlan;
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
public class CarePlanConverter extends FhirJsonHttpMessageConverter<CarePlan>
{

    public CarePlanConverter(FhirContext fhirContext)
    {
        super(fhirContext);
    }

    @Override
    protected boolean supports(Class<?> clazz)
    {
        return CarePlan.class.isAssignableFrom(clazz);
    }

    @Override
    protected MediaType getDefaultContentType(CarePlan cp)
    {
        return mediaType;
    }

    @Override
    protected CarePlan readInternal(Class<? extends CarePlan> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException
    {
        CarePlan cp;
        try
        {
            Reader reader = new InputStreamReader(inputMessage.getBody());
            cp = getParser().parseResource(CarePlan.class, reader);
        }
        catch (Exception ex)
        {
            throw new HttpMessageNotReadableException("Could not read careplan json message: " + ex.getMessage(), ex);
        }
        return cp;
    }

    @Override
    protected void writeInternal(CarePlan cp, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException
    {
        OutputStream outputStream = outputMessage.getBody();
        String body = getParser().encodeResourceToString(cp);
        outputStream.write(body.getBytes());
        outputStream.close();
    }
}
