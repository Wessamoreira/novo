CREATE OR REPLACE FUNCTION admin.consultar_is_called_sequencia(sequencia text)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
declare
 r bool;
begin
 execute format('select is_called from %s', sequencia) into r;
 return r;
end;
$function$
;

CREATE OR REPLACE FUNCTION admin.corrigir_sequencias_nao_iniciadas_tabela_registro(p_esquema text)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
declare
 resultset record; 
begin 
  for resultset in (  select sequence_name from (
      with sequencias as (
      select table_schema, table_name, column_name, replace(replace(column_default, 'nextval(''', ''), '''::regclass)', '') as sequence_name
      from information_schema.columns where table_schema = p_esquema and column_default like 'nextval%'
      )
    select 
          table_schema, table_name, column_name, sequence_name,
         coalesce(admin.consultar_maior_valor_coluna(table_schema, table_name, column_name),0) as max_value,
        admin.consultar_ultimo_valor_sequencia(sequence_name) as last_value,
         admin.consultar_is_called_sequencia(sequence_name) as is_called
    from sequencias
    ) t where 1=1  and t.max_value = t.last_value and t.is_called is false  )
   loop
    perform nextval(resultset.sequence_name);
  end loop;
 
end;
$function$
;

select admin.corrigir_sequencias_diferentes('public',true);

select admin.corrigir_sequencias_nao_iniciadas_tabela_registro('public');