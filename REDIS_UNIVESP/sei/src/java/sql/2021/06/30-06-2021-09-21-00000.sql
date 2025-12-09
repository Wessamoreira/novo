alter table questionarioRespostaOrigemMotivosPadroesEstagio add column if not exists created timestamp;
alter table questionarioRespostaOrigemMotivosPadroesEstagio add column if not exists codigoCreated int;
alter table questionarioRespostaOrigemMotivosPadroesEstagio add column if not exists nomeCreated varchar(255);
alter table questionarioRespostaOrigemMotivosPadroesEstagio add column if not exists updated timestamp;
alter table questionarioRespostaOrigemMotivosPadroesEstagio add column if not exists codigoUpdated int;
alter table questionarioRespostaOrigemMotivosPadroesEstagio add column if not exists nomeUpdated varchar(255);
