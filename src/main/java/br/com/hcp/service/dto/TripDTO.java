package br.com.hcp.service.dto;

import br.com.hcp.domain.enumeration.LocationType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.hcp.domain.Trip} entity.
 */
public class TripDTO implements Serializable {

	private static final long serialVersionUID = 9086863735871820236L;

	private Long id;

    private String driverLogin;

    @NotNull
    private Instant whenDateTime;

    private LocationType destinationType;

    @NotNull
    private Integer availableSeats;

    @NotNull
    private BigDecimal price;

    private String meetingPoint;

    private Instant createdAt;

    private VehicleDTO vehicle;

    private LocationDTO from;

    private LocationDTO to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverLogin() {
        return driverLogin;
    }

    public void setDriverLogin(String driverLogin) {
        this.driverLogin = driverLogin;
    }

    public Instant getWhenDateTime() {
        return whenDateTime;
    }

    public void setWhenDateTime(Instant whenDateTime) {
        this.whenDateTime = whenDateTime;
    }

    public LocationType getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(LocationType destinationType) {
        this.destinationType = destinationType;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMeetingPoint() {
        return meetingPoint;
    }

    public void setMeetingPoint(String meetingPoint) {
        this.meetingPoint = meetingPoint;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public VehicleDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDTO vehicle) {
        this.vehicle = vehicle;
    }

    public LocationDTO getFrom() {
        return from;
    }

    public void setFrom(LocationDTO from) {
        this.from = from;
    }

    public LocationDTO getTo() {
        return to;
    }

    public void setTo(LocationDTO to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripDTO)) {
            return false;
        }

        TripDTO tripDTO = (TripDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tripDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripDTO{" +
            "id=" + getId() +
            ", driverLogin='" + getDriverLogin() + "'" +
            ", whenDateTime='" + getWhenDateTime() + "'" +
            ", destinationType='" + getDestinationType() + "'" +
            ", availableSeats=" + getAvailableSeats() +
            ", price=" + getPrice() +
            ", meetingPoint='" + getMeetingPoint() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", vehicle=" + getVehicle() +
            ", from=" + getFrom() +
            ", to=" + getTo() +
            "}";
    }
}
