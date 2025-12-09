INSERT INTO public.disciplinaabono (abonofalta, disciplina, matricula, registroaula, faltaabonada, faltajustificada)
  (SELECT min(a.codigo) AS abono,
          matriculaperiodoturmadisciplina.disciplina,
          matriculaperiodoturmadisciplina.matricula,
          registroaula.codigo,
          abonado,
          justificado
   FROM frequenciaaula
   INNER JOIN registroaula ON registroaula.codigo = frequenciaaula.registroaula
   INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.codigo = frequenciaaula.matriculaperiodoturmadisciplina
   LEFT JOIN disciplinaabono ON disciplinaabono.registroaula = registroaula.codigo
   AND disciplinaabono.matricula = matriculaperiodoturmadisciplina.matricula
   AND disciplinaabono.disciplina = matriculaperiodoturmadisciplina.disciplina
   LEFT JOIN abonofalta a ON a.matricula = matriculaperiodoturmadisciplina.matricula
   AND a.datainicio <= registroaula."data"
   AND a.datafim >= registroaula."data"
   AND ((a.tipoabono = 'JU'
         AND justificado)
        OR (a.tipoabono != 'JU'
            AND abonado))
   WHERE 1 = 1
     AND (justificado
          OR abonado)
     AND disciplinaabono.codigo IS NULL
   GROUP BY matriculaperiodoturmadisciplina.disciplina,
            matriculaperiodoturmadisciplina.matricula,
            registroaula.codigo,
            abonado,
            justificado
   HAVING min(a.codigo) > 0);