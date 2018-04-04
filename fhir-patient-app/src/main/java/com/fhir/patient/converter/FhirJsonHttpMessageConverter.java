
package com.fhir.patient.converter;

import java.nio.charset.Charset;

import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;

/**
 * @author rakeshnair
 *
 */
public abstract class FhirJsonHttpMessageConverter<T> extends AbstractHttpMessageConverter<T>
{
    protected static final MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
    private FhirContext fhirContext;

    protected FhirJsonHttpMessageConverter(FhirContext fhirContext)
    {
        super(mediaType);
        this.fhirContext = fhirContext;
    }

    protected IParser getParser()
    {
        return EncodingEnum.JSON.newParser(this.fhirContext);
    }
}
