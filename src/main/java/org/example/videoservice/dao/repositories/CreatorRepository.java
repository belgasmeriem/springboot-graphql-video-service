package org.example.videoservice.dao.repositories;

import org.example.videoservice.dao.entities.Creator; // adapte
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<Creator, Long> {
}