package negocio.comuns.compras;

import java.util.Optional;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class MapaRequisicaoVO extends SuperVO {

	private Integer codigo;
	private RequisicaoVO requisicao;
	public static final long serialVersionUID = 1L;

	public MapaRequisicaoVO() {
		super();

	}

	public static void validarDados(MapaRequisicaoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		boolean existeQtdAutorizadoMaiorQueSolicitada = obj.getRequisicao().getRequisicaoItemVOs().stream().anyMatch(p-> p.getQuantidadeAutorizada() > p.getQuantidadeSolicitada());
		Uteis.checkState(existeQtdAutorizadoMaiorQueSolicitada, "Existem Itens de Requisição com Quantidade Autorizada superior a Quantidade Requerida.");
		Uteis.checkState(obj.getRequisicao().getSituacaoAutorizacao().equals("IN") && obj.getRequisicao().getMotivoSituacaoAutorizacao().equals(""), "O campo MOTIVO (Mapa Requisição) deve ser informado.");
		Uteis.checkState(obj.getRequisicao().getSituacaoAutorizacao().equals("AU") && obj.getRequisicao().getRequisicaoItemVOs().stream().allMatch(p-> p.getQuantidadeAutorizada().equals(0.0)), "Não foi possível realizar essa operação, pois não existe nenhum produto com a Qtd. Autorizada maior que Zero.");
	}

	public void setarQtdeAprovar() {
		getRequisicao().getRequisicaoItemVOs().forEach(p -> p.setQuantidadeAutorizada(p.getQuantidadeSolicitada()));
	}

	public Integer getCodigo() {
		codigo = Optional.ofNullable(codigo).orElse(0);
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public RequisicaoVO getRequisicao() {
		if (requisicao == null) {
			requisicao = new RequisicaoVO();
		}
		return requisicao;
	}

	public void setRequisicao(RequisicaoVO requisicao) {
		this.requisicao = requisicao;
	}
	
	public static void validarDadosIndeferir(MapaRequisicaoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		Uteis.checkState(obj.getRequisicao().getMotivoSituacaoAutorizacao().equals(""), "O campo MOTIVO (Mapa Requisição) deve ser informado.");
	}

}
