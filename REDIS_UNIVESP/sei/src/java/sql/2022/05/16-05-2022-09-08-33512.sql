create table if not exists eixocurso(
codigo serial not null,
nome varchar(255) not null unique,
constraint eixocurso_pkey primary key (codigo));

alter table if exists curso add column if not exists eixocurso int,
add constraint curso_eixocurso_fkey foreign key (eixocurso) references eixocurso (codigo) on update cascade on delete cascade;