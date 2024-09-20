package com.example.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.persistence.entities.*;

@Repository
public interface TipoTransaccionRepository extends JpaRepository<TipoTransaccion, Long> {
}
