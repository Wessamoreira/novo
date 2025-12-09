CREATE OR REPLACE FUNCTION fn_validarsituacaochequedevolucao(codigo_cheque integer) RETURNS boolean AS $$
DECLARE 
   cheque_esta_pendente boolean;
begin
	select into cheque_esta_pendente case when count(cheque.codigo) > 0 then true else false end as cheque_pendente from cheque where cheque.codigo = codigo_cheque
	and cheque.situacao = 'PE';
	return cheque_esta_pendente;
END;     
$$ LANGUAGE plpgsql;
alter table	devolucaocheque add constraint check_devolucaocheque_situacaochequedevolucao check (fn_validarsituacaochequedevolucao(cheque)) not valid;