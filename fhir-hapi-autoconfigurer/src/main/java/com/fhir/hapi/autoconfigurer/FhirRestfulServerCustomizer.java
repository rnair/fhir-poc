package com.fhir.hapi.autoconfigurer;

import ca.uhn.fhir.rest.server.RestfulServer;

@FunctionalInterface
public interface FhirRestfulServerCustomizer {

	/**
	 * Customize the server.
	 * 
	 * @param server
	 *            the server to customize
	 */
	void customize(RestfulServer server);
}
