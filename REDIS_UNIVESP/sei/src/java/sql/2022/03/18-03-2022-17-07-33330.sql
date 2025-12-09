alter table if  exists personalizacaomensagemautomatica add column if not exists curso int;
  
select create_constraint('alter table personalizacaomensagemautomatica add constraint fk_personalizacaomensagemautomatica_curso foreign key (curso) references curso (codigo)');