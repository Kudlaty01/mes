/*
 ***************************************************************************
 Copyright (c) 2015 Kudlaty01
 Project: Qcadoo MES Basic Plugin
 Version: 1.3
 ***************************************************************************    
 */
package com.qcadoo.mes.basic;

import com.qcadoo.mes.basic.constants.BasicConstants;
import static com.qcadoo.mes.basic.constants.PurchaseFields.PRICE;
import static com.qcadoo.mes.basic.constants.PurchaseFields.PRODUCT;
import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.DataDefinitionService;
import com.qcadoo.model.api.Entity;
import com.qcadoo.model.api.search.SearchCriteriaBuilder;
import com.qcadoo.model.api.search.SearchProjections;
import com.qcadoo.model.api.search.SearchRestrictions;
import com.qcadoo.model.api.search.SearchResult;
import com.qcadoo.view.api.ComponentState;
import com.qcadoo.view.api.ViewDefinitionState;
import com.qcadoo.view.api.components.FormComponent;
import com.qcadoo.view.api.components.GridComponent;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kudlaty01
 */
@Service
public class PurchaseService {

    private static final String L_FORM = "form";

    @Autowired
    private DataDefinitionService dataDefinitionService;

    private DataDefinition getPurchaseDD() {
        return dataDefinitionService.get(BasicConstants.PLUGIN_IDENTIFIER, BasicConstants.MODEL_PURCHASE);
    }

    public void getAveragePriceOfAll(final ViewDefinitionState view, final ComponentState state, final String[] args) {
        final GridComponent purchasesGrid = (GridComponent) view.getComponentByReference("grid");

        final List<Entity> purchases = purchasesGrid.getEntities();
        if (purchases.isEmpty()) {
        }

        BigDecimal sum = new BigDecimal(0);
        for (Entity purchase : purchases) {
            sum = sum.add(purchase.getDecimalField(PRICE));
        }
        BigDecimal result = sum.divide(new BigDecimal(purchases.size()));
        purchasesGrid.addMessage("basic.purchasesList.message.getAveragePricesAll", ComponentState.MessageType.SUCCESS, result.toString());
    }

    public void getAveragePriceOfProduct(final ViewDefinitionState view, final ComponentState state, final String[] args) {
        final FormComponent purchaseForm = (FormComponent) view.getComponentByReference(L_FORM);
        if (purchaseForm.getEntityId() == null) {
            return;
        }

        final Entity purchase = purchaseForm.getEntity();

        SearchCriteriaBuilder scb = purchase.getDataDefinition().find();
        scb.setProjection(SearchProjections.id());
        scb.add(SearchRestrictions.belongsTo(PRODUCT, purchase.getBelongsToField(PRODUCT)));
        SearchResult result = scb.list();
        List<Entity> purchases = result.getEntities();
        if(purchases.isEmpty())
            return;
        BigDecimal sum = new BigDecimal(0);
        for (Entity purchaseEntity : purchases) {
        purchaseForm.addMessage("basic.purchasesList.message.getAveragePriceOfProduct", ComponentState.MessageType.SUCCESS, purchaseEntity.getDecimalField("quantity").toString());
            sum = sum.add(purchaseEntity.getDecimalField(PRICE));
        }
        BigDecimal sResult = sum.divide(new BigDecimal(purchases.size()));
        purchaseForm.addMessage("basic.purchasesList.message.getAveragePriceOfProduct", ComponentState.MessageType.SUCCESS, sResult.toString());
    }
}
