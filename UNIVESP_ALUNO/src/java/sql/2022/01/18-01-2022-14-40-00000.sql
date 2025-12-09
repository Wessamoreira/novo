ALTER TABLE public.historiconotablackboard ALTER COLUMN salaaulablackboardpessoa DROP NOT NULL;
ALTER TABLE public.historiconotablackboard ALTER COLUMN historico DROP NOT NULL;
alter table if exists historiconotablackboard add column if not exists nomePessoaBlackboard varchar(250);
alter table if exists historiconotablackboard add column if not exists emailPessoaBlackboard varchar(250);
alter table if exists historiconotablackboard add column if not exists salaAulaBlackboard int;
select create_constraint('alter table historiconotablackboard add constraint fk_historiconotablackboard_historico foreign key (historico) references historico (codigo) on update restrict on delete restrict');
select create_constraint('alter table historiconotablackboard add constraint fk_historiconotablackboard_salaaulablackboardpessoa foreign key (salaaulablackboardpessoa) references salaaulablackboardpessoa (codigo) on update restrict on delete restrict');
select create_constraint('alter table historiconotablackboard add constraint fk_historiconotablackboard_salaAulaBlackboard foreign key (salaAulaBlackboard) references salaAulaBlackboard (codigo) on update restrict on delete restrict');