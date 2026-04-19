package springallinone.practice.concurrency.service;


import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springallinone.practice.concurrency.entity.Stock;
import springallinone.practice.concurrency.repository.StockRepository;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long stockId, Long quantity) {
        Stock stock = stockRepository.findByIdWithOptimisticLock(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock with id " + stockId + " not found"));
        stock.decrease(quantity);
    }


    public void decreaseWithRetry(Long stockId, Long quantity) throws InterruptedException {
        int maxRetry = 3;
        int count = 0;
        while (count < maxRetry) {
            try {
                decrease(stockId, quantity);
                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                count++;
                if (count == maxRetry) {
                    throw new IllegalStateException("재고 감소 처리 중 충돌이 반복되었습니다. 다시 시도해 주세요.");
                }
                Thread.sleep(50);
            }
        }
    }
}
