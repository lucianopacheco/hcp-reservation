package br.com.hcp.domain;

import br.com.hcp.domain.enumeration.LocationType;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A LocationUser.
 */
@Entity
@Table(name = "location_user")
public class LocationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "login")
    private String login;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type")
    private LocationType locationType;

    @ManyToOne
    private Location location;
    
    public LocationUser() {
		super();
	}

	public LocationUser(String login, LocationType locationType, Location location) {
		super();
		this.login = login;
		this.locationType = locationType;
		this.location = location;
	}

	// jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LocationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public LocationUser login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocationType getLocationType() {
        return this.locationType;
    }

    public LocationUser locationType(LocationType locationType) {
        this.setLocationType(locationType);
        return this;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocationUser location(Location location) {
        this.setLocation(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationUser)) {
            return false;
        }
        return id != null && id.equals(((LocationUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationUser{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", locationType='" + getLocationType() + "'" +
            "}";
    }
}
