package com.polito.sismic.Interactors.Helpers

/**
 * Created by Matteo on 04/09/2017.
 */

enum class StatiLimite(val multiplier : Double)
{
    SLO(0.81),
    SLD(0.63),
    SLV(0.10),
    SLC(0.05)
}


//"Static" class, no memory
class SismicActionCalculatorHelper {

    companion object {
        //Return time in years
        fun calculateTrFor(vr : Double, st : StatiLimite) : Int
        {
            return -(vr/Math.log(1-st.multiplier)).toInt()
        }


        fun calculateQ0(typology : Int, alfa : Double = 1.0, cda : Boolean = true) : Double
        {
            when(typology)
            {
                0 -> if (cda) return 4.5 * alfa else 3 * alfa
                1 -> if (cda) return 4 * alfa else 3.0
                2 -> if (cda) return 3.0 else 2.0
                3 -> if (cda) return 2.0 else 1.5
                else -> return 0.0
            }
            return 0.0
        }

        fun calculateQ(q0 : Double, kr : Double = 1.0) : Double
        {
            return q0 * kr;
        }
    }

}