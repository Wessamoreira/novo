CREATE TABLE public.tipoconcedente (
	codigo serial NOT NULL,	
	nome varchar(255),
	cnpjobrigatorio boolean DEFAULT false,
	CONSTRAINT pkey_tipoconcedente_codigo PRIMARY KEY (codigo)
);