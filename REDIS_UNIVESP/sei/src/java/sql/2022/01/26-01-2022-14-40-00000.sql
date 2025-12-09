alter table if exists documentoassinadopessoa add column if not exists tipoAssinaturaDocumentoEnum varchar(250) default 'NENHUM';

update documentoassinadopessoa set tipoAssinaturaDocumentoEnum = 'ELETRONICA' where codigo in (
select dap.codigo  from documentoassinadopessoa dap
inner join documentoassinado da on da.codigo = dap.documentoassinado and da.tipoorigemdocumentoassinado = 'TERMO_ESTAGIO_OBRIGATORIO'
where dap.tipopessoa in ('ALUNO', 'MEMBRO_COMUNIDADE')
);

update documentoassinadopessoa set tipoAssinaturaDocumentoEnum = 'CERTIFICADO_DIGITAL' where tipopessoa = 'FUNCIONARIO'; 