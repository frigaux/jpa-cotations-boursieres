package fr.fabien.jpa.cotations.entity.boursorama

import fr.fabien.jpa.cotations.Marche
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    indexes = [
        Index(columnList = "date"),
    ],
    uniqueConstraints = [
        UniqueConstraint(name = "UniqueBoursoramaDateEtTicker", columnNames = ["date", "ticker"])
    ]
)
class BoursoramaCours(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val marche: Marche,

    @Column(nullable = false, updatable = false, length = 5)
    val ticker: String,

    @Column(nullable = false, length = 100)
    var nom: String,

    @Temporal(TemporalType.DATE)
    @Column(nullable = false, updatable = false)
    val date: LocalDate,

    @Column(nullable = false)
    var ouverture: Double,

    @Column(nullable = false)
    var plusHaut: Double,

    @Column(nullable = false)
    var plusBas: Double,

    @Column(nullable = false)
    var cloture: Double,

    @Column(nullable = false)
    var volume: Long,

    @Column(nullable = false, length = 3)
    var devise: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
)