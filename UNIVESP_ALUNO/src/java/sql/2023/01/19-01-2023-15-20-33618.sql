 ALTER TABLE IF EXISTS funcionario  add COLUMN if not exists naonotificarinclusaousuario  boolean default false;
 ALTER TABLE IF EXISTS departamento  add COLUMN if not exists configuracaoLdap  integer;
select create_constraint('alter table departamento add constraint fk_departamento_configuracaoLdap foreign key (configuracaoLdap) references configuracaoLdap (codigo)');
