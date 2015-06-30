/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qcadoo.mes.basic.listeners;

import com.qcadoo.mes.basic.hooks.PurchaseDetailsHooks;
import com.qcadoo.view.api.ComponentState;
import com.qcadoo.view.api.ViewDefinitionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kudlaty01
 */
@Service
public class PurchaseDetailsListener {

    @Autowired
    private PurchaseDetailsHooks detailsHooks;

    public void fillFieldAboutUnit(final ViewDefinitionState view, final ComponentState state, final String[] args) {
        detailsHooks.fillFieldAboutUnit(view);
    }

}
