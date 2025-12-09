package negocio.comuns.crm;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;

public class TipoContatoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1340420145275863419L;

	private Integer codigo;
	private String descricao;
	private StatusAtivoInativoEnum situacao;

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		if(descricao == null){
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public StatusAtivoInativoEnum getSituacao() {
		if(situacao == null){
			situacao = StatusAtivoInativoEnum.ATIVO;
		}
		return situacao;
	}

	public void setSituacao(StatusAtivoInativoEnum situacao) {
		this.situacao = situacao;
	}
	
	

}
