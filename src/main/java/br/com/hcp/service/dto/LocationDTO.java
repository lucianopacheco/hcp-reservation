package br.com.hcp.service.dto;

import br.com.hcp.domain.enumeration.LocationType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link br.com.hcp.domain.Location} entity.
 */
public class LocationDTO implements Serializable {

	private static final long serialVersionUID = -595276024137588101L;

	private Long id;
    
    private Long existedId;

    @NotNull
    private String name;

    @NotNull
    private String zipcode;

    private String address;

    @NotNull
    private String number;

    @NotNull
    private String city;

    @NotNull
    private String state;

    private LocationType type;
    
    private Long locationUserId;
    
    public LocationDTO() {
		super();
	}

	public LocationDTO(Long id, @NotNull String name, @NotNull String zipcode, String address,
			@NotNull String number, @NotNull String city, @NotNull String state, LocationType type, Long locationUserId) {
		super();
		this.id = id;
		this.name = name;
		this.zipcode = zipcode;
		this.address = address;
		this.number = number;
		this.city = city;
		this.state = state;
		this.type = type;
		this.locationUserId = locationUserId;
	}
    
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExistedId() {
		return existedId;
	}

	public void setExistedId(Long existedId) {
		this.existedId = existedId;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }
    
    public Long getLocationUserId() {
		return locationUserId;
	}

	public void setLocationUserId(Long locationUserId) {
		this.locationUserId = locationUserId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationDTO)) {
            return false;
        }

        LocationDTO locationDTO = (LocationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, locationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", zipcode='" + getZipcode() + "'" +
            ", address='" + getAddress() + "'" +
            ", number='" + getNumber() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
