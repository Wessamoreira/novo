alter table if exists pessoa alter column   valorBolsa type numeric(20,2) ;
alter table if exists pessoa alter column  valorBolsa set default 0;
alter table if exists pessoa alter column  modalidadeBolsa set default '';
alter table if exists pessoa alter column  universidadeParceira set default '';