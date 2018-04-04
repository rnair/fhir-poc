package com.fhir.patient.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rakeshnair
 *
 */
@RestController
@RequestMapping("/fhir/Observation")
public class ObservationRestController
{

    private static final ConcurrentHashMap<String,Observation> obsMap = new ConcurrentHashMap<String,Observation>();
    private AtomicLong counter = new AtomicLong(0);

    @RequestMapping(method = RequestMethod.POST)
    public OperationOutcome create(@RequestBody Observation obs)
    {
        Long id = counter.incrementAndGet();
        obs.setId(createId(id, 1L));
        obsMap.put(id.toString(), obs);
        return new OperationOutcome().addIssue(new OperationOutcomeIssueComponent().setCode(IssueType.PROCESSING));
    }

    @RequestMapping(path = "/{id}", produces = { "application/*" })
    public Observation find(@PathVariable("id") String id)
    {
        return obsMap.get(id);
    }

    @RequestMapping(path = "/search", produces = { "application/*" })
    public Bundle search()
    {
        Bundle bundle = new Bundle();
        for (Observation obs : obsMap.values())
        {
            bundle.addEntry(new BundleEntryComponent().setResource(obs));
        }
        return bundle;
    }

    private static IdType createId(final Long id, final Long theVersionId)
    {
        return new IdType("Observation", "" + id, "" + theVersionId);
    }
}
