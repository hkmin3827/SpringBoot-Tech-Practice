package springallinone.practice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class MemberEventHandler {

    @Async("taskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMemberRegisteredEvent(MemberRegisteredEvent event) {
        log.info("Member registered async: id= {} email= {}", event.memberId(), event.email());
    }

    @EventListener
    public void handleMemberRegisteredEventSync(MemberRegisteredEvent event) {
        log.info("Member registered sycn: id= {} email= {}", event.memberId(), event.email());
    }
}
