package com.c.hotburguerdelivery.Collaborator;

import java.io.Serializable;

public class CollaboratorFunction implements Serializable {

    public static final String F_BUYER = "Buyer";
    public static final String F_DELIVERYMAN = "Deliveryman";

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
