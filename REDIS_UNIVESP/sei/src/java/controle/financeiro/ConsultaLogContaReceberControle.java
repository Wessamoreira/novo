/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConsultaLogContaReceberVO;
import negocio.comuns.financeiro.ContaReceberLogVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

/**
 *
 * @author Carlos
 */
@Controller("ConsultaLogContaReceberControle")
@Scope("viewScope")
@Lazy
public class ConsultaLogContaReceberControle extends SuperControle {

	private static final long serialVersionUID = 5279037596100854962L;
	private ContaReceberLogVO contaReceberVO;
	private List<ConsultaLogContaReceberVO> listaConsultaLogContaReceberVOs;
	private String mes;
	private String ano;
	private String acao;
	private String matricula;
	private String nossoNumero;
	private TipoOrigemContaReceber tipoOrigem;
	private Date dataInicio;
	private Date dataFim;
	private Integer codigo;
	private String campoConsultaUsuario;
    private String valorConsultaUsuario;
    private List<UsuarioVO> listaConsultaUsuarioVOs;
    private UsuarioVO usuarioLogVO;
    
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
    private Date dataInicioRecebimento;
	private Date dataFimRecebimento;
	private Boolean apresentarDetalhes;

	public ConsultaLogContaReceberControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		montarListaSelectItemAno();
		setMensagemID("msg_entre_prmconsulta");
	}

	public String editar() {
		setContaReceberVO((ContaReceberLogVO) getRequestMap().get("contaReceberItem"));
		return Uteis.getCaminhoRedirecionamentoNavegacao("consultaLogContaReceberForm");
	}

	public void consultarLogContaReceber() {
		try {
			setListaConsultaLogContaReceberVOs(getFacadeFactory().getConsultaLogContaReceberFacade().consultar(getMatricula(), getNossoNumero(), getCodigo(), getTipoOrigem(), getAno(), getMes(), getAcao(), getColuna(), getDataInicio(), getDataFim(), getDataInicioRecebimento(), getDataFimRecebimento(), getUsuarioLogVO(), true, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultarLogContaReceber();
	}

	public String irPaginaConsulta() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("consultaLogContaReceber");
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
	
	public List<SelectItem> getListaSelectItemTipoOrigemContaReceberVOs() {
		return TipoOrigemContaReceber.getListaSelectItemTipoOrigemContaReceberVOs();
	}

	public void setTipoConsultaCombo(List<SelectItem> tipoConsultaCombo) {
		this.tipoConsultaCombo = tipoConsultaCombo;
	}

	public ContaReceberLogVO getContaReceberVO() {
		if (contaReceberVO == null) {
			contaReceberVO = new ContaReceberLogVO();
		}
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberLogVO consultaLogContaReceberVO) {
		this.contaReceberVO = consultaLogContaReceberVO;
	}

	public List<SelectItem> getListaMesAnoListSelectItem() {
		return MesAnoEnum.getListaSelectItemDescricaoMes();
	}
	
	public List<SelectItem> listaSelectItemAnoVOs;
	
	public void montarListaSelectItemAno() {
		List<String> listaAnoVOs = getFacadeFactory().getConsultaLogContaReceberFacade().consultarAnoAudit(getUsuarioLogado());
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
		List<String> listaMesVOs = getFacadeFactory().getConsultaLogContaReceberFacade().consultarMesAuditPorAno(getAno(), getUsuarioLogado());
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

	public String getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = "";
		}
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
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

	public List<ConsultaLogContaReceberVO> getListaConsultaLogContaReceberVOs() {
		if (listaConsultaLogContaReceberVOs == null) {
			listaConsultaLogContaReceberVOs = new ArrayList<ConsultaLogContaReceberVO>(0);
		}
		return listaConsultaLogContaReceberVOs;
	}

	public void setListaConsultaLogContaReceberVOs(List<ConsultaLogContaReceberVO> listaConsultaLogContaReceberVOs) {
		this.listaConsultaLogContaReceberVOs = listaConsultaLogContaReceberVOs;
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

	public TipoOrigemContaReceber getTipoOrigem() {
		return tipoOrigem;
	}

	public void setTipoOrigem(TipoOrigemContaReceber tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
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
			List<String> listaColunaVOs = getFacadeFactory().getConsultaLogContaReceberFacade().consultarColunasContaReceber();
			listaSelectItemColunaVOs = new ArrayList<SelectItem>(0);
			listaSelectItemColunaVOs.add(new SelectItem("", ""));
			for (String coluna : listaColunaVOs) {
				listaSelectItemColunaVOs.add(new SelectItem(coluna, coluna));
			}
			
		}
		return listaSelectItemColunaVOs;
	}

	public Date getDataInicioRecebimento() {
		return dataInicioRecebimento;
	}

	public void setDataInicioRecebimento(Date dataInicioRecebimento) {
		this.dataInicioRecebimento = dataInicioRecebimento;
	}

	public Date getDataFimRecebimento() {
		return dataFimRecebimento;
	}

	public void setDataFimRecebimento(Date dataFimRecebimento) {
		this.dataFimRecebimento = dataFimRecebimento;
	}
	
}
