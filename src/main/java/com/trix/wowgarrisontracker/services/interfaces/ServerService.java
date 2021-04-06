package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.model.Server;

import java.util.List;

public interface ServerService {

    List<Server> getServers();

    void save(Server server);

    void deleteAll();

    void updateServers();
}
