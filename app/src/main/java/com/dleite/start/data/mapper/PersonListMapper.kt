package com.dleite.start.data.mapper

import com.dleite.start.data.local.PersonListingEntity
import com.dleite.start.data.remote.dto.ResultListApi
import com.dleite.start.domain.model.Person

fun ResultListApi.toListPerson():List<Person>{
    return this.results.map { person ->
        Person(
            name = person.name,
            url = person.url,
            gender = person.gender
        )
    }
}

fun ResultListApi.toListPersonEntity():List<PersonListingEntity>{
    return this.results.map { person ->
        PersonListingEntity(
            name = person.name,
            url = person.url,
            gender = person.gender
        )
    }
}

fun PersonListingEntity.toListPerson():Person{
    return Person(
        name = name,
        url =  url,
        gender = gender
    )
}

fun Person.toPersonListingEntity():PersonListingEntity{
    return PersonListingEntity(
        name = name,
        url = url,
        gender = gender
    )
}