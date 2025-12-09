package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.enumeradores.SituacaoTipoAdvertenciaEnum;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO.EnumCampoConsultaCompetencia;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("CompetenciaFolhaPagamentoControle")
@Scope("viewScope")
@Lazy
public class CompetenciaFolhaPagamentoControle extends SuperControle {

	private static final long serialVersionUID = 6006461488983026776L;

	private static final String TELA_FORM = "competenciaFolhaPagamentoForm";
	private static final String TELA_CONS = "competenciaFolhaPagamentoCons";
	private static final String CONTEXT_PARA_EDICAO = "itemCompetenciaFolhaPagamento";

	private Date dataCompetencia = new Date();

	private CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO;

	private CompetenciaPeriodoFolhaPagamentoVO competenciaPeriodoFolhaPagamentoVO;
	private CompetenciaPeriodoFolhaPagamentoVO competenciaPeriodoFolhaPagamentoVOEditado;

	public CompetenciaFolhaPagamentoControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
	}

	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			competenciaFolhaPagamentoVO = (CompetenciaFolhaPagamentoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setCompetenciaFolhaPagamentoVO(competenciaFolhaPagamentoVO);
			getCompetenciaFolhaPagamentoVO().setPeriodos(getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().consultarPorCompetenciaFolhaPagamento(getCompetenciaFolhaPagamentoVO()));
			setControleConsultaOtimizado(new DataModelo());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().persistir(getCompetenciaFolhaPagamentoVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@Override
	public void consultarDados() {
		try {

			if (getControleConsultaOtimizado().getCampoConsulta().equals(EnumCampoConsultaCompetencia.CODIGO.name()) 
					&& !Uteis.getIsValorNumerico(getControleConsultaOtimizado().getValorConsulta())) {
				throw new ConsistirException(UteisJSF.internacionalizar("prt_SecaoFolhaPagamento_ConsultaCampoCodigoInvalido"));
			}

			getControleConsultaOtimizado().setValorConsulta("");
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), dataCompetencia);

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().excluir(getCompetenciaFolhaPagamentoVO(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaCompetencia.DESCRICAO.name());
		setListaConsulta(new ArrayList<CompetenciaFolhaPagamentoVO>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}
	
	public List<SelectItem> tipoConsultaComboCompetencioFolhaPagamento() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem(EnumCampoConsultaCompetencia.DATA_COMPETENCIA.name(), UteisJSF.internacionalizar("enum_TipoConsultaComboSecaoFolhaPagamentoEnum_COMPETENCIA")));
		return itens;
	}
	
	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}

	public boolean getApresentarPaginador() {
		return getControleConsultaOtimizado().getListaConsulta().size() >= 10;
	}

	public CompetenciaFolhaPagamentoVO getCompetenciaFolhaPagamentoVO() {
		if (competenciaFolhaPagamentoVO == null) {
			competenciaFolhaPagamentoVO = new CompetenciaFolhaPagamentoVO();
		}
		return competenciaFolhaPagamentoVO;
	}

	public void setCompetenciaFolhaPagamentoVO(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		this.competenciaFolhaPagamentoVO = competenciaFolhaPagamentoVO;
	}

	public Date getDataCompetencia() {
		return dataCompetencia;
	}

	public void setDataCompetencia(Date dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}
	
	public CompetenciaPeriodoFolhaPagamentoVO getCompetenciaPeriodoFolhaPagamentoVO() {
		if (competenciaPeriodoFolhaPagamentoVO == null)
			competenciaPeriodoFolhaPagamentoVO = new CompetenciaPeriodoFolhaPagamentoVO();
		return competenciaPeriodoFolhaPagamentoVO;
	}

	public void setCompetenciaPeriodoFolhaPagamentoVO(
			CompetenciaPeriodoFolhaPagamentoVO competenciaPeriodoFolhaPagamentoVO) {
		this.competenciaPeriodoFolhaPagamentoVO = competenciaPeriodoFolhaPagamentoVO;
	}

	public CompetenciaPeriodoFolhaPagamentoVO getCompetenciaPeriodoFolhaPagamentoVOEditado() {
		if (competenciaPeriodoFolhaPagamentoVOEditado == null)
			competenciaPeriodoFolhaPagamentoVOEditado = new CompetenciaPeriodoFolhaPagamentoVO();
		return competenciaPeriodoFolhaPagamentoVOEditado;
	}

	public void setCompetenciaPeriodoFolhaPagamentoVOEditado(
			CompetenciaPeriodoFolhaPagamentoVO competenciaPeriodoFolhaPagamentoVOEditado) {
		this.competenciaPeriodoFolhaPagamentoVOEditado = competenciaPeriodoFolhaPagamentoVOEditado;
	}

	public void	adicionarPeriodoCompetencia() {
		try {
			getCompetenciaPeriodoFolhaPagamentoVO().setCompetenciaFolhaPagamento(getCompetenciaFolhaPagamentoVO());
			getCompetenciaFolhaPagamentoVO().adicionarPeriodoCompetenciaFolhaPagamentoVO(getCompetenciaPeriodoFolhaPagamentoVO());
			setCompetenciaPeriodoFolhaPagamentoVO(new CompetenciaPeriodoFolhaPagamentoVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void editarPeriodoDaCompetencia() {
		//Clona o objeto da grid que sera editado para criar outra referencia de memoria
		setCompetenciaPeriodoFolhaPagamentoVOEditado((CompetenciaPeriodoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("periodo"));
		getCompetenciaPeriodoFolhaPagamentoVOEditado().setNovoObj(false);
		getCompetenciaPeriodoFolhaPagamentoVOEditado().setItemEmEdicao(true);
        try {
        	setCompetenciaPeriodoFolhaPagamentoVO((CompetenciaPeriodoFolhaPagamentoVO) BeanUtils.cloneBean(getCompetenciaPeriodoFolhaPagamentoVOEditado()));
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), UteisJSF.internacionalizar("prt_RegistroEntrada_erro"));
		}
	}
	
	public void removerPeriodoDaCompetencia() {
		try {
			CompetenciaPeriodoFolhaPagamentoVO obj = (CompetenciaPeriodoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("periodo");
    		getCompetenciaFolhaPagamentoVO().getPeriodos().remove(obj);
            setMensagemID("msg_dados_excluidos");    		
    	}catch (Exception e) {
    		e.printStackTrace();
		}
	}
	
	public void encerrar() {
		try {
			getCompetenciaFolhaPagamentoVO().setUsuarioUltimaAlteracao(getUsuarioLogado());
			getCompetenciaFolhaPagamentoVO().setDataUltimaAlteracao(new Date());
			getCompetenciaFolhaPagamentoVO().setSituacao(SituacaoTipoAdvertenciaEnum.INATIVO);
			encerrarVigencia();
			criarNovaVigencia();
			encerrarLancamentosDoMes();
			setMensagemID("msg_CompetenciaFolhaPagamento_encerrarVigenciaComSucesso");
		} catch (Exception e) {
			e.printStackTrace();
			getCompetenciaFolhaPagamentoVO().setSituacao(SituacaoTipoAdvertenciaEnum.ATIVO);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void encerrarVigencia() throws Exception {
		getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().encerrarVigencia(getCompetenciaFolhaPagamentoVO(), getUsuarioLogado());
	}

	private void criarNovaVigencia() {
		getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().clonarVigencia(getCompetenciaFolhaPagamentoVO());
	}
	
	private void encerrarLancamentosDoMes() {
		getFacadeFactory().getLancamentoFolhaPagamentoInterfaceFacade().encerrarVigencia(getCompetenciaFolhaPagamentoVO()); 
	}
}