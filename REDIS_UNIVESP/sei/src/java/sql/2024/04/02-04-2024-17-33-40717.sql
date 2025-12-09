CREATE OR REPLACE FUNCTION public.fn_alterarsituacaomatriculaintegralizada_finalizada(usuarioalteracao integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
 DECLARE
  matriculas                  record; 
  query_update_matricula      text := '';
  query_update_matriculap     text := ''; 
begin	
		
   for matriculas in select  m.matricula,t.percentualintegralizado from  matricula m 
     				 inner join matriculaIntegralizacaoCurricular(m.matricula) as t on t.percentualintegralizado is not null
     				 where m.situacao in ('AT','FI')
     				
     				 
     		
          loop          
           raise notice 'matricula  : %',matriculas.matricula;
            begin
	            
	            if(
	            	matriculas.percentualintegralizado >= 100::numeric
	            	
	            ) then 
	             query_update_matricula := ' update matricula set situacao = ''FI''  where matricula = ''' || matriculas.matricula::text ||''' ;-- EXECUTADO PELA FUNCÃO fn_alterarSituacaoMatriculaIntegralizada_finalizada -- ul:' || usuarioAlteracao;
	             EXECUTE(query_update_matricula);
	         
	              query_update_matriculap := ' update matriculaperiodo  set situacaoMatriculaPeriodo = ''FI''  where matriculaperiodo.codigo  = 	(select mp.codigo  from matriculaperiodo mp
			      where mp.matricula = '''|| matriculas.matricula::text ||'''  and  mp.situacaoMatriculaPeriodo != ''PC'' and mp.situacaoMatriculaPeriodo  !=''FI''  order by (mp.ano || ''/'' || mp.semestre) desc,
	              case when mp.situacaoMatriculaPeriodo in (''AT'', ''PR'', ''FI'', ''FO'') then 1 else 2 end, mp.codigo desc  limit 1 );-- EXECUTADO PELA FUNCÃO fn_alterarSituacaoMatriculaIntegralizada_finalizada -- ul:' || usuarioAlteracao;
	         	  EXECUTE(query_update_matriculap);
	         
	             else 
	            	query_update_matricula := ' update matricula set situacao = ''AT''  where matricula = ''' || matriculas.matricula::text ||''' and situacao = ''FI'' ;-- EXECUTADO PELA FUNCÃO fn_alterarSituacaoMatriculaIntegralizada_finalizada -- ul:' || usuarioAlteracao;
	                EXECUTE(query_update_matricula);		
				 
	             end if;
	            
	          
            exception when others then 
            raise notice 'matricula com erro ao atualizar situação . possui calendario de matricula para matricula periodo ';
           end ;
        END LOOP;
        
  RETURN true;
END;
$function$
;
