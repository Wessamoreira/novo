alter table public.grupopessoa add column if not exists pessoasupervisorgrupo int;
ALTER TABLE public.grupopessoa ADD CONSTRAINT fk_grupopessoa_pessoasupervisorgrupo FOREIGN KEY (pessoasupervisorgrupo) REFERENCES pessoa(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;
