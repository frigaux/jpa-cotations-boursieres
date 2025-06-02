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
        var valeurGLE: Valeur? = null
        var valeurBNP: Valeur? = null
    }

    @BeforeTest
    fun setup() {
        valeurGLE = Valeur("GLE", Marche.EURO_LIST_A, "Societe Generale", setOf())
        repositoryValeur.save(valeurGLE!!)

        repositoryCours.save(
            Cours(
                valeurGLE!!, LocalDate.now().minusDays(1), 36.895, 37.03, 36.65, 36.925,
                2597395, mutableListOf(), false
            )
        )

        repositoryCours.save(
            Cours(
                valeurGLE!!, LocalDate.now(), 36.8, 37.0, 36.6, 36.9,
                2500000, mutableListOf(), false
            )
        )

        valeurBNP = Valeur("BNP", Marche.EURO_LIST_A, "Bnp Paribas", setOf())
        repositoryValeur.save(valeurBNP!!)

        repositoryCours.save(
            Cours(
                valeurBNP!!, LocalDate.now().minusDays(1), 70.89, 71.2, 69.97, 70.3,
                2497449, mutableListOf(), false
            )
        )

        repositoryCours.save(
            Cours(
                valeurBNP!!, LocalDate.now(), 70.37, 71.54, 70.37, 70.87,
                3309017, mutableListOf(), false
            )
        )
    }

    @Test
    fun `Given 2 Valeur when queryJoinCoursByDate then return 2 Valeurs`() {
        Assertions.assertThat<Valeur>(repositoryValeur.queryJoinCoursByDate(LocalDate.now()))
            .hasSize(2)
    }

    @Test
    fun `Given 2 Valeur when queryJoinLastCours then return 2 Valeurs`() {
        Assertions.assertThat<Valeur>(repositoryValeur.queryJoinLastCours())
            .hasSize(2)
    }

    @Test
    fun `Given 2 Valeur when findByTicker then return 1 Valeur`() {
        Assertions.assertThat<Valeur>(repositoryValeur.findByTicker("GLE"))
            .isNotNull
    }

    @Test
    fun `Given 2 Valeur avec 2 Cours when findAll then return 4 Cours`() {
        Assertions.assertThat<Cours>(repositoryCours.findAll())
            .hasSize(4)
    }

    @Test
    fun `Given 2 Valeur avec 2 Cours when queryBeforeDateByValeur then return 1 Cours`() {
        Assertions.assertThat<Cours>(
            repositoryCours.queryBeforeDateByValeur(
                valeurGLE!!,
                LocalDate.now().minusDays(1),
                2
            )
        )
            .hasSize(1)
    }

    @Test
    fun `Given 2 Valeur avec 2 Cours when queryJoinValeur then return 4 Cours`() {
        Assertions.assertThat<Cours>(repositoryCours.queryJoinValeur())
            .hasSize(4)
    }

    @Test
    fun `Given 2 Valeur avec 2 Cours when queryLastByTicker then return 1 Cours`() {
        Assertions.assertThat<Cours>(repositoryCours.queryLastByTicker("GLE"))
            .isNotNull
    }

    @Test
    fun `Given 2 Valeur avec 2 Cours when queryLatestByTicker then return 2 Cours`() {
        Assertions.assertThat<Cours>(repositoryCours.queryLatestByTicker("GLE", 3))
            .hasSize(2)
    }

    @Test
    fun `Given 2 Valeur avec 2 Cours when queryLatestLightByTicker then return 2 Cours`() {
        Assertions.assertThat<Array<Object>>(repositoryCours.queryLatestLightByTicker("GLE", 3))
            .hasSize(2)
    }

    @Test
    fun `Given 2 Valeur avec 2 Cours when queryLastByTickers then return 2 Cours`() {
        Assertions.assertThat<Cours>(repositoryCours.queryLastByTickers(setOf("GLE", "BNP"))).hasSize(2)
    }

    @Test
    fun `Given 2 Valeur avec 2 Cours when queryLatestLightByTickers then return 4 Cours`() {
        Assertions.assertThat<Array<Object>>(repositoryCours.queryLatestLightByTickers(setOf("GLE", "BNP"), 6))
            .hasSize(4)
    }
}