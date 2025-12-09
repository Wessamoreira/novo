package negocio.comuns.faturamento.nfe;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.faturamento.nfe.enumeradores.TipoNaturezaOperacaoEnum;
import negocio.comuns.faturamento.nfe.enumeradores.TipoOrigemDestinoNaturezaOperacaoEnum;

/**
 * 
 * @author Pedro Otimize
 *
 */
public class NaturezaOperacaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5045489612880851276L;
	private Integer codigo;
	private Integer codigoNaturezaOperacao;
	private String nome;
	private String descricao;
	private TipoNaturezaOperacaoEnum tipoNaturezaOperacaoEnum;
	private TipoOrigemDestinoNaturezaOperacaoEnum tipoOrigemDestinoNaturezaOperacaoEnum;
	
	
	public enum enumCampoConsultaNaturezaOperacao {
		NOME, CODIGONATUREZAOPERACAO, TIPONATURECAOPERACAO, CODIGO,
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

	public Integer getCodigoNaturezaOperacao() {
		if (codigoNaturezaOperacao == null) {
			codigoNaturezaOperacao = 0;
		}
		return codigoNaturezaOperacao;
	}

	public void setCodigoNaturezaOperacao(Integer codigoNaturezaOperacao) {
		this.codigoNaturezaOperacao = codigoNaturezaOperacao;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoNaturezaOperacaoEnum getTipoNaturezaOperacaoEnum() {
		return tipoNaturezaOperacaoEnum;
	}

	public void setTipoNaturezaOperacaoEnum(TipoNaturezaOperacaoEnum tipoNaturezaOperacaoEnum) {
		this.tipoNaturezaOperacaoEnum = tipoNaturezaOperacaoEnum;
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

	public TipoOrigemDestinoNaturezaOperacaoEnum getTipoOrigemDestinoNaturezaOperacaoEnum() {
		return tipoOrigemDestinoNaturezaOperacaoEnum;
	}

	public void setTipoOrigemDestinoNaturezaOperacaoEnum(TipoOrigemDestinoNaturezaOperacaoEnum tipoOrigemDestinoNaturezaOperacaoEnum) {
		this.tipoOrigemDestinoNaturezaOperacaoEnum = tipoOrigemDestinoNaturezaOperacaoEnum;
	}

}