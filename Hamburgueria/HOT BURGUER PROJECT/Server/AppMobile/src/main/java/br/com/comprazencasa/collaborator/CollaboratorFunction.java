/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.collaborator;

import java.io.Serializable;

/**
 *
 * @author Marlon
 */
public class CollaboratorFunction implements Serializable{
    
    public static String BUYER = "Buyer" ;
    public static String DELIVERY_MAN = "DeliveryMan" ;
    
    private int id ;
    private double salary ;
    private String companyFunction, responsability;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getCompanyFunction() {
        return companyFunction;
    }

    public void setCompanyFunction(String companyFunction) {
        this.companyFunction = companyFunction;
    }

    public String getResponsability() {
        return responsability;
    }

    public void setResponsability(String responsability) {
        this.responsability = responsability;
    }
}
