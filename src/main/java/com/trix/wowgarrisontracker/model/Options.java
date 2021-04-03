package com.trix.wowgarrisontracker.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "options")
@Entity
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Account account;

    @Column(name = "serverName")
    private String serverName;

    @Column(name = "emailNotifications")
    private boolean receiveEmailNotifications;

    public Options() {
        this.serverName = "";
        this.receiveEmailNotifications = false;
    }
}
