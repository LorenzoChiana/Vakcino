-- *********************************************
-- * Standard SQL generation                   
-- *--------------------------------------------
-- * DB-MAIN version: 10.0.0              
-- * Generator date: Dec 20 2016              
-- * Generation date: Sun May 14 22:12:30 2017 
-- * LUN file: D:\loren\Documents\workspace\android\vaccini\DB\Vaccini.lun 
-- * Schema: VACCINEBOOKLET/SQL 
-- ********************************************* 


-- Database Section
-- ________________ 

create database VACCINEBOOKLET;

use VACCINEBOOKLET;


-- DBSpace Section
-- _______________


-- Tables Section
-- _____________ 

create table ACCOUNT (
     email varchar(50) not null,
     pw varchar(50) not null,
     constraint ID_ACCOUNT_ID primary key (email));

create table DEVE_FARE (
     ID numeric(1) not null,
     Antigeni varchar(20) not null,
     constraint ID_DEVE_FARE_ID primary key (ID, Antigeni));

create table DOVE (
     COD_STATO varchar(2) not null,
     Antigeni varchar(20) not null,
     obbligatorieta_estesa char not null,
     constraint ID_DOVE_ID primary key (Antigeni, COD_STATO));

create table HA_FATTO (
     ID numeric(1) not null,
     Antigeni varchar(20) not null,
     in_data date not null,
     constraint ID_HA_FATTO_ID primary key (ID, Antigeni));

create table PAESE (
     COD_STATO varchar(2) not null,
     nome varchar(20) not null,
     constraint ID_PAESE_ID primary key (COD_STATO));

create table TEMPO (
     ID numeric(1) not null,
     anni numeric(1) not null,
     Antigeni varchar(20) not null,
     constraint ID_TEMPO_ID primary key (ID));

create table UTENTE (
     ID numeric(1) not null,
     nome varchar(30) not null,
     cognome varchar(30) not null,
     dataNascita date not null,
     tipo varchar(1) not null,
     email varchar(50) not null,
     constraint ID_UTENTE_ID primary key (ID));

create table VACCINAZIONE (
     Antigeni varchar(20) not null,
     Descrizione varchar(50) not null,
     constraint ID_VACCINAZIONE_ID primary key (Antigeni));


-- Constraints Section
-- ___________________ 

alter table DEVE_FARE add constraint FKDEV_VAC_FK
     foreign key (Antigeni)
     references VACCINAZIONE;

alter table DEVE_FARE add constraint FKDEV_UTE
     foreign key (ID)
     references UTENTE;

alter table DOVE add constraint FKDOV_VAC
     foreign key (Antigeni)
     references VACCINAZIONE;

alter table DOVE add constraint FKDOV_PAE_FK
     foreign key (COD_STATO)
     references PAESE;

alter table HA_FATTO add constraint FKHA__VAC_FK
     foreign key (Antigeni)
     references VACCINAZIONE;

alter table HA_FATTO add constraint FKHA__UTE
     foreign key (ID)
     references UTENTE;

alter table PAESE add constraint ID_PAESE_CHK
     check(exists(select * from DOVE
                  where DOVE.COD_STATO = COD_STATO)); 

alter table TEMPO add constraint FKSCHEDULARITA_FK
     foreign key (Antigeni)
     references VACCINAZIONE;

alter table UTENTE add constraint FKPOSSIEDE_FK
     foreign key (email)
     references ACCOUNT;

alter table VACCINAZIONE add constraint ID_VACCINAZIONE_CHK
     check(exists(select * from DOVE
                  where DOVE.Antigeni = Antigeni)); 

alter table VACCINAZIONE add constraint ID_VACCINAZIONE_CHK
     check(exists(select * from TEMPO
                  where TEMPO.Antigeni = Antigeni)); 


-- Index Section
-- _____________ 

create unique index ID_ACCOUNT_IND
     on ACCOUNT (email);

create unique index ID_DEVE_FARE_IND
     on DEVE_FARE (ID, Antigeni);

create index FKDEV_VAC_IND
     on DEVE_FARE (Antigeni);

create unique index ID_DOVE_IND
     on DOVE (Antigeni, COD_STATO);

create index FKDOV_PAE_IND
     on DOVE (COD_STATO);

create unique index ID_HA_FATTO_IND
     on HA_FATTO (ID, Antigeni);

create index FKHA__VAC_IND
     on HA_FATTO (Antigeni);

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

