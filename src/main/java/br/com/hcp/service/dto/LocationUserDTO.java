package br.com.hcp.service.dto;

import br.com.hcp.domain.enumeration.LocationType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.hcp.domain.LocationUser} entity.
 */
public class LocationUserDTO implements Serializable {

    private Long id;

    private String login;

    private LocationType locationType;

    private LocationDTO location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationUserDTO)) {
            return false;
        }

        LocationUserDTO locationUserDTO = (LocationUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, locationUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationUserDTO{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", locationType='" + getLocationType() + "'" +
            ", location=" + getLocation() +
            "}";
    }
}
