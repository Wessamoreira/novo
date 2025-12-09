alter table if exists salaaulablackboardoperacao add column if not exists agrupamentounidadeensino int;
select create_constraint('alter table salaaulablackboardoperacao add constraint fk_salaaulablackboardoperacao_agrupamentounidadeensino foreign key (agrupamentounidadeensino) references agrupamentounidadeensino (codigo) on update restrict on delete restrict');
