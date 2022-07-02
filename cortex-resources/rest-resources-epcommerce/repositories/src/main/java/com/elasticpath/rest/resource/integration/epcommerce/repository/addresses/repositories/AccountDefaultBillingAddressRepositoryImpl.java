/*
 * Copyright © 2020 Elastic Path Software Inc. All rights reserved.
 */
package com.elasticpath.rest.resource.integration.epcommerce.repository.addresses.repositories;

import io.reactivex.Single;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.elasticpath.repository.AliasRepository;
import com.elasticpath.rest.definition.addresses.AccountAddressIdentifier;
import com.elasticpath.rest.definition.addresses.AccountAddressesIdentifier;
import com.elasticpath.rest.definition.addresses.AccountDefaultBillingAddressIdentifier;
import com.elasticpath.rest.definition.addresses.AddressIdentifier;
import com.elasticpath.rest.id.type.StringIdentifier;
import com.elasticpath.rest.resource.integration.epcommerce.repository.customer.CustomerRepository;
import com.elasticpath.rest.resource.integration.epcommerce.repository.transform.ReactiveAdapter;

/**
 * Account Default Billing Address Alias Repository.
 *
 * @param <AI> the alias identifier type
 * @param <I>  the identifier type
 */
@Component
public class AccountDefaultBillingAddressRepositoryImpl<AI extends AccountDefaultBillingAddressIdentifier, I extends AddressIdentifier>
		implements AliasRepository<AccountDefaultBillingAddressIdentifier, AccountAddressIdentifier> {

	private CustomerRepository customerRepository;

	private ReactiveAdapter reactiveAdapter;

	@Override
	public Single<AccountAddressIdentifier> resolve(final AccountDefaultBillingAddressIdentifier accountDefaultBillingAddressIdentifier) {
		AccountAddressesIdentifier accountAddressesIdentifier =
				accountDefaultBillingAddressIdentifier.getAccountBillingAddresses().getAccountAddresses();
		String accountGuid = accountAddressesIdentifier.getAccountId().getValue();
		return customerRepository.getCustomer(accountGuid)
				.flatMap(customer -> reactiveAdapter.fromServiceAsSingle(customer::getPreferredBillingAddress))
				.map(address -> AccountAddressIdentifier.builder()
						.withAccountAddresses(accountAddressesIdentifier)
						.withAccountAddressId(StringIdentifier.of(address.getGuid()))
						.build());
	}

	@Reference
	public void setCustomerRepository(final CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Reference
	public void setReactiveAdapter(final ReactiveAdapter reactiveAdapter) {
		this.reactiveAdapter = reactiveAdapter;
	}
}
