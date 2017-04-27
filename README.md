## BPC Core API

### Database (psql)

`createuser my-test`

`createdb --encoding=UTF8 --owner=my-test my-test`

Launch activator and compile try to reload the page when Schema definitions are written then copy it into the `conf/evolution/default/1.sql`

Reload the page and apply `evolutions` after program compiling.

---

API HTTP Endpoints
------

| PROTOCOLS|
| -------- |
| GET   |
| POST    |
| PATCH   |
| DELETE  |


## Present Methods

### FIRM

#### Fetch all firms

**GET** `/firm`

*Response*

```
{
  "name" : "COMPANY AAA",
  "address" : "San Juanico Bridge near here",
  "postingPeriod" : 1,
  ...
  more here
  ...
  "idParent" : 1,
  "idFirmType" : 2,
  "idEnable" : true,
  "id" : 1
}
```

---

#### Fetch all firms by type

**GET** `/firm/{id}`

*Response*

```
[{
  "name" : "COMPANY AAA",
  "address" : "San Juanico Bridge near here",
  "postingPeriod" : 1,
  ...
  more here
  ...
  "idParent" : 1,
  "idFirmType" : 2,
  "idEnable" : true,
  "id" : 1
}, {
  "name" : "COMPANY AAA",
  "address" : "San Juanico Bridge near here",
  "postingPeriod" : 1,
  ...
  more here
  ...
  "idParent" : 1,
  "idFirmType" : 2,
  "idEnable" : true,
  "id" : 1
}]
```

---

#### Add firm

**POST** `/firm`

*Request*

```
{
  "firm" : {
    "name" : "{data}",
    "address" : "{data}",
    "postingPeriod" : "{data}",
    "accountingPeriodCode" : "{data}",
    "type" : "{data}",
    "idBusinessType" : "{data}",
    "idVat" : "{data}",
    "areaCode" : "{data}",
    "codeAccountingMethod" : "{data}",
    "codePostingMethod" : "{data}",
    "idParent" : "{data}",
    "idFirmType" : "{data}",
    "idEnable" : "{data}"
  },
  "contact_infos" : [
    "414 - 7130",
    "example.com"
    "+639225550095"
  ]
}
```

*Response*

```
{
  "status": "success",
  "message" : "Firm Successfully Added"
}
```

---

#### Update firm

**PATCH** `/firm`

*Request*

```
{
  "name" : "{data}",
  "address" : "{data}",
  "postingPeriod" : "{data}",
  "accountingPeriodCode" : "{data}",
  "type" : "{data}",
  "idBusinessType" : "{data}",
  "idVat" : "{data}",
  "areaCode" : "{data}",
  "codeAccountingMethod" : "{data}",
  "codePostingMethod" : "{data}",
  "idParent" : "{data}",
  "idFirmType" : "{data}",
  "isEnable" : "{data}",
  "id": {id}
}
```

*Response*

```
{
  "status": "success",
  "message" : "Firm Successfully Updated"
}
```

---

#### Disable firm

**PATCH** `/firm`

*Request*

```
{
  "id": {id},
  "isEnable": {true/false}
}
```

*Response*

```
{
  "status": "success",
  "message" : "Firm Enabled/Disabled"
}
```

---

#### Fetch all firm types

**GET** `/firm/type`

*Response*

```
[
  {
    "name": "COMPANY/FRANCHISEE",
    "id": 1
  }, {
    "name": "BRANCH",
    "id": 2
  }, {
    "name": "LOGISTICS",
    "id": 3
  }, {
    "name": "COMMISSARY",
    "id": 4
  }
]
```

---

#### Fetch firm type by ID

**GET** `/firm/type/{id}`

*Response*

```
{
  "name": "COMPANY/FRANCHISEE",
  "id": 1
}
```

---

#### Update firm type

**PATCH** `/firm`

*Request*

```
{
  "type" : "{data}",
  "id" : {id}
}
```

*Response*

```
{
  "status": "success",
  "message" : "Firm Type Successfully Updated"
}
```

---

#### Fetch all contacts

**GET** `/firm/contactinfo`

*Response*

```
[
  {
    "id": 1,
    "idFirm" : 1,
    "value" : "414 - 7130"
  }, {
    "id": 2,
    "idFirm" : 1,
    "value" : "example.com"
  }
]
```

---

#### Fetch all contacts by firm ID

**GET** `/firm/contactinfo/{id}`

*Response*

```
{
  "id": 1,
  "idFirm" : 1,
  "value" : "414 - 7130"
}
```

---

#### Update contacts

**PATCH** `/firm/contactinfo`

*Request*

```
{
  "id": 1,
  "idFirm" : 1,
  "value" : "414 - 7130"
}
```

*Response*

```
{
  "status": "success",
  "message" : "Info Successfully Updated"
}
```

---

#### Delete contacts

**DELETE** `/firm/contactinfo`

*Request*

```
{
  "id": 1
}
```

*Response*

```
{
  "status": "success",
  "message" : "Info Successfully Deleted"
}
```

---

## LOCATION

---

#### Get all countries

**GET** `/location/country`

*Response*

```
[
  {
    "name": "PH",
    "id": 1
  },
  {
    "name": "US",
    "id": 2
  },
  {
    "name": "JP",
    "id": 3
  }
]
```

---

#### Fetch municipality country by ID

**GET** `location/municipality/country/{id}`

*Response*

```
[
  {
    "countryId": 1,
    "countryName": "PH",
    "regionId": 1,
    "regionName": "Region VII",
    "provinceId": 1,
    "provinceName": "CEBU",
    "municipalityId": 1,
    "municipalityName": "Cebu City"
  },
  ...
  more here
  ...
  {
    "countryId": 1,
    "countryName": "PH",
    "regionId": 2,
    "regionName": "REGION 4-A",
    "provinceId": 2,
    "provinceName": "RIZAL",
    "municipalityId": 12,
    "municipalityName": "Morong"
  }
]
```

---

#### Fetch municipality region by ID

**GET** `/location/municipality/region/{id}`

*Response*

```
[
  {
    "regionId": 1,
    "regionName": "Region VII",
    "provinceId": 1,
    "provinceName": "CEBU",
    "municipalityId": 1,
    "municipalityName": "Cebu City"
  },
  ...
  more here
  ...
  {
    "regionId": 1,
    "regionName": "Region VII",
    "provinceId": 1,
    "provinceName": "CEBU",
    "municipalityId": 6,
    "municipalityName": "Dalaguete"
  }
]
```

---

#### Fetch municipality province by ID

**GET** `/location/municipality/province/{id}`

*Response*

```
[
  {
    "provinceId": 1,
    "provinceName": "CEBU",
    "municipalityId": 1,
    "municipalityName": "Cebu City"
  },
  ...
  more here
  ...
  {
    "provinceId": 1,
    "provinceName": "CEBU",
    "municipalityId": 6,
    "municipalityName": "Dalaguete"
  }
]
```

---

#### Fetch all provinces

**GET** `/location/province`

*Response*

```
[
  {
    "name": "CEBU",
    "idRegion": 1,
    "id": 1
  }, {
    "name": "RIZAL",
    "idRegion": 2,
    "id": 2
  }
]
```

---

#### Fetch all regions

**GET** `/location/region`

*Response*

```
[
  {
    "name": "Region VII",
    "idCountry": 1,
    "id": 1
  }, {
    "name": "REGION 4-A",
    "idCountry": 1,
    "id": 2
  }
]
```

---

## ACCOUNTS

#### Fetch all accounting method

**GET** `/accounts/method`

*Response*

```
[
  {
    "code": "AC",
    "name": "ACCRUAL"
  }, {
    "code": "CA",
    "name": "CASH"
  }, {
    "code": "HY",
    "name": "HYBRID"
  }
]
```

---

#### Fetch all accounting period

**GET** `/accounts/period`

*Response*

```
//TO BE DECIDED
```

---

#### Fetch all Business Type

**GET** `/accounts/businesstype`

*Response*

```
[
  {
    "id": 1,
    "name": "Corporation"
  }, {
    "id": 2,
    "name": "Partnership"
  }, {
    "id": 3,
    "name": "Sole Proprietorship"
  }
]
```

---

#### Add Business Type

**POST** `/accounts/businesstype`

*Request*

```
{
  "name" : "{data}"
}
```

*Response*

```
{
  "status": "success",
  "message" : "Business Type Successfully Added"
}
```

---

#### Update Business Type

**PATCH** `/accounts/businesstype`

*Request*

```
{
  "name" : "{data}"
  "id" : {id}
}
```

*Response*

```
{
  "status": "success",
  "message" : "Business Type Successfully Updated"
}
```

---

#### Fetch VAT

**GET** `/accounts/vat`

*Response*

```
[
  {
    "name": "VAT",
    "value": 0.12,
    "id": 1
  },
  {
    "name": "NON-VAT",
    "value": 1.25,
    "id": 2
  }
]
```

---

#### Fetch VAT by ID

**GET** `/accounts/vat/{id}`

*Response*

```
{
  "name": "VAT",
  "value": 0.12,
  "id": 1
}
```

---

#### Add VAT

**POST** `/accounts/vat`

*Request*

```
{
  "name" : "{data}"
  "value" : "{data}"
}
```

*Response*

```
{
  "status": "success",
  "message" : "Vat Classification Successfully Added"
}
```

---

#### Update VAT

**PATCH** `/accounts/vat`

*Request*

```
{
  "name" : "{data}"
  "value" : "{data}"
  "id" : {id}
}
```

*Response*

```
{
  "status": "success",
  "message" : "Vat Classification Successfully Updated"
}
```

---
