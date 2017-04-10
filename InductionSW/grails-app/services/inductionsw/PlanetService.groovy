package inductionsw

import java.util.Map;
import grails.converters.JSON
import grails.transaction.Transactional
import groovy.json.JsonSlurper
import com.google.gson.Gson
import redis.clients.jedis.Jedis


@Transactional
class PlanetService {
	

	Map randomPlanet(planetNum) {
		
		//Variables de proceso
		def planetName
		def middleDiameter
		def middleOrbitalPeriod
		def middleRotationPeriod
		def middleSurfaceWater
		def planetMap
		
		def planetNumString = planetNum.toString()		
		Jedis jedis = new Jedis("localhost")
		def dataCache = jedis.hget(planetNumString, "planetaBase")
		
		if (dataCache == null) {
			
			//Procesa Coruscant
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
			
			//Procesa el planeta 
			def url = new URL ('http://swapi.co/api/planets/'+planetNum+'/')
			def connection = (HttpURLConnection)url.openConnection()
			connection.setRequestMethod("GET")
			connection.setRequestProperty("Accept", "application/json")
			connection.setRequestProperty("User-Agent", "Mozilla/5.0")
			JsonSlurper json = new JsonSlurper()
			Map planet = (Map) json.parse(connection.getInputStream())
			
			//Calculos
			planetName = planet.name
			
			if (planet.diameter != 'unknown'){
				middleDiameter = ((diameterCoruscant+Integer.parseInt(planet.diameter))/2) + ' km'
			} else {
				middleDiameter = diameterCoruscant + ' km'
			}
			
			if (planet.orbital_period != 'unknown') {
				middleOrbitalPeriod = ((orbitalPeriodCoruscant+Integer.parseInt(planet.orbital_period))/2) + ' dias'
			} else {
				middleOrbitalPeriod = orbitalPeriodCoruscant + ' dias'
			}
			
			if (planet.rotation_period != 'unknown') {
				middleRotationPeriod = ((rotationPeriodCoruscant+Integer.parseInt(planet.rotation_period))/2) + ' hs'
			} else {
				middleRotationPeriod = rotationPeriodCoruscant + ' hs'
			}
			
			if (planet.surface_water != 'unknown') {
				middleSurfaceWater = (Integer.parseInt(planet.surface_water)) + ' %'
			} else {
				middleSurfaceWater = 'unknown'
			}
			
			planetMap = [
				planetaBase:planetName,
				diametroPromedio:middleDiameter,
				periodoOrbitalPromedio:middleOrbitalPeriod,
				periodoRotacionalPromedio:middleRotationPeriod,
				superficieAcuaticaPromedio:middleSurfaceWater
			]
			
			jedis.hmset(planetNumString, planetMap)
			
		} else {
		
			planetMap = [
				planetaBase:jedis.hget(planetNumString, "planetaBase"),
				diametroPromedio:jedis.hget(planetNumString, "diametroPromedio"),
				periodoOrbitalPromedio:jedis.hget(planetNumString, "periodoOrbitalPromedio"),
				periodoRotacionalPromedio:jedis.hget(planetNumString, "periodoRotacionalPromedio"),
				superficieAcuaticaPromedio:jedis.hget(planetNumString, "superficieAcuaticaPromedio")
			]
			
		}
		
		return planetMap
		
	}
	
}
