alter table if exists registroldap  add column if not exists	pessoa int;
alter table if exists registroldap  add column if not exists	pessoaemailinstitucional int;
alter table if exists registroldap  add constraint fk_registroldap_pessoa foreign key (pessoa) references pessoa(codigo) on delete cascade on update cascade;
alter table if exists registroldap  add constraint fk_registroldap_pessoaemailinstitucional foreign key (pessoaemailinstitucional) references pessoaemailinstitucional(codigo) on delete cascade on update cascade;

