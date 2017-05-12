-- *********************************************
-- * Standard SQL generation                   
-- *--------------------------------------------
-- * DB-MAIN version: 10.0.0              
-- * Generator date: Dec 20 2016              
-- * Generation date: Fri May 12 13:12:01 2017 
-- * LUN file: D:\loren\Documents\workspace\android\vaccini\DB\Vaccini.lun 
-- * Schema: SCHEMA_LOG/SQL 
-- ********************************************* 


-- Database Section
-- ________________ 

create database SCHEMA_LOG;


-- DBSpace Section
-- _______________


-- Tables Section
-- _____________ 

create table ACCOUNT (
     email char(1) not null,
     password char(1) not null,
     constraint ID_ACCOUNT_ID primary key (email));

create table DEVE_FARE (
     email char(1) not null,
     Antigeni char(1) not null,
     constraint ID_DEVE_FARE_ID primary key (email, Antigeni));

create table HA_FATTO (
     email char(1) not null,
     Antigeni char(1) not null,
     data char(1) not null,
     constraint ID_HA_FATTO_ID primary key (Antigeni, email));

create table IN (
     COD_STATO char(1) not null,
     Antigeni char(1) not null,
     obbligatorieta_estesa char(1) not null,
     constraint ID_IN_ID primary key (Antigeni, COD_STATO));

create table PAESE (
     COD_STATO char(1) not null,
     nome char(1) not null,
     constraint ID_PAESE_ID primary key (COD_STATO));

create table TEMPO (
     ID char(1) not null,
     anni char(1) not null,
     Antigeni char(1) not null,
     constraint ID_TEMPO_ID primary key (ID));

create table UTENTE (
     ID char(1) not null,
     nome char(1) not null,
     cognome char(1) not null,
     dataNascita char(1) not null,
     tipo char(1) not null,
     email char(1) not null,
     constraint ID_UTENTE_ID primary key (ID));

create table VACCINAZIONE (
     Antigeni char(1) not null,
     Descrizione char(1) not null,
     constraint ID_VACCINAZIONE_ID primary key (Antigeni));


-- Constraints Section
-- ___________________ 

alter table DEVE_FARE add constraint FKDEV_VAC_FK
     foreign key (Antigeni)
     references VACCINAZIONE;

alter table DEVE_FARE add constraint FKDEV_ACC
     foreign key (email)
     references ACCOUNT;

alter table HA_FATTO add constraint FKHA__VAC
     foreign key (Antigeni)
     references VACCINAZIONE;

alter table HA_FATTO add constraint FKHA__ACC_FK
     foreign key (email)
     references ACCOUNT;

alter table IN add constraint FKIN_VAC
     foreign key (Antigeni)
     references VACCINAZIONE;

alter table IN add constraint FKIN_PAE_FK
     foreign key (COD_STATO)
     references PAESE;

alter table PAESE add constraint ID_PAESE_CHK
     check(exists(select * from IN
                  where IN.COD_STATO = COD_STATO)); 

alter table TEMPO add constraint FKSCHEDULARITA_FK
     foreign key (Antigeni)
     references VACCINAZIONE;

alter table UTENTE add constraint FKPOSSIEDE_FK
     foreign key (email)
     references ACCOUNT;

alter table VACCINAZIONE add constraint ID_VACCINAZIONE_CHK
     check(exists(select * from IN
                  where IN.Antigeni = Antigeni)); 

alter table VACCINAZIONE add constraint ID_VACCINAZIONE_CHK
     check(exists(select * from TEMPO
                  where TEMPO.Antigeni = Antigeni)); 


-- Index Section
-- _____________ 

create unique index ID_ACCOUNT_IND
     on ACCOUNT (email);

create unique index ID_DEVE_FARE_IND
     on DEVE_FARE (email, Antigeni);

create index FKDEV_VAC_IND
     on DEVE_FARE (Antigeni);

create unique index ID_HA_FATTO_IND
     on HA_FATTO (Antigeni, email);

create index FKHA__ACC_IND
     on HA_FATTO (email);

create unique index ID_IN_IND
     on IN (Antigeni, COD_STATO);

create index FKIN_PAE_IND
     on IN (COD_STATO);

create unique index ID_PAESE_IND
     on PAESE (COD_STATO);

create unique index ID_TEMPO_IND
     on TEMPO (ID);

create index FKSCHEDULARITA_IND
     on TEMPO (Antigeni);

create unique index ID_UTENTE_IND
     on UTENTE (ID);

create index FKPOSSIEDE_IND
     on UTENTE (email);

create unique index ID_VACCINAZIONE_IND
     on VACCINAZIONE (Antigeni);

