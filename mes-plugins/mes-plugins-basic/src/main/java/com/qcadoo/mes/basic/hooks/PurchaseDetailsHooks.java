/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qcadoo.mes.basic.hooks;

import static com.qcadoo.mes.basic.constants.ProductFields.UNIT;
import com.qcadoo.model.api.Entity;
import com.qcadoo.view.api.ViewDefinitionState;
import com.qcadoo.view.api.components.FieldComponent;
import com.qcadoo.view.api.components.LookupComponent;
import org.springframework.stereotype.Service;

/**
 *
 * @author kudlaty01
 */
@Service
public class PurchaseDetailsHooks {
    
    public void fillFieldAboutUnit(final ViewDefinitionState view) {
        LookupComponent lookup = (LookupComponent) view.getComponentByReference("product");
        Entity product = lookup.getEntity();

        FieldComponent productUnit = (FieldComponent) view.getComponentByReference("productUnit");

        if (product != null) {
            productUnit.setFieldValue(product.getField(UNIT));
        } else {
            productUnit.setFieldValue(null);
        }
    }
}
