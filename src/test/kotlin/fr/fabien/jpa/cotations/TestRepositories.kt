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
    fun given1Valeur_whenQueryValeur_then1ValeurIsReturned() {
        Assertions.assertThat<Valeur>(repositoryValeur.queryJoinCoursByDate(LocalDate.now()))
            .hasSize(1)
    }

    @Test
    fun given1ValeurAvec2Cours_whenQueryCoursByDate_then1CoursIsReturned() {
        Assertions.assertThat<Cours>(repositoryCours.query300BeforeDate(valeur!!, LocalDate.now().minusDays(1)))
            .hasSize(1)
    }

    @Test
    fun given1ValeurAvec2Cours_whenQueryAllCours_then2CoursAreReturned() {
        Assertions.assertThat<Cours>(repositoryCours.findAll())
            .hasSize(2)
    }
}