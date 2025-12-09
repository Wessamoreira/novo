/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoCamposAlteradosVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.AuditVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;

/**
 *
 * @author Carlos
 */
@Controller("LogAuditHistoricoControle")
@Scope("viewScope")
@Lazy
public class LogAuditHistoricoControle extends SuperControle {

	private static final long serialVersionUID = 5279037596100854962L;
	private HistoricoVO historicoVO;
	private List<AuditVO> listaLogHistoricoVOs;
	private String mes;
	private String ano;
	private String acao;
	private String matricula;
	private DisciplinaVO disciplinaVO;
	private Date dataInicio;
	private Date dataFim;
	private Integer codigo;
	private String campoConsultaUsuario;
    private String valorConsultaUsuario;
    private List<UsuarioVO> listaConsultaUsuarioVOs;
    private UsuarioVO usuarioLogVO;
    
    private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private SituacaoHistorico situacaoHistorico;
	private HistoricoCamposAlteradosVO historicoCamposAlteradosVO;
    
    public Boolean getApresentarDetalhes() {
		if(apresentarDetalhes == null) {
			apresentarDetalhes = false;
		}
    	return apresentarDetalhes;
	}

	public void setApresentarDetalhes(Boolean apresentarDetalhes) {
		this.apresentarDetalhes = apresentarDetalhes;
	}

	private String coluna;
    private Boolean apresentarDetalhes;

	public LogAuditHistoricoControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		montarListaSelectItemAno();
		setMensagemID("msg_entre_prmconsulta");
	}

	public String editar() {
		setHistoricoVO((HistoricoVO) getRequestMap().get("historicoItem"));
		return Uteis.getCaminhoRedirecionamentoNavegacao("logAuditHistoricoForm");
	}

	public void consultarLogHistorico() {
		try {
			if (getAno().equals("")) {
				throw new Exception("O campo Ano deve ser Informado.");
			}
			if (getMes().equals("")) {
				throw new Exception("O campo Mês deve ser Informado.");
			}
			if(!Uteis.isAtributoPreenchido(getMatricula()) && !Uteis.isAtributoPreenchido(getUsuarioLogVO()) && !Uteis.isAtributoPreenchido(getCodigo())) {
				throw new Exception("Deve ser informado pelo menos um dos filtros listados a seguir.[Matrícula, Usuário Log ou Código]");
			}
			setListaLogHistoricoVOs(getFacadeFactory().getLogAuditHistoricoFacade().consultar(getMatricula(), getSituacaoHistorico(), getCodigo(), getDisciplinaVO().getCodigo(), getAno(), getMes(), getAcao(), getColuna(), getDataInicio(), getDataFim(), getUsuarioLogVO(), true, getHistoricoCamposAlteradosVO(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultarLogHistorico();
	}

	public String irPaginaConsulta() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("logAuditHistoricoCons");
	}

	private List<SelectItem> tipoConsultaCombo;

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("nossoNumero", "Nosso Número"));
			tipoConsultaCombo.add(new SelectItem("matricula", "Matrícula Aluno"));
			tipoConsultaCombo.add(new SelectItem("sacado", "Sacado"));
			tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
		}
		
		return tipoConsultaCombo;
	}
	
	private List<SelectItem> tipoConsultaComboAcao;
	public List<SelectItem> getTipoConsultaComboAcao() {
		if (tipoConsultaComboAcao == null) {
			tipoConsultaComboAcao = new ArrayList<SelectItem>(0);
			tipoConsultaComboAcao.add(new SelectItem("", ""));
			tipoConsultaComboAcao.add(new SelectItem("U", "Alteração"));
			tipoConsultaComboAcao.add(new SelectItem("I", "Inserção"));
			tipoConsultaComboAcao.add(new SelectItem("D", "Exclusão"));
		}
		return tipoConsultaComboAcao;
	}

	public void setTipoConsultaCombo(List<SelectItem> tipoConsultaCombo) {
		this.tipoConsultaCombo = tipoConsultaCombo;
	}

	public HistoricoVO getHistoricoVO() {
		if (historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

	public List<SelectItem> getListaMesAnoListSelectItem() {
		return MesAnoEnum.getListaSelectItemDescricaoMes();
	}
	
	public List<SelectItem> listaSelectItemAnoVOs;
	
	public void montarListaSelectItemAno() {
		List<String> listaAnoVOs = getFacadeFactory().getLogAuditHistoricoFacade().consultarAnoAudit(getUsuarioLogado());
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		for (String ano : listaAnoVOs) {
			itens.add(new SelectItem(ano, ano));
		}
		setListaSelectItemAnoVOs(itens);
	}
	
	public List<SelectItem> listaSelectItemMesVOs;
	
	public void montarListaSelectItemMes() {
		if (getAno().equals("")) {
			setListaSelectItemMesVOs(new ArrayList<SelectItem>(0));
			return;
		}
		List<String> listaMesVOs = getFacadeFactory().getLogAuditHistoricoFacade().consultarMesAuditPorAno(getAno(), getUsuarioLogado());
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		for (String mes : listaMesVOs) {
			itens.add(new SelectItem(mes, getMes_Apresentar(mes)));
		}
		setListaSelectItemMesVOs(itens);
	}
	
	 public void consultarUsuario() {
	        try {
	            setListaConsultaUsuarioVOs(getFacadeFactory().getMapaAberturaTurmaFacade().consultarUsuario(getCampoConsultaUsuario(), null, getValorConsultaUsuario(), getUsuarioLogado()));
	            setMensagemID("msg_dados_consultados");
	        } catch (Exception e) {
	            setListaConsultaUsuarioVOs(new ArrayList(0));
	            setMensagemDetalhada("msg_erro", e.getMessage());
	        }
	    }

	    public void selecionarUsuario() {
	        UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioItens");
	        setUsuarioLogVO(obj);
	        setCampoConsultaUsuario("");
	        setValorConsultaUsuario("");
	        setListaConsultaUsuarioVOs(new ArrayList(0));
	    }

	    public void limparConsultaUsuario() {
	        getListaConsultaUsuarioVOs().clear();
	    }

	    public void limparDadosUsuario() {
	        setUsuarioLogVO(null);
	    }
	
	public String getMes_Apresentar(String mes) {
		if (mes.equals("01")) {
			return "Janeiro";
		}
		if (mes.equals("02")) {
			return "Fevereiro";
		}
		if (mes.equals("03")) {
			return "Março";
		}
		if (mes.equals("04")) {
			return "Abril";
		}
		if (mes.equals("05")) {
			return "Maio";
		}
		if (mes.equals("06")) {
			return "Junho";
		}
		if (mes.equals("07")) {
			return "Julho";
		}
		if (mes.equals("08")) {
			return "Agosto";
		}
		if (mes.equals("09")) {
			return "Setembro";
		}
		if (mes.equals("10")) {
			return "Outubro";
		}
		if (mes.equals("11")) {
			return "Novembro";
		}
		if (mes.equals("12")) {
			return "Dezembro";
		}
		return "";
	}

	public String getMes() {
		if (mes == null) {
			mes = "";
		}
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
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

	public List<AuditVO> getListaLogHistoricoVOs() {
		if (listaLogHistoricoVOs == null) {
			listaLogHistoricoVOs = new ArrayList<AuditVO>(0);
		}
		return listaLogHistoricoVOs;
	}

	public void setListaLogHistoricoVOs(List<AuditVO> listaLogHistoricoVOs) {
		this.listaLogHistoricoVOs = listaLogHistoricoVOs;
	}

	public List<SelectItem> getListaSelectItemMesVOs() {
		if (listaSelectItemMesVOs == null) {
			listaSelectItemMesVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMesVOs;
	}

	public void setListaSelectItemMesVOs(List<SelectItem> listaSelectItemMesVOs) {
		this.listaSelectItemMesVOs = listaSelectItemMesVOs;
	}

	public List<SelectItem> getListaSelectItemAnoVOs() {
		if (listaSelectItemAnoVOs == null) {
			listaSelectItemAnoVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemAnoVOs;
	}

	public void setListaSelectItemAnoVOs(List<SelectItem> listaSelectItemAnoVOs) {
		this.listaSelectItemAnoVOs = listaSelectItemAnoVOs;
	}

	public String getCampoConsultaUsuario() {
		if (campoConsultaUsuario == null) {
			campoConsultaUsuario = "";
		}
		return campoConsultaUsuario;
	}

	public void setCampoConsultaUsuario(String campoConsultaUsuario) {
		this.campoConsultaUsuario = campoConsultaUsuario;
	}

	public String getValorConsultaUsuario() {
		if (valorConsultaUsuario == null) {
			valorConsultaUsuario = "";
		}
		return valorConsultaUsuario;
	}

	public void setValorConsultaUsuario(String valorConsultaUsuario) {
		this.valorConsultaUsuario = valorConsultaUsuario;
	}

	public List<UsuarioVO> getListaConsultaUsuarioVOs() {
		if (listaConsultaUsuarioVOs == null) {
			listaConsultaUsuarioVOs = new ArrayList<UsuarioVO>(0);
		}
		return listaConsultaUsuarioVOs;
	}

	public void setListaConsultaUsuarioVOs(List<UsuarioVO> listaConsultaUsuarioVOs) {
		this.listaConsultaUsuarioVOs = listaConsultaUsuarioVOs;
	}

	public UsuarioVO getUsuarioLogVO() {
		if (usuarioLogVO == null) {
			usuarioLogVO = new UsuarioVO();
		}
		return usuarioLogVO;
	}

	public void setUsuarioLogVO(UsuarioVO usuarioLogVO) {
		this.usuarioLogVO = usuarioLogVO;
	}
	
	public List<SelectItem> getTipoConsultaComboUsuario() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("username", "Login"));
        return itens;
    }

	public String getAcao() {
		if (acao == null) {
			acao = "";
		}
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}
	
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getColuna() {
		if (coluna == null) {
			coluna = "";
		}
		return coluna;
	}

	public void setColuna(String coluna) {
		this.coluna = coluna;
	}

	public List<SelectItem> listaSelectItemColunaVOs;
	
	public List<SelectItem> getListaSelectItemColunaVOs() {
		if (listaSelectItemColunaVOs == null) {
			List<String> listaColunaVOs = getFacadeFactory().getLogAuditHistoricoFacade().consultarColunasAuditHistorico();
			listaSelectItemColunaVOs = new ArrayList<SelectItem>(0);
			listaSelectItemColunaVOs.add(new SelectItem("", ""));
			for (String coluna : listaColunaVOs) {
				listaSelectItemColunaVOs.add(new SelectItem(coluna, coluna));
			}
			
		}
		return listaSelectItemColunaVOs;
	}


	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}
	
	private List<SelectItem> tipoConsultaComboDisciplina;

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}

	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<>(0);
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			setDisciplinaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparDisciplina() throws Exception {
		try {
			setDisciplinaVO(null);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoHistoricoVOs() {
		return SituacaoHistorico.getSituacaoHistoricoEnum(Boolean.TRUE);
	}

	public SituacaoHistorico getSituacaoHistorico() {
		return situacaoHistorico;
	}

	public void setSituacaoHistorico(SituacaoHistorico situacaoHistorico) {
		this.situacaoHistorico = situacaoHistorico;
	}

	public HistoricoCamposAlteradosVO getHistoricoCamposAlteradosVO() {
		if (historicoCamposAlteradosVO == null) {
			historicoCamposAlteradosVO = new HistoricoCamposAlteradosVO();
		}
		return historicoCamposAlteradosVO;
	}

	public void setHistoricoCamposAlteradosVO(HistoricoCamposAlteradosVO historicoCamposAlteradosVO) {
		this.historicoCamposAlteradosVO = historicoCamposAlteradosVO;
	}
}
