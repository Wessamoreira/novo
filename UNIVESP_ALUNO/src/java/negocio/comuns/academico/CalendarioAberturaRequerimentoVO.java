package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Arquivo. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CalendarioAberturaRequerimentoVO extends SuperVO {

	private Integer codigo;
	private String descricao;

	private List<CalendarioAberturaTipoRequerimentoraPrazoVO> calendarioAberturaTipoRequerimentoraPrazoVOs;	
	private UnidadeEnsinoVO unidadeEnsinoVO;
	
	
	
	public CalendarioAberturaRequerimentoVO() {
		super();
		
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		setDescricao(getDescricao().toUpperCase());
	}


	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return (descricao);
	}

	public String getDescricaoCurto() {
		if (descricao == null) {
			descricao = "";
		}
		return (descricao.substring(0, 21));
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public List<CalendarioAberturaTipoRequerimentoraPrazoVO> getCalendarioAberturaTipoRequerimentoraPrazoVOs() {
		if(calendarioAberturaTipoRequerimentoraPrazoVOs == null ){
			calendarioAberturaTipoRequerimentoraPrazoVOs = new ArrayList<CalendarioAberturaTipoRequerimentoraPrazoVO>(0);
		}
		return calendarioAberturaTipoRequerimentoraPrazoVOs;
	}

	public void setCalendarioAberturaTipoRequerimentoraPrazoVOs(List<CalendarioAberturaTipoRequerimentoraPrazoVO> calendarioAberturaTipoRequerimentoraPrazoVOs) {
		this.calendarioAberturaTipoRequerimentoraPrazoVOs = calendarioAberturaTipoRequerimentoraPrazoVOs;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null){
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	
}
