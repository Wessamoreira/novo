ALTER TABLE public.impressaodeclaracao DROP CONSTRAINT fk_impressaodeclaracao_documentoassinado; 
ALTER TABLE public.impressaodeclaracao ADD CONSTRAINT fk_impressaodeclaracao_documentoassinado FOREIGN KEY (documentoassinado) REFERENCES documentoassinado(codigo) ON DELETE cascade;
ALTER TABLE public.documentoassinado drop CONSTRAINT fk_documentoassinado_matriculaperiodo;
ALTER TABLE public.documentoassinado ADD CONSTRAINT fk_documentoassinado_matriculaperiodo FOREIGN KEY (matriculaperiodo) REFERENCES matriculaperiodo(codigo) ON UPDATE CASCADE ON DELETE cascade;