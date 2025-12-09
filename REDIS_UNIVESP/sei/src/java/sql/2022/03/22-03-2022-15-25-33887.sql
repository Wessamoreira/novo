CREATE OR REPLACE FUNCTION public.validaalunoassinoucontratomatricula(codmatriculaperiodo integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
begin 
    IF exists(
    select documetacaomatricula.codigo from documetacaomatricula
	inner join tipodocumento on 
	documetacaomatricula.tipodedocumento=tipodocumento.codigo
	inner join matricula on documetacaomatricula.matricula=matricula.matricula
	inner join matriculaperiodo on matriculaperiodo.matricula=matricula.matricula
	where tipodocumento.contrato is true and
	documetacaomatricula.entregue is true
	and matriculaperiodo.codigo = codMatriculaPeriodo
	and exists (select configuracaogeralsistema.codigo from configuracaogeralsistema where configuracaogeralsistema.idautenticacaoservotimize = 'IPOG')
    ) or exists(
    select documentoassinado.codigo from documentoassinado
	 inner join documentoassinadopessoa on documentoassinado.codigo = documentoassinadopessoa.documentoassinado
	 WHERE documentoassinado.matriculaperiodo = codMatriculaPeriodo
	 and documentoassinadopessoa.situacaodocumentoassinadopessoa = 'ASSINADO'
	 and documentoassinado.tipoorigemdocumentoassinado = 'CONTRATO'	
	 and documentoassinado.codigo = (
	  select codigo from documentoassinado where matriculaperiodo = codMatriculaPeriodo order by codigo desc limit 1
	 )
	and exists (select configuracaogeralsistema.codigo from configuracaogeralsistema where configuracaogeralsistema.idautenticacaoservotimize != 'IPOG')
    )  THEN
       RETURN   TRUE;
    ELSE 
       RETURN false;  
    END IF; 
end;
$function$
;