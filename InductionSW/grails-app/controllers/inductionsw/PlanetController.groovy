package inductionsw

import grails.converters.JSON

class PlanetController {
	
	def planetService

    def index() {
		
		def json = request.getJSON()
		def planetJSON = planetService.randomPlanet(json.planetNum)
		
		render planetJSON as JSON
    }
}
