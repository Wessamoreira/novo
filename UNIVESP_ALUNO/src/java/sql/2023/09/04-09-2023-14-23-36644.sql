ALTER TABLE IF EXISTS tiporequerimento ADD COLUMN IF NOT EXISTS permitirAlunoAlterarCurso  boolean default false;
ALTER TABLE IF EXISTS tiporequerimento ADD COLUMN IF NOT EXISTS validarVagasPorNumeroComputadoresUnidadeEnsino boolean default false;
ALTER TABLE IF EXISTS tiporequerimento ADD COLUMN IF NOT EXISTS registrarTransferenciaProximoSemestre boolean default false;
ALTER TABLE IF EXISTS tiporequerimento ADD COLUMN IF NOT EXISTS cidDeferirAutomaticamente text;
ALTER TABLE IF EXISTS requerimento ADD COLUMN IF NOT EXISTS cid varchar(20);