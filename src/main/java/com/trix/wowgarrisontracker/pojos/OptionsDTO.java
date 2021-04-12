package com.trix.wowgarrisontracker.pojos;

import com.trix.wowgarrisontracker.model.Server;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionsDTO {

    private Long id;
    private Long accountId;
    private Server server;
    private boolean receiveEmailNotifications;
}
