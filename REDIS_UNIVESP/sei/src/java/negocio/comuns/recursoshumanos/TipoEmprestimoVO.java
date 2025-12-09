package negocio.comuns.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.AgenciaVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoEmprestimoEnum;

/**
 * Reponsável por manter os dados da entidade TipoEmprestimo. Classe do tipo 
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TipoEmprestimoVO extends SuperVO {

	private static final long serialVersionUID = 1118455064007015996L;

	private Integer codigo;
	private String descricao;
	private BancoVO banco;
	private AgenciaVO agencia;
	private TipoEmprestimoEnum tipoEmprestimo;

	public enum EnumCampoConsultaTipoEmprestimo {
		DESCRICAO,
		BANCO,
		CODIGO;
	}

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

	public BancoVO getBanco() {
		if (banco == null) {
			banco = new BancoVO();
		}
		return banco;
	}

	public void setBanco(BancoVO banco) {
		this.banco = banco;
	}

	public AgenciaVO getAgencia() {
		if (agencia == null) {
			agencia = new AgenciaVO();
		}
		return agencia;
	}

	public void setAgencia(AgenciaVO agencia) {
		this.agencia = agencia;
	}

	public TipoEmprestimoEnum getTipoEmprestimo() {
		return tipoEmprestimo;
	}

	public void setTipoEmprestimo(TipoEmprestimoEnum tipoEmprestimo) {
		this.tipoEmprestimo = tipoEmprestimo;
	}

}
