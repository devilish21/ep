/*
 * Copyright (c) Elastic Path Software Inc., 2019
 */
package com.elasticpath.rest.resource.integration.epcommerce.repository.payments.instruments.profile.impl;

import static com.elasticpath.rest.resource.integration.epcommerce.repository.payments.commons.PaymentResourceHelpers.buildPaymentInstrumentEntity;
import static com.elasticpath.rest.resource.integration.epcommerce.repository.payments.commons.PaymentResourceHelpers.buildPaymentInstrumentIdentifier;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.reactivex.Completable;
import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.elasticpath.domain.customer.Customer;
import com.elasticpath.domain.orderpaymentapi.CustomerPaymentInstrument;
import com.elasticpath.provider.payment.service.instrument.PaymentInstrumentDTO;
import com.elasticpath.rest.definition.paymentinstruments.PaymentInstrumentEntity;
import com.elasticpath.rest.definition.paymentinstruments.PaymentInstrumentIdentifier;
import com.elasticpath.rest.id.type.StringIdentifier;
import com.elasticpath.rest.resource.ResourceOperationContext;
import com.elasticpath.rest.resource.integration.epcommerce.repository.customer.CustomerRepository;
import com.elasticpath.rest.resource.integration.epcommerce.repository.payments.instruments.CustomerDefaultPaymentInstrumentRepository;
import com.elasticpath.rest.resource.integration.epcommerce.repository.payments.instruments.CustomerPaymentInstrumentRepository;
import com.elasticpath.rest.resource.integration.epcommerce.repository.payments.instruments.PaymentInstrumentManagementRepository;
import com.elasticpath.service.orderpaymentapi.FilteredPaymentInstrumentService;

/**
 * Tests for {@link PaymentInstrumentEntityRepositoryImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class PaymentInstrumentEntityRepositoryTest {

	private static final String SCOPE = "MOBEE";
	private static final String CUSTOMER_ID = "CUSTOMER_ID";
	private static final String PAYMENT_INSTRUMENT_ID = "PAYMENT_INSTRUMENT_ID";
	private static final String CUSTOMER_PAYMENT_INSTRUMENT_ID = "CUSTOMER_PAYMENT_INSTRUMENT_ID";

	@Mock
	private ResourceOperationContext resourceOperationContext;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private CustomerPaymentInstrumentRepository customerPaymentInstrumentRepository;

	@Mock
	private CustomerDefaultPaymentInstrumentRepository customerDefaultPaymentInstrumentRepository;

	@Mock
	private PaymentInstrumentManagementRepository paymentInstrumentManagementRepository;

	@Mock
	private FilteredPaymentInstrumentService filteredPaymentInstrumentService;

	@InjectMocks
	private PaymentInstrumentEntityRepositoryImpl<PaymentInstrumentEntity, PaymentInstrumentIdentifier> repository;

	private final CustomerPaymentInstrument customerPaymentInstrument = createTestCustomerPaymentInstrument();

	private static CustomerPaymentInstrument createTestCustomerPaymentInstrument() {
		return mock(CustomerPaymentInstrument.class);
	}

	@Before
	public void setUp() {
		when(resourceOperationContext.getUserIdentifier()).thenReturn(CUSTOMER_ID);

		final Customer customer = mock(Customer.class);
		when(customerRepository.getCustomer(CUSTOMER_ID)).thenReturn(Single.just(customer));

		when(customerPaymentInstrument.getGuid()).thenReturn(CUSTOMER_PAYMENT_INSTRUMENT_ID);
		when(customerPaymentInstrument.getPaymentInstrumentGuid()).thenReturn(PAYMENT_INSTRUMENT_ID);
		when(customerPaymentInstrumentRepository.findByGuid(CUSTOMER_PAYMENT_INSTRUMENT_ID))
				.thenReturn(Single.just(customerPaymentInstrument));
		when(filteredPaymentInstrumentService.findCustomerPaymentInstrumentsForCustomerAndStore(customer, SCOPE))
				.thenReturn(ImmutableList.of(customerPaymentInstrument));

		when(paymentInstrumentManagementRepository.getPaymentInstrumentByGuid(PAYMENT_INSTRUMENT_ID))
				.thenReturn(Single.just(createTestPaymentInstrumentDTO()));
	}

	private PaymentInstrumentIdentifier createTestPaymentInstrumentIdentifier() {
		return buildPaymentInstrumentIdentifier(
				StringIdentifier.of(SCOPE),
				StringIdentifier.of(CUSTOMER_PAYMENT_INSTRUMENT_ID)
		);
	}

	private PaymentInstrumentDTO createTestPaymentInstrumentDTO() {
		final PaymentInstrumentDTO dto = new PaymentInstrumentDTO();
		dto.setGUID(PAYMENT_INSTRUMENT_ID);
		dto.setName("Test Name");
		dto.setData(ImmutableMap.of("key", "data"));
		dto.setPaymentProviderConfigurationGuid("test-payment-provider-config-guid");
		dto.setPaymentProviderConfiguration(ImmutableMap.of("configKey", "configValue"));
		dto.setBillingAddressGuid("billingAddressGuid");
		dto.setSupportingMultiCharges(false);
		dto.setSingleReservePerPI(false);
		return dto;
	}

	@Test
	public void findOneReturnsPaymentInstrumentWithExpectedDefaultFields() {
		when(customerDefaultPaymentInstrumentRepository.isDefault(customerPaymentInstrument)).thenReturn(Single.just(false));

		final PaymentInstrumentEntity expectedEntity = buildPaymentInstrumentEntity(false, ImmutableMap.of("key", "data"), "Test Name");

		repository.findOne(createTestPaymentInstrumentIdentifier())
				.test()
				.assertNoErrors()
				.assertValue(expectedEntity);
	}

	@Test
	public void findOneMapsDefaultPaymentInstrumentToDefaultFlag() {
		when(customerDefaultPaymentInstrumentRepository.isDefault(customerPaymentInstrument)).thenReturn(Single.just(true));

		final PaymentInstrumentEntity expectedEntity = buildPaymentInstrumentEntity(true, ImmutableMap.of("key", "data"), "Test Name");

		repository.findOne(createTestPaymentInstrumentIdentifier())
				.test()
				.assertNoErrors()
				.assertValue(expectedEntity);
	}

	@Test
	public void findAllReturnsAvailablePaymentInstruments() {
		repository.findAll(StringIdentifier.of(SCOPE))
				.test()
				.assertNoErrors()
				.assertValues(createTestPaymentInstrumentIdentifier());
	}

	@Test
	public void deleteRemovesTargetPaymentInstrument() {
		when(customerPaymentInstrumentRepository.remove(customerPaymentInstrument)).thenReturn(Completable.complete());
		repository.delete(createTestPaymentInstrumentIdentifier())
				.test()
				.assertNoErrors()
				.assertComplete();

		verify(customerPaymentInstrumentRepository).remove(customerPaymentInstrument);
	}
}
