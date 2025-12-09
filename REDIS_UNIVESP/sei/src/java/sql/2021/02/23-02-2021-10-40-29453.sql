alter table pixcontacorrente add column if not exists valorcontareceberenvio numeric(20,2);
alter table pixcontacorrente add column if not exists tempoexpiracao integer;
alter table pixcontacorrente drop column updated;