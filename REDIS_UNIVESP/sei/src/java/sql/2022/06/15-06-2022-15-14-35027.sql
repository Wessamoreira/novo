delete from transferenciaturma where codigo in(
	select tf.codigo from transferenciaturma tf where not exists(select from matricula ma where ma.matricula = tf.matricula)
);

alter table if exists transferenciaturma drop constraint if exists fk_transferenciaturma_matricula;

select create_constraint('alter table transferenciaturma add constraint fk_transferenciaturma_matricula_fkey foreign key (matricula) references matricula (matricula) on delete cascade');