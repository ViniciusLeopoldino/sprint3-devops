// src/main/java/br/com/fiap/mottucontrol/repository/MotoRepository.java
package br.com.fiap.mottucontrol.repository;

import br.com.fiap.mottucontrol.model.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {
}