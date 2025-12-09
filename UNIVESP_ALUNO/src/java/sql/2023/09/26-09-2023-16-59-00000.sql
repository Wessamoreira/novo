CREATE OR REPLACE FUNCTION public.create_constraint(constraint_sql text)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
DECLARE
  v_nomeconstraint text := '';
BEGIN
    v_nomeconstraint  := lower(substring(trim( split_part( fn_remover_espaco_duplicado(constraint_sql) , ' ' , 6) , ' ââ '),1,63));
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = v_nomeconstraint) THEN
        EXECUTE constraint_sql;
    END IF;
END;
$function$
;