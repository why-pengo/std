package std

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.scheduling.annotation.Scheduled

@Slf4j
@CompileStatic
class RefreshDevicesService {

    // Force service to initialize, or the jobs will not execute
    static lazyInit = false

    StapiService stapiService

    @Scheduled(fixedDelay = 440000L, initialDelay = 15000L)
    void execute() {
        log.debug("running...")
        stapiService.refresh()
    }
}
