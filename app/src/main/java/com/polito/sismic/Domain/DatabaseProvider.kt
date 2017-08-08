package com.polito.sismic.Domain

/**
 * Created by Matteo on 28/07/2017.
 */
class DatabaseProvider {

    fun getDatabase() : IDatabase {
        return DatabaseStub()
    }

}