package springallinone.practice.concurrency.exception;

public class DistributedLockAcquisitionFailException extends RuntimeException {
    public DistributedLockAcquisitionFailException(String lockKey) {
        super("락 획득 실패: " + lockKey);
    }

    public DistributedLockAcquisitionFailException(String lockKey, Throwable cause) {
        super("락 획득 중 인터럽트 발생: " + lockKey, cause);
    }
}
