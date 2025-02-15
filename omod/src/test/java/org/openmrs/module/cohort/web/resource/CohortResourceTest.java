package org.openmrs.module.cohort.web.resource;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.module.cohort.CohortM;
import org.openmrs.module.cohort.api.CohortService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.representation.CustomRepresentation;
import org.openmrs.module.webservices.rest.web.representation.NamedRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Qualifier;

@RunWith(PowerMockRunner.class)
public class CohortResourceTest extends BaseCohortResourceTest<CohortM, CohortResource> {
	
	private static final String COHORT_UUID = "737ed593-3769-4df4-9de0-0af149de35ff";
	
	private static final String COHORT_NAME = "Test cohort attribute type";
	
	@Mock
	@Qualifier("cohort.cohortService")
	private CohortService cohortService;
	
	CohortM cohort;
	
	@Before
	public void setup() {
		cohort = new CohortM();
		cohort.setUuid(COHORT_UUID);
		cohort.setName(COHORT_NAME);
		
		//Mocks
		this.prepareMocks();
		when(Context.getService(CohortService.class)).thenReturn(cohortService);
		
		this.setResource(new CohortResource());
		this.setObject(cohort);
	}
	
	@Test
	public void shouldGetRegisteredService() {
		assertThat(cohortService, notNullValue());
	}
	
	@Test
	public void shouldReturnDefaultRepresentation() {
		verifyDefaultRepresentation("name", "description", "uuid");
	}
	
	@Test
	public void shouldReturnFullRepresentation() {
		verifyFullRepresentation("name", "description", "uuid", "auditInfo");
	}
	
	@Test
	public void shouldReturnNullForRepresentationOtherThenDefaultOrFull() {
		CustomRepresentation customRepresentation = new CustomRepresentation("some-rep");
		assertThat(getResource().getRepresentationDescription(customRepresentation), is(nullValue()));
		
		NamedRepresentation namedRepresentation = new NamedRepresentation("some-named-rep");
		assertThat(getResource().getRepresentationDescription(namedRepresentation), is(nullValue()));
		
		RefRepresentation refRepresentation = new RefRepresentation();
		assertThat(getResource().getRepresentationDescription(refRepresentation), is(nullValue()));
	}
	
	@Test
	public void shouldGetResourceByUniqueUuid() {
		when(cohortService.getCohortByUuid(COHORT_UUID)).thenReturn(cohort);
		
		CohortM result = getResource().getByUniqueId(COHORT_UUID);
		
		assertThat(result, notNullValue());
		assertThat(result.getUuid(), is(COHORT_UUID));
		assertThat(result.getName(), is(COHORT_NAME));
	}
	
	@Test
	public void shouldCreateNewResource() {
		when(cohortService.saveCohort(getObject())).thenReturn(getObject());
		
		CohortM newlyCreatedObject = getResource().save(getObject());
		
		assertThat(newlyCreatedObject, notNullValue());
		assertThat(newlyCreatedObject.getUuid(), is(COHORT_UUID));
		assertThat(newlyCreatedObject.getName(), is(COHORT_NAME));
	}
	
	@Test
	public void shouldGetAllResources() {
		when(cohortService.findAll()).thenReturn(Collections.singletonList(getObject()));
		
		PageableResult results = getResource().doGetAll(new RequestContext());
		
		assertThat(results, notNullValue());
	}
	
	@Test
	public void shouldInstantiateNewDelegate() {
		assertThat(getResource().newDelegate(), notNullValue());
	}
	
	@Test
	public void verifyResourceVersion() {
		assertThat(getResource().getResourceVersion(), is("1.8"));
	}
	
}
