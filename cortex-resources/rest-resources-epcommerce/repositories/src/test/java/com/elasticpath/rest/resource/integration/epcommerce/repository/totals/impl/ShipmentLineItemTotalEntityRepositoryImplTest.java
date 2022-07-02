/*
 * Copyright © 2017 Elastic Path Software Inc. All rights reserved.
 */
package com.elasticpath.rest.resource.integration.epcommerce.repository.totals.impl;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;

import io.reactivex.Single;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import com.elasticpath.money.Money;
import com.elasticpath.rest.ResourceOperationFailure;
import com.elasticpath.rest.definition.base.CostEntity;
import com.elasticpath.rest.definition.purchases.PurchaseIdentifier;
import com.elasticpath.rest.definition.purchases.PurchasesIdentifier;
import com.elasticpath.rest.definition.shipments.ShipmentIdentifier;
import com.elasticpath.rest.definition.shipments.ShipmentLineItemIdentifier;
import com.elasticpath.rest.definition.shipments.ShipmentLineItemsIdentifier;
import com.elasticpath.rest.definition.shipments.ShipmentsIdentifier;
import com.elasticpath.rest.definition.totals.ShipmentLineItemTotalIdentifier;
import com.elasticpath.rest.definition.totals.TotalEntity;
import com.elasticpath.rest.id.type.StringIdentifier;
import com.elasticpath.rest.resource.integration.epcommerce.repository.calc.ShipmentTotalsCalculator;

/**
 * Test for {@link ShipmentLineItemTotalEntityRepositoryImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ShipmentLineItemTotalEntityRepositoryImplTest {

	private static final String SCOPE = "scope";
	private static final String ORDER_ID = "order_id";
	private static final String SHIPMENT_ID = "shipment_id";
	private static final String SHIPMENT_ITEM_ID = "shipment_item_id";
	private static final Money TEN_CAD = Money.valueOf(BigDecimal.TEN, Currency.getInstance("CAD"));

	@Mock
	private ShipmentTotalsCalculator shipmentTotalsCalculator;

	@Mock
	private ConversionService conversionService;

	@InjectMocks
	private ShipmentLineItemTotalEntityRepositoryImpl<TotalEntity, ShipmentLineItemTotalIdentifier> shipmentLineItemTotalEntityRepository;

	@Test
	public void shouldGetTotal() {
		TotalEntity totalEntity = createTotalEntity();
		when(shipmentTotalsCalculator.calculateTotalForLineItem(ORDER_ID, SHIPMENT_ID, SHIPMENT_ITEM_ID)).thenReturn(Single.just(TEN_CAD));
		when(conversionService.convert(TEN_CAD, TotalEntity.class)).thenReturn(totalEntity);

		shipmentLineItemTotalEntityRepository.findOne(createShipmentLineItemTotalIdentifier())
				.test()
				.assertNoErrors()
				.assertValue(totalEntity);
	}

	@Test
	public void shouldNotGetTotal() {
		when(shipmentTotalsCalculator.calculateTotalForLineItem(ORDER_ID, SHIPMENT_ID, SHIPMENT_ITEM_ID))
				.thenReturn(Single.error(ResourceOperationFailure.badRequestBody()));
		shipmentLineItemTotalEntityRepository.findOne(createShipmentLineItemTotalIdentifier())
				.test()
				.assertError(ResourceOperationFailure.badRequestBody());
	}


	private TotalEntity createTotalEntity() {
		final CostEntity costEntity = CostEntity.builder()
				.withAmount(TEN_CAD.getAmount())
				.withCurrency(TEN_CAD.getCurrency().getDisplayName())
				.withDisplay(TEN_CAD.getAmount().toString())
				.build();

		return TotalEntity.builder()
				.withCost(Collections.singletonList(costEntity))
				.build();
	}


	private ShipmentLineItemTotalIdentifier createShipmentLineItemTotalIdentifier() {
		final PurchasesIdentifier purchasesIdentifier = PurchasesIdentifier.builder()
				.withScope(StringIdentifier.of(SCOPE))
				.build();

		final PurchaseIdentifier purchaseIdentifier = PurchaseIdentifier.builder()
				.withPurchaseId(StringIdentifier.of(ORDER_ID))
				.withPurchases(purchasesIdentifier)
				.build();

		final ShipmentsIdentifier shipmentsIdentifier = ShipmentsIdentifier.builder()
				.withPurchase(purchaseIdentifier)
				.build();

		final ShipmentIdentifier shipmentIdentifier = ShipmentIdentifier.builder()
				.withShipmentId(StringIdentifier.of(SHIPMENT_ID))
				.withShipments(shipmentsIdentifier)
				.build();

		final ShipmentLineItemsIdentifier shipmentLineItemsIdentifier = ShipmentLineItemsIdentifier.builder()
				.withShipment(shipmentIdentifier)
				.build();

		final ShipmentLineItemIdentifier shipmentLineItemIdentifier = ShipmentLineItemIdentifier.builder()
				.withShipmentLineItemId(StringIdentifier.of(SHIPMENT_ITEM_ID))
				.withShipmentLineItems(shipmentLineItemsIdentifier)
				.build();

		return ShipmentLineItemTotalIdentifier.builder()
				.withShipmentLineItem(shipmentLineItemIdentifier)
				.build();
	}
}