package com.aranae.api.celebrity.controller;

import com.aranae.api.celebrity.controller.dto.CelebrityCreateRequest;
import com.aranae.api.celebrity.controller.dto.CelebrityResponse;
import com.aranae.api.celebrity.controller.dto.CelebrityUpdateRequest;
import com.aranae.api.celebrity.service.CelebrityService;
import com.aranae.api.common.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/celebrities")
public class CelebrityController {

    private final CelebrityService celebrityService;

    public CelebrityController(CelebrityService celebrityService) {
        this.celebrityService = celebrityService;
    }

    @GetMapping
    public PageResponse<CelebrityResponse> findAll(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return PageResponse.from(celebrityService.findAll(name, pageable));
    }

    @GetMapping("/{id}")
    public CelebrityResponse findById(@PathVariable Long id) {
        return celebrityService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CelebrityResponse create(@RequestBody @Valid CelebrityCreateRequest request) {
        return celebrityService.create(request);
    }

    @PutMapping("/{id}")
    public CelebrityResponse update(@PathVariable Long id,
                                    @RequestBody @Valid CelebrityUpdateRequest request) {
        return celebrityService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        celebrityService.delete(id);
    }
}
