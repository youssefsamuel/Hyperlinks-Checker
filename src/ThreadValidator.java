/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Youssef
 */
public class ThreadValidator extends Thread{
    
    private Link validatingLink;
    private int currentDepth;
    private int finalDepth;
    public static ExecutorService es;
    public static int numberOfThreads = 1;
    
    public ThreadValidator() {
    }

    public ThreadValidator(Link validatingLink, int currentDepth, int finalDepth) {
        this.validatingLink = validatingLink;
        this.currentDepth = currentDepth;
        this.finalDepth = finalDepth;
    }
    
    public Link getValidatingLink() {
        return validatingLink;
    }

    public void setValidatingLink(Link validatingLink) {
        this.validatingLink = validatingLink;
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    public void setCurrentDepth(int currentDepth) {
        this.currentDepth = currentDepth;
    }

    public int getFinalDepth() {
        return finalDepth;
    }

    public void setFinalDepth(int finalDepth) {
        this.finalDepth = finalDepth;
    }
    public static int getNumberOfThreads()  {
        return numberOfThreads;
    }
    public static void setNumberOfThreads(int num) {
        numberOfThreads = num;
    }
 
    @Override
    public void run() {
        try {
            
            validatingLink.validateURLs(currentDepth, finalDepth, getNumberOfThreads());
        } catch (IOException ex) {
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
