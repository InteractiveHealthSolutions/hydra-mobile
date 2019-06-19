package ihsinformatics.com.hydra_mobile.data.remote.model.patient

import java.util.*

class Patient(
    var identifier: String,
    var givenName: String,
    var familyName: String,
    var uuid: String,
    var age: Int,
    var birthDate: Date,
    var gender: String,
    var locationId: Int

) {/*
    var identifier: String
    var givenName: String
    var familyName: String
    var uuid: String
    var age: Int
    var birthDate: Date
    var gender: String
    var locationId: Int

    constructor(
        identifier: String,
        givenName: String,
        familyName: String,
        uuid: String,
        age: Int,
        birthDate: Date,
        gender: String,
        locationId: Int
    ) {
        this.identifier = identifier
        this.givenName = givenName
        this.familyName = familyName
        this.uuid = uuid
        this.age = age
        this.birthDate = birthDate
        this.gender = gender
        this.locationId = locationId
    }*/
}