alter table public.gradecurricular add column if not exists percentualpermitiriniciartcc real default 62.5;
alter table public.gradecurricular alter column percentualpermitiriniciarestagio type real;
alter table public.configuracaogeralsistema add column if not exists textoparaorientacaotcc text;
alter table public.disciplina add column if not exists nrMinimoAlunosPorSala int;
alter table public.disciplina add column if not exists nrMinimoAlunosPorGrupo int;

 
