/*
 * Copyright © 2018 Elastic Path Software Inc. All rights reserved.
 */
package com.elasticpath.rest.resource.items.lookups.prototypes;

import io.reactivex.Single;

import com.elasticpath.rest.ResourceInfo;
import com.elasticpath.rest.definition.items.LookupItemFormResource;
import com.elasticpath.rest.resource.items.constant.ItemsResourceConstants;

/**
 * Item Lookup Form prototype for Info operation.
 */
public class InfoItemLookupFormPrototype implements LookupItemFormResource.Info {

	private static final Single<ResourceInfo> INFO_SINGLE = Single.just(ResourceInfo.builder()
			.withMaxAge(ItemsResourceConstants.MAX_AGE)
			.build());

	@Override
	public Single<ResourceInfo> onInfo() {
		return INFO_SINGLE;
	}
}
