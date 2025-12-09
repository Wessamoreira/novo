update historiconotablackboard set salaaulablackboard = t.salaaulablackboard from (
select salaaulablackboardpessoa.salaaulablackboard, historiconotablackboard.codigo from salaaulablackboardpessoa
inner join historiconotablackboard on salaaulablackboardpessoa.codigo = historiconotablackboard.salaaulablackboardpessoa
) as t where t.codigo = historiconotablackboard.codigo;