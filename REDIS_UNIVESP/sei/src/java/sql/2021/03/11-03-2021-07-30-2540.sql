alter table if exists textopadrao add column if not exists funcionarioprimario int;
select create_constraint('alter table textopadrao
add constraint  fk_textopadrao_funcionarioprimario foreign key (funcionarioprimario) references funcionario(codigo) on update restrict on delete restrict;'); 
alter table if exists textopadrao add column if not exists cargofuncionarioprimario int;
select create_constraint('alter table textopadrao add constraint
fk_textopadrao_cargofuncionarioprimario foreign key (cargofuncionarioprimario) references cargo(codigo) on update restrict on delete restrict;'); 
alter table if exists textopadrao add column if not exists funcionariosecundario int;
select create_constraint('alter table textopadrao add constraint  fk_textopadrao_funcionariosecundario
foreign key (funcionariosecundario) references funcionario(codigo) on update restrict on delete restrict;'); 
alter table if exists textopadrao add column if not exists cargofuncionariosecundario int;
select create_constraint('alter table textopadrao add constraint fk_textopadrao_cargofuncionariosecundario
foreign key (cargofuncionariosecundario) references cargo(codigo) on update restrict on delete restrict;');
alter table if exists textopadrao add column if not exists assinarDocumentoAutomaticamenteFuncionarioPrimario boolean default false;
alter table if exists textopadrao add column if not exists assinarDocumentoAutomaticamenteFuncionarioSecundario boolean default false;
alter table if exists configuracaogedorigem add column if not exists apresentarAssinaturaAluno boolean default  false;
alter table if exists configuracaogedorigem add column if not exists alturaAssinaturaAluno int default 100;
alter table if exists configuracaogedorigem add column if not exists larguraAssinaturaAluno int default 100;
alter table if exists configuracaogedorigem add column if not exists posicaoXAssinaturaAluno int default 330;
alter table if exists configuracaogedorigem add column if not exists posicaoYAssinaturaAluno int default 10;
alter table if exists configuracaogedorigem add column if not exists ultimaPaginaAssinaturaAluno boolean default  true;
alter table if exists documentoassinado add column if not exists matriculaperiodo int;
select create_constraint('alter table documentoassinado
add constraint  fk_documentoassinado_matriculaperiodo foreign key (matriculaperiodo) references matriculaperiodo(codigo) on update restrict on delete restrict;'); 
