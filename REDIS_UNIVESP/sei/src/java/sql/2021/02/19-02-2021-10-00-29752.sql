
update turmadisciplinanotaparcial set variavel = 'N01' where variavel = 'N1';
update turmadisciplinanotaparcial set variavel = 'N02' where variavel = 'N2';
update turmadisciplinanotaparcial set variavel = 'N03' where variavel = 'N3';
update turmadisciplinanotaparcial set variavel = 'N04' where variavel = 'N4';
update turmadisciplinanotaparcial set variavel = 'N05' where variavel = 'N5';
update turmadisciplinanotaparcial set variavel = 'N06' where variavel = 'N6';
update turmadisciplinanotaparcial set variavel = 'N07' where variavel = 'N7';
update turmadisciplinanotaparcial set variavel = 'N08' where variavel = 'N8';
update turmadisciplinanotaparcial set variavel = 'N09' where variavel = 'N9';

update turmadisciplinanotatitulo set formula= t.formula from (
select codigo, replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(formula, 'N'||ordem, 'N0'||ordem)
, 'N0'||ordem||'0', 'N'||ordem||'0'), 'N0'||ordem||'1', 'N'||ordem||'1'), 'N0'||ordem||'2', 'N'||ordem||'2'), 'N0'||ordem||'3', 'N'||ordem||'3'), 'N0'||ordem||'4', 'N'||ordem||'4'), 
'N0'||ordem||'5', 'N'||ordem||'5'), 'N0'||ordem||'6', 'N'||ordem||'6'), 'N0'||ordem||'7', 'N'||ordem||'7'), 'N0'||ordem||'8', 'N'||ordem||'8'), 'N0'||ordem||'9', 'N'||ordem||'9') as formula
from 
(select codigo, replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(formula, 'N'||ordem, 'N0'||ordem)
, 'N0'||ordem||'0', 'N'||ordem||'0'), 'N0'||ordem||'1', 'N'||ordem||'1'), 'N0'||ordem||'2', 'N'||ordem||'2'), 'N0'||ordem||'3', 'N'||ordem||'3'), 'N0'||ordem||'4', 'N'||ordem||'4'), 
'N0'||ordem||'5', 'N'||ordem||'5'), 'N0'||ordem||'6', 'N'||ordem||'6'), 'N0'||ordem||'7', 'N'||ordem||'7'), 'N0'||ordem||'8', 'N'||ordem||'8'), 'N0'||ordem||'9', 'N'||ordem||'9') as formula
from 
(select codigo, replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(formula, 'N'||ordem, 'N0'||ordem)
, 'N0'||ordem||'0', 'N'||ordem||'0'), 'N0'||ordem||'1', 'N'||ordem||'1'), 'N0'||ordem||'2', 'N'||ordem||'2'), 'N0'||ordem||'3', 'N'||ordem||'3'), 'N0'||ordem||'4', 'N'||ordem||'4'), 
'N0'||ordem||'5', 'N'||ordem||'5'), 'N0'||ordem||'6', 'N'||ordem||'6'), 'N0'||ordem||'7', 'N'||ordem||'7'), 'N0'||ordem||'8', 'N'||ordem||'8'), 'N0'||ordem||'9', 'N'||ordem||'9') as formula
from 
(select codigo, replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(formula, 'N'||ordem, 'N0'||ordem)
, 'N0'||ordem||'0', 'N'||ordem||'0'), 'N0'||ordem||'1', 'N'||ordem||'1'), 'N0'||ordem||'2', 'N'||ordem||'2'), 'N0'||ordem||'3', 'N'||ordem||'3'), 'N0'||ordem||'4', 'N'||ordem||'4'), 
'N0'||ordem||'5', 'N'||ordem||'5'), 'N0'||ordem||'6', 'N'||ordem||'6'), 'N0'||ordem||'7', 'N'||ordem||'7'), 'N0'||ordem||'8', 'N'||ordem||'8'), 'N0'||ordem||'9', 'N'||ordem||'9') as formula
from 
(select codigo, replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(formula, 'N'||ordem, 'N0'||ordem)
, 'N0'||ordem||'0', 'N'||ordem||'0'), 'N0'||ordem||'1', 'N'||ordem||'1'), 'N0'||ordem||'2', 'N'||ordem||'2'), 'N0'||ordem||'3', 'N'||ordem||'3'), 'N0'||ordem||'4', 'N'||ordem||'4'), 
'N0'||ordem||'5', 'N'||ordem||'5'), 'N0'||ordem||'6', 'N'||ordem||'6'), 'N0'||ordem||'7', 'N'||ordem||'7'), 'N0'||ordem||'8', 'N'||ordem||'8'), 'N0'||ordem||'9', 'N'||ordem||'9') as formula
from 
(select codigo, replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(formula, 'N'||ordem, 'N0'||ordem)
, 'N0'||ordem||'0', 'N'||ordem||'0'), 'N0'||ordem||'1', 'N'||ordem||'1'), 'N0'||ordem||'2', 'N'||ordem||'2'), 'N0'||ordem||'3', 'N'||ordem||'3'), 'N0'||ordem||'4', 'N'||ordem||'4'), 
'N0'||ordem||'5', 'N'||ordem||'5'), 'N0'||ordem||'6', 'N'||ordem||'6'), 'N0'||ordem||'7', 'N'||ordem||'7'), 'N0'||ordem||'8', 'N'||ordem||'8'), 'N0'||ordem||'9', 'N'||ordem||'9') as formula
from 
(select codigo, replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(formula, 'N'||ordem, 'N0'||ordem)
, 'N0'||ordem||'0', 'N'||ordem||'0'), 'N0'||ordem||'1', 'N'||ordem||'1'), 'N0'||ordem||'2', 'N'||ordem||'2'), 'N0'||ordem||'3', 'N'||ordem||'3'), 'N0'||ordem||'4', 'N'||ordem||'4'), 
'N0'||ordem||'5', 'N'||ordem||'5'), 'N0'||ordem||'6', 'N'||ordem||'6'), 'N0'||ordem||'7', 'N'||ordem||'7'), 'N0'||ordem||'8', 'N'||ordem||'8'), 'N0'||ordem||'9', 'N'||ordem||'9') as formula
from 
(select codigo, replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(formula, 'N'||ordem, 'N0'||ordem)
, 'N0'||ordem||'0', 'N'||ordem||'0'), 'N0'||ordem||'1', 'N'||ordem||'1'), 'N0'||ordem||'2', 'N'||ordem||'2'), 'N0'||ordem||'3', 'N'||ordem||'3'), 'N0'||ordem||'4', 'N'||ordem||'4'), 
'N0'||ordem||'5', 'N'||ordem||'5'), 'N0'||ordem||'6', 'N'||ordem||'6'), 'N0'||ordem||'7', 'N'||ordem||'7'), 'N0'||ordem||'8', 'N'||ordem||'8'), 'N0'||ordem||'9', 'N'||ordem||'9') as formula
from 
(select turmadisciplinanotatitulo.codigo, 
replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(turmadisciplinanotatitulo.formula, 'N'||ordem, 'N0'||ordem)
, 'N0'||ordem||'0', 'N'||ordem||'0'), 'N0'||ordem||'1', 'N'||ordem||'1'), 'N0'||ordem||'2', 'N'||ordem||'2'), 'N0'||ordem||'3', 'N'||ordem||'3'), 'N0'||ordem||'4', 'N'||ordem||'4'), 
'N0'||ordem||'5', 'N'||ordem||'5'), 'N0'||ordem||'6', 'N'||ordem||'6'), 'N0'||ordem||'7', 'N'||ordem||'7'), 'N0'||ordem||'8', 'N'||ordem||'8'), 'N0'||ordem||'9', 'N'||ordem||'9')
as formula
from turmadisciplinanotatitulo,
(select row_number() over(order by pessoa.codigo) as ordem from pessoa order by codigo limit 9) as ordem  
where 1=1
and turmadisciplinanotatitulo.formula is not null and  turmadisciplinanotatitulo.formula != ''
and ordem = 1) as t
inner join (select row_number() over(order by pessoa.codigo) as ordem from pessoa order by codigo limit 9) as ordem on ordem = 2
) as t
inner join (select row_number() over(order by pessoa.codigo) as ordem from pessoa order by codigo limit 9) as ordem on ordem = 3
) as t
inner join (select row_number() over(order by pessoa.codigo) as ordem from pessoa order by codigo limit 9) as ordem on ordem = 4
) as t
inner join (select row_number() over(order by pessoa.codigo) as ordem from pessoa order by codigo limit 9) as ordem on ordem = 5
) as t
inner join (select row_number() over(order by pessoa.codigo) as ordem from pessoa order by codigo limit 9) as ordem on ordem = 6
) as t
inner join (select row_number() over(order by pessoa.codigo) as ordem from pessoa order by codigo limit 9) as ordem on ordem = 7
) as t
inner join (select row_number() over(order by pessoa.codigo) as ordem from pessoa order by codigo limit 9) as ordem on ordem = 8
) as t
inner join (select row_number() over(order by pessoa.codigo) as ordem from pessoa order by codigo limit 9) as ordem on ordem = 9
) as  t where t.codigo = turmadisciplinanotatitulo.codigo;