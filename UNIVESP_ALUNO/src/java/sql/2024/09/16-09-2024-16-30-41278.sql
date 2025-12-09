alter table if exists tiporequerimento add column if not exists campoafastamento BOOLEAN default false;
alter table if exists requerimento  add column if not exists dataafastamentoinicio  TIMESTAMP;
alter table if exists requerimento  add column if not exists dataafastamentofim  TIMESTAMP;