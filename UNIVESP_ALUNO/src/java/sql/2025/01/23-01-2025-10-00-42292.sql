ALTER TABLE IF EXISTS tiporequerimento ADD COLUMN IF NOT EXISTS textopadraocontratoestagio INT;

ALTER TABLE tiporequerimento ADD CONSTRAINT fk_tiporequerimento_textopadraocontratoestagio foreign key (textopadraocontratoestagio) references textopadrao(codigo);