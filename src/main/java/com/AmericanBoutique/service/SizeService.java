package com.AmericanBoutique.service;

import com.AmericanBoutique.model.Orders;
import com.AmericanBoutique.model.Size;
import com.AmericanBoutique.repo.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeService {

    private final SizeRepository sizeRepository;

    @Autowired
    public SizeService(SizeRepository sizeRepository){
        this.sizeRepository = sizeRepository;
    }

    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }
}
