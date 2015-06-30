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
import com.qcadoo.model.api.search.SearchOrders;
import com.qcadoo.model.api.search.SearchProjection;
import com.qcadoo.model.api.search.SearchProjections;
import com.qcadoo.model.api.search.SearchRestrictions;
import com.qcadoo.view.api.ComponentState;
import com.qcadoo.view.api.ViewDefinitionState;
import com.qcadoo.view.api.components.FormComponent;
import com.qcadoo.view.api.components.GridComponent;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private static final String AVG_PRICE_PROJECTION_ALIAS = "avgPriceProjection";

    @Autowired
    private DataDefinitionService dataDefinitionService;

    private DataDefinition getPurchaseDD() {
        return dataDefinitionService.get(BasicConstants.PLUGIN_IDENTIFIER, BasicConstants.MODEL_PURCHASE);
    }

    public void getAveragePriceOfAll(final ViewDefinitionState view, final ComponentState state, final String[] args) {
        final GridComponent purchasesGrid = (GridComponent) view.getComponentByReference("grid");
        List<Entity> selectedPurchases = purchasesGrid.getSelectedEntities();
        
        if (!selectedPurchases.isEmpty()) {
            
            BigDecimal average = selectedPurchases.stream().map(x->x.getDecimalField(PRICE)).reduce(BigDecimal.ZERO, (a,b)->a.add(b)).divide(new BigDecimal(selectedPurchases.size()),4,RoundingMode.HALF_UP);
            purchasesGrid.addMessage("basic.purchasesList.message.getAveragePricesSelected", ComponentState.MessageType.SUCCESS, average.toString());
            
        } else {
            SearchCriteriaBuilder scb = getPurchaseDD().find();
            SearchProjection average = SearchProjections.alias(SearchProjections.avg(PRICE),
                    AVG_PRICE_PROJECTION_ALIAS);
            scb.setProjection(SearchProjections.list().add(average));
            // for compatibility purposes (projection error)
            scb.addOrder(SearchOrders.asc(AVG_PRICE_PROJECTION_ALIAS));
            Entity avgPrice = scb.setMaxResults(1).uniqueResult();
            purchasesGrid.addMessage("basic.purchasesList.message.getAveragePricesAll", ComponentState.MessageType.SUCCESS, avgPrice.getField(AVG_PRICE_PROJECTION_ALIAS).toString());
        }

    }

    public void getAveragePriceOfProduct(final ViewDefinitionState view, final ComponentState state, final String[] args) {
        final FormComponent purchaseForm = (FormComponent) view.getComponentByReference(L_FORM);

        if (purchaseForm.getEntityId() == null) {
            return;
        }

        final Entity purchase = purchaseForm.getEntity();
        Entity product = purchase.getBelongsToField(PRODUCT);
        Object result = CalculateAverageProductPrice(product);
        purchaseForm.addMessage("basic.purchasesList.message.getAveragePriceOfProduct", ComponentState.MessageType.SUCCESS, result.toString());
    }
    
    public Object CalculateAverageProductPrice(final Entity product){
        SearchProjection average = SearchProjections.alias(SearchProjections.avg(PRICE),
                AVG_PRICE_PROJECTION_ALIAS);
        SearchCriteriaBuilder scb = getPurchaseDD().find().setProjection(SearchProjections.list().add(average));
        scb.add(SearchRestrictions.belongsTo(PRODUCT, product));
        scb.addOrder(SearchOrders.asc(AVG_PRICE_PROJECTION_ALIAS));
        Entity avgPrice = scb.setMaxResults(1).uniqueResult();
        return avgPrice.getField(AVG_PRICE_PROJECTION_ALIAS);
    }
}
