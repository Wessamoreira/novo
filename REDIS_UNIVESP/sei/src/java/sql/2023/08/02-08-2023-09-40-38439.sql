alter table renovacaomatriculaturma  add column if not exists liberadoInclusaoTurmaOutroUnidadeEnsino boolean default false;
alter table renovacaomatriculaturma  add column if not exists liberadoInclusaoTurmaOutroCurso boolean default false;
alter table renovacaomatriculaturma  add column if not exists liberadoInclusaoTurmaOutroMatrizCurricular boolean default true;