alter table if exists pessoa add column if not exists universidadeParceira varchar(255) default '', 
 add column if not exists modalidadeBolsa varchar(255) default '', 
 add column if not exists valorBolsa numeric default 0;