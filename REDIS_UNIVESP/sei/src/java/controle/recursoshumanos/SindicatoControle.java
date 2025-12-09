package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.PeriodoAquisitivoFeriasVO.EnumCampoConsultaPeriodoAquisitivoFuncionario;
import negocio.comuns.recursoshumanos.SindicatoMedia13VO;
import negocio.comuns.recursoshumanos.SindicatoMediaFeriasVO;
import negocio.comuns.recursoshumanos.SindicatoMediaRescisaoVO;
import negocio.comuns.recursoshumanos.SindicatoVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoPeriodoAquisitivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("SindicatoControle")
@Scope("viewScope")
@Lazy
@SuppressWarnings({"rawtypes", "unchecked"})
public class SindicatoControle extends SuperControle {
	
	private static final long serialVersionUID = -8870111568804712763L;
	
	private static final String TELA_FORM = "sindicatoForm";
	private static final String TELA_CONS = "sindicatoCons";
	private static final String CONTEXT_PARA_EDICAO = "sindicatoItem";

	private static final String MSG_SINDICATO_GRUPO = "msg_Sindicado_grupo";
	private static final String MSG_SINDICATO_EVENTO = "msg_Sindicado_evento";
	private static final String MSG_VALOR_DUPLICADO = "msg_erro_valorduplicado";

	private SindicatoVO sindicatoVO;
	private SindicatoMediaFeriasVO sindicatoMediaFeriasVO;
	private SindicatoMedia13VO sindicatoMedia13VO;
	private SindicatoMediaRescisaoVO sindicatoMediaRescisaoVO;

	private List<SelectItem> listaSelectItemSituacaoPeriodo;

	private String valorConsultaSituacao;
	
	private String campoConsultaParceiro;
	private String valorConsultaParceiro;
	private List<ParceiroVO> listaParceiros;
	
	private String campoConsultadoEventoFP;
	private String campoConsultaEventoFP;
	private String valorConsultaEventoFP;
	private List<EventoFolhaPagamentoVO> listaEventoFP;
	
	public SindicatoControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
	}

	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		setSindicatoVO(new SindicatoVO());
		setSindicatoMediaFeriasVO(new SindicatoMediaFeriasVO());
		setSindicatoMedia13VO(new SindicatoMedia13VO());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			SindicatoVO obj = (SindicatoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setSindicatoVO(getFacadeFactory().getSindicatoInterfaceFacade().consultarPorChavePrimaria(obj.getCodigo(), getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS));	
		}catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			Uteis.isAtributoPreenchido(getSindicatoVO().getCodigo());
			getFacadeFactory().getSindicatoInterfaceFacade().persistir(getSindicatoVO(), true, getUsuarioLogado());
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
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getFacadeFactory().getSindicatoInterfaceFacade().consultarPorFiltro(getControleConsultaOtimizado());
			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String excluir() {
		try {
			getFacadeFactory().getSindicatoInterfaceFacade().excluir(getSindicatoVO(), true, getUsuarioLogado());
			setSindicatoVO(new SindicatoVO());
			getSindicatoVO().setMediaDasFerias(new ArrayList<>());
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
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaPeriodoAquisitivoFuncionario.FUNCIONARIO.name());
		setListaConsulta(new ArrayList<>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public boolean getApresentarResultadoConsulta() {
		return getControleConsultaOtimizado().getListaConsulta().size() > 0;
	}

	public boolean getApresentarPaginadorResultadoConsulta() {
		return getControleConsultaOtimizado().getTotalRegistrosEncontrados() > 10;
	}

	public void adicionarMediaFerias() {
		try {
			
			validarDadosMediaFerias(); 

			if (getSindicatoMediaFeriasVO().getItemEmEdicao()) {
				
				Iterator<SindicatoMediaFeriasVO> i = getSindicatoVO().getMediaDasFerias().iterator();
				int index = 0;
				int aux = -1;
				SindicatoMediaFeriasVO objAux = new SindicatoMediaFeriasVO();
				while(i.hasNext()) {
					SindicatoMediaFeriasVO objExistente = i.next();

					if (objExistente.getCodigo().equals(getSindicatoMediaFeriasVO().getCodigo()) && objExistente.getItemEmEdicao()){
						getSindicatoMediaFeriasVO().setItemEmEdicao(false);
				       	aux = index;
				       	objAux = getSindicatoMediaFeriasVO();
		 			}
		            index++;
				}

				if(aux >= 0) {
					getSindicatoVO().getMediaDasFerias().set(aux, objAux);
				}
			} else {		
				getSindicatoVO().getMediaDasFerias().add(0, getSindicatoMediaFeriasVO());
			}

			limparDadosMediaFerias();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void adicionarMedia13() {
		try {
			
			validarDadosMedia13(); 

			if (getSindicatoMedia13VO().getItemEmEdicao()) {
				
				Iterator<SindicatoMedia13VO> i = getSindicatoVO().getMedia13().iterator();
				int index = 0;
				int aux = -1;
				SindicatoMedia13VO objAux = new SindicatoMedia13VO();
				while(i.hasNext()) {
					SindicatoMedia13VO objExistente = i.next();

					if (objExistente.getCodigo().equals(getSindicatoMediaFeriasVO().getCodigo()) && objExistente.getItemEmEdicao()){
						getSindicatoMediaFeriasVO().setItemEmEdicao(false);
				       	aux = index;
				       	objAux = getSindicatoMedia13VO();
		 			}
		            index++;
				}

				if(aux >= 0) {
					getSindicatoVO().getMedia13().set(aux, objAux);
				}
			} else {		
				getSindicatoVO().getMedia13().add(0, getSindicatoMedia13VO());
			}

			limparDadosMedia13();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void adicionarMediaRescisao() {
		try {
			
			validarDadosMediaRescisao(); 

			if (getSindicatoMediaRescisaoVO().getItemEmEdicao()) {
				
				Iterator<SindicatoMediaRescisaoVO> i = getSindicatoVO().getSindicatoMediaRescisaoVOs().iterator();
				int index = 0;
				int aux = -1;
				SindicatoMediaRescisaoVO objAux = new SindicatoMediaRescisaoVO();
				while(i.hasNext()) {
					SindicatoMediaRescisaoVO objExistente = i.next();

					if (objExistente.getCodigo().equals(getSindicatoMediaFeriasVO().getCodigo()) && objExistente.getItemEmEdicao()){
						getSindicatoMediaFeriasVO().setItemEmEdicao(false);
				       	aux = index;
				       	objAux = getSindicatoMediaRescisaoVO();
		 			}
		            index++;
				}

				if(aux >= 0) {
					getSindicatoVO().getSindicatoMediaRescisaoVOs().set(aux, objAux);
				}
			} else {		
				getSindicatoVO().getSindicatoMediaRescisaoVOs().add(getSindicatoMediaRescisaoVO());
			}

			limparDadosEventoMediaRescisao();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	private void validarDadosMediaFerias() throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(getSindicatoMediaFeriasVO().getGrupo())) {
			throw new ConsistirException(UteisJSF.internacionalizar(MSG_SINDICATO_GRUPO));
		}

		if (!Uteis.isAtributoPreenchido(getSindicatoMediaFeriasVO().getEventoMediaVO().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar(MSG_SINDICATO_EVENTO));
		}
		
		if(getSindicatoVO().getMediaDasFerias().stream().filter(p -> p.getGrupo().equals(getSindicatoMediaFeriasVO().getGrupo()) && p.getEventoMediaVO().getCodigo().equals(getSindicatoMediaFeriasVO().getEventoMediaVO().getCodigo())).count() > 0)
			throw new ConsistirException(UteisJSF.internacionalizar(MSG_VALOR_DUPLICADO));
		
	}
	
	private void validarDadosMedia13() throws ConsistirException {
		
		if (!Uteis.isAtributoPreenchido(getSindicatoMedia13VO().getGrupo())) {
			throw new ConsistirException(UteisJSF.internacionalizar(MSG_SINDICATO_GRUPO));
		}

		if (!Uteis.isAtributoPreenchido(getSindicatoMedia13VO().getEventoMediaVO().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar(MSG_SINDICATO_EVENTO));
		}
		
		if(getSindicatoVO().getMedia13().stream().filter(p -> p.getGrupo().equals(getSindicatoMedia13VO().getGrupo()) && p.getEventoMediaVO().getCodigo().equals(getSindicatoMedia13VO().getEventoMediaVO().getCodigo())).count() > 0)
			throw new ConsistirException(UteisJSF.internacionalizar(MSG_VALOR_DUPLICADO));
		
	}

	private void validarDadosMediaRescisao() throws ConsistirException {

		if (!Uteis.isAtributoPreenchido(getSindicatoMediaRescisaoVO().getEventoFolhaPagamento().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar(MSG_SINDICATO_EVENTO));
		}

		if (!Uteis.isAtributoPreenchido(getSindicatoMediaRescisaoVO().getGrupo())) {
			throw new ConsistirException(UteisJSF.internacionalizar(MSG_SINDICATO_GRUPO));
		}
		
		if(getSindicatoVO().getSindicatoMediaRescisaoVOs().stream().filter(p -> p.getGrupo().equals(getSindicatoMediaRescisaoVO().getGrupo()) && p.getEventoFolhaPagamento().getCodigo().equals(getSindicatoMediaRescisaoVO().getEventoFolhaPagamento().getCodigo())).count() > 0)
			throw new ConsistirException(UteisJSF.internacionalizar(MSG_VALOR_DUPLICADO));

	}

	public void cancelarEdicao() {
		setSindicatoMediaFeriasVO(new SindicatoMediaFeriasVO());
		getSindicatoMediaFeriasVO().setItemEmEdicao(false);
		setMensagemID(MSG_TELA.msg_entre_dados.name());
	}
	
	public void limparDadosMediaFerias() {
		this.cancelarEdicao();
	}
	
	public void cancelarEdicao13() {
		setSindicatoMedia13VO(new SindicatoMedia13VO());
		getSindicatoMedia13VO().setItemEmEdicao(false);
		setMensagemID(MSG_TELA.msg_entre_dados.name());
	}
	
	public void limparDadosMedia13() {
		this.cancelarEdicao13();
	}
	
	public void removerItem(SindicatoMediaFeriasVO obj) {
		getSindicatoVO().getMediaDasFerias().removeIf(p -> p.getGrupo().equals(obj.getGrupo()) && p.getEventoMediaVO().getCodigo().equals(obj.getEventoMediaVO().getCodigo()));
		limparDadosMediaFerias();
	}
	
	public void removerItemRescisao(SindicatoMediaRescisaoVO obj) {
		getSindicatoVO().getSindicatoMediaRescisaoVOs().removeIf(p -> p.getGrupo().equals(obj.getGrupo()) && p.getEventoFolhaPagamento().getCodigo().equals(obj.getEventoFolhaPagamento().getCodigo()));
		limparDadosEventoMediaRescisao();
	}

	public void removerItem13(SindicatoMedia13VO obj) {
		getSindicatoVO().getMedia13().removeIf(p -> p.getGrupo().equals(obj.getGrupo()) && p.getEventoMediaVO().getCodigo().equals(obj.getEventoMediaVO().getCodigo()));
		limparDadosMedia13();
	}

	public String editarMediaFerias()  {
		limparDadosMediaFerias();
        try {
        	SindicatoMediaFeriasVO obj =(SindicatoMediaFeriasVO) context().getExternalContext().getRequestMap().get("mediaFerias");
        	obj.setItemEmEdicao(true);
        	//Clona o objeto da grid que sera editado para criar outra referencia de memoria
        	setSindicatoMediaFeriasVO((SindicatoMediaFeriasVO) SerializationUtils.clone(obj));       	
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "";
    }

	public String getValorConsultaSituacao() {
		if (valorConsultaSituacao == null) {
			valorConsultaSituacao = "";
		}
		return valorConsultaSituacao;
	}

	public void setValorConsultaSituacao(String valorConsultaSituacao) {
		this.valorConsultaSituacao = valorConsultaSituacao;
	}

	public List<SelectItem> getListaSelectItemSituacaoPeriodo() {
		if(listaSelectItemSituacaoPeriodo == null)
			listaSelectItemSituacaoPeriodo = SituacaoPeriodoAquisitivoEnum.getValorSituacaoPeriodoAquisitivoEnum();
		return listaSelectItemSituacaoPeriodo;
	}

	public void setListaSelectItemSituacaoPeriodo(List<SelectItem> listaSelectItemSituacaoPeriodo) {
		this.listaSelectItemSituacaoPeriodo = listaSelectItemSituacaoPeriodo;
	}
	
	public SindicatoVO getSindicatoVO() {
		if (sindicatoVO == null)
			sindicatoVO = new SindicatoVO();
		return sindicatoVO;
	}

	public void setSindicatoVO(SindicatoVO sindicatoVO) {
		this.sindicatoVO = sindicatoVO;
	}

	public SindicatoMediaFeriasVO getSindicatoMediaFeriasVO() {
		if (sindicatoMediaFeriasVO == null)
			sindicatoMediaFeriasVO = new SindicatoMediaFeriasVO();
		return sindicatoMediaFeriasVO;
	}

	public void setSindicatoMediaFeriasVO(SindicatoMediaFeriasVO sindicatoMediaFeriasVO) {
		this.sindicatoMediaFeriasVO = sindicatoMediaFeriasVO;
	}
	
	public void consultarParceiro() {
		try {
			
			if (!Uteis.isAtributoPreenchido(getValorConsultaParceiro())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_ParametroConsulta_informeUmParametro"));
			}
			List objs = getFacadeFactory().getParceiroFacade().consultarPorTipoSindicato(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			setListaParceiros(objs);
            setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaParceiros(new ArrayList<>());
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getCampoConsultaParceiro() {
		if (campoConsultaParceiro == null)
			campoConsultaParceiro = "";
		return campoConsultaParceiro;
	}

	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	public String getValorConsultaParceiro() {
		if (valorConsultaParceiro == null)
			valorConsultaParceiro = "";
		return valorConsultaParceiro;
	}

	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	public List<ParceiroVO> getListaParceiros() {
		if (listaParceiros == null)
			listaParceiros = new ArrayList<>();
		return listaParceiros;
	}

	public void setListaParceiros(List<ParceiroVO> listaParceiros) {
		this.listaParceiros = listaParceiros;
	}

	public void selecionarParceiro() {
		ParceiroVO parceiro = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroVO");
		getSindicatoVO().getParceiroVO().setCodigo(parceiro.getCodigo());
		getSindicatoVO().getParceiroVO().setNome(parceiro.getNome());
	}
	
	public EventoFolhaPagamentoVO consultarEventoPorIdentificador(String identificadorEvento) {
		try {
			if (Uteis.isAtributoPreenchido(identificadorEvento)) {
				return getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChaveIdentificador(identificadorEvento, false, getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return new EventoFolhaPagamentoVO();
	}
	
	public void consultarEventoParaAdiantamentoPorIdentificador() {
		getSindicatoVO().setEventoAdiantamentoFerias(consultarEventoPorIdentificador(getSindicatoVO().getEventoAdiantamentoFerias().getIdentificador()));
	}
	
	public void consultarEventoPrimeiraParcela13PorIdentificador() {
		getSindicatoVO().setEventoPrimeiraParcela13(consultarEventoPorIdentificador(getSindicatoVO().getEventoPrimeiraParcela13().getIdentificador()));
	}
	
	public void consultarEventoDescontoPrimeiraParcela13PorIdentificador() {
		getSindicatoVO().setEventoDescontoPrimeiraParcela13(consultarEventoPorIdentificador(getSindicatoVO().getEventoDescontoPrimeiraParcela13().getIdentificador()));
	}
	
	public void consultarEventoParaDescontoDoAdiantamentoPorIdentificador() {
		getSindicatoVO().setEventoDescontoAdiantamentoFerias(consultarEventoPorIdentificador(getSindicatoVO().getEventoDescontoAdiantamentoFerias().getIdentificador()));
	}
	
	public void consultarEventoDeMediaPorIdentificador() {
		getSindicatoMediaFeriasVO().setEventoMediaVO(consultarEventoPorIdentificador(getSindicatoMediaFeriasVO().getEventoMediaVO().getIdentificador()));
	}
	
	public void consultarEventoDeMedia13PorIdentificador() {
		getSindicatoMedia13VO().setEventoMediaVO(consultarEventoPorIdentificador(getSindicatoMedia13VO().getEventoMediaVO().getIdentificador()));
	}

	public void consultarEventoDeMediaRescisaoPorIdentificador() {
		getSindicatoMediaRescisaoVO().setEventoFolhaPagamento(consultarEventoPorIdentificador(getSindicatoMediaRescisaoVO().getEventoFolhaPagamento().getIdentificador()));
	}
	
	public void consultarEventoLancamentoFaltaPorIdentificador() {
		getSindicatoVO().setEventoLancamentoFalta(consultarEventoPorIdentificador(getSindicatoVO().getEventoLancamentoFalta().getIdentificador()));
	}
	
	public void consultarEventoLancamentoDSRPorIdentificador() {
		getSindicatoVO().setEventoDSRPerdida(consultarEventoPorIdentificador(getSindicatoVO().getEventoDSRPerdida().getIdentificador()));
	}
	
	public void consultarDevolucaoFaltaPorIdentificador() {
		getSindicatoVO().setEventoDevolucaoFalta(consultarEventoPorIdentificador(getSindicatoVO().getEventoDevolucaoFalta().getIdentificador()));
	}
	
	public void limparDadosEventoAdiantamento() {
		getSindicatoVO().setEventoAdiantamentoFerias(new EventoFolhaPagamentoVO());
	}
	
	public void limparDadosEventoMediaRescisao() {
		setSindicatoMediaRescisaoVO(new SindicatoMediaRescisaoVO());
	}
	
	public void limparDadosEventoPrimeiraParcela() {
		getSindicatoVO().setEventoPrimeiraParcela13(new EventoFolhaPagamentoVO());
	}
	
	public void limparDadosEventoDescontoPrimeiraParcela() {
		getSindicatoVO().setEventoDescontoPrimeiraParcela13(new EventoFolhaPagamentoVO());
	}
	
	public void limparDadosEventoDescontoAdiantamento() {
		getSindicatoVO().setEventoDescontoAdiantamentoFerias(new EventoFolhaPagamentoVO());
	}

	public void limparDadosEventoFalta() {
		getSindicatoVO().setEventoLancamentoFalta(new EventoFolhaPagamentoVO());
	}
	
	public void limparDadosEventoDSRPerdida() {
		getSindicatoVO().setEventoDSRPerdida(new EventoFolhaPagamentoVO());
	}
	
	public void limparDadosEventoDevolucaoFalta() {
		getSindicatoVO().setEventoDevolucaoFalta(new EventoFolhaPagamentoVO());
	}
	
	public void limparDadosEventoMedia() {
		getSindicatoMediaFeriasVO().setEventoMediaVO(new EventoFolhaPagamentoVO());
	}
	
	public void limparDadosEventoMedia13() {
		getSindicatoMedia13VO().setEventoMediaVO(new EventoFolhaPagamentoVO());
	}
	
	public void limparCampoParceiro() {
		getSindicatoVO().setParceiroVO(new ParceiroVO());
	}
	
	public void selecionarEvento() {
		
		EventoFolhaPagamentoVO evento = (EventoFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("eventoItem");
		
		switch (getCampoConsultadoEventoFP()) {
		case "adiantamento":
			getSindicatoVO().getEventoAdiantamentoFerias().setCodigo(evento.getCodigo());
			getSindicatoVO().getEventoAdiantamentoFerias().setIdentificador(evento.getIdentificador());
			getSindicatoVO().getEventoAdiantamentoFerias().setDescricao(evento.getDescricao());
			break;
		case "desconto":
			getSindicatoVO().getEventoDescontoAdiantamentoFerias().setCodigo(evento.getCodigo());
			getSindicatoVO().getEventoDescontoAdiantamentoFerias().setIdentificador(evento.getIdentificador());
			getSindicatoVO().getEventoDescontoAdiantamentoFerias().setDescricao(evento.getDescricao());
			break;
		case "falta":
			getSindicatoVO().getEventoLancamentoFalta().setCodigo(evento.getCodigo());
			getSindicatoVO().getEventoLancamentoFalta().setIdentificador(evento.getIdentificador());
			getSindicatoVO().getEventoLancamentoFalta().setDescricao(evento.getDescricao());
			break;
		case "dsr":
			getSindicatoVO().getEventoDSRPerdida().setCodigo(evento.getCodigo());
			getSindicatoVO().getEventoDSRPerdida().setIdentificador(evento.getIdentificador());
			getSindicatoVO().getEventoDSRPerdida().setDescricao(evento.getDescricao());
			break;
		case "devolucaoFalta":
			getSindicatoVO().getEventoDevolucaoFalta().setCodigo(evento.getCodigo());
			getSindicatoVO().getEventoDevolucaoFalta().setIdentificador(evento.getIdentificador());
			getSindicatoVO().getEventoDevolucaoFalta().setDescricao(evento.getDescricao());
			break;
		case "primeiraParcela13":
			getSindicatoVO().getEventoPrimeiraParcela13().setCodigo(evento.getCodigo());
			getSindicatoVO().getEventoPrimeiraParcela13().setIdentificador(evento.getIdentificador());
			getSindicatoVO().getEventoPrimeiraParcela13().setDescricao(evento.getDescricao());
			break;
		case "descontoPrimeiraParcela13":
			getSindicatoVO().getEventoDescontoPrimeiraParcela13().setCodigo(evento.getCodigo());
			getSindicatoVO().getEventoDescontoPrimeiraParcela13().setIdentificador(evento.getIdentificador());
			getSindicatoVO().getEventoDescontoPrimeiraParcela13().setDescricao(evento.getDescricao());
			break;
		case "decimoTerceiro":
			getSindicatoMedia13VO().getEventoMediaVO().setCodigo(evento.getCodigo());
			getSindicatoMedia13VO().getEventoMediaVO().setIdentificador(evento.getIdentificador());
			getSindicatoMedia13VO().getEventoMediaVO().setDescricao(evento.getDescricao());
			break;
		case "rescisao" :
			getSindicatoMediaRescisaoVO().setEventoFolhaPagamento(evento);
		default:
			getSindicatoMediaFeriasVO().getEventoMediaVO().setCodigo(evento.getCodigo());
			getSindicatoMediaFeriasVO().getEventoMediaVO().setIdentificador(evento.getIdentificador());
			getSindicatoMediaFeriasVO().getEventoMediaVO().setDescricao(evento.getDescricao());
			break;
		}
	}

	public void limparCamposConsultaEvento() {
		setValorConsultaEventoFP("");
		setListaEventoFP(new ArrayList<>());
	}
	
	public void setEventoAdiantamento() {
		setCampoConsultadoEventoFP("adiantamento");
		limparCamposConsultaEvento();
	}
	
	public void setEventoPrimeiraParcela13() {
		setCampoConsultadoEventoFP("primeiraParcela13");
		limparCamposConsultaEvento();
	}
	
	public void setEventoDescontoPrimeiraParcela13() {
		setCampoConsultadoEventoFP("descontoPrimeiraParcela13");
		limparCamposConsultaEvento();
	}
	
	public void setEventoDesconto() {
		setCampoConsultadoEventoFP("desconto");
		limparCamposConsultaEvento();
	}
	
	public void setEventoFalta() {
		setCampoConsultadoEventoFP("falta");
		limparCamposConsultaEvento();
	}
	
	public void setEventoDSR() {
		setCampoConsultadoEventoFP("dsr");
		limparCamposConsultaEvento();
	}
	
	public void setEventoDevolucaoFalta() {
		setCampoConsultadoEventoFP("devolucaoFalta");
		limparCamposConsultaEvento();
	}
	
	public void setEventoMedia() {
		setCampoConsultadoEventoFP("media");
		limparCamposConsultaEvento();
	}

	public void setEventoMediaRescisao() {
		setCampoConsultadoEventoFP("rescisao");
		limparCamposConsultaEvento();
	}
	
	public void setEventoMedia13() {
		setCampoConsultadoEventoFP("decimoTerceiro");
		limparCamposConsultaEvento();
	}
	
	public String getCampoConsultaEventoFP() {
		if (campoConsultaEventoFP == null)
			campoConsultaEventoFP = "";
		return campoConsultaEventoFP;
	}

	public void setCampoConsultaEventoFP(String campoConsultaEventoFP) {
		this.campoConsultaEventoFP = campoConsultaEventoFP;
	}

	public String getValorConsultaEventoFP() {
		if (valorConsultaEventoFP == null)
			valorConsultaEventoFP = "";
		return valorConsultaEventoFP;
	}

	public void setValorConsultaEventoFP(String valorConsultaEventoFP) {
		this.valorConsultaEventoFP = valorConsultaEventoFP;
	}

	public List<EventoFolhaPagamentoVO> getListaEventoFP() {
		if (listaEventoFP == null)
			listaEventoFP = new ArrayList<>();
		return listaEventoFP;
	}

	public void setListaEventoFP(List<EventoFolhaPagamentoVO> listaEventoFP) {
		this.listaEventoFP = listaEventoFP;
	}
	
	public List<SelectItem> getTipoConsultaComboEvento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", UteisJSF.internacionalizar("prt_TextoPadrao_descricao")));
		itens.add(new SelectItem("identificador", UteisJSF.internacionalizar("prt_TextoPadrao_identificador")));
		return itens;
	}

	public String getCampoConsultadoEventoFP() {
		if (campoConsultadoEventoFP == null)
			campoConsultadoEventoFP = "";
		return campoConsultadoEventoFP;
	}

	public void setCampoConsultadoEventoFP(String campoConsultadoEventoFP) {
		this.campoConsultadoEventoFP = campoConsultadoEventoFP;
	}
	
	public void consultarEvento() {
		try {
			if(getCampoConsultaEventoFP().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta());
			} 
			
			setListaEventoFP(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorFiltro(getCampoConsultaEventoFP(), getValorConsultaEventoFP(), "ATIVO", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	/**
	 * Caso seja desconsiderado a validacao das faltas, o sistema limpa os campos do evento de falta
	 * para nao correr o risco do usuario salvar com os dados do evento preenchido e o 
	 * validarFaltas desmarcado
	 */
	public void considerarFaltaDeFerias () {
		if(!getSindicatoVO().getValidarFaltas()) {
			limparDadosEventoFalta();
			limparDadosEventoDSRPerdida();
			limparDadosEventoDevolucaoFalta();
		}
	}
	
	public SindicatoMedia13VO getSindicatoMedia13VO() {
		if (sindicatoMedia13VO == null)
			sindicatoMedia13VO = new SindicatoMedia13VO();
		return sindicatoMedia13VO;
	}

	public void setSindicatoMedia13VO(SindicatoMedia13VO sindicatoMedia13VO) {
		this.sindicatoMedia13VO = sindicatoMedia13VO;
	}

	public SindicatoMediaRescisaoVO getSindicatoMediaRescisaoVO() {
		if (sindicatoMediaRescisaoVO == null) {
			sindicatoMediaRescisaoVO = new SindicatoMediaRescisaoVO();
		}
		return sindicatoMediaRescisaoVO;
	}

	public void setSindicatoMediaRescisaoVO(SindicatoMediaRescisaoVO sindicatoMediaRescisaoVO) {
		this.sindicatoMediaRescisaoVO = sindicatoMediaRescisaoVO;
	}
	
	
}