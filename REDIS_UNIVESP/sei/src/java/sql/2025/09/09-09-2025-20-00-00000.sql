alter table ConfiguracaoGeralSistema add column if not exists ativarDebugEmail boolean default false;
alter table ConfiguracaoGeralSistema add column if not exists timeOutFilaEmail int default 0;