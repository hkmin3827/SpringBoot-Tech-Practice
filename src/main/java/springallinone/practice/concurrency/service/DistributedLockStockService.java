package springallinone.practice.concurrency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springallinone.practice.concurrency.annotation.DistributedLock;
import springallinone.practice.concurrency.entity.Stock;
import springallinone.practice.concurrency.repository.StockRepository;

@Service
@RequiredArgsConstructor
public class DistributedLockStockService {

    private final StockRepository stockRepository;

    @DistributedLock(key = "stock", waitTime = 5, leaseTime = 3)
    @Transactional
    public void decrease(Long stockId, Long quantity) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("stock not found"));
        stock.decrease(quantity);
    }
}
