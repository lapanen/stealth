package org.lapanen.stealth.spring.rmi.impl;

import java.util.Random;

import org.lapanen.stealth.spring.rmi.api.IntegerService;
import org.lapanen.stealth.spring.rmi.api.StringService;
import org.lapanen.stealth.spring.rmi.api.StringUpdater;

public class ServiceImpl implements StringService, IntegerService, StringUpdater {
    
    private String s;

    @Override
    public String getString() {
        return s;
    }

    @Override
    public Integer getRandom() {
        return new Random().nextInt(Integer.MAX_VALUE);
    }

    @Override
    public void updateString(String s) {
        this.s = s;
    }

}
