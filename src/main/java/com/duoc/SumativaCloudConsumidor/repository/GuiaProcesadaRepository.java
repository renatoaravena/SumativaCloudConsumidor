package com.duoc.SumativaCloudConsumidor.repository;

import com.duoc.SumativaCloudConsumidor.model.GuiaProcesada;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GuiaProcesadaRepository extends JpaRepository<GuiaProcesada, Long> {
    Optional<GuiaProcesada> findByS3KeyOriginal(String s3KeyOriginal);
}