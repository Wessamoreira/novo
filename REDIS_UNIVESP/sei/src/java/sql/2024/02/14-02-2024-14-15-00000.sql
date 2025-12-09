ALTER TABLE IF EXISTS artefatoajuda ADD COLUMN IF NOT EXISTS textoInformativo TEXT;

ALTER TABLE IF EXISTS novidadeSEI ADD COLUMN IF NOT EXISTS textoInformativo TEXT;
ALTER TABLE IF EXISTS novidadeSEI ALTER COLUMN dataLimiteDisponibilidade type TIMESTAMP;
ALTER TABLE IF EXISTS novidadeSEI ADD COLUMN IF NOT EXISTS dataInicioDisponibilidade TIMESTAMP;
ALTER TABLE IF EXISTS novidadeSEI ADD COLUMN IF NOT EXISTS destaque boolean ;

select create_constraint('alter table configuracaofinanceiro add constraint fk_configuracaofinanceiro_contacorrentepadraocontrolecobranca foreign key (contacorrentepadraocontrolecobranca) references contacorrente(codigo)');
select create_constraint('alter table configuracaofinanceiro add constraint fk_configuracaofinanceiro_contacorrentereimpressaoboletos foreign key (contacorrentereimpressaoboletos) references contacorrente(codigo)');
