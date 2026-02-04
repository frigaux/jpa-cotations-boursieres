package fr.fabien.jpa.cotations.checker

import fr.fabien.jpa.cotations.enumerations.TypeAlerte
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.operator.Operator
import java.util.regex.Pattern


class ExpressionAlerteChecker {
    companion object {
        private val regexpSeuilBas = Pattern.compile("^CLOTURE\\(1\\)\\s*<\\s*\\d+(\\.\\d+)?\$")
        private val regexpSeuilHaut = Pattern.compile("^CLOTURE\\(1\\)\\s*>\\s*\\d+(\\.\\d+)?\$")
        private val regexpVariation = Pattern.compile("^VARIATION\\(1,2\\)\\s*>\\s*\\d+(\\.\\d+)?\$")
        private val regexpTunnel = Pattern.compile("^CLOTURE\\(1\\)\\s*<\\s*\\d+(\\.\\d+)?\\s*\\|\\|\\s*CLOTURE\\(1\\)\\s*>\\s*\\d+(\\.\\d+)?\$")
        private val regexpCroisementMmBas = Pattern.compile("^CLOTURE\\(1\\)\\s*<\\s*MOYENNE_MOBILE\\(\\d+\\)\$")
        private val regexpCroisementMmHaut = Pattern.compile("^CLOTURE\\(1\\)\\s*>\\s*MOYENNE_MOBILE\\(\\d+\\)\$")
        private val regexpPlusHaut = Pattern.compile("^CLOTURE\\(1\\)\\s*=\\s*PLUS_HAUT\\(\\d+\\)\$")
        private val regexpPlusBas = Pattern.compile("^CLOTURE\\(1\\)\\s*=\\s*PLUS_BAS\\(\\d+\\)\$")

        private val cloture = object : net.objecthunter.exp4j.function.Function("CLOTURE", 1) {
            override fun apply(vararg args: Double): Double {
                return args[0]
            }
        }
        private val variation = object : net.objecthunter.exp4j.function.Function("VARIATION", 2) {
            override fun apply(vararg args: Double): Double {
                return args[0] / args[1]
            }
        }
        private val moyenneMobile = object : net.objecthunter.exp4j.function.Function("MOYENNE_MOBILE", 1) {
            override fun apply(vararg args: Double): Double {
                return args[0]
            }
        }
        private val plusHaut = object : net.objecthunter.exp4j.function.Function("PLUS_HAUT", 1) {
            override fun apply(vararg args: Double): Double {
                return args[0]
            }
        }
        private val plusBas = object : net.objecthunter.exp4j.function.Function("PLUS_BAS", 1) {
            override fun apply(vararg args: Double): Double {
                return args[0]
            }
        }

        private val plusGrand: Operator = object : Operator(">", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            public override fun apply(values: DoubleArray): Double {
                if (values[0] > values[1]) {
                    return 1.0
                } else {
                    return 0.0
                }
            }
        }

        private val plusGrandOuEgal: Operator = object : Operator(">=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            public override fun apply(values: DoubleArray): Double {
                if (values[0] >= values[1]) {
                    return 1.0
                } else {
                    return 0.0
                }
            }
        }

        private val plusPetit: Operator = object : Operator("<", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            public override fun apply(values: DoubleArray): Double {
                if (values[0] < values[1]) {
                    return 1.0
                } else {
                    return 0.0
                }
            }
        }

        private val plusPetitOuEgal: Operator = object : Operator("<=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            public override fun apply(values: DoubleArray): Double {
                if (values[0] <= values[1]) {
                    return 1.0
                } else {
                    return 0.0
                }
            }
        }

        private val ou: Operator = object : Operator("||", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            public override fun apply(values: DoubleArray): Double {
                if (values[0] == 1.0 || values[1] == 1.0) {
                    return 1.0
                } else {
                    return 0.0
                }
            }
        }

        private val et: Operator = object : Operator("&&", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            public override fun apply(values: DoubleArray): Double {
                if (values[0] == 1.0 && values[1] == 1.0) {
                    return 1.0
                } else {
                    return 0.0
                }
            }
        }

        private fun validerAvecRegexp(expression: String, regexp: Pattern) {
            if (!regexp.matcher(expression).find())
                throw IllegalArgumentException("expression invalide : ${expression}")
        }

        fun validerExpressionSelonType(expression: String, type: TypeAlerte) {
            when(type) {
                TypeAlerte.LIBRE -> ExpressionBuilder(expression)
                    .operator(ou, et, plusGrand, plusGrandOuEgal, plusPetit, plusPetitOuEgal)
                    .functions(cloture, moyenneMobile, variation, plusHaut, plusBas)
                    .build()
                    .evaluate()
                TypeAlerte.SEUIL_BAS -> validerAvecRegexp(expression, regexpSeuilBas)
                TypeAlerte.SEUIL_HAUT -> validerAvecRegexp(expression, regexpSeuilHaut)
                TypeAlerte.VARIATION -> validerAvecRegexp(expression, regexpVariation)
                TypeAlerte.TUNNEL -> validerAvecRegexp(expression, regexpTunnel)
                TypeAlerte.CROISEMENT_MM_BAS -> validerAvecRegexp(expression, regexpCroisementMmBas)
                TypeAlerte.CROISEMENT_MM_HAUT -> validerAvecRegexp(expression, regexpCroisementMmHaut)
                TypeAlerte.PLUS_HAUT -> validerAvecRegexp(expression, regexpPlusHaut)
                TypeAlerte.PLUS_BAS -> validerAvecRegexp(expression, regexpPlusBas)
            }

        }
    }
}
fun main(vararg args: String) {
    ExpressionAlerteChecker.validerExpressionSelonType("(CLOTURE(23) > 23.5) || (CLOTURE(23) >= 24.6)", TypeAlerte.LIBRE)
}