alter table if exists historicodependentes drop constraint if exists historicodependentes_funcionario_fkey;

select create_constraint('alter table historicodependentes add constraint fk_historicodependentes_funcionario_fkey foreign key (funcionario) references funcionario (codigo) on delete cascade');