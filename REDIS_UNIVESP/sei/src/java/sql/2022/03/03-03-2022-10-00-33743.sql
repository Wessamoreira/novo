ALTER TABLE public.comunicacaointerna drop constraint if exists fk_comunicacaointerna_aluno;
ALTER TABLE public.comunicacaointerna ADD CONSTRAINT fk_comunicacaointerna_aluno FOREIGN KEY (aluno) REFERENCES pessoa(codigo) ON UPDATE RESTRICT ON DELETE cascade;
ALTER TABLE public.comunicadointernodestinatario drop constraint if exists  fk_comunicadointernodestinatario_destinatario;
ALTER TABLE public.comunicadointernodestinatario ADD CONSTRAINT fk_comunicadointernodestinatario_destinatario FOREIGN KEY (destinatario) REFERENCES pessoa(codigo) ON UPDATE RESTRICT ON DELETE cascade;