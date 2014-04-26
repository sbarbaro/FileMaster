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
public enum FileAgeOperator {
    
    OLDER("More than"),
    YOUNGER("Less than");
    
    FileAgeOperator(String text) {
        this.text = text;
    }
    public final String text;

    @Override
    public String toString() {
        return text;
    }
}
