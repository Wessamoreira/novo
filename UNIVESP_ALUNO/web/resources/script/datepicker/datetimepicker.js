//https://www.jqueryscript.net/time-clock/Date-Time-Picker-Bootstrap-4.html

(function($) {
	iniciarCalendar =  function(id, pattern){
			var habilitarHora = pattern.indexOf('HH') >= 0;		
			pattern = pattern.replaceAll('dd', 'DD');	
			var viewMode =  'days';
			if(id.id.indexOf('dataCompetencia') >= 0){
				if(pattern.toUpperCase() == 'MM/YYYY'){
					viewMode =   'months';
				}
			}
			$(id).datetimepicker({
					format:pattern,					
					locale: moment.locale('pt-br'),
					viewMode: viewMode,						
					icons: {
  						time:'fas fa-clock',
  						date:'fas fa-calendar',
  						up:'fas fa-chevron-up',
  						down:'fas fa-chevron-down',
  						previous:'fas fa-chevron-left',
  						next:'fas fa-chevron-right',
  						today:'fas fa-crosshairs',
  						clear:'fas fa-trash-o',
  						close:'fas fa-times'
					},
				
					tooltips:{
						today:'Hoje',
						clear: 'Limpar',
						close: 'Fechar',
						selectMonth:'Selecionar Mês',
						prevMonth:'Mês Anterior',
						nextMonth:'Próximo Mês',
						selectYear:'Selecionar Ano',
						prevYear:'Ano Anterior',
						nextYear:'Próximo Ano',
						selectDecade:'Selecionar Década',
						prevDecade:'Década Anterior',
  						nextDecade:'Próximoa Década',
  						prevCentury:'Século Anterior',
  						nextCentury:'Próximo Século',
  						pickHour:'Selecionar Hora',
  						incrementHour:'Incrementar Hora',
  						decrementHour:'Decrementar Hora',
  						pickMinute:'Selecionar Minuto',
  						incrementMinute:'Incrementar Minuto',
  						decrementMinute:'Decrementar Minuto',
  						pickSecond:'Selecionar Segundo',
  						incrementSecond:'Incrementar Segundo',
  						decrementSecond:'Decrementar Segundo',
  						togglePeriod:'Alternar Período',
  						selectTime:'Selecionar Hora',
  						showTodayButton:true,
						showClear:true,
						showClose:true,
						disabledTimeIntervals:!habilitarHora,
						disabledHours:!habilitarHora,
						enabledHours:habilitarHora
					}
				}
			);
//			$(id).data("DateTimePicker").format(pattern);
//			if(jsFunction != ''){
//				$(id).datetimepicker().on('dp.change', function(){
//					jsFunction;
//				})				
//			}
	};
})(jQuery);