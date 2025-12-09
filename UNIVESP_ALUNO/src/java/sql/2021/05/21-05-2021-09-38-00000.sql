update layoutpadrao set assinaturafunc1 =  null where assinaturafunc1 = 0;
update layoutpadrao set assinaturafunc2 =  null where assinaturafunc2 = 0;
alter table layoutpadrao add constraint fk_layoutpadrao_funass1 foreign key (assinaturafunc1) references funcionario(codigo) on delete cascade on update cascade;
alter table layoutpadrao add constraint fk_layoutpadrao_funass2 foreign key (assinaturafunc2) references funcionario(codigo) on delete cascade on update cascade;