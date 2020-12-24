package com.aem.community.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.community.core.services.MyService;
import com.day.cq.commons.jcr.JcrConstants;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Component
public class ResourceResolverSubServicesModel {

	private static final Logger log = LoggerFactory.getLogger(ResourceResolverSubServicesModel.class);

	@SlingObject
	Resource resource;

	@ValueMapValue(name = JcrConstants.JCR_TITLE)
	String title;

	@OSGiService
	private MyService osgiService;

	List<Resource> slides = new ArrayList<Resource>();
	private static final long serialVersionUID = 2610051404257637265L;

	@Inject
	private SlingHttpServletRequest slingHttpServletRequest;

	@PostConstruct
	protected void init() {
		if (slingHttpServletRequest.getPathInfo().contains("readContentPage")) {
			readContentPage("readContentPage");
		} else if (slingHttpServletRequest.getPathInfo().contains("readReports")) {
			readReports("readReports");
		} else if (slingHttpServletRequest.getPathInfo().contains("readAssets")) {
			readAssets("readAssets");
		} else if (slingHttpServletRequest.getPathInfo().contains("writeContent")) {
			writeContent("writeContent");
		}

	}

	private void readReports(String serviceName) {
		ResourceResolver resourceResolver = osgiService.getResourceResolverForPage(serviceName);
		Resource resourceReport = resourceResolver.getResource("/etc/reports/auditreport");
		Resource resourceAsset = resourceResolver.getResource("/content/dam/we-retail/en/features/cart.png");
		Resource resourceContentPage = resourceResolver
				.getResource("/content/we-retail/ca/en/multiple-service-impl-demo");

		log.info("resourceReport = {}", resourceReport);
		log.info("resourceAsset = {}", resourceAsset);
		log.info("resourceContentPage = {}", resourceContentPage);

	}

	private void readAssets(String serviceName) {
		ResourceResolver resourceResolver = osgiService.getResourceResolverForPage(serviceName);
		Resource resourceAsset = resourceResolver.getResource("/content/dam/we-retail/en/features/cart.png");
		Resource resourceContentPage = resourceResolver
				.getResource("/content/we-retail/ca/en/multiple-service-impl-demo");
		Resource resourceReport = resourceResolver.getResource("/etc/reports/auditreport");
		log.info("resourceReport = {}", resourceReport);
		log.info("resourceAsset = {}", resourceAsset);
		log.info("resourceContentPage = {}", resourceContentPage);
	}

	private void writeContent(String serviceName) {
		try (ResourceResolver resourceResolver = osgiService.getResourceResolverForPage(serviceName)) {
			Resource resource = resourceResolver.getResource("/content/we-retail/ca/en/multiple-service-impl-demo/jcr:content/root/responsivegrid/multipleservicedemo");
			ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);
			properties.put("myKey", "myValue");
			resourceResolver.commit();
		} catch (PersistenceException e) {
			log.error("Error occured while saving property, error = {}" + e.getMessage());
		}
	}

	private void readContentPage(String serviceName) {
		ResourceResolver resourceResolver = osgiService.getResourceResolverForPage(serviceName);
		Resource resourceContentPage = resourceResolver
				.getResource("/content/we-retail/ca/en/multiple-service-impl-demo");
		Resource resourceAsset = resourceResolver.getResource("/content/dam/we-retail/en/features/cart.png");
		Resource resourceReport = resourceResolver.getResource("/etc/reports/auditreport");

		log.info("resourceReport = {}", resourceReport);
		log.info("resourceAsset = {}", resourceAsset);
		log.info("resourceContentPage = {}", resourceContentPage);
	}

}
