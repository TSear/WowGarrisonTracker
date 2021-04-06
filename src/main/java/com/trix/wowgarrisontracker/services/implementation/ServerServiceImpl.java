package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.Server;
import com.trix.wowgarrisontracker.repository.ServerRepository;
import com.trix.wowgarrisontracker.services.interfaces.ServerService;
import com.trix.wowgarrisontracker.utils.BlizzardRequestUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ServerServiceImpl implements ServerService {

    private ServerRepository repository;
    private BlizzardRequestUtils blizzardRequestUtils;

    public ServerServiceImpl(ServerRepository repository) {
        this.repository = repository;
        this.blizzardRequestUtils = new BlizzardRequestUtils();
    }

    @Override
    public List<Server> getServers() {
        return repository.findAll();
    }

    @Override
    public void save(Server server) {
       repository.save(server);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void updateServers() {
        List<Server> servers = blizzardRequestUtils.getListOfServers();
        deleteAll();
        servers.forEach(this::save);
    }


}
