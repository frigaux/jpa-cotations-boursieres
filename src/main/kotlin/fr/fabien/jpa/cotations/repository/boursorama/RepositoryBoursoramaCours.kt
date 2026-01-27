package fr.fabien.jpa.cotations.repository.boursorama

import fr.fabien.jpa.cotations.entities.boursorama.BoursoramaCours
import org.springframework.data.repository.CrudRepository
import java.time.LocalDate

interface RepositoryBoursoramaCours : CrudRepository<BoursoramaCours, Int> {
    fun findByDateAndTickerIn(date: LocalDate, tickers: List<String>): List<BoursoramaCours>
    fun findByDate(date: LocalDate): List<BoursoramaCours>
}