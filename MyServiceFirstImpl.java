package com.aem.community.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.aem.community.core.services.MyService;
import com.aem.community.core.services.SampleConfiguration;


@Component(
	    property = {
	        "service.ranking:Integer=106"
	    },
	    service = MyService.class
	)
public class MyServiceFirstImpl implements MyService {


	@Reference
	private ResourceResolverFactory resolverFactory;
	
	@Activate
	protected void activate(SampleConfiguration config) {

	}

	@Override
	public String myMethod() {
		return "Call from MyServiceFirstImpl";
	}

	

	/**
	 * Gets the Resource resolver object in service.
	 *
	 * @return resourceResolver the resourceResolverObject
	 */
	@Override
	public ResourceResolver getResourceResolverForPage(String serviceName) {
		ResourceResolver resourceResolver = null;
		try {
			Map<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, serviceName);
			resourceResolver = resolverFactory.getServiceResourceResolver(param);
		} catch (Exception e) {
		}
		return resourceResolver;
	}
}