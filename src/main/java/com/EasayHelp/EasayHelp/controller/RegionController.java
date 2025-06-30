package com.EasayHelp.EasayHelp.controller;

import com.EasayHelp.EasayHelp.dto.RegionDTO;
import com.EasayHelp.EasayHelp.entity.Regions;
import com.EasayHelp.EasayHelp.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/easayHelp/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;
    private final ModelMapper mapper;               // ou ton mapper favori

    /* ---------- CREATE ---------- */
    @PostMapping
    public ResponseEntity<RegionDTO> create(@RequestBody RegionDTO dto) {
        Regions saved = regionService.save(mapper.map(dto, Regions.class));
        return new ResponseEntity<>(mapper.map(saved, RegionDTO.class), HttpStatus.CREATED);
    }

    /* ---------- READ ---------- */
    @GetMapping
    public ResponseEntity<List<RegionDTO>> findAll() {
        //System.out.println("Début de récupération des régions");
        List<RegionDTO> allregions = new ArrayList<>(regionService.getAll());
        //System.out.println("Nombre de régions trouvées : {}", allregions.size());
        System.out.println(allregions.size());
        return ResponseEntity.ok(allregions);
    }

    @GetMapping("/regions/{id}")
    public RegionDTO findById(@PathVariable Long id) {
        return mapper.map(regionService.getById(id), RegionDTO.class);
    }

    @GetMapping("/search")
    public ResponseEntity<RegionDTO> findByNom(@RequestParam String nom) {
        return regionService.getByNom(nom)
                .map(r -> ResponseEntity.ok(mapper.map(r, RegionDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    /* ---------- UPDATE ---------- */
    @PutMapping("/upadate/{id}")
    public RegionDTO update(@PathVariable Long id, @RequestBody RegionDTO dto) {
        Regions updated = regionService.update(id, mapper.map(dto, Regions.class));
        return mapper.map(updated, RegionDTO.class);
    }

    /* ---------- DELETE ---------- */
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        regionService.delete(id);
    }
}
