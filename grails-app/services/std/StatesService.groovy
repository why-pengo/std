package std

import grails.gorm.services.Service

@Service(States)
interface StatesService {

    States get(Serializable id)

    List<States> list(Map args)

    Long count()

    void delete(Serializable id)

    States save(States states)

}