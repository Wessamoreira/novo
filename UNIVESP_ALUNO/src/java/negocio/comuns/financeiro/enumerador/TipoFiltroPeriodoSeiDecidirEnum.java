package negocio.comuns.financeiro.enumerador;

import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirModuloEnum;
import negocio.comuns.utilitarias.UteisJSF;

public enum TipoFiltroPeriodoSeiDecidirEnum {

	NENHUM(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA, RelatorioSEIDecidirModuloEnum.ACADEMICO, RelatorioSEIDecidirModuloEnum.CRM, RelatorioSEIDecidirModuloEnum.RECURSOS_HUMANOS, RelatorioSEIDecidirModuloEnum.FINANCEIRO_ARVORE_FECHAMENTO_MES}), 
	DATA_VENCIMENTO(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}), 
	DATA_COMPENSACAO(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}), 
	DATA_VENCIMENTO_E_DATA_COMPENSACAO(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}), 	 
	DATA_VENCIMENTO_E_DATA_RECEBIMENTO(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}), 
	DATA_VENCIMENTO_E_DATA_COMPETENCIA(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}), 
	DATA_RECEBIMENTO(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}), 
	DATA_RECEBIMENTO_E_DATA_COMPETENCIA(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}),
	DATA_RECEBIMENTO_E_DATA_COMPENSACAO(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}),
	DATA_COMPETENCIA(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}),
	DATA_COMPETENCIA_DATA_COMPENSACAO(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}),
	 	 
	DATA_VENCIMENTO_E_DATA_PAGAMENTO(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA}), 	
	DATA_PAGAMENTO(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA}), 
	DATA_PAGAMENTO_E_DATA_COMPETENCIA(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA}),
	DATA_PAGAMENTO_E_DATA_COMPENSACAO(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_DESPESA}),
	
	PERIODO_COMPROMISSO(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.CRM}),
	PERIODO_INTERACAO_WORKFLOW(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.CRM}),
	PERIODO_FOLLOW_UP(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.CRM}),
	PERIODO_PRE_INSCRICAO(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.CRM}),
	
	PERIODO_DATA_MOVIMENTACAO_CENTRO_ORIGEM(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.FINANCEIRO_CENTRO_RESULTADO}),
	
	
	DATA_MATRICULA(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.ACADEMICO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}),
	DATA_MATRICULA_E_ANO_SEMESTRE(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.ACADEMICO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA}),	
	ANO_SEMESTRE(new RelatorioSEIDecidirModuloEnum[]{RelatorioSEIDecidirModuloEnum.ACADEMICO, RelatorioSEIDecidirModuloEnum.FINANCEIRO_RECEITA, RelatorioSEIDecidirModuloEnum.FINANCEIRO_FECHAMENTO_MES_RECEITA});
	
	
	
	
	private RelatorioSEIDecidirModuloEnum[] modulo;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoFiltroPeriodoSeiDecidirEnum_"+this.name());
	}
	

	private TipoFiltroPeriodoSeiDecidirEnum(RelatorioSEIDecidirModuloEnum[] modulo) {
		this.modulo = modulo;
	}

	public RelatorioSEIDecidirModuloEnum[] getModulo() {
		return modulo;
	}

	public void setModulo(RelatorioSEIDecidirModuloEnum[] modulo) {
		this.modulo = modulo;
	}
	
	public static TipoFiltroPeriodoSeiDecidirEnum[] getValues(RelatorioSEIDecidirModuloEnum modulo){
		TipoFiltroPeriodoSeiDecidirEnum[] filtros = new TipoFiltroPeriodoSeiDecidirEnum[TipoFiltroPeriodoSeiDecidirEnum.values().length];
		int y = 0;
		for(TipoFiltroPeriodoSeiDecidirEnum filtro: TipoFiltroPeriodoSeiDecidirEnum.values()){
			for(int x = 0; x<filtro.getModulo().length;x++){
				if(filtro.getModulo()[x].equals(modulo)){
					filtros[y++] = filtro;
					break;
				}
			}
		}
		return filtros;
	}
	
	
	
}
