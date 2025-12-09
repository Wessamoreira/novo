package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
 *
 */
public enum TipoAvaliacaoPBLEnum {
	/**
	 * @author Victor Hugo de Paula Costa - 30 de jun de 2016 
	 */
	AUTO_AVALIACAO, ALUNO_AVALIA_ALUNO, PROFESSOR_AVALIA_ALUNO, RESULTADO_FINAL, RESULTADO_FINAL_GERAL;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoAvaliacaoPBLEnum_" + this.name());
	}
	
	public String getName() {
		return this.name();
	}
	
	public boolean isAutoAvaliacao() {
    	return name().equals(TipoAvaliacaoPBLEnum.AUTO_AVALIACAO.name()) ? true : false;
    }
	
	public boolean isAvaliacaoAuno() {
		return name().equals(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO.name()) ? true : false;
	}
	
	public boolean isProfessorAvaliacaoAuno() {
		return name().equals(TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO.name()) ? true : false;
	}
	
	public boolean isResultadoFinal() {
		return name().equals(TipoAvaliacaoPBLEnum.RESULTADO_FINAL.name()) ? true : false;
	}
	
	public boolean isResultadoFinalGeral() {
		return name().equals(TipoAvaliacaoPBLEnum.RESULTADO_FINAL_GERAL.name()) ? true : false;
	}
}
