package springallinone.practice.concurrency.service;

import jakarta.persistence.PessimisticLockException;
import jakarta.persistence.QueryTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springallinone.practice.concurrency.entity.Stock;
import springallinone.practice.concurrency.repository.StockRepository;

@Service
@RequiredArgsConstructor
public class PessimisticLockStockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long stockId, Long quantity) {
        try {
            Stock stock = stockRepository.findByIdWithPessimisticLock(stockId)
                    .orElseThrow(() -> new IllegalArgumentException("Stock with id " + stockId + " does not exist"));
            stock.decrease(quantity);
        } catch (PessimisticLockException | QueryTimeoutException e) {
            throw new IllegalStateException("현재 다른 요청이 처리 중입니다. 잠시 후 다시 시도해주세요.");
        }
    }
}
