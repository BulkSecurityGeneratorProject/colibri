package com.project.colibri.service.impl;

import com.project.colibri.service.ProfileService;
import com.project.colibri.domain.Profile;
import com.project.colibri.repository.ProfileRepository;
import com.project.colibri.service.dto.ProfileDTO;
import com.project.colibri.service.mapper.ProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/**
 * Service Implementation for managing Profile.
 */
@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    /**
     * Save a profile.
     *
     * @param profileDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProfileDTO save(ProfileDTO profileDTO) {
        log.debug("Request to save Profile : {}", profileDTO);
        Profile profile = profileMapper.toEntity(profileDTO);
        profile = profileRepository.save(profile);
        return profileMapper.toDto(profile);
    }

    /**
     * Get all the profiles.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProfileDTO> findAll() {
        log.debug("Request to get all Profiles");
        return profileRepository.findAll().stream()
            .map(profileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
     *  get all the profiles where Association is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProfileDTO> findAllWhereAssociationIsNull() {
        log.debug("Request to get all profiles where Association is null");
        return StreamSupport
            .stream(profileRepository.findAll().spliterator(), false)
            .filter(profile -> profile.getAssociation() == null)
            .map(profileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one profile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileDTO> findOne(Long id) {
        log.debug("Request to get Profile : {}", id);
        return profileRepository.findById(id)
            .map(profileMapper::toDto);
    }

    /**
     * Delete the profile by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Profile : {}", id);
        profileRepository.deleteById(id);
    }

    /**
     * Get one profile by id.
     *
     * @param userId the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileDTO> findByUserId(Long userId) {
        log.debug("Request to get Profile : {}", userId);
        return profileRepository.findByUserId(userId)
            .map(profileMapper::toDto);
    }
}
