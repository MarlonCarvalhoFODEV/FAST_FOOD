package br.com.comprazencasa.analyrics;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class CzcAnalytics implements Serializable {

    private final TimeCounter counter ;

    public CzcAnalytics(){
        this.counter = new TimeCounter();
    }

    public TimeCounter getCounter() {
        return counter;
    }

    public class TimeCounter extends Thread{

        private int secound, minutes, hours ;

        private void delay(int delayInMills){
            try {
                Thread.sleep(delayInMills);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private int getSecound() {
            return secound;
        }

        private int getMinutes() {
            return minutes;
        }

        private int getHours() {
            return hours;
        }

        public String getFullTime(){
            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
            StringBuilder b = new StringBuilder();
            b.append(getHours());
            b.append(":");
            b.append(getMinutes());
            b.append(":");
            b.append(getSecound());

            return format.format(b.toString());
        }

        @Override
        public void run() {
            while(hours <= 24){

                secound++;

                if(secound == 60){
                    minutes++ ;
                    secound = 0 ;
                } else if(minutes == 60){
                    minutes = 0 ;
                    secound = 0 ;
                    hours++;
                } else if( hours == 24){
                    minutes = 0 ;
                    secound = 0 ;
                    hours = 0 ;
                }

                delay(1000);
            }
        }
    }
}
