alter table curso add column if not exists permitirAssinarContratoPendenciaDocumentacao boolean default false;
alter table curso add column if not exists ativarPreMatriculaAposEntregaDocumentosObrigatorios boolean default false;
alter table matricula add column if not exists escolaPublica boolean;