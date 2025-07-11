// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.data.base

interface DataMapper<T, O> {
  fun mapToDomainModel(model: T): O

//    fun mapFromDomainModel(domainModel: O): T
}
