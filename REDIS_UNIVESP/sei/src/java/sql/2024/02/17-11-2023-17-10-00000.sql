delete from novidadesei where codigo >= 0;
alter table novidadesei add column if not exists tipoArtefato varchar(30) default 'PDF';
alter table novidadesei add column if not exists dataVersao date default current_date;
alter table novidadesei add column if not exists palavrasChaves text;
alter table novidadesei add column if not exists tipoNovidade varchar(30) default 'NEWS';
alter table novidadesei add column if not exists dataLimiteDisponibilidade DATE ;
alter table preferenciasistemausuario add column if not exists apresentarNovidade boolean default true;
delete from artefatoajuda where titulo ilike ('%teste%');