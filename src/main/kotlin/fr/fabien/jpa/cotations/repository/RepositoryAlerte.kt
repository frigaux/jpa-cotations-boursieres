package fr.fabien.jpa.cotations.repository

import fr.fabien.jpa.cotations.entities.Alerte
import org.springframework.data.repository.ListCrudRepository

interface RepositoryAlerte : ListCrudRepository<Alerte, Int> {
}