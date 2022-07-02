/**
 * Copyright (c) Elastic Path Software Inc., 2007
 */
package com.elasticpath.cmclient.core.dialog.value.cell;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.elasticpath.cmclient.core.dialog.value.dialog.AbstractValueDialog;
import com.elasticpath.cmclient.core.dialog.value.dialog.ShortTextDialog;

/**
 * Short Text (Text Field) cell editor.
 */
public class ShortTextCellEditor extends AbstractCellEditor<String> {

	/**
	 * Construct the Short Text editor.
	 * @param parent the composite
	 * @param value the value
	 */
	public ShortTextCellEditor(final Composite parent, final String value) {
		super(parent, value);
	}
	
	/**
	 * Construct the Short Text editor.
	 * @param parent the composite
	 * @param value the value
	 * @param label the label
	 * @param isLabelBold the flag for style type
	 */
	public ShortTextCellEditor(final Composite parent, final String value, final String label, final boolean isLabelBold) {
		super(parent, value, label, isLabelBold);
	}
	
	/**
	 * Construct the Short Text editor.
	 * @param parent the composite
	 * @param value the value
	 * @param valueRequired true if value is required, false is value is not required
	 * @param label the label
	 * @param isLabelBold the flag for style type
	 */
	public ShortTextCellEditor(final Composite parent, final String value, 
			final boolean valueRequired, final String label, final boolean isLabelBold) {
		super(parent, value, valueRequired, label, isLabelBold);
	}
	
	@Override
	protected AbstractValueDialog<String> getValueDialog(
			final Control cellEditorWindow) {
		return new ShortTextDialog(
				cellEditorWindow.getShell(), this.getInitalValue(), true, this.isValueRequired(), this.getLabel(),
				this.isLabelBold());
	}

}
