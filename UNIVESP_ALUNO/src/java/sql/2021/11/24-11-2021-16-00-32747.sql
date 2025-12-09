alter table if exists renovacaomatriculaturma  add column if not exists renovarapenasalunosrenovacaoautomatica boolean default true;
alter table if exists renovacaomatriculaturma  add column if not exists unidadeensino int;
alter table if exists renovacaomatriculaturma  add column if not exists curso int;
alter table if exists renovacaomatriculaturmamatriculaperiodo  add column if not exists novamatriculaperiodo int;
alter table if exists renovacaomatriculaturma  drop column if exists debitoFinanceiro;
alter table if exists renovacaomatriculaturma  drop column if exists convenio;
alter table if exists renovacaomatriculaturma  drop column if exists planoDesconto;
alter table if exists renovacaomatriculaturma  alter column gradecurricularatual drop not null;
select create_constraint('alter table renovacaomatriculaturma add constraint fk_renovacaomatriculaturma_unidadeensino foreign key (unidadeensino) references unidadeensino (codigo) on update restrict on delete restrict');
select create_constraint('alter table renovacaomatriculaturma add constraint fk_renovacaomatriculaturma_curso foreign key (curso) references curso (codigo) on update restrict on delete restrict');
select create_constraint('alter table renovacaomatriculaturmamatriculaperiodo add constraint fk_renovacaomatriculaturmamatriculaperiodo_novamatriculaperiodo foreign key (novamatriculaperiodo) references matriculaperiodo (codigo) on update cascade on delete cascade');





