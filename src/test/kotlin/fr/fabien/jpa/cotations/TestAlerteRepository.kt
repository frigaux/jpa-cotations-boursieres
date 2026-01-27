package fr.fabien.jpa.cotations

import fr.fabien.jpa.cotations.entities.Alerte
import fr.fabien.jpa.cotations.entities.Valeur
import fr.fabien.jpa.cotations.enumerations.Marche
import fr.fabien.jpa.cotations.enumerations.TypeAlerte
import fr.fabien.jpa.cotations.enumerations.TypeNotification
import fr.fabien.jpa.cotations.repository.RepositoryAlerte
import fr.fabien.jpa.cotations.repository.RepositoryValeur
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import java.util.regex.Pattern
import kotlin.test.BeforeTest


@DataJpaTest
class TestAlerteRepository(
    @Autowired private val repositoryValeur: RepositoryValeur,
    @Autowired private val repositoryAlerte: RepositoryAlerte
) {

    companion object {
        var valeurGLE: Valeur? = null
        var alerte: Alerte? = null
    }

    @BeforeTest
    fun setup() {
        valeurGLE = Valeur("GLE", Marche.EURO_LIST_A, "Societe Generale", setOf())
        repositoryValeur.save(valeurGLE!!)

        alerte = Alerte(
            valeurGLE!!, "la clôture est supérieure à la MM50 et la variation du cours supérieure à 2%",
            TypeAlerte.LIBRE, "(CLOTURE(1) > MOYENNE_MOBILE(50)) && (VARIATION(1,2) > 0.02)", null, false, TypeNotification.SYSTEME
        )
        repositoryAlerte.save(alerte!!)
    }

    @Test
    fun `Given 1 Valeur when findAll then return 1 Valeur`() {
        Assertions.assertThat<Valeur>(repositoryValeur.findAll()).hasSize(1)
    }

    @Test
    fun `Given 1 Alerte when findAll then return 1 Alerte`() {
        Assertions.assertThat<Alerte>(repositoryAlerte.findAll())
            .hasSize(1)
    }

    @Test
    fun `Given x Alerte dont la condition ne correspond pas au type attendu when save then InvalidDataAccessApiUsageException`() {
        assertThrows(InvalidDataAccessApiUsageException::class.java, {
            repositoryAlerte.save(Alerte(
                valeurGLE!!, "n'importe quoi",
                TypeAlerte.SEUIL_BAS, "CLOTURE(1) > 22.2", null, false, TypeNotification.SYSTEME
            ))
        })
        assertThrows(InvalidDataAccessApiUsageException::class.java, {
            repositoryAlerte.save(Alerte(
                valeurGLE!!, "n'importe quoi",
                TypeAlerte.SEUIL_HAUT, "CLOTURE(1) < 22.2", null, false, TypeNotification.SYSTEME
            ))
        })
        assertThrows(InvalidDataAccessApiUsageException::class.java, {
            repositoryAlerte.save(Alerte(
                valeurGLE!!, "n'importe quoi",
                TypeAlerte.VARIATION, "VARIATION(1,3) > 2.25", null, false, TypeNotification.SYSTEME
            ))
        })

        assertThrows(InvalidDataAccessApiUsageException::class.java, {
            repositoryAlerte.save(Alerte(
                valeurGLE!!, "n'importe quoi",
                TypeAlerte.TUNNEL, "CLOTURE(1) < 22.2 || CLOTURE(1) < 25.2", null, false, TypeNotification.SYSTEME
            ))
        })
        assertThrows(InvalidDataAccessApiUsageException::class.java, {
            repositoryAlerte.save(Alerte(
                valeurGLE!!, "n'importe quoi",
                TypeAlerte.CROISEMENT_MM_BAS, "CLOTURE(1) > MOYENNE_MOBILE(50)", null, false, TypeNotification.SYSTEME
            ))
        })
        assertThrows(InvalidDataAccessApiUsageException::class.java, {
            repositoryAlerte.save(Alerte(
                valeurGLE!!, "n'importe quoi",
                TypeAlerte.CROISEMENT_MM_HAUT, "CLOTURE(1) < MOYENNE_MOBILE(50)", null, false, TypeNotification.SYSTEME
            ))
        })
        assertThrows(InvalidDataAccessApiUsageException::class.java, {
            repositoryAlerte.save(Alerte(
                valeurGLE!!, "n'importe quoi",
                TypeAlerte.PLUS_HAUT, "CLOTURE(1) > PLUS_HAUT(50)", null, false, TypeNotification.SYSTEME
            ))
        })
        assertThrows(InvalidDataAccessApiUsageException::class.java, {
            repositoryAlerte.save(Alerte(
                valeurGLE!!, "n'importe quoi",
                TypeAlerte.PLUS_BAS, "CLOTURE(1) < PLUS_BAS(50)", null, false, TypeNotification.SYSTEME
            ))
        })
    }

    @Test
    fun `Given 1 Alerte dont la condition de type LIBRE est erronée when save then InvalidDataAccessApiUsageException`() {
        assertThrows(InvalidDataAccessApiUsageException::class.java, {
            repositoryAlerte.save(Alerte(
                valeurGLE!!, "n'importe quoi",
                TypeAlerte.LIBRE, "NIMPORTEQUOI", null, false, TypeNotification.SYSTEME
            ))
        })
    }
}