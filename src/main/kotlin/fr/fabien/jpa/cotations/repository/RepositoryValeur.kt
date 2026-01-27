package fr.fabien.jpa.cotations.repository

import fr.fabien.jpa.cotations.entities.Valeur
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface RepositoryValeur : CrudRepository<Valeur, Int> {
    fun findByTicker(ticker: String): Valeur?

    @Query("SELECT l FROM Valeur l JOIN FETCH l.cours c WHERE c.date = :date")
    fun queryJoinCoursByDate(@Param("date") date: LocalDate): List<Valeur>

    @Query("SELECT l FROM Valeur l JOIN FETCH l.cours c WHERE c.date = (SELECT max(date) FROM Cours c)")
    fun queryJoinLastCours(): List<Valeur>
}