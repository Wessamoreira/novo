alter table if exists configuracaoseiblackboard add column if not exists fontededadosconteudomasterblackboard text;
alter table if exists disciplina add column if not exists idconteudomasterblackboard text;
alter table if exists curso add column if not exists idconteudomasterblackboardestagio text;
