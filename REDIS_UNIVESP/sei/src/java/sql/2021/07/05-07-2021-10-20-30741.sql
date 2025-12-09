alter table public.estagio add column if not exists ano varchar(8);
alter table public.estagio add column if not exists semestre varchar(1);

alter table public.estagio add column if not exists cidadenome varchar(250);
update public.estagio set cidadenome = (select nome from cidade where cidade.codigo = estagio.cidade) where 1=1;
update public.estagio set cidade = null where 1=1;
ALTER TABLE public.estagio drop CONSTRAINT if exists  fk_estagio_cidade ;
alter table public.estagio alter column cidade type varchar(250);
update public.estagio set cidade = cidadenome where 1=1;
alter table public.estagio drop column cidadenome;

alter table public.concedente add column if not exists cidadenome varchar(250);
update public.concedente set cidadenome = (select nome from cidade where cidade.codigo = concedente.cidade) where 1=1;
update public.concedente set cidade = null where 1=1;
ALTER TABLE public.concedente drop CONSTRAINT if exists  fk_concedente_cidade ;
alter table public.concedente alter column cidade type varchar(250);
update public.concedente set cidade = cidadenome where 1=1;
alter table public.concedente drop column cidadenome;