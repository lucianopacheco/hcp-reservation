package br.com.hcp.service.mapper;

import br.com.hcp.domain.Reservation;
import br.com.hcp.domain.Trip;
import br.com.hcp.service.dto.ReservationDTO;
import br.com.hcp.service.dto.TripDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservation} and its DTO {@link ReservationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {
    @Mapping(target = "trip", source = "trip", qualifiedByName = "tripId")
    ReservationDTO toDto(Reservation s);

    @Named("tripId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TripDTO toDtoTripId(Trip trip);
}
