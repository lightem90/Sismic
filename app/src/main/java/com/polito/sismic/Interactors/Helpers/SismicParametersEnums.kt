package com.polito.sismic.Interactors.Helpers

import com.polito.sismic.R

/**
 * Created by Matteo on 22/08/2017.
 */

enum class CoordinateDatabaseParameters
{
    ID,
    LON,
    LAT,
    ag30,
    F030,
    Tc30,
    ag50,
    F050,
    Tc50,
    ag72,
    F072,
    Tc72,
    ag101,
    F0101,
    Tc101,
    ag140,
    F0140,
    Tc140,
    ag201,
    F0201,
    Tc201,
    ag475,
    F0475,
    Tc475,
    ag975,
    F0975,
    Tc975,
    ag2475,
    F02475,
    Tc2475
}

enum class TempiRitorno(val years : Int, val color : Int)
{
    Y30(30, R.color.Y30),
    Y50(50, R.color.Y50),
    Y72(72, R.color.Y72),
    Y101(101, R.color.Y101),
    Y140(140, R.color.Y140),
    Y201(201, R.color.Y201),
    Y475(475, R.color.Y475),
    Y975(975, R.color.Y975),
    Y2475(2475, R.color.Y2475)
}

enum class StatiLimite(val multiplier : Double, val color : Int)
{
    SLO(0.81, R.color.slo),
    SLD(0.63, R.color.sld),
    SLV(0.10, R.color.slv),
    SLC(0.05, R.color.slc)
}

enum class ClasseUso(val multiplier: Double)
{
    I(0.7),
    II(1.0),
    III(1.5),
    IV(2.0)
}

enum class Alfa(val multiplier: Double)
{
    I(1.1),
    II(1.2),
    III(1.3),
    IV(2.0),
    V(1.0),
    VI(1.2)
}

enum class CategoriaSottosuolo(val multiplierCC: Double, val expCC: Double)
{
    A(1.0, 1.0),
    B(1.1, -0.2),
    C(1.05, -0.33),
    D(1.25, -0.5),
    E(1.25, -0.4)
}

enum class CategoriaTopografica(val multiplier: Double)
{
    T1(1.0),
    T2(1.2),
    T3(1.2),
    T4(1.4)
}

enum class ZonaSismica(val multiplier: Double)
{
    I(0.35),
    II(0.25),
    III(0.15),
    IV(0.05)
}