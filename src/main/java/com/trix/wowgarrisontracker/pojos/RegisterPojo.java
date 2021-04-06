package com.trix.wowgarrisontracker.pojos;

import com.trix.wowgarrisontracker.model.Server;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterPojo {

    private String login;
    private String password;
    private Server server;
    private String email;

}
