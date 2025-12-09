alter table if exists unidadeensino  add column if not exists orientadorPadraoEstagio int;
alter table if exists unidadeensino  add column if not exists observacao text;
alter table unidadeensino add constraint fk_unidadeensino_orientadorPadraoEstagio foreign key (orientadorPadraoEstagio) references funcionario(codigo) on delete restrict on update restrict;



 