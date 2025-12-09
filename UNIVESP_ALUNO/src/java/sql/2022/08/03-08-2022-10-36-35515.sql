ALTER TABLE IF EXISTS curso ALTER COLUMN permitirAssinarContratoPendenciaDocumentacao SET DEFAULT(TRUE);

UPDATE curso SET permitirassinarcontratopendenciadocumentacao = TRUE;