package com.EasayHelp.EasayHelp.repository;

import com.EasayHelp.EasayHelp.entity.Service;
import com.EasayHelp.EasayHelp.entity.Enum.StatutService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service,Long> {

    List<Service> findAllById(Long id);

    Optional<Service> findByNom(String nom);

    List<Service> findByNomContainingIgnoreCase(String motCle);

    List<Service> findByPrixLessThan(int prixMax);

    List<Service> findByStatut(StatutService statut);

    List<Service> findAllServiceByCategorieId(Long categorie_id);

    List<Service> findAllServiceByVilleId(Long ville_id);


}
