package com.aranae.api.celebrity.service;

import com.aranae.api.celebrity.controller.dto.CelebrityCreateRequest;
import com.aranae.api.celebrity.controller.dto.CelebrityResponse;
import com.aranae.api.celebrity.controller.dto.CelebrityUpdateRequest;
import com.aranae.api.celebrity.domain.Celebrity;
import com.aranae.api.common.exception.NotFoundException;
import com.aranae.api.celebrity.repository.CelebrityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional(readOnly = true)
public class CelebrityService {

    private final CelebrityRepository celebrityRepository;

    public CelebrityService(CelebrityRepository celebrityRepository) {
        this.celebrityRepository = celebrityRepository;
    }

    public Page<CelebrityResponse> findAll(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            return celebrityRepository.findByNameContainingIgnoreCase(name, pageable)
                    .map(CelebrityResponse::from);
        }
        return celebrityRepository.findAll(pageable).map(CelebrityResponse::from);
    }

    public CelebrityResponse findById(Long id) {
        Celebrity celebrity = celebrityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 연예인입니다. id=" + id));
        return CelebrityResponse.from(celebrity);
    }

    @Transactional
    public CelebrityResponse create(CelebrityCreateRequest request) {
        Celebrity celebrity = Celebrity.create(
                request.name(), request.groupName(), request.jobType(), request.height()
        );
        return CelebrityResponse.from(celebrityRepository.save(celebrity));
    }

    @Transactional
    public CelebrityResponse update(Long id, CelebrityUpdateRequest request) {
        Celebrity celebrity = celebrityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 연예인입니다. id=" + id));
        celebrity.update(request.name(), request.groupName(), request.jobType(),
                request.height(), request.profileImageKey());
        return CelebrityResponse.from(celebrity);
    }

    @Transactional
    public void delete(Long id) {
        if (!celebrityRepository.existsById(id)) {
            throw new NotFoundException("존재하지 않는 연예인입니다. id=" + id);
        }
        celebrityRepository.deleteById(id);
    }
}
