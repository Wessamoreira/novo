alter table mapaconvocacaoenade alter column unidadeensino drop not null;
alter table mapaconvocacaoenade drop constraint mapaconvocacaoenade_unidadeensino_fkey;
alter table mapaconvocacaoenade alter column unidadeensino type text;