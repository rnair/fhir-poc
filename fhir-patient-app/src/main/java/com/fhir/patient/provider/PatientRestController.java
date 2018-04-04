/**
 * 
 */
package com.fhir.patient.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.dstu3.model.Patient;
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
@RequestMapping("/fhir/Patient")
public class PatientRestController
{
    private static final ConcurrentHashMap<String,Patient> patients = new ConcurrentHashMap<String,Patient>();
    private AtomicLong counter = new AtomicLong(0);

    @RequestMapping(method = RequestMethod.POST)
    public OperationOutcome create(@RequestBody Patient patient)
    {
        Long id = counter.incrementAndGet();
        patient.setId(createId(id, 1L));
        patients.put(id.toString(), patient);
        return new OperationOutcome().addIssue(new OperationOutcomeIssueComponent()
                .setDetails(new CodeableConcept().
                        setText("ALL OK"))
                .setCode(IssueType.INFORMATIONAL));
    }

    @RequestMapping(path = "/{id}", produces = { "application/*" })
    public Patient find(@PathVariable("id") String id)
    {
        return patients.get(id);
    }

    private static IdType createId(final Long id, final Long theVersionId)
    {
        return new IdType("Patient", "" + id, "" + theVersionId);
    }
}
