alter table processomatriculacalendario add column if not exists diaSemanaAula varchar(50) default 'NENHUM';
alter table processomatriculacalendario add column if not exists turnoAula varchar(50) default 'NENHUM';
alter table matricula add column if not exists diaSemanaAula varchar(50) default 'NENHUM';
alter table matricula add column if not exists turnoAula varchar(50) default 'NENHUM';