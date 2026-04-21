package springallinone.practice.batch.skipListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.SkipListener;
import org.springframework.stereotype.Component;
import springallinone.practice.jpa.entity.Member;

@Component
@Slf4j
public class BatchSkipListener implements SkipListener<Member, Member> {

    @Override
    public void onSkipInRead(Throwable t) {
        log.error("Skip in read: {}", t.getMessage(), t);
    }

    @Override
    public void onSkipInProcess(Member item, Throwable t) {
        log.error("Skip in process - memberId: {}, error: {}", item.getId(), t.getMessage(), t);
    }

    @Override
    public void onSkipInWrite(Member item, Throwable t) {
        log.error("Skip in write - memberId: {}, error: {}", item.getId(), t.getMessage(), t);
    }
}
