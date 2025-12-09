CREATE OR REPLACE FUNCTION public.f_sem_acentos(text)
 RETURNS text
 LANGUAGE sql
 IMMUTABLE PARALLEL SAFE STRICT
AS $function$
SELECT public.unaccent('public.unaccent', trim(replace($1, '''', '')))
$function$;

CREATE OR REPLACE FUNCTION public.fn_pessoa_validarunicidadecpf(codigo_pessoa integer, cpf_pessoa character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
   is_cpf_unico boolean;
BEGIN
	SELECT INTO is_cpf_unico CASE WHEN pe.codigo IS NULL THEN TRUE ELSE FALSE END AS existe_registro FROM pessoa pe 
	WHERE pe.codigo != COALESCE(codigo_pessoa, 0)
	AND pe.cpf IS NOT NULL
	AND cpf_pessoa IS NOT NULL
	AND REPLACE(REPLACE(pe.cpf, '.', ''), '-', '') = REPLACE(REPLACE(cpf_pessoa, '.', ''), '-', '');
    RETURN is_cpf_unico;
END;
$function$;

ALTER TABLE IF EXISTS configuracaogeralsistema ADD COLUMN IF NOT EXISTS habilitarrecursosacademicosvisaoaluno bool DEFAULT TRUE;

CREATE INDEX IF NOT EXISTS idx_pessoaemailinstitucional_email_sem_acentos ON public.pessoaemailinstitucional USING gin (f_sem_acentos((email)::text) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_pessoa_email_sem_acentos ON public.pessoa USING gin (f_sem_acentos((email)::text) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_pessoa_email2_sem_acentos ON public.pessoa USING gin (f_sem_acentos((email2)::text) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_pessoa_nome_sem_acentos ON public.pessoa USING gin (f_sem_acentos((nome)::text) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_requerimento_documentoassinado ON public.requerimento USING btree (documentoassinado);

CREATE INDEX IF NOT EXISTS idx_impressaodeclaracao_documentoassinado ON public.impressaodeclaracao USING btree (documentoassinado);

CREATE INDEX IF NOT EXISTS idx_impressaodeclaracao_matricula ON public.impressaodeclaracao USING btree (matricula);

CREATE INDEX IF NOT EXISTS idx_documentacaomatricula_arquivoassinado ON public.documetacaomatricula USING btree (arquivoassinado);

CREATE INDEX IF NOT EXISTS idx_documentacaomatricula_arquivoverso ON public.documetacaomatricula USING btree (arquivoverso);

CREATE INDEX IF NOT EXISTS idx_documentoassinado_arquivovisual ON public.documentoassinado USING btree (arquivovisual);

CREATE INDEX IF NOT EXISTS idx_registroatividadecomplementarmatricula_arquivo ON public.registroatividadecomplementarmatricula USING btree (arquivo);

CREATE INDEX IF NOT EXISTS idx_requerimento_comprovantesolicitacaoisencao ON public.requerimento USING btree (comprovantesolicitacaoisencao);

CREATE INDEX IF NOT EXISTS idx_prospects_arquivo ON public.prospects USING btree (arquivofoto);