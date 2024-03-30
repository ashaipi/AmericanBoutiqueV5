package com.AmericanBoutique.service;

import com.AmericanBoutique.model.Color;
import com.AmericanBoutique.model.Size;
import com.AmericanBoutique.repo.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorService {

    private ColorRepository colorRepository;

    @Autowired
    public ColorService(ColorRepository colorRepository){
        this.colorRepository = colorRepository;
    }

    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }
}
