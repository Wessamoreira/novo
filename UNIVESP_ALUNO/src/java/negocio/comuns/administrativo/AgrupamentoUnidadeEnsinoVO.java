package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;

public class AgrupamentoUnidadeEnsinoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6004294812450654884L;
	private Integer codigo;
	private String descricao;
	private String abreviatura;
	private StatusAtivoInativoEnum statusAtivoInativoEnum;
	private List<AgrupamentoUnidadeEnsinoItemVO> listaAgrupamentoUnidadeEnsinoItemVO;

	/**
	 * Transient
	 * 
	 * @return
	 */
	private String filtroNomeUnidadeEnsino;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getAbreviatura() {
		if (abreviatura == null) {
			abreviatura = "";
		}
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public StatusAtivoInativoEnum getStatusAtivoInativoEnum() {
		if (statusAtivoInativoEnum == null) {
			statusAtivoInativoEnum = StatusAtivoInativoEnum.ATIVO;
		}
		return statusAtivoInativoEnum;
	}

	public void setStatusAtivoInativoEnum(StatusAtivoInativoEnum statusAtivoInativoEnum) {
		this.statusAtivoInativoEnum = statusAtivoInativoEnum;
	}

	public List<AgrupamentoUnidadeEnsinoItemVO> getListaAgrupamentoUnidadeEnsinoItemVO() {
		if (listaAgrupamentoUnidadeEnsinoItemVO == null) {
			listaAgrupamentoUnidadeEnsinoItemVO = new ArrayList<>();
		}
		return listaAgrupamentoUnidadeEnsinoItemVO;
	}

	public void setListaAgrupamentoUnidadeEnsinoItemVO(List<AgrupamentoUnidadeEnsinoItemVO> listaAgrupamentoUnidadeEnsinoItemVO) {
		this.listaAgrupamentoUnidadeEnsinoItemVO = listaAgrupamentoUnidadeEnsinoItemVO;
	}

	public String getFiltroNomeUnidadeEnsino() {
		if (filtroNomeUnidadeEnsino == null) {
			filtroNomeUnidadeEnsino = "";
		}
		return filtroNomeUnidadeEnsino;
	}

	public void setFiltroNomeUnidadeEnsino(String filtroNomeUnidadeEnsino) {
		this.filtroNomeUnidadeEnsino = filtroNomeUnidadeEnsino;
	}

}
