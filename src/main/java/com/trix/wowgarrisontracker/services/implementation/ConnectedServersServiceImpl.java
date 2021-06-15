package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.ConnectedServersModel;
import com.trix.wowgarrisontracker.repository.ConnectedServersRepository;
import com.trix.wowgarrisontracker.services.interfaces.ConnectedServersService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ConnectedServersServiceImpl implements ConnectedServersService {

    private final ConnectedServersRepository repository;

    public ConnectedServersServiceImpl(ConnectedServersRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean save(ConnectedServersModel model) {
        if (model != null) {
            ConnectedServersModel saved = repository.save(model);
            return true;
        }
        return false;
    }

    @Override
    public boolean saveAll(Collection<ConnectedServersModel> models) {

        if(models != null){
            repository.saveAll(models);
            return true;
        }
        return false;
    }
}
