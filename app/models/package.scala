import play.api.libs.json._

import models.domain._

package object models {
  implicit def getByCountry = new Writes [(Country, Region, Province, Municipality)] {
    def writes(getByCountry: (Country, Region, Province, Municipality)) =
      Json.obj(
        "countryId" -> getByCountry._1.id,
        "countryName" -> getByCountry._1.name,
        "regionId" -> getByCountry._2.id,
        "regionName" -> getByCountry._2.name,
        "provinceId" -> getByCountry._3.id,
        "provinceName" -> getByCountry._3.name,
        "municipalityId" -> getByCountry._4.id,
        "municipalityName" -> getByCountry._4.name
      )
  }

  implicit def getByRegion = new Writes [(Region, Province, Municipality)] {
    def writes(getByRegion: (Region, Province, Municipality)) =
      Json.obj(
        "regionId" -> getByRegion._1.id,
        "regionName" -> getByRegion._1.name,
        "provinceId" -> getByRegion._2.id,
        "provinceName" -> getByRegion._2.name,
        "municipalityId" -> getByRegion._3.id,
        "municipalityName" -> getByRegion._3.name
      )
  }

  implicit def getByProvince = new Writes [(Province, Municipality)] {
    def writes(getByProvince: (Province, Municipality)) =
      Json.obj(
        "provinceId" -> getByProvince._1.id,
        "provinceName" -> getByProvince._1.name,
        "municipalityId" -> getByProvince._2.id,
        "municipalityName" -> getByProvince._2.name
      )
  }
}
