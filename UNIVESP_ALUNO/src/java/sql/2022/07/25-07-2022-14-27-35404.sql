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
    	select matriculaperiodo.codigo from matriculaperiodo
    	inner join matricula on matricula.matricula = matriculaperiodo.matricula
    	inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo
    	left join configuracaogedorigem on configuracaogedorigem.configuracaoged  = unidadeensino.configuracaoged and configuracaogedorigem.tipoorigemdocumentoassinado = 'CONTRATO'
	 	inner join textopadrao on textopadrao.codigo = matriculaperiodo.contratomatricula 
	 	WHERE (coalesce(textopadrao.assinardigitalmentetextopadrao, false)  is false
	 	or (
	 		coalesce(textopadrao.assinardigitalmentetextopadrao, false)
	 		and (unidadeensino.configuracaoged is null or configuracaogedorigem.codigo is NULL OR coalesce(configuracaogedorigem.assinardocumento, false) is false)
	 	))
	 	and matriculaperiodo.codigo = codMatriculaPeriodo
		and exists (select configuracaogeralsistema.codigo from configuracaogeralsistema where configuracaogeralsistema.idautenticacaoservotimize != 'IPOG')
    ) or exists(
    	select documentoassinado.codigo from  documentoassinado
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
