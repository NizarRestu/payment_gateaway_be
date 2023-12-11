package com.payment.gateaway.repository;

import com.payment.gateaway.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode , Integer> {
    PromoCode findByCode(String code);
}
