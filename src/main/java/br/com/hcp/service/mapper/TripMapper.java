package br.com.hcp.service.mapper;

import br.com.hcp.domain.Location;
import br.com.hcp.domain.Trip;
import br.com.hcp.domain.Vehicle;
import br.com.hcp.service.dto.LocationDTO;
import br.com.hcp.service.dto.TripDTO;
import br.com.hcp.service.dto.VehicleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trip} and its DTO {@link TripDTO}.
 */
@Mapper(componentModel = "spring")
public interface TripMapper extends EntityMapper<TripDTO, Trip> {
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleModel")
    @Mapping(target = "from", source = "from", qualifiedByName = "locationName")
    @Mapping(target = "to", source = "to", qualifiedByName = "locationName")
    TripDTO toDto(Trip s);

    @Named("vehicleModel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "model", source = "model")
    VehicleDTO toDtoVehicleModel(Vehicle vehicle);

    @Named("locationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocationDTO toDtoLocationName(Location location);
}
