package com.trix.wowgarrisontracker.services.interfaces;


import com.trix.wowgarrisontracker.model.ConnectedServersModel;

import java.util.Collection;
import java.util.List;

public interface ConnectedServersService {

    boolean save(ConnectedServersModel model);

    boolean saveAll(Collection<ConnectedServersModel> models);

    List<ConnectedServersModel> findAll();
}
