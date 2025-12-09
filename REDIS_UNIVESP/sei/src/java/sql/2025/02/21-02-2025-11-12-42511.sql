-- public.avaliacaoinstitucionalunidadeensino definição

-- Drop table

-- DROP TABLE public.avaliacaoinstitucionalunidadeensino;

CREATE TABLE public.avaliacaoinstitucionalunidadeensino (
                                                            codigo serial4 NOT NULL,
                                                            unidadeensino int4 NOT NULL,
                                                            avaliacaoinstitucional int4 NOT NULL,
                                                            created timestamp NULL,
                                                            codigocreated int4 NULL,
                                                            nomecreated varchar(255) NULL,
                                                            updated timestamp NULL,
                                                            codigoupdated int4 NULL,
                                                            nomeupdated varchar(255) NULL,
                                                            CONSTRAINT pk_avaliacaoinstitucionalunidadeensino PRIMARY KEY (codigo),
                                                            CONSTRAINT unq_avaliacaoinstitucionalunidadeensino_avalinst_unidadeensino UNIQUE (avaliacaoinstitucional, unidadeensino)
);
CREATE INDEX idx_avaliacaoinstitucionalunidadeensino_avalinst ON public.avaliacaoinstitucionalunidadeensino USING btree (avaliacaoinstitucional);
CREATE INDEX idx_avaliacaoinstitucionalunidadeensino_avalinst_unidadeensino ON public.avaliacaoinstitucionalunidadeensino USING btree (avaliacaoinstitucional, unidadeensino);
CREATE INDEX idx_avaliacaoinstitucionalunidadeensino_unidadeensino ON public.avaliacaoinstitucionalunidadeensino USING btree (unidadeensino);

-- Table Triggers
DROP TRIGGER IF EXISTS audit_trigger_row ON public.avaliacaoinstitucionalunidadeensino;
create trigger audit_trigger_row after
    insert
    or
delete
or
update
    on
    public.avaliacaoinstitucionalunidadeensino for each row execute function audit.if_modified_func('true');
DROP TRIGGER IF EXISTS audit_trigger_stm ON public.avaliacaoinstitucionalunidadeensino;
create trigger audit_trigger_stm after
    truncate
        on
        public.avaliacaoinstitucionalunidadeensino for each statement execute function audit.if_modified_func('true');


-- public.avaliacaoinstitucionalunidadeensino chaves estrangeiras

ALTER TABLE public.avaliacaoinstitucionalunidadeensino ADD CONSTRAINT fk_avaliacaoinstitucionalunidadeensino_avaliacaoinstitucional FOREIGN KEY (avaliacaoinstitucional) REFERENCES public.avaliacaoinstitucional(codigo) ON DELETE CASCADE;
ALTER TABLE public.avaliacaoinstitucionalunidadeensino ADD CONSTRAINT fk_avaliacaoinstitucionalunidadeensino_unidadeensino FOREIGN KEY (unidadeensino) REFERENCES public.unidadeensino(codigo);





