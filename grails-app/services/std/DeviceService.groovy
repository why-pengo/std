package std

import grails.gorm.services.Service

@Service(Device)
interface DeviceService {

    Device get(Serializable id)

    List<Device> list(Map args)

    Long count()

    void delete(Serializable id)

    Device save(Device device)

}