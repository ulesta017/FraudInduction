package inductionsw

import java.util.Map;

import grails.converters.JSON
import grails.transaction.Transactional
import groovy.json.JsonSlurper
import com.google.gson.Gson

@Transactional
class PlanetService {
	

	Map randomPlanet(planetNum) { 
								
		def url = new URL ('http://swapi.co/api/planets/'+planetNum+'/')
		def connection = (HttpURLConnection)url.openConnection()
		connection.setRequestMethod("GET")
		connection.setRequestProperty("Accept", "application/json")
		connection.setRequestProperty("User-Agent", "Mozilla/5.0")
		JsonSlurper json = new JsonSlurper()
		Map planet = (Map) json.parse(connection.getInputStream())
		
		def urlCoruscant = new URL ('http://swapi.co/api/planets/9/')
		def connectionCoruscant = (HttpURLConnection)urlCoruscant.openConnection()
		connectionCoruscant.setRequestMethod("GET")
		connectionCoruscant.setRequestProperty("Accept", "application/json")
		connectionCoruscant.setRequestProperty("User-Agent", "Mozilla/5.0")
		JsonSlurper jsonCoruscant = new JsonSlurper()
		Map coruscant = (Map) jsonCoruscant.parse(connectionCoruscant.getInputStream())

		def diameterCoruscant = Integer.parseInt(coruscant.diameter)
		def orbitalPeriodCoruscant = Integer.parseInt(coruscant.orbital_period)
		def rotationPeriodCoruscant = Integer.parseInt(coruscant.rotation_period)
		
		def middleDiameter
		def middleOrbitalPeriod
		def middleRotationPeriod
		def middleSurfaceWater
		
		if (planet.diameter != 'unknown'){
			middleDiameter = (diameterCoruscant+Integer.parseInt(planet.diameter))/2
		} else {
			middleDiameter = diameterCoruscant
		}
		
		if (planet.orbital_period != 'unknown') {
			middleOrbitalPeriod = (orbitalPeriodCoruscant+Integer.parseInt(planet.orbital_period))/2
		} else {
			middleOrbitalPeriod = orbitalPeriodCoruscant
		}
		
		if (planet.rotation_period != 'unknown') {
			middleRotationPeriod = (rotationPeriodCoruscant+Integer.parseInt(planet.rotation_period))/2
		} else {
			middleRotationPeriod = rotationPeriodCoruscant
		}
		
		if (planet.surface_water != 'unknown') {
			middleSurfaceWater = Integer.parseInt(planet.surface_water)
		} else {
			middleSurfaceWater = 0
		}

		def planetMap = [planetaBase:planet.name, diametroPromedio:middleDiameter+' km', periodoOrbitalPromedio:middleOrbitalPeriod+' dias', periodoRotacionalPromedio:middleRotationPeriod+' hs', superficieAguaPromedio:middleSurfaceWater+' %']
		
	}
	
}
