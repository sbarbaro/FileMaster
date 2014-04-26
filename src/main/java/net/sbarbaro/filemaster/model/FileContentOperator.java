/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sbarbaro.filemaster.model;

/**
 *
 * @author sab
 */
public enum FileContentOperator {
    

    CONTAINS("Contains"),
    NOT_CONTAINS("Does not contain"),
    MATCHES("Matches reqular expression");
    
    FileContentOperator(String text) {
        this.text = text;
    }
    
    private final String text;
    
    @Override
    public String toString() {
        return text;
    }
}
