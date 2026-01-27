package fr.fabien.jpa.cotations.repository

import fr.fabien.jpa.cotations.entities.Cours
import fr.fabien.jpa.cotations.entities.Valeur
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface RepositoryCours : CrudRepository<Cours, Int> {
    @Query("SELECT c FROM Cours c WHERE c.valeur = :valeur AND c.date <= :date ORDER BY c.date DESC LIMIT :limit")
    fun queryBeforeDateByValeur(
        @Param("valeur") valeur: Valeur,
        @Param("date") date: LocalDate,
        @Param("limit") limit: Int
    ): List<Cours>

    @Query("SELECT c FROM Cours c INNER JOIN FETCH c.valeur")
    fun queryJoinValeur(): List<Cours>

    @Query(
        "SELECT c FROM Cours c WHERE c.valeur.ticker = :ticker" +
                " AND c.date = (SELECT max(date) FROM Cours c WHERE c.valeur.ticker = :ticker)"
    )
    fun queryLastByTicker(@Param("ticker") ticker: String): Cours?

    @Query(
        "SELECT c FROM Cours c WHERE c.valeur.ticker = :ticker" +
                " ORDER BY c.date DESC LIMIT :limit"
    )
    fun queryLatestByTicker(@Param("ticker") ticker: String, @Param("limit") limit: Int): List<Cours>

    @Query(
        "SELECT c.date, c.cloture, c.volume FROM Cours c WHERE c.valeur.ticker = :ticker" +
                " ORDER BY c.date DESC LIMIT :limit"
    )
    fun queryLatestLightByTicker(@Param("ticker") ticker: String, @Param("limit") limit: Int): List<Array<Object>>

    @Query(
        "SELECT c FROM Cours c INNER JOIN FETCH c.valeur" +
                " WHERE c.valeur.ticker IN (:tickers)" +
                " AND c.date = (SELECT max(date) FROM Cours c WHERE c.valeur.ticker IN (:tickers))"
    )
    fun queryLastByTickers(@Param("tickers") tickers: Set<String>): List<Cours>

    @Query(
        "SELECT c.valeur.ticker, c.date, c.cloture, c.volume FROM Cours c WHERE c.valeur.ticker IN (:tickers)" +
                " ORDER BY c.date DESC LIMIT :limit"
    )
    fun queryLatestLightByTickers(@Param("tickers") tickers: Set<String>, @Param("limit") limit: Int): List<Array<Object>>
}