alter table public.adminsdkgoogle add column if not exists tipoAdminSdkIntegracaoEnum varchar(255) default 'NENHUM';
update public.adminsdkgoogle set tipoAdminSdkIntegracaoEnum = 'GSUITE' where tipoAdminSdkIntegracaoEnum = 'NENHUM';
ALTER TABLE public.adminsdkgoogle RENAME TO adminsdkintegracao;
