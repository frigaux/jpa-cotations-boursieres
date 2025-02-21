package fr.fabien.jpa.cotations

import fr.fabien.jpa.cotations.entity.abcbourse.AbcCotation
import fr.fabien.jpa.cotations.entity.abcbourse.AbcLibelle
import fr.fabien.jpa.cotations.repository.abcbourse.RepositoryAbcCotation
import fr.fabien.jpa.cotations.repository.abcbourse.RepositoryAbcLibelle
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDate
import kotlin.test.BeforeTest

@DataJpaTest
class TestAbcRepositories(
    @Autowired private val repositoryAbcCotation: RepositoryAbcCotation,
    @Autowired private val repositoryAbcLibelle: RepositoryAbcLibelle
) {

    @BeforeTest
    fun setup() {
        val abcCotation: AbcCotation = AbcCotation(
            36.895, 37.03, 36.65, 36.925,
            2597395
        )
        repositoryAbcCotation.save(abcCotation)

        repositoryAbcLibelle.save(
            AbcLibelle(
                LocalDate.now().minusDays(1), "GLE", "FR0000130809p",
                Marche.EURO_LIST_A, "Societe Generale", abcCotation
            )
        )

        val abcCotation2: AbcCotation = AbcCotation(
            36.8, 37.0, 36.6, 36.9,
            2500000
        )
        repositoryAbcCotation.save(abcCotation2)

        repositoryAbcLibelle.save(
            AbcLibelle(
                LocalDate.now(), "GLE", "FR0000130809p",
                Marche.EURO_LIST_A, "Societe Generale", abcCotation2
            )
        )
    }

    @Test
    fun given2Cotations_whenFindAll_then2CotationsAreReturned() {
        Assertions.assertThat<AbcCotation>(repositoryAbcCotation.findAll())
            .hasSize(2)
    }

    @Test
    fun given2Libelles_whenFindAll_then2LibellesAreReturned() {
        Assertions.assertThat<AbcLibelle>(repositoryAbcLibelle.findAll())
            .hasSize(2)
    }

    @Test
    fun given2Libelles_whenFindByDateAndTicker_then1LibelleIsReturned() {
        Assertions.assertThat<AbcLibelle>(repositoryAbcLibelle.findByDateAndTickerIn(LocalDate.now(), listOf("GLE")))
            .hasSize(1)
    }

    @Test
    fun given2Libelles_whenQueryByDateAndTicker_then1LibelleIsReturned() {
        Assertions.assertThat<AbcLibelle>(repositoryAbcLibelle.queryByDateAndTickerIn(LocalDate.now(), listOf("GLE")))
            .hasSize(1)
    }

    @Test
    fun given2Libelles_whenQueryByDate_then1LibelleIsReturned() {
        Assertions.assertThat<AbcLibelle>(repositoryAbcLibelle.queryByDate(LocalDate.now()))
            .hasSize(1)
    }
}