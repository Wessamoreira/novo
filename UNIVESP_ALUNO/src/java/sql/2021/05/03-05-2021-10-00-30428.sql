alter table if exists curso  add column if not exists grupoPessoaAnaliseRelatorioFinalEstagio int;
alter table if exists curso  add column if not exists grupoPessoaAnaliseAproveitamentoEstagioObrigatorio int;
alter table if exists curso  add column if not exists grupoPessoaAnaliseEquivalenciaEstagioObrigatorio int;
alter table if exists curso  add column if not exists funcionarioResponsavelAssinaturaTermoEstagio int;

alter table curso add constraint fk_curso_grupoPessoaAnaliseRelatorioFinalEstagio foreign key (grupoPessoaAnaliseRelatorioFinalEstagio) references grupopessoa(codigo) on delete restrict on update restrict;
alter table curso add constraint fk_curso_grupoPessoaAnaliseAproveitamentoEstagioObrigatorio foreign key (grupoPessoaAnaliseAproveitamentoEstagioObrigatorio) references grupopessoa(codigo) on delete restrict on update restrict;
alter table curso add constraint fk_curso_grupoPessoaAnaliseEquivalenciaEstagioObrigatorio foreign key (grupoPessoaAnaliseEquivalenciaEstagioObrigatorio) references grupopessoa(codigo) on delete restrict on update restrict;
alter table curso add constraint fk_curso_funcionarioResponsavelAssinaturaTermoEstagio foreign key (funcionarioResponsavelAssinaturaTermoEstagio) references funcionario(codigo) on delete restrict on update restrict;



