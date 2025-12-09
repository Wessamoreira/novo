ALTER TABLE devolucaocheque ADD COLUMN desconsiderarConciliacaoBancaria BOOLEAN DEFAULT false;

CREATE OR REPLACE FUNCTION public.fn_validarsituacaochequedevolucao(codigo_cheque integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
   cheque_esta_pendente boolean;
begin
	select into cheque_esta_pendente case when count(cheque.codigo) > 0 then true else false end as cheque_pendente from cheque where cheque.codigo = codigo_cheque
	and cheque.situacao IN ('PE', 'BA');
	return true;
END;     
$function$
;