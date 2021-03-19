package com.ak.feastit.domain.base

interface DataMapper<T, O>{

    fun mapToDomainModel(model: T): O

//    fun mapFromDomainModel(domainModel: O): T
}