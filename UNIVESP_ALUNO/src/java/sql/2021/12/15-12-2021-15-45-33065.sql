alter table personalizacaomensagemautomatica add column if not exists enviaremailinstitucional boolean default false;
alter table comunicacaointerna add column if not exists enviaremailinstitucional boolean default false;
