package com.AmericanBoutique.service;

import com.AmericanBoutique.model.Color;
import com.AmericanBoutique.model.State;
import com.AmericanBoutique.repo.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {
    private final StateRepository stateRepository;

    @Autowired
    public StateService(StateRepository stateRepository ){
        this.stateRepository = stateRepository;
    }

    public List<State> getAllStates() {
        return stateRepository.findAll();
    }
}
