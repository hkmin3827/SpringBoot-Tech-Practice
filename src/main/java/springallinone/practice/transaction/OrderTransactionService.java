package springallinone.practice.transaction;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import springallinone.practice.jpa.entity.Member;
import springallinone.practice.jpa.entity.Product;
import springallinone.practice.jpa.repository.MemberRepository;
import springallinone.practice.jpa.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class OrderTransactionService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderLogService orderLogService;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void placeOrder(Long memberId, Long productId, int quantity) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.decreaseStock(quantity);
        orderLogService.saveLog("Order placed: memberId=" + memberId + ", productId=" + productId);
    }

    @Transactional(rollbackFor = Exception.class, noRollbackFor = IllegalArgumentException.class)
    public void processWithCustomRollback(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.decreaseStock(quantity);
    }
}
