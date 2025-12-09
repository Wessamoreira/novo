alter table if exists ConfiguracaoBiblioteca  add column if not exists habilitarIntegracaoBvPearson boolean default false;
alter table if exists ConfiguracaoBiblioteca  add column if not exists linkacessobvperson varchar(150); 
alter table if exists ConfiguracaoBiblioteca  add column if not exists chavetokenbvperson varchar(150); 