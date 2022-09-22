package br.com.hcp.service.mapper;

import br.com.hcp.domain.Location;
import br.com.hcp.domain.LocationUser;
import br.com.hcp.service.dto.LocationDTO;
import br.com.hcp.service.dto.LocationUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LocationUser} and its DTO {@link LocationUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationUserMapper extends EntityMapper<LocationUserDTO, LocationUser> {
    @Mapping(target = "location", source = "location", qualifiedByName = "locationName")
    LocationUserDTO toDto(LocationUser s);

    @Named("locationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocationDTO toDtoLocationName(Location location);
}
