package de.astahsrm.gremiomat.gremium;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

public class GremiumServiceImpl implements GremiumService{

    @Autowired
    private GremiumRepository gremiumRepository;

    @Override
    public Optional<Gremium> getGremiumById(Long id) {
        return gremiumRepository.findById(id);
    }

    @Override
    public void delGremium(Long id) {
        gremiumRepository.deleteById(id); 
    }

    @Override
    public Gremium saveGremium(Gremium gremium) {
        return gremiumRepository.save(gremium);
    }

    
}
