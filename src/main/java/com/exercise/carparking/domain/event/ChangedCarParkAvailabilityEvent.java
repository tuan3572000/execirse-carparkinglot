package com.exercise.carparking.domain.event;

import org.springframework.context.ApplicationEvent;

public class ChangedCarParkAvailabilityEvent extends ApplicationEvent {
    public ChangedCarParkAvailabilityEvent(Object source) {
        super(source);
    }
}
