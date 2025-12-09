alter table if exists configuracaogeralsistema 
add ativarIntegracaoLGPD boolean default (false),
add nomeDestinatarioEmailIntegracaoLGPD varchar(250),
add emailDestinatarioIntegracaoLGPD varchar(250),
add assuntoMensagemLGPD varchar(250),
add mensagemLGPD text,
add textopadraodeclaracao int,
add constraint fk_textopadraodeclaracao foreign key (textopadraodeclaracao) references textopadraodeclaracao(codigo) on update cascade on delete cascade;