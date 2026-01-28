package fr.fabien.jpa.cotations.entities

import fr.fabien.jpa.cotations.enumerations.TypeAlerte
import fr.fabien.jpa.cotations.enumerations.TypeNotification
import fr.fabien.jpa.cotations.checker.ExpressionAlerteChecker
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "UniqueNom", columnNames = ["nom"])
    ]
)
class Alerte(
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idValeur", nullable = false, updatable = false)
    val valeur: Valeur,

    @Column(nullable = false, length = 100)
    val nom: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: TypeAlerte,

    @Column(nullable = false, length = 100)
    val expression: String,

    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    val dateLimite: LocalDate?,

    @Column(nullable = false)
    val declenchementUnique: Boolean,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val notification: TypeNotification,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
) {
    @PreUpdate
    @PrePersist
    fun checkCondition() {
        ExpressionAlerteChecker.validerExpressionSelonType(expression, type)
    }
}