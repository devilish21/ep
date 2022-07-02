/*
 * Copyright © 2021 Elastic Path Software Inc. All rights reserved.
 */

package com.elasticpath.xpf.converters;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.elasticpath.base.common.dto.StructuredErrorMessage;
import com.elasticpath.base.common.dto.StructuredErrorMessageType;
import com.elasticpath.base.common.dto.StructuredErrorResolution;
import com.elasticpath.xpf.connectivity.dto.XPFStructuredErrorMessage;
import com.elasticpath.xpf.connectivity.dto.XPFStructuredErrorMessageType;
import com.elasticpath.xpf.connectivity.dto.XPFStructuredErrorResolution;

@RunWith(MockitoJUnitRunner.class)
public class StructuredErrorMessageConverterTest {

	@Mock
	private StructuredErrorResolutionConverter structuredErrorResolutionConverter;

	@Mock
	private StructuredErrorMessageTypeConverter structuredErrorMessageTypeConverter;

	@InjectMocks
	public StructuredErrorMessageConverter converter;

	@Mock
	public StructuredErrorResolution structuredErrorResolution;

	@Mock
	public XPFStructuredErrorResolution xpfStructuredErrorResolution;

	@Mock
	public Map<String, String> data;

	@Test
	public void testConvert() {
		when(structuredErrorMessageTypeConverter.convert(XPFStructuredErrorMessageType.ERROR))
				.thenReturn(StructuredErrorMessageType.ERROR);
		when(structuredErrorResolutionConverter.convert(xpfStructuredErrorResolution))
				.thenReturn(structuredErrorResolution);

		XPFStructuredErrorMessage xpfStructuredErrorMessage = new XPFStructuredErrorMessage(
				XPFStructuredErrorMessageType.ERROR, "messageID", "debug message",
				data, xpfStructuredErrorResolution);

		StructuredErrorMessage expectedStructuredErrorMessage = new StructuredErrorMessage(
				StructuredErrorMessageType.ERROR, "messageID", "debug message",
				data, structuredErrorResolution);

		assertThat(converter.convert(xpfStructuredErrorMessage)).isEqualTo(expectedStructuredErrorMessage);
	}
}
