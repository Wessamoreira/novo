CREATE TABLE pixContaCorrente(
  codigo SERIAL NOT NULL,
  datageracao timestamp,
  nossonumero varchar(25),
  qrcode text,
  link text,
  jsonenvio text,
  jsonretorno text,
  horario timestamp,
  valor numeric(20,2),
  infoPagador varchar(140),
  endtoendid varchar(32),
  situacaopixenum varchar(20),
  contareceber INT,
  usuario INT,
  contacorrente INT,
  CONSTRAINT pix_pkey PRIMARY KEY (codigo),  
  CONSTRAINT unique_pix_nossonumero_contacorrente UNIQUE (nossonumero, contacorrente),
  CONSTRAINT pix_usuario_fkey FOREIGN KEY (usuario) REFERENCES usuario(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT pix_contacorrente_fkey FOREIGN KEY (contacorrente) REFERENCES contacorrente(codigo) ON UPDATE RESTRICT ON DELETE RESTRICT
);


