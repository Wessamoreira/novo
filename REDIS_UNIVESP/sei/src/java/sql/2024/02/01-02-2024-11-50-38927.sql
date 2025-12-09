 ALTER TABLE IF EXISTS curso  add COLUMN if not exists textopadraocontratomatriculaCalouro int4 null;			
 SELECT create_constraint('ALTER TABLE curso  ADD CONSTRAINT fk_curso_textopadrao FOREIGN KEY (textopadraocontratomatriculaCalouro) REFERENCES textopadrao(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT;');
