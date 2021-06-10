package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.blizzarapi.BlizzardApiRequests;
import com.trix.wowgarrisontracker.model.Server;
import com.trix.wowgarrisontracker.repository.ServerRepository;
import com.trix.wowgarrisontracker.services.interfaces.ServerService;
import com.trix.wowgarrisontracker.utils.BlizzardRequestUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerServiceImpl implements ServerService {

    private final ServerRepository repository;
    private final BlizzardApiRequests blizzardApiRequests;

    public ServerServiceImpl(ServerRepository repository, BlizzardApiRequests blizzardApiRequests) {
        this.repository = repository;
        this.blizzardApiRequests = blizzardApiRequests;
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
        List<Server> servers = blizzardApiRequests.getListOfServers();
        deleteAll();
        servers.forEach(this::save);
    }


}
