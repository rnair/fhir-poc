/**
 * 
 */
package com.fhir.patient.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.hl7.fhir.dstu3.model.CarePlan;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.IdType;
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
@RequestMapping("/fhir/CarePlan")
public class CarePlanRestController
{

    private static final ConcurrentHashMap<String,CarePlan> carePlans = new ConcurrentHashMap<String,CarePlan>();
    private AtomicLong counter = new AtomicLong(0);

    @RequestMapping(method = RequestMethod.POST)
    public OperationOutcome create(@RequestBody CarePlan carePlan)
    {
        Long id = counter.incrementAndGet();
        carePlan.setId(createId(id, 1L));
        carePlans.put(id.toString(), carePlan);

        return new OperationOutcome()
                                    .addIssue(new OperationOutcomeIssueComponent()
                                    .setDetails(new CodeableConcept()
                                            .setText("ALL OK"))
                                    .setCode(IssueType.INFORMATIONAL));
    }

    @RequestMapping(path = "/{id}", produces = { "application/*" })
    public CarePlan find(@PathVariable("id") String id)
    {
        return carePlans.get(id);
    }

    private static IdType createId(final Long id, final Long theVersionId)
    {
        return new IdType("Patient", "" + id, "" + theVersionId);
    }
}
