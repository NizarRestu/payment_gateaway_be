package com.payment.gateaway.service;

import com.payment.gateaway.model.PromoCode;
import com.payment.gateaway.repository.PromoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PromoCodeServiceImpl implements PromoCodeService{
    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @Override
    public PromoCode add(PromoCode promoCode) {
        return promoCodeRepository.save(promoCode);
    }

    @Override
    public PromoCode get(Integer id) {
        return promoCodeRepository.findById(id).get();
    }

    @Override
    public PromoCode edit(Integer id, PromoCode promoCode) {
        PromoCode update = promoCodeRepository.findById(id).get();
        update.setCode(promoCode.getCode());
        update.setDiscount_percentage(promoCode.getDiscount_percentage());
        update.setMin_transaction(promoCode.getMin_transaction());
        update.setName_sponsor(promoCode.getName_sponsor());
        update.setMax_transaction(promoCode.getMax_transaction());
        return promoCodeRepository.save(update);
    }

    @Override
    public List<PromoCode> getAll() {
        return promoCodeRepository.findAll();
    }

    @Override
    public Map<String, Boolean> delete(Integer id) {
        try {
            promoCodeRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted", Boolean.TRUE);
            return res;
        } catch (Exception e) {
            return null;
        }
    }
}
