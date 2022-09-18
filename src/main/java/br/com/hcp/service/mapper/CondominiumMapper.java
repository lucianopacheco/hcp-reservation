package br.com.hcp.service.mapper;

import br.com.hcp.domain.Condominium;
import br.com.hcp.service.dto.CondominiumDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Condominium} and its DTO {@link CondominiumDTO}.
 */
@Mapper(componentModel = "spring")
public interface CondominiumMapper extends EntityMapper<CondominiumDTO, Condominium> {}
