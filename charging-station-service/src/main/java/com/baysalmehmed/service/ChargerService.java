package com.baysalmehmed.service;

import com.baysalmehmed.exception.type.charger.ChargerDidNotSaveException;
import com.baysalmehmed.exception.type.charger.ChargerDoesNotExistException;
import com.baysalmehmed.factory.charger.ChargerDaoFactory;
import com.baysalmehmed.factory.charger.ChargerFactory;
import com.baysalmehmed.model.dao.ChargerDao;
import com.baysalmehmed.model.in.ChargerIn;
import com.baysalmehmed.model.out.ChargerOut;
import com.baysalmehmed.model.out.ChargerTypeOut;
import com.baysalmehmed.repository.ChargerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChargerService {

    private ChargerRepository chargerRepository;
    private ChargerTypeService chargerTypeService;

    public ChargerService(ChargerRepository chargerRepository, ChargerTypeService chargerTypeService) {
        this.chargerRepository = chargerRepository;
        this.chargerTypeService = chargerTypeService;
    }

    public List<ChargerOut> getChargers(Double latitude, Double longitude, Double miles){
        List<ChargerTypeOut> chargerTypes = chargerTypeService.getChargerTypes();

        List<ChargerDao> chargerList;
        if(latitude != null && longitude != null && miles != null){
            chargerList = chargerRepository.getChargersWithinRadius(latitude, longitude, miles);
        } else {
            chargerList = chargerRepository.findAll();
        }

        if(!chargerList.isEmpty()){
            return ChargerFactory.createChargers(chargerList, chargerTypes);
        } else{
            throw new ChargerDoesNotExistException();
        }
    }

    public ChargerOut getCharger(Integer chargerId){
        List<ChargerTypeOut> chargerTypes = chargerTypeService.getChargerTypes();
        Optional<ChargerDao> charger = chargerRepository.findById(chargerId);
        if(charger.isPresent()) {
            return ChargerFactory.createCharger(charger.get(), chargerTypes);
        } else {
            throw new ChargerDoesNotExistException(chargerId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ChargerDidNotSaveException.class)
    public ChargerOut createCharger(ChargerIn newCharger){
        ChargerDao savedCharger;
        List<ChargerTypeOut> chargerTypes = chargerTypeService.getChargerTypes();
        try {
            savedCharger = chargerRepository.save(ChargerDaoFactory.createCharger(newCharger));
        } catch (Exception e){
            throw new ChargerDidNotSaveException(e.getMessage());
        }
        return ChargerFactory.createCharger(savedCharger, chargerTypes);
    }

    public void deleteCharger(Integer chargerId){
        chargerRepository.deleteById(chargerId);
    }
}
