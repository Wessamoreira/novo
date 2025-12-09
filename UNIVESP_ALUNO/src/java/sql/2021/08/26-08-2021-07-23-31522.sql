alter table atendimentointeracaosolicitante add column if not exists usuarioquestionamento int;

alter table atendimentointeracaosolicitante add constraint fk_atendimentointeracaosolicitante_usuarioquestionamento foreign key (usuarioquestionamento)references usuario (codigo);

alter table atendimentointeracaosolicitante add column if not exists usuariorespostaquestionamento int;

alter table atendimentointeracaosolicitante add constraint  fk_atendimentointeracaosolicitante_usuariorespostaquestionamento foreign key (usuariorespostaquestionamento) references usuario (codigo);


alter table atendimentointeracaodepartamento add column if not exists usuarioquestionamento int;

alter table atendimentointeracaodepartamento add constraint fk_atendimentointeracaodepartamento_usuarioquestionamento
foreign key (usuarioquestionamento) references usuario (codigo);

alter table atendimentointeracaodepartamento add column if not exists usuariorespostaquestionamento int;

alter table atendimentointeracaodepartamento add constraint fk_atendimentointeracaodepartamento_usuariorespostaquestionamento
foreign key (usuariorespostaquestionamento) references usuario (codigo);