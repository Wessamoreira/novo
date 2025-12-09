CREATE OR REPLACE FUNCTION
    public.fn_motivoindeferimentodocumentoaluno_validarunicidadenome(param_codigo integer, param_nome varchar) RETURNS boolean
    LANGUAGE
        plpgsql AS $function$
DECLARE
    is_nome_duplicado boolean;
BEGIN
    SELECT COUNT(*) > 0 INTO is_nome_duplicado
    FROM motivoindeferimentodocumentoaluno m
    WHERE trim(UPPER(sem_acentos(m.nome))) = trim(UPPER(sem_acentos(param_nome)))
      AND m.codigo != param_codigo;
    RETURN NOT is_nome_duplicado;
END; $function$;

ALTER TABLE IF EXISTS motivoindeferimentodocumentoaluno
    DROP CONSTRAINT IF EXISTS motivoindeferimentodocumentoaluno_nome_key,
    ADD CONSTRAINT ck_motivoindeferimentodocumentoaluno_unicidadenome
        CHECK (fn_motivoindeferimentodocumentoaluno_validarunicidadenome(codigo, nome)) NOT VALID;