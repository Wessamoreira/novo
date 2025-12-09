alter table personalizacaomensagemautomatica add column if not exists usuarioultimaalteracao int;

select create_constraint('alter table personalizacaomensagemautomatica add constraint fk_personalizacaomensagemautomatica_usuarioultimaalteracao foreign key (usuarioultimaalteracao) references usuario (codigo)');

alter table personalizacaomensagemautomatica add column if not exists dataultimaalteracao timestamp;
