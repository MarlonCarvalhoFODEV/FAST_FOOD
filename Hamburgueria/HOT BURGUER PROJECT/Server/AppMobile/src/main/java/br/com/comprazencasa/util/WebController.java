/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.util;

/**
 *
 * @author Marlon
 */
public class WebController {

    public static final String IMAGE_CONTROLLER = "Image/";
    public static final String SERVICE_CONTROLLER = "Service/";
    public static final String SCHEDULER_CONTROLLER = "Scheduler/";
    public static final String PROFESSIONAL_CONTROLLER = "Professional/";
    public static final String GALERY = "Galery/";
    public static final String AVAILABLE_HOUR = "AvailableHour/";

    public static class Images{
        public static final String INSERT = "insert/";
        public static final String GET = "get/";
        public static final String DELETE = "delete/";
        public static final String UPDATE = "update/";
    }

    public static class Collaborators{
        public static final String COLL_CONTROLLER = "Collaborator/";
        public static final String GET_ALL_COLL_IDS = "get-all-coll-ids/";
    }


    
    public static class Methods {
        public static String GET = "GET" ;
        public static String POST = "POST" ;
        public static String DELETE = "DELETE" ;
        public static String UPDATE = "UPDATE" ;
    }
}