alter table  if exists mapaconvocacaoenade add column if not exists arquivoalunoingressante integer;
alter table  if exists mapaconvocacaoenade add column if not exists arquivoalunoconcluinte integer;
alter table  if exists enade add column if not exists codigoprojeto varchar(7);
SELECT create_constraint('ALTER TABLE mapaconvocacaoenade ADD CONSTRAINT fk_mapaconvocacaoenade_arquivoingressante FOREIGN KEY (arquivoalunoingressante) REFERENCES arquivo(codigo) ON DELETE RESTRICT ON UPDATE RESTRICT;');
SELECT create_constraint('ALTER TABLE mapaconvocacaoenade ADD CONSTRAINT fk_mapaconvocacaoenade_arquivoconcluinte FOREIGN KEY (arquivoalunoconcluinte) REFERENCES arquivo(codigo) ON DELETE RESTRICT ON UPDATE RESTRICT;');
