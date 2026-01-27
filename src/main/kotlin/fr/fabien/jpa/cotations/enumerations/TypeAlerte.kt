package fr.fabien.jpa.cotations.enumerations

enum class TypeAlerte {
    /**
     * SEUIL_BAS: la clôture franchit un seuil à la baisse
     * SEUIL_HAUT: la clôture franchit un seuil à la hausse
     * VARIATION : variation en % de la clôture par rapport à la veille
     * TUNNEL : la clôture sort d'un tunnel compris entre deux bornes
     * CROISEMENT_MM_BAS : la clôture croise sa moyenne mobile 20/50/100 jours à la baisse
     * CROISEMENT_MM_HAUT : la clôture croise sa moyenne mobile 20/50/100 jours à la hausse
     * PLUS_HAUT : la clôture franchit son plus haut sur un nombre de jours
     * PLUS_BAS : la clôture franchit son plus bas sur un nombre de jours
     */
    LIBRE, SEUIL_BAS, SEUIL_HAUT, VARIATION, TUNNEL, CROISEMENT_MM_BAS, CROISEMENT_MM_HAUT, PLUS_HAUT, PLUS_BAS
}