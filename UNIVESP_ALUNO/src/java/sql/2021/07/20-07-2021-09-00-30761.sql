alter table if exists unidadeensino  add column if not exists operadorResponsavel int;
alter table unidadeensino add constraint fk_unidadeensino_operadorResponsavel foreign key (operadorResponsavel) references funcionario(codigo) on delete restrict on update restrict;



 