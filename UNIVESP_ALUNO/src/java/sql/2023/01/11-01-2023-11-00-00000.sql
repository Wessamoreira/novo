alter table ArtefatoAjuda add column if not exists submodulo varchar(250);
alter table ArtefatoAjuda add column if not exists tela varchar(500); 
alter table ArtefatoAjuda add column if not exists created timestamp default now();
alter table ArtefatoAjuda add column if not exists updated timestamp;
alter table ArtefatoAjuda add column if not exists versao varchar(50);
alter table ArtefatoAjuda add column if not exists recurso varchar(250);

alter table artefatoajuda alter column link type text;
alter table artefatoajuda alter column palavraschave type text;
alter table artefatoajuda alter column titulo type varchar(250);
