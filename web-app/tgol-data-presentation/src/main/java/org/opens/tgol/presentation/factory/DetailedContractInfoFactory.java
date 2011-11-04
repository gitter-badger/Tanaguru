/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2011  Open-S Company
 *
 * This file is part of Tanaguru.
 *
 * Tanaguru is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: open-s AT open-s DOT com
 */
package org.opens.tgol.presentation.factory;

import org.opens.tgol.entity.contract.Act;
import org.opens.tgol.entity.contract.Contract;
import org.opens.tgol.entity.product.ScopeEnum;
import org.opens.tgol.presentation.data.DetailedContractInfo;
import org.opens.tgol.presentation.data.DetailedContractInfoImpl;
import java.util.Collection;

/**
 *
 * @author jkowalczyk
 */
public final class DetailedContractInfoFactory extends ContractInfoFactory {

    /**
     * The maximum number of acts associated with each Contract.
     */
    private int nbMaxActInfo = 5;

    public int getNbMaxActInfo() {
        return nbMaxActInfo;
    }

    public void setNbMaxActInfo(int nbMaxActInfo) {
        this.nbMaxActInfo = nbMaxActInfo;
    }

//    private ContractActionHandler contractActionHandler;
//    public ContractActionHandler getContractActionHandler() {
//        return contractActionHandler;
//    }
//
//    @Autowired
//    public void setContractActionHandler(ContractActionHandler contractActionHandler) {
//        this.contractActionHandler = contractActionHandler;
//    }

    /**
     * The unique shared instance of DetailedContractInfoFactory
     */
    private static DetailedContractInfoFactory detailedContractInfoFactory;

    /**
     * Default private constructor
     */
    private DetailedContractInfoFactory() {}

    public static synchronized DetailedContractInfoFactory getInstance() {
        if (detailedContractInfoFactory == null) {
            detailedContractInfoFactory = new DetailedContractInfoFactory();
        }
        return detailedContractInfoFactory;
    }

    public DetailedContractInfo getDetailedContractInfo(Contract contract) {
        DetailedContractInfo detailedContractInfo = new DetailedContractInfoImpl();

        detailedContractInfo = (DetailedContractInfoImpl) setBasicContractInfo(contract, detailedContractInfo);

        detailedContractInfo.setContractCreationDate(contract.getBeginDate());

        detailedContractInfo = (DetailedContractInfoImpl) setAuditResultTrend(contract, detailedContractInfo);

//        detailedContractInfo.setContractActionList(contractActionHandler.getActionList(contract));
//                setActionAccessibility(contract.getProduct(), detailedContractInfo);

        detailedContractInfo = (DetailedContractInfoImpl) setLastActInfo(contract, detailedContractInfo);

        detailedContractInfo = setNLastActInfo(contract, detailedContractInfo);
        
        return detailedContractInfo;
    }

    /**
     * Sets the act info of the n last acts launched from this contract
     *
     * @param contract
     * @param detailedContractInfo
     * @return
     */
    protected DetailedContractInfo setNLastActInfo(Contract contract, DetailedContractInfo detailedContractInfo) {
        int nb0fAct = getActDataService().getNumberOfAct(contract);
        if (nb0fAct > nbMaxActInfo) {
            nb0fAct = nbMaxActInfo;
        }
        detailedContractInfo.setNumberOfAct(nb0fAct);
        Collection<Act> lastActSet = getActDataService().getActsByContract(contract, nb0fAct, 2, null, false);
        for (Act act : lastActSet) {
            detailedContractInfo.addActInfo(ActInfoFactory.getInstance().getActInfo(act));
        }
        detailedContractInfo.setNumberOfDisplayedAct(lastActSet.size());
        detailedContractInfo.setSiteActInfoSet(ActInfoFactory.getInstance().getActInfoSet(
                getActDataService().getActsByContract(contract, -1, 1, ScopeEnum.DOMAIN, true)));
        return detailedContractInfo;
    }

}