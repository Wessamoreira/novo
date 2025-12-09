create or replace
function public.fn_validarprocessoseletivoitemprovainscricao(processoseletivoinscricao integer, itemprocessoseletivodataprova integer) returns boolean language plpgsql as $function$ 
declare processoseletivoitemprova integer;

begin if(processoseletivoinscricao is not null and processoseletivoinscricao > 0 and itemprocessoseletivodataprova is not null and itemprocessoseletivodataprova > 0) then
 select	into processoseletivoitemprova procseletivo as processoseletivoitemprova
from itemprocseletivodataprova
where itemprocseletivodataprova.codigo = itemprocessoseletivodataprova;

 if (processoseletivoinscricao <> processoseletivoitemprova) then return false;
end if;
end if;
return true;
end;
$function$;


ALTER TABLE public.inscricao ADD CONSTRAINT ck_validarprocessoseletivoitemprovainscricao CHECK ((fn_validarprocessoseletivoitemprovainscricao(procseletivo, itemprocessoseletivodataprova) = true)) NOT VALID;