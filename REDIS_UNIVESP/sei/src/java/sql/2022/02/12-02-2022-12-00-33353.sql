alter table tiporequerimento add column if not exists percentualIntegralizacaoCurricularInicial int default 100;
alter table tiporequerimento add column if not exists percentualIntegralizacaoCurricularFinal int default 100;
alter table tiporequerimento add column if not exists registrarAproveitamentoDisciplinaTCC boolean default false;

alter table requerimento alter column titulomonografia type text;