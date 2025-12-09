alter table if exists documentoassinado  add column if not exists	motivodocumentoassinadoinvalido text;
alter table if exists documentoassinado  add column if not exists	documentoassinadoinvalido boolean default false;




