# SmartCity API
Documentation for the REST api.

**Base URL** : `/api/`

Endpoints :
- [Zones](#zones)
- [Sensors](#sensors)

## Zones
### `GET` /zones/closed
Returns the closed zones in the city.

##### Responses :
Code | Description
-----|------
**200** | OK, see structure below.

###### Structure :
```
{
    "zones": [ String ]
}
```
Example :
```
{ 
    "zones": ["NO", "SE", "BUS"] 
}
```


## Sensors
### `POST` /sensors
Add a new sensor with its value.

##### Parameters
Name | Type | Description
-----|------|-------------
**name**\* | String | The name of the sensor.
**value**\* | Double | The value of the sensor (in the conventional unit, for example Celsius degree for a temperature sensor)
**gross_value**\* | Double | The gross value of the sensor (as it was taken, with no conversion whatsoever)

##### Responses
Code | Description
-----|------
**200** | OK, see structure below.
**400** | One (or more) parameter(s) is (are) missing. <br><br> ```{ "error": "Missing parameter(s)" }```

##### Structure :
```
{
  "id": Long,
  "name": String,
  "value": Double,
  "grossValue": Double,
  "createdAt": String
}
```
Example :
```
{
  "id": 6,
  "name": "light",
  "value": 190.0,
  "grossValue": 9128.0,
  "createdAt": "2017-04-22T12:43:57Z"
}
```
