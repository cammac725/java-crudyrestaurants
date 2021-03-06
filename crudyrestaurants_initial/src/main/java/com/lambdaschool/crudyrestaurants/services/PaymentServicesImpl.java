package com.lambdaschool.crudyrestaurants.services;

import com.lambdaschool.crudyrestaurants.models.Payment;
import com.lambdaschool.crudyrestaurants.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service(value = "paymentServices")
public class PaymentServicesImpl implements PaymentServices {

    @Autowired
    PaymentRepository paymentrepos;

    @Override
    public Payment save(Payment payment) {
        return paymentrepos.save(payment);
    }

    @Transactional
    @Override
    public void deleteAllPayments() {
        paymentrepos.deleteAll();
    }
}
