alter table cursocoordenador alter unidadeensino drop not null;
ALTER TABLE public.cursocoordenador ADD CONSTRAINT fk_cursocoordenador_unidadeensino FOREIGN KEY (unidadeensino) REFERENCES unidadeensino(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;
