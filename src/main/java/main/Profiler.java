package main;

public class Profiler {
    private static long startTime, endTime;

    public void startCount(){
        startTime = System.currentTimeMillis();
    }

    public void stopCount(String message){
        endTime = System.currentTimeMillis();
        System.out.println(message + (endTime-startTime) + " ms");
    }

}
