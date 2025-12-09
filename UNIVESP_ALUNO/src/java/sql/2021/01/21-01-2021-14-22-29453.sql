alter table contacorrente add column if not exists habilitarregistropix boolean default false;
alter table contacorrente add column if not exists ambientecontacorrentepix varchar(50) default 'HOMOLOGACAO';
alter table contacorrente add column if not exists tokenidregistropix varchar(250);
alter table contacorrente add column if not exists tokenkeyregistropix varchar(250);
alter table contacorrente add column if not exists chavepix varchar(50);
alter table contacorrente add column if not exists formarecebimentopadraopix int ;
alter table contacorrente ADD CONSTRAINT fk_contacorrente_formapagamento FOREIGN KEY (formarecebimentopadraopix) REFERENCES formapagamento(codigo) ON UPDATE RESTRICT ON DELETE restrict;