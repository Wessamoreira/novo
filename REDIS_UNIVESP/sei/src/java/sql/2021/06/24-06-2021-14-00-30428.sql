alter table if exists estagio  add column if not exists cargahorariadeferida int;
alter table if exists estagio  add column if not exists responsaveldeferimento int;
alter table if exists estagio  add column if not exists	datadeferimento timestamp;
alter table if exists estagio  add column if not exists responsavelindeferimento int;
alter table if exists estagio  add column if not exists	dataindeferimento timestamp;
alter table if exists estagio  add CONSTRAINT fk_estagio_responsaveldeferimento FOREIGN KEY (responsaveldeferimento) REFERENCES public.usuario(codigo) ON DELETE RESTRICT ON UPDATE RESTRICT;
alter table if exists estagio  add CONSTRAINT fk_estagio_responsavelindeferimento FOREIGN KEY (responsavelindeferimento) REFERENCES public.usuario(codigo) ON DELETE RESTRICT ON UPDATE RESTRICT;