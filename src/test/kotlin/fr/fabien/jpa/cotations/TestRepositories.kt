package fr.fabien.jpa.cotations

import fr.fabien.jpa.cotations.entity.Cours
import fr.fabien.jpa.cotations.entity.Valeur
import fr.fabien.jpa.cotations.repository.RepositoryCours
import fr.fabien.jpa.cotations.repository.RepositoryValeur
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDate
import kotlin.test.BeforeTest

@DataJpaTest
class TestRepositories(
    @Autowired private val repositoryValeur: RepositoryValeur,
    @Autowired private val repositoryCours: RepositoryCours
) {

    companion object {
        var valeur: Valeur? = null
    }

    @BeforeTest
    fun setup() {
        valeur = Valeur("GLE", Marche.EURO_LIST_A, "Societe Generale", setOf())
        repositoryValeur.save(valeur!!)

        repositoryCours.save(
            Cours(
                valeur!!, LocalDate.now().minusDays(1), 36.895, 37.03, 36.65, 36.925,
                2597395, mutableListOf(), false
            )
        )

        repositoryCours.save(
            Cours(
                valeur!!, LocalDate.now(), 36.8, 37.0, 36.6, 36.9,
                2500000, mutableListOf(), false
            )
        )
    }

    @Test
    fun `Given 1 Valeur when queryJoinCoursByDate then return 1 Valeur`() {
        Assertions.assertThat<Valeur>(repositoryValeur.queryJoinCoursByDate(LocalDate.now()))
            .hasSize(1)
    }

    @Test
    fun `Given 1 Valeur when queryJoinLastCours then return 1 Valeur`() {
        Assertions.assertThat<Valeur>(repositoryValeur.queryJoinLastCours())
            .hasSize(1)
    }

    @Test
    fun `Given 1 Valeur when findByTicker then return 1 Valeur`() {
        Assertions.assertThat<Valeur>(repositoryValeur.findByTicker("GLE"))
            .isNotNull
    }

    @Test
    fun `Given 1 Valeur avec 2 Cours when findAll then return 2 Cours`() {
        Assertions.assertThat<Cours>(repositoryCours.findAll())
            .hasSize(2)
    }

    @Test
    fun `Given 1 Valeur avec 2 Cours when queryBeforeDateByValeur then return 1 Cours`() {
        Assertions.assertThat<Cours>(repositoryCours.queryBeforeDateByValeur(valeur!!, LocalDate.now().minusDays(1), 2))
            .hasSize(1)
    }

    @Test
    fun `Given 1 Valeur avec 2 Cours when queryJoinValeur then return 2 Cours`() {
        Assertions.assertThat<Cours>(repositoryCours.queryJoinValeur())
            .hasSize(2)
    }

    @Test
    fun `Given 1 Valeur avec 2 Cours when queryLastByTicker then return 1 Cours`() {
        Assertions.assertThat<Cours>(repositoryCours.queryLastByTicker("GLE"))
            .isNotNull
    }

    @Test
    fun `Given 1 Valeur avec 2 Cours when queryLatestByTicker then return 2 Cours`() {
        Assertions.assertThat<Cours>(repositoryCours.queryLatestByTicker("GLE", 3))
            .hasSize(2)
    }
}