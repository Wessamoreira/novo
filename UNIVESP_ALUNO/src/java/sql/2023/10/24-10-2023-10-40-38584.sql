alter table registroatividadecomplementarmatricula
add column responsavelEditarCHConsiderada int null,
add column dataEditarCHConsiderada date null,
add CONSTRAINT fk_regativcompmatricula_responsavelEditarCHConsiderada FOREIGN KEY (responsavelEditarCHConsiderada) REFERENCES public.usuario(codigo);