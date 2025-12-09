package controle.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.recursoshumanos.FaltasFuncionarioVO;
import negocio.comuns.recursoshumanos.FaltasFuncionarioVO.EnumCampoConsultaFaltaFuncionario;
import negocio.comuns.recursoshumanos.enumeradores.TipoFaltaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

@Controller("FaltasFuncionarioControle")
@Scope("viewScope")
@Lazy
public class FaltasFuncionarioControle extends SuperControle {
	
	private static final long serialVersionUID = -1952371111339226391L;

	private static final String TELA_FORM = "faltasFuncionarioForm";
	private static final String TELA_CONS = "faltasFuncionarioCons";
	private static final String CONTEXT_PARA_EDICAO = "itemFaltasFuncionario";

	private FaltasFuncionarioVO faltasFuncionario;
	private FuncionarioCargoVO funcionarioCargoVO;

	private List<FaltasFuncionarioVO> listaFaltasFuncionarios;
	
	private List<SelectItem> listaSelectItemTipoFalta;

	private String valorConsultaSituacao;
	
	private Date dataInicialLancamento;
	private Integer qtdDiasLancamento;
	private TipoFaltaEnum tipoFaltaLancamento;
	private String motivoLancamento;

	public FaltasFuncionarioControle() {
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
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		setFuncionarioCargoVO(obj);
		return prepararDadosParaEdicao(obj);
	}

	public String prepararDadosParaEdicao(FuncionarioCargoVO obj) {
		try {
			getFaltasFuncionario().setFuncionarioCargo(obj);
			setListaFaltasFuncionarios(getFacadeFactory().getFaltasFuncionarioInterfaceFacade().consultarPorFuncionarioCargo(funcionarioCargoVO, false, getUsuarioLogado()));
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
			if (Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getCodigo())) {
				getFacadeFactory().getFaltasFuncionarioInterfaceFacade().persistirTodos(getListaFaltasFuncionarios(), getFuncionarioCargoVO(), true, getUsuarioLogado());
				getFaltasFuncionario().setFuncionarioCargo(getFuncionarioCargoVO());
				setMensagemID(MSG_TELA.msg_dados_gravados.name());
			} else {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), "Nenhum Funcionário selecionado");
			}
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
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getFacadeFactory().getFaltasFuncionarioInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), getValorConsultaSituacao());
			
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarFuncionarioPelaMatriculaCargo() {
		try {
			setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getFaltasFuncionario().getFuncionarioCargo().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

		if(getFuncionarioCargoVO().getCodigo() > 0) {
			getControleConsultaOtimizado().setNivelMontarDados(Uteis.NIVELMONTARDADOS_COMBOBOX);
			prepararDadosParaEdicao(getFuncionarioCargoVO());
		} else {
			setMensagemID("msg_erro_dadosnaoencontrados");
			setFuncionarioCargoVO(new FuncionarioCargoVO());
			setFaltasFuncionario(new FaltasFuncionarioVO());
			setListaFaltasFuncionarios(new ArrayList<>());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getFaltasFuncionarioInterfaceFacade().excluirPorFuncionarioCargo(getFuncionarioCargoVO(), false, getUsuario());
			setListaFaltasFuncionarios(new ArrayList<>());
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
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaFaltaFuncionario.FUNCIONARIO.name());
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

	public void adicionarFaltas() {
		try {
			getFacadeFactory().getFaltasFuncionarioInterfaceFacade().validarDados(getFaltasFuncionario().getDataInicio(), getFuncionarioCargoVO(), getFaltasFuncionario()); 

			validarDadosFaltaFuncionario(getFaltasFuncionario());

			if (getFaltasFuncionario().getItemEmEdicao()) {
				Iterator<FaltasFuncionarioVO> i = getListaFaltasFuncionarios().iterator();
				int index = 0;
				int aux = -1;
				FaltasFuncionarioVO objAux = new FaltasFuncionarioVO();
				while(i.hasNext()) {
					FaltasFuncionarioVO objExistente = i.next();

					if (objExistente.getCodigo().equals(getFaltasFuncionario().getCodigo()) && objExistente.getItemEmEdicao()){
						getFaltasFuncionario().setItemEmEdicao(false);
				       	aux = index;
				       	objAux = getFaltasFuncionario();
		 			}
		            index++;
				}

				if(aux >= 0) {
					getListaFaltasFuncionarios().set(aux, objAux);
				}
			} else {		
				getListaFaltasFuncionarios().add(getFaltasFuncionario());
			}

			limparDadosFaltas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	/**
	 * Valida a falta:
	 * - Falta pode ser do tipo justificada ou injustificada, caso
	 * uma faltaseja do tipo Estorno, deve ter uma falta injustificada lancada 
	 * 
	 * @param faltasFuncionarioVO
	 * @return true: valido
	 * false : invalido
	 */
	public void validarDadosFaltaFuncionario(FaltasFuncionarioVO faltasFuncionarioVO) throws ConsistirException  {

		for(FaltasFuncionarioVO faltas : getListaFaltasFuncionarios()) {

			//Valida que as datas nao sao iguais
			if(faltas.getDataInicio().equals(faltasFuncionarioVO.getDataInicio())) {

				//So existe uma falta do tipo ESTORNO para uma falta INJUSTIFICADA
				// Obs.: Faltas justificadas nao sao estornadas
				if(!faltasFuncionarioVO.getTipoFalta().equals(TipoFaltaEnum.ESTORNO) && !faltas.getTipoFalta().equals(TipoFaltaEnum.INJUSTIFICADA)) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_FaltasFuncionario_faltaJaCadastradaParaEsseDia"));
				}
			}
		}

		//Valida caso o tipo da falta for ESTORNO se existe uma outra falta lancada para o mesmo dia
		//Na validacao anterior ja foi verificado que para aceitar a mesma data, deve se ter uma falta do tipo ESTORNO e obrigatoriamente uma do tipo INJUSTIFICADA 
		if(faltasFuncionarioVO.getTipoFalta().equals(TipoFaltaEnum.ESTORNO)) {
			if(!getListaFaltasFuncionarios().stream().anyMatch(p -> p.getDataInicio().equals(faltasFuncionarioVO.getDataInicio()))) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_FaltasFuncionario_faltaEstornoSemFaltaInjustificada"));
			}	
		}
	}

	public void cancelarFalta() {
		setFaltasFuncionario(new FaltasFuncionarioVO());
		getFaltasFuncionario().setFuncionarioCargo(getFuncionarioCargoVO());
		getFaltasFuncionario().setItemEmEdicao(false);
		setMensagemID(MSG_TELA.msg_entre_dados.name());
	}
	
	public void limparDadosFaltas() {
		this.cancelarFalta();
	}
	
	public void removerFaltas(FaltasFuncionarioVO obj) {
		getListaFaltasFuncionarios().removeIf(p -> p.getDataInicio().equals(obj.getDataInicio()) && p.getTipoFalta().equals(obj.getTipoFalta()));
		limparDadosFaltas();
	}

	public String editarFaltasFuncionario()  {
		limparDadosFaltas();
        try {
        	FaltasFuncionarioVO obj =(FaltasFuncionarioVO) context().getExternalContext().getRequestMap().get("falta");
        	obj.setItemEmEdicao(true);
        	//Clona o objeto da grid que sera editado para criar outra referencia de memoria
        	setFaltasFuncionario((FaltasFuncionarioVO) SerializationUtils.clone(obj));       	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
        return "";
    }

	public void lancarFaltas () {

		int somaDias = 1;
		while(somaDias <= getQtdDiasLancamento()) {
			setFaltasFuncionario(new FaltasFuncionarioVO());
			getFaltasFuncionario().setDataInicio(UteisData.adicionarDiasEmData(getDataInicialLancamento(), somaDias-1));
			getFaltasFuncionario().setTipoFalta(getTipoFaltaLancamento());
			getFaltasFuncionario().setMotivo(getMotivoLancamento());
			getFaltasFuncionario().setFuncionarioCargo(getFuncionarioCargoVO());

			adicionarFaltas();
			
			somaDias++;
		}
	}
	
	public BigDecimal calcularTotalDeFaltas() {
		BigDecimal integral =  new BigDecimal(getListaFaltasFuncionarios().stream().filter(p -> p.getIntegral()).count());
		BigDecimal naoIntegral = new BigDecimal(getListaFaltasFuncionarios().stream().filter(p -> !p.getIntegral()).count());
		BigDecimal totalFaltas = integral.add(naoIntegral.divide(new BigDecimal("2")));

		return totalFaltas;
	}

	//GETTER AND SETTER
	public FaltasFuncionarioVO getFaltasFuncionario() {
		if (faltasFuncionario == null) {
			faltasFuncionario = new FaltasFuncionarioVO();
		}
		return faltasFuncionario;
	}

	public void setFaltasFuncionario(FaltasFuncionarioVO faltasFuncionario) {
		this.faltasFuncionario = faltasFuncionario;
	}

	public String getValorConsultaSituacao() {
		if (valorConsultaSituacao == null) {
			valorConsultaSituacao = "TODOS";
		}
		return valorConsultaSituacao;
	}

	public void setValorConsultaSituacao(String valorConsultaSituacao) {
		this.valorConsultaSituacao = valorConsultaSituacao;
	}

	public List<FaltasFuncionarioVO> getListaFaltasFuncionarios() {
		if (listaFaltasFuncionarios == null) {
			listaFaltasFuncionarios = new ArrayList<>();
		}
		return listaFaltasFuncionarios;
	}

	public void setListaFaltasFuncionarios(List<FaltasFuncionarioVO> listaEventoValeTransporteFuncionarioCargo) {
		this.listaFaltasFuncionarios = listaEventoValeTransporteFuncionarioCargo;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null) {
			funcionarioCargoVO = new FuncionarioCargoVO();
		}
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public List<SelectItem> getListaSelectItemTipoFalta() {
		if (listaSelectItemTipoFalta == null) {
			listaSelectItemTipoFalta = new ArrayList<>();
			listaSelectItemTipoFalta = TipoFaltaEnum.getValorTipoFaltaEnum();
		}
		return listaSelectItemTipoFalta;
	}

	public void setListaSelectItemTipoFalta(List<SelectItem> listaSelectItemTipoFalta) {
		this.listaSelectItemTipoFalta = listaSelectItemTipoFalta;
	}
	
	public void lancarPeriodoDeFalta () {
		
	}

	public Date getDataInicialLancamento() {
		if (dataInicialLancamento == null)
			dataInicialLancamento = new Date();
		return dataInicialLancamento;
	}

	public void setDataInicialLancamento(Date dataInicialLancamento) {
		this.dataInicialLancamento = dataInicialLancamento;
	}

	public Integer getQtdDiasLancamento() {
		if (qtdDiasLancamento == null)
			qtdDiasLancamento = 0;
		return qtdDiasLancamento;
	}

	public void setQtdDiasLancamento(Integer qtdDiasLancamento) {
		this.qtdDiasLancamento = qtdDiasLancamento;
	}
	
	public TipoFaltaEnum getTipoFaltaLancamento() {
		if (tipoFaltaLancamento == null)
			tipoFaltaLancamento = TipoFaltaEnum.INJUSTIFICADA;
		return tipoFaltaLancamento;
	}

	public void setTipoFaltaLancamento(TipoFaltaEnum tipoFaltaLancamento) {
		this.tipoFaltaLancamento = tipoFaltaLancamento;
	}

	public String getMotivoLancamento() {
		if (motivoLancamento == null)
			motivoLancamento = "";
		return motivoLancamento;
	}

	public void setMotivoLancamento(String motivoLancamento) {
		this.motivoLancamento = motivoLancamento;
	}
}