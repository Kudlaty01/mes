/*
 ***************************************************************************
 Copyright (c) 2015 Kudlaty01
 Project: Qcadoo MES Basic Plugin
 Version: 1.3
 ***************************************************************************    
 */
package com.qcadoo.mes.basic.hooks;

import static com.qcadoo.mes.basic.constants.PurchaseFields.PRODUCT;
import static com.qcadoo.mes.basic.constants.PurchaseFields.PRICE;
import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.Entity;
import com.qcadoo.model.api.search.SearchCriteriaBuilder;
import com.qcadoo.model.api.search.SearchProjections;
import com.qcadoo.model.api.search.SearchRestrictions;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kudlaty01
 */
@Service
public class PurchaseHooks {

    public boolean checkIfNoSameProductAndPrice(final DataDefinition purchaseDD, final Entity purchase) {
        SearchCriteriaBuilder scb = purchaseDD.find();
        scb.setProjection(SearchProjections.id());
        scb.add(SearchRestrictions.belongsTo(PRODUCT, purchase.getBelongsToField(PRODUCT)));
        scb.add(SearchRestrictions.eq(PRICE, purchase.getDecimalField(PRICE)));

        if (scb.setMaxResults(1).uniqueResult() != null) {
            purchase.addError(purchaseDD.getField(PRODUCT), "basic.purchase.sameProductAndPriceExist");
            return false;
        }

        return true;
    }
}
