alter table if exists questionariorespostaorigem  add column if not exists estagio int;
alter table if exists questionariorespostaorigem  add column if not exists nrVersao int;
alter table if exists questionariorespostaorigem  add column if not exists observacaofinal text;
alter table if exists questionariorespostaorigem  add column if not exists motivo text;
alter table if exists questionariorespostaorigem  add column if not exists situacaoQuestionarioRespostaOrigemEnum varchar(100) default 'EM_PREENCHIMENTO';
alter table if exists questionariorespostaorigem  add column if not exists	dataLimiteAnalise timestamp;
alter table if exists questionariorespostaorigem  add column if not exists	dataLimiteCorrecao timestamp;
alter table if exists questionariorespostaorigem  add column if not exists	dataEnvioAnalise timestamp;
alter table if exists questionariorespostaorigem  add column if not exists	dataEnvioCorrecao timestamp;
alter table if exists questionariorespostaorigem  add CONSTRAINT fk_questionariorespostaorigem_estagio FOREIGN KEY (estagio) REFERENCES public.estagio(codigo) ON DELETE CASCADE ON UPDATE CASCADE;




