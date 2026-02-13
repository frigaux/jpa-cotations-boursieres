package fr.fabien.jpa.cotations.repository

import fr.fabien.jpa.cotations.entities.Alerte
import fr.fabien.jpa.cotations.entities.Valeur
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.query.Param

interface RepositoryAlerte : ListCrudRepository<Alerte, Int> {
    @Query(
        "SELECT a FROM Alerte a WHERE a.valeur.ticker = :ticker" +
                " ORDER BY a.dateLimite"
    )
    fun queryByTicker(@Param("ticker") ticker: String): List<Alerte>

    fun findByValeurOrderByDateLimiteDesc(valeur: Valeur): List<Alerte>

    @Query(
        "SELECT a FROM Alerte a WHERE a.valeur.ticker IN (:tickers)" +
                " ORDER BY a.dateLimite"
    )
    fun queryByTickers(@Param("tickers") tickers: Set<String>): List<Alerte>
}