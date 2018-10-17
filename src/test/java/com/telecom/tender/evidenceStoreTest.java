package com.telecom.tender;

import com.telecom.tender.service.impl.DepositServiceImpl;

public class evidenceStoreTest {
    public static void main(String[] args) {
        DepositServiceImpl depositService = new DepositServiceImpl();
        depositService.getEvidengceDetail("1573");
    }


}
