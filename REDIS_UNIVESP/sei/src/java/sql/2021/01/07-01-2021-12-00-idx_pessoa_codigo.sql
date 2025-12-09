DROP INDEX IF EXISTS idx_pessoa_codigo_funcao_encript;
CREATE INDEX IF NOT EXISTS idx_pessoa_codigo_encripted ON public.pessoa USING btree (encode(digest(codigo::text, 'sha256'::text), 'hex'));