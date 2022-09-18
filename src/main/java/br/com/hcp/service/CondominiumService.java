package br.com.hcp.service;

import br.com.hcp.domain.Condominium;
import br.com.hcp.repository.CondominiumRepository;
import br.com.hcp.service.dto.CondominiumDTO;
import br.com.hcp.service.mapper.CondominiumMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Condominium}.
 */
@Service
@Transactional
public class CondominiumService {

    private final Logger log = LoggerFactory.getLogger(CondominiumService.class);

    private final CondominiumRepository condominiumRepository;

    private final CondominiumMapper condominiumMapper;

    public CondominiumService(CondominiumRepository condominiumRepository, CondominiumMapper condominiumMapper) {
        this.condominiumRepository = condominiumRepository;
        this.condominiumMapper = condominiumMapper;
    }

    /**
     * Save a condominium.
     *
     * @param condominiumDTO the entity to save.
     * @return the persisted entity.
     */
    public CondominiumDTO save(CondominiumDTO condominiumDTO) {
        log.debug("Request to save Condominium : {}", condominiumDTO);
        Condominium condominium = condominiumMapper.toEntity(condominiumDTO);
        condominium = condominiumRepository.save(condominium);
        return condominiumMapper.toDto(condominium);
    }

    /**
     * Update a condominium.
     *
     * @param condominiumDTO the entity to save.
     * @return the persisted entity.
     */
    public CondominiumDTO update(CondominiumDTO condominiumDTO) {
        log.debug("Request to save Condominium : {}", condominiumDTO);
        Condominium condominium = condominiumMapper.toEntity(condominiumDTO);
        condominium = condominiumRepository.save(condominium);
        return condominiumMapper.toDto(condominium);
    }

    /**
     * Partially update a condominium.
     *
     * @param condominiumDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CondominiumDTO> partialUpdate(CondominiumDTO condominiumDTO) {
        log.debug("Request to partially update Condominium : {}", condominiumDTO);

        return condominiumRepository
            .findById(condominiumDTO.getId())
            .map(existingCondominium -> {
                condominiumMapper.partialUpdate(existingCondominium, condominiumDTO);

                return existingCondominium;
            })
            .map(condominiumRepository::save)
            .map(condominiumMapper::toDto);
    }

    /**
     * Get all the condominiums.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CondominiumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Condominiums");
        return condominiumRepository.findAll(pageable).map(condominiumMapper::toDto);
    }

    /**
     * Get one condominium by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CondominiumDTO> findOne(Long id) {
        log.debug("Request to get Condominium : {}", id);
        return condominiumRepository.findById(id).map(condominiumMapper::toDto);
    }

    /**
     * Delete the condominium by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Condominium : {}", id);
        condominiumRepository.deleteById(id);
    }
}
