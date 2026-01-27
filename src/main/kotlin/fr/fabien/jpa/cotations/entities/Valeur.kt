package fr.fabien.jpa.cotations.entities

import fr.fabien.jpa.cotations.enumerations.Marche
import jakarta.persistence.*

@Entity
@Table(
    indexes = [
        Index(columnList = "ticker"),
        Index(columnList = "marche")
    ],
    uniqueConstraints = [
        UniqueConstraint(name = "UniqueTicker", columnNames = ["ticker"]),
        UniqueConstraint(name = "UniqueTickerEtMarche", columnNames = ["ticker", "marche"])
    ]
)
class Valeur(
    @Column(nullable = false, updatable = false, length = 5)
    val ticker: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var marche: Marche,

    @Column(nullable = false, length = 100)
    var libelle: String,

    @OneToMany(mappedBy="valeur", fetch = FetchType.LAZY)
    val cours: Set<Cours>,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
)