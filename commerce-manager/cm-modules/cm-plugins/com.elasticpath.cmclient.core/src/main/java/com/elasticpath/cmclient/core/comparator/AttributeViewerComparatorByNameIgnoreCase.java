/**
 * Copyright (c) Elastic Path Software Inc., 2017
 */
package com.elasticpath.cmclient.core.comparator;

import java.util.Locale;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.elasticpath.cmclient.core.CorePlugin;
import com.elasticpath.domain.attribute.Attribute;

/**
 * Sorts attributes by name (ignore case).
 */
public class AttributeViewerComparatorByNameIgnoreCase extends ViewerComparator {

	@Override
	public int compare(final Viewer viewer, final Object object1, final Object object2) {
		int cat1 = category(object1);
		int cat2 = category(object2);
		
		if (cat1 != cat2) {
			return cat1 - cat2;
		}
		
		Attribute attribute1 = (Attribute) object1;
		Attribute attribute2 = (Attribute) object2;
		Locale locale = CorePlugin.getDefault().getDefaultLocale();
		
		String name1 = attribute1.getDisplayName(locale, false, false);
		String name2 = attribute2.getDisplayName(locale, false, false);
		
		if (name1 == null) {
			name1 = ""; //$NON-NLS-1$
		}
		if (name2 == null) {
			name2 = ""; //$NON-NLS-1$
		}
		return name1.compareToIgnoreCase(name2);
	}

}
