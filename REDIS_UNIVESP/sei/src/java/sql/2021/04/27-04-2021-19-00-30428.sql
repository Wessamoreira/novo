CREATE TABLE public.grupopessoa (
	codigo serial NOT NULL,
	nome varchar(255) NOT NULL,
	CONSTRAINT pkey_grupopessoa_codigo PRIMARY KEY (codigo)
);

CREATE TABLE public.grupopessoaitem (
	codigo serial NOT NULL,
	grupopessoa int NOT NULL,
	pessoa int NOT NULL,
	statusAtivoInativoEnum varchar(20) NOT NULL DEFAULT 'ATIVO',
	CONSTRAINT pkey_grupopessoaitem_codigo PRIMARY KEY (codigo),
	CONSTRAINT fk_grupopessoaitem_grupopessoa FOREIGN KEY (grupopessoa) REFERENCES grupopessoa(codigo) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_grupopessoaitem_pessoa FOREIGN KEY (pessoa) REFERENCES pessoa(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT
);

