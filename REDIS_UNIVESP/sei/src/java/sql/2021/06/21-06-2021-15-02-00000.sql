alter table disciplina add column grupopessoa int;
alter table disciplina add constraint fk_disciplina_grupopessoa foreign key (grupopessoa) references grupopessoa(codigo) on delete cascade on update cascade;
