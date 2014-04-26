/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sbarbaro.filemaster.model;

/**
 *
 * @author ajb
 */
public enum FileAgeUnit {
    MINUTES(60),
    HOURS(60) , 
    DAYS(24), 
    WEEKS(7), 
    MONTHS(30);
    
    FileAgeUnit(int m) {
        setAcc(m);
        this.m = getAcc();
    }
    public final int m;
    
    private static int acc = 1000;
    
    private static void setAcc(int m) {
        acc *= m;
    }
    private static int getAcc() {
        return acc;
    }
    
        @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
