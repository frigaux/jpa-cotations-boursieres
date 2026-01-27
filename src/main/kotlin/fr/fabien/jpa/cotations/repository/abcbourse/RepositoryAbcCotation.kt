package fr.fabien.jpa.cotations.repository.abcbourse

import fr.fabien.jpa.cotations.entities.abcbourse.AbcCotation
import org.springframework.data.repository.CrudRepository

interface RepositoryAbcCotation : CrudRepository<AbcCotation, Int> {
}