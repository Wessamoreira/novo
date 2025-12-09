alter table if exists estagio  add column if not exists	dataenvioassinaturapendente timestamp;
alter table if exists configuracaoestagioobrigatorio add column if not exists qtddiasmaximoparaassinaturaestagio int;
alter table if exists configuracaoestagioobrigatorio add column if not exists qtddiasnotificacaoassinaturaestagio int;