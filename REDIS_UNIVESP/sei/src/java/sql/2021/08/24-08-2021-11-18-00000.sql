create table if not exists historicoNotaBlackboard
(
    codigo            serial,
    salaAulaBlackboardPessoa     int not null,
    notaAnterior     numeric(20, 2),
    nota     numeric(20, 2),
    historico int not null,
    situacaoHistoricoNotaBlackboardEnum varchar(200) not null,
    motivo       text,
    usuarioResponsavel       int null,
    dataDeferimentoIndeferimento    timestamp default now(),
    created           timestamp default now(),
    codigoCreated     int,
    nomeCreated       varchar(200),
    updated           timestamp default now(),
    codigoUpdated     int,
    nomeUpdated       varchar(200),
    constraint pk_historicoNotaBlackboard primary key (codigo),
    constraint fk_hnb_salaAulaBlackboardPessoa foreign key (salaAulaBlackboardPessoa) references salaAulaBlackboardPessoa (codigo) on update restrict on delete restrict,
    constraint fk_hnb_historico foreign key (historico) references historico (codigo) on update restrict on delete restrict,
    constraint fk_hnb_usuarioResponsavel foreign key (usuarioResponsavel) references usuario (codigo) on update restrict on delete restrict
);
