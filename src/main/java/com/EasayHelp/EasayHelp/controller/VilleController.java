package com.EasayHelp.EasayHelp.controller;

import com.EasayHelp.EasayHelp.dto.VilleDTO;
import com.EasayHelp.EasayHelp.dto.mapper.VilleMapper;
import com.EasayHelp.EasayHelp.entity.Regions;
import com.EasayHelp.EasayHelp.entity.Villes;
import com.EasayHelp.EasayHelp.service.RegionService;
import com.EasayHelp.EasayHelp.service.VilleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/easayHelp/ville")
@RequiredArgsConstructor
public class VilleController {

    private final VilleService villeService;
    private final ModelMapper mapper;
    private final RegionService regionService;
    private final VilleMapper villeMapper;

    /* ---------- CREATE ---------- */
    @PostMapping
    public ResponseEntity<VilleDTO> create(@Validated @RequestBody VilleDTO dto) {

        Regions region = regionService.findById(dto.getRegionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Region not found"));

        Villes ville = villeMapper.toEntity(dto);
        ville.setRegion(region);

        Villes saved = villeService.save(ville);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(villeMapper.toDTO(saved));
    }

    /* ---------- READ ---------- */
    @GetMapping
    public List<VilleDTO> findAll() {
        return villeService.getAll()
                .stream()
                .map(v -> mapper.map(v, VilleDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/villes/{id}")
    public VilleDTO findById(@PathVariable Long id) {
        return mapper.map(villeService.getById(id), VilleDTO.class);
    }

    @GetMapping("/search")
    public ResponseEntity<VilleDTO> findByNom(@RequestParam String nom) {
        return villeService.getByNom(nom)
                .map(v -> ResponseEntity.ok(mapper.map(v, VilleDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    /* ---------- READ : villes d’une région ---------- */
    @GetMapping("/by-region/{regionId}")
    public List<VilleDTO> findByRegion(@PathVariable Long regionId) {
        return villeService.getVillesByRegionId(regionId)
                .stream()
                .map(v -> mapper.map(v, VilleDTO.class))
                .collect(Collectors.toList());
    }

    /* ---------- UPDATE ---------- */
    @PutMapping("/update/{id}")
    public VilleDTO update(@PathVariable Long id, @RequestBody VilleDTO dto) {
        Villes updated = villeService.update(id, mapper.map(dto, Villes.class));
        return mapper.map(updated, VilleDTO.class);
    }

    /* ---------- DELETE ---------- */
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        villeService.delete(id);
    }
}
