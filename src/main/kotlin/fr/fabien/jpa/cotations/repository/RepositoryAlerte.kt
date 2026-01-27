package fr.fabien.jpa.cotations.repository

import fr.fabien.jpa.cotations.entities.Alerte
import org.springframework.data.repository.CrudRepository

interface RepositoryAlerte : CrudRepository<Alerte, Int> {
}