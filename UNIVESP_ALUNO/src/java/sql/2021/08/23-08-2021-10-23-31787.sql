delete from calendarioAberturaTipoRequerimentoraPrazo where codigo in (
	select codigo from calendarioAberturaTipoRequerimentoraPrazo
	where (select count(*) from calendarioAberturaTipoRequerimentoraPrazo catrp
	where calendarioAberturaTipoRequerimentoraPrazo.tiporequerimento = catrp.tiporequerimento
	and calendarioAberturaTipoRequerimentoraPrazo.calendarioaberturarequerimento = catrp.calendarioaberturarequerimento
	) > 1
	order by codigo desc limit 1
);
alter table calendarioAberturaTipoRequerimentoraPrazo ADD CONSTRAINT unique_calendarioAberturaTipoRequerimentoraPrazo_tipoRequerimento_calendarioAberturaRequerimento UNIQUE (tiporequerimento, calendarioaberturarequerimento);