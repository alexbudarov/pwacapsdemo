package com.haulmont.pwacapsdemo.component;

import java.time.Instant;

public record TimedCoordinates(double latitude, double longitude, Instant timestamp) {
}
