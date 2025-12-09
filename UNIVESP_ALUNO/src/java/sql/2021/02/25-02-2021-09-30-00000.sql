ALTER TABLE public.usuariogsuiteperfilacesso RENAME COLUMN usuariogsuite TO usuariooauth2;
ALTER TABLE public.usuariogsuiteperfilacesso RENAME COLUMN perfilacessogsuite TO perfilacessooauth2;
ALTER TABLE public.usuariogsuite RENAME TO usuariooauth2;
ALTER TABLE public.usuariogsuiteperfilacesso RENAME TO usuariooauth2perfilacesso;
ALTER TABLE public.perfilacessogsuite RENAME TO perfilacessooauth2;
ALTER TABLE public.usuariooauth2 add column if not exists nomewebservice varchar(100);
update usuariooauth2 set nomewebservice = 'SEI_GSUITE' where codigo = 1;