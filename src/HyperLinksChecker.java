/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 *
 * @author Youssef
 */
public class HyperLinksChecker {
    private  int optimalThreads;
    private long minimumTime;
    private ArrayList<Long>time ;

    public int getOptimalThreads() {
        return optimalThreads;
    }

    public void setOptimalThreads(int optimalThreads) {
        this.optimalThreads = optimalThreads;
    }

    public long getMinimumTime() {
        return minimumTime;
    }

    public void setMinimumTime(long minimumTime) {
        this.minimumTime = minimumTime;
    }
    
    public ArrayList<Long> getTime(){
        return time;
    }
    
    public static void main(String[] args) throws IOException, InterruptedException 
    {
        InputFrame frame = new InputFrame();
    }
  
    public void startChecking(Link parentLink, int depth) throws IOException, InterruptedException
    {
        time = new ArrayList<>();
        int valid=0, invalid=0;
        long start, end, duration = 0;
        boolean finish = false;
        int i = 1;
        do{
            start = System.currentTimeMillis();
           // start = (TimeUnit.MILLISECONDS.toSeconds(start));
            ThreadValidator.setNumberOfThreads(i);
            if (ThreadValidator.getNumberOfThreads() == 1)
            {
                parentLink.validateURLs(0, depth, ThreadValidator.getNumberOfThreads());
                System.out.println("Valid = " + (Link.getNumValidURLs()-1));
                System.out.println("Invalid = " + Link.getNumInvalidURLs());
            }
            else{
                ThreadValidator.es = Executors.newFixedThreadPool(ThreadValidator.getNumberOfThreads());
                parentLink.validateURLs(0, depth, ThreadValidator.getNumberOfThreads());
                while (((ThreadPoolExecutor) ThreadValidator.es).getActiveCount() != 0)
                {
                
                }
                ThreadValidator.es.shutdown();
                if(ThreadValidator.es.awaitTermination(10, TimeUnit.HOURS))
                    {
                        System.out.println("Valid = " + (Link.getNumValidURLs()-1));
                        System.out.println("Invalid = " + Link.getNumInvalidURLs());
                        valid = Link.getNumValidURLs()-1;
                        invalid = Link.getNumInvalidURLs();
                    }
                }
            
                end = System.currentTimeMillis();
               // end = (TimeUnit.MILLISECONDS.toSeconds(end));
                time.add((end-start));
                if ((Math.abs(duration - (end - start)) < 400)&& i!=1)
                {
                    duration = end - start;
                    this.optimalThreads = ThreadValidator.getNumberOfThreads();
                    this.minimumTime = duration;
                    finish = true;
                }
                else if(((end - start) > duration)&& i!=1)
                {
                    optimalThreads = (ThreadValidator.getNumberOfThreads()-1);
                    minimumTime=duration;
                    duration = end - start;
                    finish = true;
                }
                else{
                    duration = end - start;
                }
                System.out.println("Time with " + i + " thread(s) = " +  duration + " milliseconds") ;
                i++;
                Link.setNumInvalidURLs(0);
                Link.setNumValidURLs(0);
            } while(!finish);
         
        System.out.println("Minumum Time = " + minimumTime);
        System.out.println("Optimal Threads = " + optimalThreads);
        OutputFrame frameOut = new OutputFrame(valid, invalid, optimalThreads, minimumTime, time);
        frameOut.setVisible(true);
        frameOut.setLocationRelativeTo(null);
        }   
}

