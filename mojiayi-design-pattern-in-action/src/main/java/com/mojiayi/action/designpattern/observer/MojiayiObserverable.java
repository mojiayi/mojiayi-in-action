package com.mojiayi.action.designpattern.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liguangri
 */
public class MojiayiObserverable {
    private int state;
    private final List<MojiayiObserver> observerList = new ArrayList<>();

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void addObserver(MojiayiObserver observer) {
        observerList.add(observer);
    }

    public void removeObserver(MojiayiObserver observer) {
        observerList.remove(observer);
    }

    public void notifyAllObservers() {
        if (state != 1) {
            System.out.println("不是通知的状态");
            return;
        }
        for (MojiayiObserver observer : observerList) {
            observer.doEvent();
        }
    }
}
