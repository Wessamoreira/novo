ALTER TABLE planoensino ALTER COLUMN unidadeensino DROP NOT NULL;
ALTER TABLE planoensino ALTER COLUMN curso DROP NOT NULL;

alter table planoensino add column if not exists periodicidade varchar(2) default 'SE';

ALTER TABLE configuracaogeralsistema add column if not exists questionarioplanoensino int4 NULL;

ALTER TABLE  configuracaogeralsistema ADD CONSTRAINT fk_configuracaogeralsistema_questionarioplanoensino 
FOREIGN KEY (questionarioplanoensino) REFERENCES questionario(codigo)ON UPDATE cascade ON DELETE cascade;
