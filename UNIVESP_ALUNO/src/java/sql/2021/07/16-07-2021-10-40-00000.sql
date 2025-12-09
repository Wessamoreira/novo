alter table curso add column if not exists nrmaximoalunosporgrupo int;
alter table curso add column if not exists nrminimoalunosporgrupo int;

alter table gradecurricular add column if not exists disciplinapadraotcc int;
alter table if exists gradecurricular  add CONSTRAINT fk_gradecurricular_disciplinapadraotcc FOREIGN KEY (disciplinapadraotcc) REFERENCES disciplina(codigo) ON DELETE RESTRICT ON UPDATE RESTRICT;
