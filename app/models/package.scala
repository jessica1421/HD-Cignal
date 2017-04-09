import play.api.libs.json._

import models.domain._

package object models {
  implicit def getByCountry = new Writes [(Country, Region, Province, Municipality)] {
    def writes(getByCountry: (Country, Region, Province, Municipality)) =
      getByCountry match {  case (country, region, province, municipality) =>
        Json.obj(
          "countryId" -> country.id,
          "countryName" -> country.name,
          "regionId" -> region.id,
          "regionName" -> region.name,
          "provinceId" -> province.id,
          "provinceName" -> province.name,
          "municipalityId" -> municipality.id,
          "municipalityName" -> municipality.name
      )}
  }

  implicit def getByRegion = new Writes [(Region, Province, Municipality)] {
    def writes(getByRegion: (Region, Province, Municipality)) =
      getByRegion match { case (region, province, municipality) =>
        Json.obj(
          "regionId" -> region.id,
          "regionName" -> region.name,
          "provinceId" -> province.id,
          "provinceName" -> province.name,
          "municipalityId" -> municipality.id,
          "municipalityName" -> municipality.name
      )}
  }

  implicit def getByProvince = new Writes [(Province, Municipality)] {
    def writes(getByProvince: (Province, Municipality)) =
      getByProvince match { case  (province, municipality) =>
        Json.obj(
          "provinceId" -> province.id,
          "provinceName" -> province.name,
          "municipalityId" -> municipality.id,
          "municipalityName" -> municipality.name
      )}
  }
}
