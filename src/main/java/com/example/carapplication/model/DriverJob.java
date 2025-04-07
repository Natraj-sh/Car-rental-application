package com.example.carapplication.model;

import com.example.carapplication.model.Driver;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DriverJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String customerName;
    private String customerLocation;
    private String carName;
    private LocalDate entryDate;
    private LocalDate returnDate;
    private List<String> carLocations;
    @ManyToOne
    @JsonIgnore
    private Driver driver;
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    public enum JobStatus{
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }
}
