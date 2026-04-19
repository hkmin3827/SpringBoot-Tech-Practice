package springallinone.practice.concurrency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import springallinone.practice.concurrency.entity.Stock;
import springallinone.practice.concurrency.repository.StockRepository;

@Service
@RequiredArgsConstructor
public class SynchronizedStockService {

    private final StockRepository stockRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized void decrease(Long stockId, Long quantity) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found" + stockId));
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }
}
