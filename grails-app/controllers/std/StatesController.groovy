package std

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class StatesController {

    StatesService statesService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond statesService.list(params), model:[statesCount: statesService.count()]
    }

    def show(Long id) {
        respond statesService.get(id)
    }

    def create() {
        respond new States(params)
    }

    def save(States states) {
        if (states == null) {
            notFound()
            return
        }

        try {
            statesService.save(states)
        } catch (ValidationException e) {
            respond states.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'states.label', default: 'States'), states.id])
                redirect states
            }
            '*' { respond states, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond statesService.get(id)
    }

    def update(States states) {
        if (states == null) {
            notFound()
            return
        }

        try {
            statesService.save(states)
        } catch (ValidationException e) {
            respond states.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'states.label', default: 'States'), states.id])
                redirect states
            }
            '*'{ respond states, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        statesService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'states.label', default: 'States'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'states.label', default: 'States'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
