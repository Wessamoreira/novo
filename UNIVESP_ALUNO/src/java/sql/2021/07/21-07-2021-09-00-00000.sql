ALTER TABLE public.textopadraodeclaracao ALTER COLUMN alinhamentoassinaturadigitalenum SET DEFAULT 'RODAPE_DIREITA';
ALTER TABLE public.textopadraodeclaracao ALTER COLUMN tipodesignetextoenum SET DEFAULT 'PDF';
update public.textopadraodeclaracao set alinhamentoassinaturadigitalenum =  'RODAPE_DIREITA' where alinhamentoassinaturadigitalenum is null;
update public.textopadraodeclaracao set tipodesignetextoenum =  'PDF' where tipodesignetextoenum is null;


