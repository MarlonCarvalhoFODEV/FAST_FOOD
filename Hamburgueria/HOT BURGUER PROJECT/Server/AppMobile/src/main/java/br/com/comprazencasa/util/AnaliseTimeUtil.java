/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.util;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Marlon
 */
public class AnaliseTimeUtil {
    
    private long startTime, andTime;;

    public long getResultTime_MILLSEGS() {
        
        return TimeUnit.MILLISECONDS.convert(andTime - startTime, TimeUnit.NANOSECONDS);
    }

    public void startTime() {
        this.startTime = System.nanoTime();
    }

    public void andTime() {
        this.andTime = System.nanoTime();
    }
    
    
}
