
create table account (id bigint not null auto_increment, amountOfEntries bigint, login varchar(255), password varchar(255), primary key (id)) engine=InnoDB
create table accountCharacter (id bigint not null auto_increment, characterName varchar(255), garrisonResources bigint, warPaint bigint, accountId bigint, primary key (id)) engine=InnoDB
create table auctionEntity (id bigint not null, itemId bigint, quantity bigint, unitPrice bigint, primary key (id)) engine=InnoDB
create table entry (id bigint not null auto_increment, entryDate date, garrisonResources int default 0, warPaint int default 0, accountCharacterId bigint, primary key (id)) engine=InnoDB
create table itemEntity (id bigint not null, name varchar(255), primary key (id)) engine=InnoDB
create table options (id bigint not null auto_increment, emailNotifications bit, serverName varchar(255), accountId bigint, primary key (id)) engine=InnoDB
create table server (id integer not null, name varchar(255), region varchar(255), slug varchar(255), primary key (id)) engine=InnoDB
alter table accountCharacter add constraint FKoqqvbr5ss7xujyx0p3vf1gvvq foreign key (accountId) references account (id)
alter table entry add constraint FKjwhh5n48bgch7a6nqngmho5o foreign key (accountCharacterId) references accountCharacter (id)
alter table options add constraint FKqjthdjor9jfpmtu7x0faqixxu foreign key (accountId) references account (id)
