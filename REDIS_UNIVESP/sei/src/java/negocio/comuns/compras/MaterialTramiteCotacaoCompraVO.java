package negocio.comuns.compras;

import java.util.Optional;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.SuperVO;

public class MaterialTramiteCotacaoCompraVO extends SuperVO {

	private Integer codigo;
	private CotacaoHistoricoVO cotacaoHistorico;
	private String descricao;
	private ArquivoVO arquivoVO;
	public static final long serialVersionUID = 1L;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CotacaoHistoricoVO getCotacaoHistorico() {
		return cotacaoHistorico;
	}

	public void setCotacaoHistorico(CotacaoHistoricoVO cotacaoHistorico) {
		this.cotacaoHistorico = cotacaoHistorico;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public ArquivoVO getArquivoVO() {
		arquivoVO = Optional.ofNullable(this.arquivoVO).orElse(new ArquivoVO());
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}

}
