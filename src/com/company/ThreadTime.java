package com.company;

public class ThreadTime extends Thread {

    public String time;

    public void run() {
        while (true) {
            for (int i = 0; i >= 0; i++) {
                for (int j = 0; j < 60; j++) {
                    time = i + ":" + j;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}

