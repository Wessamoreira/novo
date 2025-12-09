package negocio.comuns.recursoshumanos;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.enumeradores.SituacaoTipoAdvertenciaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * Reponsável por manter os dados da entidade CompetenciaFolhaPagamento. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CompetenciaFolhaPagamentoVO extends SuperVO {

	private static final long serialVersionUID = 2767753032867695645L;

	private Integer codigo;
	private Date dataCompetencia;
	private Date dataCaixa;
	private Date dataFechamento;
	private SituacaoTipoAdvertenciaEnum situacao;

	private UsuarioVO usuarioUltimaAlteracao;
	private Date dataUltimaAlteracao;
	private Integer quantidadeDiasUteis;
	private Integer quantidadeDiasUteisMeioExpediente;

	private List<CompetenciaPeriodoFolhaPagamentoVO> periodos;

	public enum EnumCampoConsultaCompetencia {
		DESCRICAO, CODIGO, DATA_COMPETENCIA;
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

	public Date getDataCompetencia() {
		return dataCompetencia;
	}

	public void setDataCompetencia(Date dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}

	public Date getDataCaixa() {
		return dataCaixa;
	}

	public void setDataCaixa(Date dataCaixa) {
		this.dataCaixa = dataCaixa;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public SituacaoTipoAdvertenciaEnum getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoTipoAdvertenciaEnum situacao) {
		this.situacao = situacao;
	}

	public List<CompetenciaPeriodoFolhaPagamentoVO> getPeriodos() {
		if (periodos == null)
			periodos = new ArrayList<>();
		return periodos;
	}

	public void setPeriodos(List<CompetenciaPeriodoFolhaPagamentoVO> periodos) {
		this.periodos = periodos;
	}

	public UsuarioVO getUsuarioUltimaAlteracao() {
		if (usuarioUltimaAlteracao == null)
			usuarioUltimaAlteracao = new UsuarioVO();
		return usuarioUltimaAlteracao;
	}

	public void setUsuarioUltimaAlteracao(UsuarioVO usuarioUltimaAlteracao) {
		this.usuarioUltimaAlteracao = usuarioUltimaAlteracao;
	}

	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}

	public Integer getQuantidadeDiasUteis() {
		if (quantidadeDiasUteis == null) {
			quantidadeDiasUteis = 0;
		}
		return quantidadeDiasUteis;
	}

	public void setQuantidadeDiasUteis(Integer quantidadeDiasUteis) {
		this.quantidadeDiasUteis = quantidadeDiasUteis;
	}

	public Integer getQuantidadeDiasUteisMeioExpediente() {
		if (quantidadeDiasUteisMeioExpediente == null) {
			quantidadeDiasUteisMeioExpediente = 0;
		}
		return quantidadeDiasUteisMeioExpediente;
	}

	public void setQuantidadeDiasUteisMeioExpediente(Integer quantidadeDiasUteisMeioExpediente) {
		this.quantidadeDiasUteisMeioExpediente = quantidadeDiasUteisMeioExpediente;
	}

	public String getSituacaoApresentar() {
		if (getSituacao() != null && getSituacao().equals(SituacaoTipoAdvertenciaEnum.ATIVO)) {
			return UteisJSF.internacionalizar("prt_TextoPadrao_Ativo");
		} else {
			return UteisJSF.internacionalizar("prt_TextoPadrao_Inativo");
		}
	}

	public void adicionarPeriodoCompetenciaFolhaPagamentoVO(CompetenciaPeriodoFolhaPagamentoVO obj) throws Exception {
		CompetenciaPeriodoFolhaPagamentoVO.validarDados(obj);
		Iterator<CompetenciaPeriodoFolhaPagamentoVO> i = getPeriodos().iterator();
		int index = 0;
		int aux = -1;
		CompetenciaPeriodoFolhaPagamentoVO objAux = new CompetenciaPeriodoFolhaPagamentoVO();
		while (i.hasNext()) {

			CompetenciaPeriodoFolhaPagamentoVO objExistente = (CompetenciaPeriodoFolhaPagamentoVO) i.next();

			if (objExistente.getPeriodo().equals(obj.getPeriodo())) {
				if (!objExistente.getItemEmEdicao()) {
					throw new ConsistirException(
							UteisJSF.internacionalizar("prt_CompetenciaFolhaPagamento_PeriodoJaCadastrado"));
				}
			}

			if (objExistente.getCodigo().equals(obj.getCodigo()) && objExistente.getItemEmEdicao()) {
				obj.setItemEmEdicao(false);
				aux = index;
				objAux = obj;
			}
			index++;

		}
		if (aux >= 0) {
			getPeriodos().set(aux, objAux);
		} else {
			getPeriodos().add(obj);
		}
	}
}