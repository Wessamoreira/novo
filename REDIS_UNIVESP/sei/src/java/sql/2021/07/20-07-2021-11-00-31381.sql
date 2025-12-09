
CREATE OR REPLACE FUNCTION public.restringir_exclusao_pmc_matriculaperiodo()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
 DECLARE 
  quantidadeRegistro integer; 
 BEGIN  
         --Efetuar Consulta para Obter registro de Matricula Período Vinculada Ao Calendário de Matrícula que o Usuário deseja Excluir.
         SELECT INTO quantidadeRegistro COUNT(matriculaperiodo.codigo) as quantidade_registro 
         FROM matriculaperiodo 
         INNER JOIN processomatricula            ON matriculaperiodo.processomatricula = processomatricula.codigo  and processomatricula.codigo = OLD.processomatricula 
         INNER JOIN unidadeensinocurso            ON matriculaperiodo.unidadeensinocurso = unidadeensinocurso.codigo and unidadeensinocurso.curso = OLD.curso
         and unidadeensinocurso.turno = OLD.turno
         INNER JOIN  processomatriculacalendario ON processomatricula.codigo = processomatriculacalendario.processomatricula 
         AND unidadeensinocurso.curso = processomatriculacalendario.curso
         AND unidadeensinocurso.turno = processomatriculacalendario.turno         
         ;
      
        -- Verificar se existe Matricula Período Vinculada Ao Calendário de Matrícula que o Usuário deseja Excluir.
        IF quantidadeRegistro > 0 THEN
            RAISE EXCEPTION 'O Registro Não Pode Ser Excluído! Existem Matrículas Vinculadas a este Processo Matricula Calendário.';
        END IF;
        RETURN OLD;
    END;
$function$
;