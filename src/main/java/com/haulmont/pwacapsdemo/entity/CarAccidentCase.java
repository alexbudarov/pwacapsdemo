package com.haulmont.pwacapsdemo.entity;

import io.jmix.core.FileRef;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

@JmixEntity
@Table(name = "CAR_ACCIDENT_CASE")
@Entity
public class CarAccidentCase {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @NotNull
    @InstanceName
    @Column(name = "DESCRIPTION", nullable = false)
    @Lob
    private String description;

    @NotNull
    @Column(name = "ACCIDENT_DATE", nullable = false)
    private OffsetDateTime accidentDate;

    @NotNull
    @Column(name = "PHOTO", nullable = false, length = 1024)
    private FileRef photo;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public OffsetDateTime getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(OffsetDateTime accidentDate) {
        this.accidentDate = accidentDate;
    }

    public FileRef getPhoto() {
        return photo;
    }

    public void setPhoto(FileRef photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}