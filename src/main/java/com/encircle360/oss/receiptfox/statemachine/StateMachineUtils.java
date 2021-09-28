package com.encircle360.oss.receiptfox.statemachine;

import com.encircle360.oss.receiptfox.model.AbstractEntity;

public class StateMachineUtils {

    private StateMachineUtils() {}

    public static  <T extends AbstractEntity> String id(T entity) {
        return entity.getClass().getName() + "-" + entity.getId();
    }
}
