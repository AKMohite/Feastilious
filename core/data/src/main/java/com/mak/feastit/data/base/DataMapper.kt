package com.mak.feastit.data.base

interface DataMapper<T, O>{

    fun mapToDomainModel(model: T): O

//    fun mapFromDomainModel(domainModel: O): T
}