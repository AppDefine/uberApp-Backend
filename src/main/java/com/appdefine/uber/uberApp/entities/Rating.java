package com.appdefine.uber.uberApp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_rating_rider", columnList = "rider_id"),
        @Index(name = "idx_rating_driver", columnList = "driver_id")
})
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne
    private Ride ride;

    @ManyToOne
    private Rider rider;

    @ManyToOne
    private Driver driver;

    private Integer driverRating; //rating for driver
    private Integer riderRating; //rating for rider
}
