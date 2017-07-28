package com.polito.sismic.Domain

/**
 * Created by Matteo on 28/07/2017.
 */
class DatabaseProvider {

    fun GetDatabase() : IDatabase {
        return DatabaseStub()
    }

}