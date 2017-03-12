package ru.project;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Task implements Delayed {
    private final LocalDateTime dateTime;
    private final Callable callable;

    public Task(LocalDateTime dateTime, Callable callable) {
        this.dateTime = dateTime;
        this.callable = callable;
    }

    public Callable getCallable() {
        return callable;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(Duration.between(LocalDateTime.now(), dateTime).getSeconds(), TimeUnit.SECONDS);

    }

    @Override
    public int compareTo(Delayed d) {
        return dateTime.compareTo(((Task) d).getDateTime());
    }

    @Override
    public String toString() {
        return "Task{" +
                "callable=" + callable +
                ", dateTime=" + dateTime +
                '}';
    }
}
