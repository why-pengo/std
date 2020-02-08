package std

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class DeviceServiceSpec extends Specification {

    DeviceService deviceService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Device(...).save(flush: true, failOnError: true)
        //new Device(...).save(flush: true, failOnError: true)
        //Device device = new Device(...).save(flush: true, failOnError: true)
        //new Device(...).save(flush: true, failOnError: true)
        //new Device(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //device.id
    }

    void "test get"() {
        setupData()

        expect:
        deviceService.get(1) != null
    }

    void "test list"() {
        when:
        List<Device> deviceList = deviceService.list()

        then:
        deviceList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        deviceService.count() == 5
    }

    void "test delete"() {
        Long deviceId = setupData()

        expect:
        deviceService.count() == 5

        when:
        deviceService.delete(deviceId)
        sessionFactory.currentSession.flush()

        then:
        deviceService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Device device = new Device()
        deviceService.save(device)

        then:
        device.id != null
    }
}
