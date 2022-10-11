package br.com.hcp.domain;

import br.com.hcp.domain.enumeration.LocationType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Trip.
 */
@Entity
@Table(name = "trip")
public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "driver_login", nullable = false)
    private String driverLogin;

    @NotNull
    @Column(name = "when_date_time", nullable = false)
    private Instant whenDateTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "destination_type", nullable = false)
    private LocationType destinationType;

    @NotNull
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "meeting_point")
    private String meetingPoint;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    private Vehicle vehicle;

    @ManyToOne(optional = false)
    @NotNull
    private Location from;

    @ManyToOne(optional = false)
    @NotNull
    private Location to;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverLogin() {
        return this.driverLogin;
    }

    public Trip driverLogin(String driverLogin) {
        this.setDriverLogin(driverLogin);
        return this;
    }

    public void setDriverLogin(String driverLogin) {
        this.driverLogin = driverLogin;
    }

    public Instant getWhenDateTime() {
        return this.whenDateTime;
    }

    public Trip whenDateTime(Instant whenDateTime) {
        this.setWhenDateTime(whenDateTime);
        return this;
    }

    public void setWhenDateTime(Instant whenDateTime) {
        this.whenDateTime = whenDateTime;
    }

    public LocationType getDestinationType() {
        return this.destinationType;
    }

    public Trip destinationType(LocationType destinationType) {
        this.setDestinationType(destinationType);
        return this;
    }

    public void setDestinationType(LocationType destinationType) {
        this.destinationType = destinationType;
    }

    public Integer getAvailableSeats() {
        return this.availableSeats;
    }

    public Trip availableSeats(Integer availableSeats) {
        this.setAvailableSeats(availableSeats);
        return this;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Trip price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMeetingPoint() {
        return this.meetingPoint;
    }

    public Trip meetingPoint(String meetingPoint) {
        this.setMeetingPoint(meetingPoint);
        return this;
    }

    public void setMeetingPoint(String meetingPoint) {
        this.meetingPoint = meetingPoint;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Trip createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Trip vehicle(Vehicle vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    public Location getFrom() {
        return this.from;
    }

    public void setFrom(Location location) {
        this.from = location;
    }

    public Trip from(Location location) {
        this.setFrom(location);
        return this;
    }

    public Location getTo() {
        return this.to;
    }

    public void setTo(Location location) {
        this.to = location;
    }

    public Trip to(Location location) {
        this.setTo(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trip)) {
            return false;
        }
        return id != null && id.equals(((Trip) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trip{" +
            "id=" + getId() +
            ", driverLogin='" + getDriverLogin() + "'" +
            ", whenDateTime='" + getWhenDateTime() + "'" +
            ", destinationType='" + getDestinationType() + "'" +
            ", availableSeats=" + getAvailableSeats() +
            ", price=" + getPrice() +
            ", meetingPoint='" + getMeetingPoint() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
