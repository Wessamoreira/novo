update gradecurricular
set qtdeanosemestreparaintegralizacaocurso = periodo.qtdeanosemestreparaintegralizacaocurso from (
SELECT (count(*) + 2) as qtdeanosemestreparaintegralizacaocurso, p.gradecurricular
FROM periodoletivo p
         INNER JOIN gradecurricular g ON p.gradecurricular = g.codigo
WHERE g.qtdeanosemestreparaintegralizacaocurso = 0 
GROUP BY p.gradecurricular) as periodo
where gradecurricular.codigo = periodo.gradecurricular
  and gradecurricular.qtdeanosemestreparaintegralizacaocurso = 0;
