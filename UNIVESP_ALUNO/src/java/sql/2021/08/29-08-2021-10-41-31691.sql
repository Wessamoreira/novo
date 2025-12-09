alter table tiporequerimento add column if not exists deferirAutomaticamenteTrancamento boolean;
alter table requerimento add column if not exists justificativaTrancamento text;
alter table requerimento add column if not exists motivoCancelamentoTrancamento integer;
alter table requerimento ADD constraint fk_requerimento_motivoCancelamentoTrancamento FOREIGN KEY (motivoCancelamentoTrancamento) REFERENCES motivoCancelamentoTrancamento(codigo) ON UPDATE RESTRICT ON DELETE restrict;