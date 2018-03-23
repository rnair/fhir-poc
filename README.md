# fhir-poc
FHIR server POC using HAPI FHIR lib under the hood.

How to build: </br>
`mvn clean install` from root ie `fhir-poc`

How to run sample patient app:</br>
It is a spring boot app, the main class is `PatientFhirApplication.java` and just run it in IDE or run as jar just like any other spirng boot app.

How to test:</br>
Creating a patient `POST` `http://localhost:8080/fhir/Patient`
Example payload : https://www.hl7.org/fhir/patient-example.json.html

Reading a patient record `GET` `http://localhost:8080/fhir/Patient/1`
