package com.polito.sismic.Interactors.Helpers

/**
 * Created by it0003971 on 15/09/2017.
 */

enum class LivelloConoscenza(val multiplier : Double)
{
    I(1.35),
    II(1.20),
    III(1.00)
}

class SismicBuildingCalculatorHelper {

    companion object {

        fun calculateECM(fcm : Double) : Double
        {
            return 22000*(Math.pow((fcm/10), 0.3))
        }

        fun calculateFCD(fck : Double) : Double
        {
            return 0.85 * fck / 1.5
        }

        fun calculateFYD(fyk : Double) : Double
        {
            return fyk / 1.15
        }
    }
}