package com.fhir.hapi.autoconfigurer;

import java.util.List;

import javax.servlet.ServletException;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ResourceCondition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsProvider;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseValidatingInterceptor;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for HAPI FHIR.
 *
 */
@Configuration
@EnableConfigurationProperties(FhirProperties.class)
public class FhirAutoConfiguration {

	private final FhirProperties properties;

	public FhirAutoConfiguration(FhirProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public FhirContext fhirContext() {
		FhirContext fhirContext = new FhirContext(properties.getVersion());
		return fhirContext;
	}

	@Configuration
	@ConditionalOnClass(AbstractJaxRsProvider.class)
	@EnableConfigurationProperties(FhirProperties.class)
	@ConfigurationProperties("hapi.fhir.rest")
	@SuppressWarnings("serial")
	static class FhirRestfulServerConfiguration extends RestfulServer {

		private final FhirProperties properties;

		private final FhirContext fhirContext;

		private final List<IResourceProvider> resourceProviders;

		private final IPagingProvider pagingProvider;

		private final List<IServerInterceptor> interceptors;

		private final List<FhirRestfulServerCustomizer> customizers;

		public FhirRestfulServerConfiguration(FhirProperties properties, FhirContext fhirContext,
				ObjectProvider<List<IResourceProvider>> resourceProviders,
				ObjectProvider<IPagingProvider> pagingProvider, ObjectProvider<List<IServerInterceptor>> interceptors,
				ObjectProvider<List<FhirRestfulServerCustomizer>> customizers) {
			this.properties = properties;
			this.fhirContext = fhirContext;
			this.resourceProviders = resourceProviders.getIfAvailable();
			this.pagingProvider = pagingProvider.getIfAvailable();
			this.interceptors = interceptors.getIfAvailable();
			this.customizers = customizers.getIfAvailable();
		}

		@Bean
		public ServletRegistrationBean fhirServerRegistrationBean() {
			ServletRegistrationBean registration = new ServletRegistrationBean(this,
					this.properties.getServer().getPath());
			registration.setLoadOnStartup(1);
			return registration;
		}

		@Override
		protected void initialize() throws ServletException {
			super.initialize();

			setFhirContext(this.fhirContext);
			setResourceProviders(this.resourceProviders);
			setPagingProvider(this.pagingProvider);
			setInterceptors(this.interceptors);
			setServerAddressStrategy(new HardcodedServerAddressStrategy(this.properties.getServer().getPath()));

			customize();
		}

		private void customize() {
			if (this.customizers != null) {
				AnnotationAwareOrderComparator.sort(this.customizers);
				for (FhirRestfulServerCustomizer customizer : this.customizers) {
					customizer.customize(this);
				}
			}
		}
	}

	@Configuration
	@Conditional(FhirValidationConfiguration.SchemaAvailableCondition.class)
	@ConditionalOnProperty(name = "hapi.fhir.validation.enabled", matchIfMissing = true)
	static class FhirValidationConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public RequestValidatingInterceptor requestValidatingInterceptor() {
			return new RequestValidatingInterceptor();
		}

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(name = "hapi.fhir.validation.request-only", havingValue = "false")
		public ResponseValidatingInterceptor responseValidatingInterceptor() {
			return new ResponseValidatingInterceptor();
		}

		static class SchemaAvailableCondition extends ResourceCondition {

			SchemaAvailableCondition() {
				super("ValidationSchema", "hapi.fhir.validation", "schema-location",
						"classpath:/org/hl7/fhir/instance/model/schema",
						"classpath:/org/hl7/fhir/dstu2016may/model/schema",
						"classpath:/org/hl7/fhir/dstu3/model/schema");
			}
		}
	}

}
