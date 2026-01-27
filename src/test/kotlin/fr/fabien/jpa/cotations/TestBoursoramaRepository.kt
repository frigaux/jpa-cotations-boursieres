package fr.fabien.jpa.cotations

import fr.fabien.jpa.cotations.entities.boursorama.BoursoramaCours
import fr.fabien.jpa.cotations.enumerations.Marche
import fr.fabien.jpa.cotations.repository.boursorama.RepositoryBoursoramaCours
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDate
import kotlin.test.BeforeTest

@DataJpaTest
class TestBoursoramaRepository(
    @Autowired private val repositoryBoursoramaCours: RepositoryBoursoramaCours
) {

    @BeforeTest
    fun setup() {
        val cours1 = BoursoramaCours(
            Marche.EURO_LIST_A, "GLE", "Societe Generale", LocalDate.now(),
            36.895, 37.03, 36.65, 36.925,
            2597395, "EUR"
        );

        repositoryBoursoramaCours.save(cours1)

        val cours2 = BoursoramaCours(
            Marche.EURO_LIST_A, "GLE", "Societe Generale",
            LocalDate.now().minusDays(1),
            36.8, 37.0, 36.6, 36.9,
            2500000, "EUR"
        );

        repositoryBoursoramaCours.save(cours2)
    }

    @Test
    fun `Given 2 cours when findAll then return 2 cours`() {
        Assertions.assertThat<BoursoramaCours>(repositoryBoursoramaCours.findAll())
            .hasSize(2)
    }

    @Test
    fun `Given 2 cours when findByDateAndTickerIn then return 1 cours`() {
        Assertions.assertThat<BoursoramaCours>(
            repositoryBoursoramaCours.findByDateAndTickerIn(
                LocalDate.now(),
                listOf("GLE")
            )
        )
            .hasSize(1)
    }

    @Test
    fun `Given 2 cours when queryByDate then return 1 cours`() {
        Assertions.assertThat<BoursoramaCours>(repositoryBoursoramaCours.findByDate(LocalDate.now()))
            .hasSize(1)
    }
}