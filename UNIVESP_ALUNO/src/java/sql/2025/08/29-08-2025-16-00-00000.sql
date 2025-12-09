alter table integracaomestregr add column if not exists ano varchar(4);
alter table integracaomestregr add column if not exists semestre varchar(1);
alter table integracaomestregr add column if not exists bimestre int;
alter table integracaomestregr add column if not exists unidadeEnsinos text;
alter table integracaomestregr add column if not exists cursos text;
alter table integracaomestregr add column if not exists disciplina int;
alter table integracaomestregr add column if not exists periodoRequerimentoInicio date;
alter table integracaomestregr add column if not exists periodoRequerimentoTermino date;