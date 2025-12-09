update matricula set datacolacaograu = t.data from (
select matricula.matricula , colacaograu.data from programacaoformaturaaluno 
inner join matricula on matricula.matricula = programacaoformaturaaluno.matricula
inner join colacaograu on colacaograu.codigo = programacaoformaturaaluno.colacaograu
where programacaoformaturaaluno.colougrau = 'SI' and (matricula.dataconclusaocurso is null or matricula.datacolacaograu::DATE  != colacaograu."data"::DATE )
) as t where t.matricula = matricula.matricula;