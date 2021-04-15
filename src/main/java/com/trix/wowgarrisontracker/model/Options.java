package com.trix.wowgarrisontracker.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "options")
@Entity
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "serverId",
            referencedColumnName = "id",
            nullable = false,
            columnDefinition = "INT default 1")
    @ColumnDefault("1")
    private Server server;

    @Column(name = "emailNotifications")
    private boolean receiveEmailNotifications;

    public Options() {
        this.receiveEmailNotifications = false;
    }


    public Options(Account account) {
        this();
        this.server = new Server(1);
        this.account = account;
    }

}
