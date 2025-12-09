package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaCamposAlteradosVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.OpcaoTabelaLogMatriculaEnum;
import negocio.comuns.administrativo.AuditVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;

@Controller("LogMatriculaControle")
@Scope("viewScope")
@Lazy
public class LogMatriculaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 5279037596100854962L;
	private MatriculaVO matriculaVO;
	private List<AuditVO> listaLogMatriculaVOs;
	private String mes;
	private String ano;
	private String acao;
	private Date dataInicio;
	private Date dataFim;
	private Integer codigoMatriculaPeriodo;
	private String campoConsultaUsuario;
    private String valorConsultaUsuario;
    private List<UsuarioVO> listaConsultaUsuarioVOs;
    private UsuarioVO usuarioLogVO;
	private SituacaoVinculoMatricula situacaoMatricula;
	private MatriculaCamposAlteradosVO matriculaCamposAlteradosVO;
	private OpcaoTabelaLogMatriculaEnum opcaoTabelaLogMatricula;
	private String anoMatriculaPeriodo;
	private String semestreMatriculaPeriodo;
	private TurmaVO turmaVO;
	private List<TurmaVO> listaConsultaTurma;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
	
	protected List<MatriculaVO> listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private Integer codigoTransacao;
    
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

	public LogMatriculaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		montarListaSelectItemAno();
		montarListaSelectItemColuna();
		setMensagemID("msg_entre_prmconsulta");
	}

	public String editar() {
		setMatriculaVO((MatriculaVO) getRequestMap().get("matriculaItem"));
		return Uteis.getCaminhoRedirecionamentoNavegacao("logAuditMatriculaForm");
	}

	public void consultarLogMatricula() {
		try {
			if (getAno().equals("")) {
				throw new Exception("O campo Ano deve ser Informado.");
			}
			if (getMes().equals("")) {
				throw new Exception("O campo Mês deve ser Informado.");
			}
			if (getCodigoTransacao().equals(0)) {
				if (getOpcaoTabelaLogMatricula().name().equals(OpcaoTabelaLogMatriculaEnum.MATRICULA.name())) {
					if(!Uteis.isAtributoPreenchido(getMatriculaVO().getMatricula()) && !Uteis.isAtributoPreenchido(getUsuarioLogVO())) {
						throw new Exception("Deve ser informado pelo menos um dos filtros listados a seguir.[Matrícula ou Usuário]");
					}
				} else {
					if(!Uteis.isAtributoPreenchido(getMatriculaVO().getMatricula()) && !Uteis.isAtributoPreenchido(getUsuarioLogVO()) && !Uteis.isAtributoPreenchido(getTurmaVO().getCodigo())) {
						throw new Exception("Deve ser informado pelo menos um dos filtros listados a seguir.[Matrícula, Turma ou Usuário]");
					}
				}
			}
			setListaLogMatriculaVOs(getFacadeFactory().getLogMatriculaFacade().consultar(getMatriculaVO().getMatricula(), getSituacaoMatricula(), getOpcaoTabelaLogMatricula(), getAno(), getMes(), getAcao(), getColuna(), getDataInicio(), getDataFim(), getUsuarioLogVO(), true, getMatriculaCamposAlteradosVO(), getTurmaVO().getCodigo(), getAnoMatriculaPeriodo(), getSemestreMatriculaPeriodo(), getCodigoTransacao(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultarLogMatricula();
	}

	public String irPaginaConsulta() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("logMatriculaForm");
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

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public List<SelectItem> getListaMesAnoListSelectItem() {
		return MesAnoEnum.getListaSelectItemDescricaoMes();
	}
	
	public List<SelectItem> listaSelectItemAnoVOs;
	
	public void montarListaSelectItemAno() {
		List<String> listaAnoVOs = getFacadeFactory().getLogMatriculaFacade().consultarAnoAudit(getUsuarioLogado());
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
		List<String> listaMesVOs = getFacadeFactory().getLogMatriculaFacade().consultarMesAuditPorAno(getAno(), getUsuarioLogado());
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

	public List<AuditVO> getListaLogMatriculaVOs() {
		if (listaLogMatriculaVOs == null) {
			listaLogMatriculaVOs = new ArrayList<AuditVO>(0);
		}
		return listaLogMatriculaVOs;
	}

	public void setListaLogMatriculaVOs(List<AuditVO> listaLogMatriculaVOs) {
		this.listaLogMatriculaVOs = listaLogMatriculaVOs;
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
			listaSelectItemColunaVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemColunaVOs;
	}
	
	public void montarListaSelectItemColuna() {
		List<String> listaColunaVOs = getFacadeFactory().getLogMatriculaFacade().consultarColunasAuditMatricula(getOpcaoTabelaLogMatricula());
		listaSelectItemColunaVOs = new ArrayList<SelectItem>(0);
		listaSelectItemColunaVOs.add(new SelectItem("", ""));
		for (String coluna : listaColunaVOs) {
			listaSelectItemColunaVOs.add(new SelectItem(coluna, coluna));
		}
	}
	
	public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaAluno().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsultaAluno().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunoPorMatricula() throws Exception {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (!objAluno.getMatricula().equals("")) {
            	setMatriculaVO(objAluno);
            }
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            setListaConsultaAluno(new ArrayList(0));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() throws Exception {
        setMatriculaVO((MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens"));
        setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
        setCampoConsultaAluno("");
        setValorConsultaAluno("");
        setListaConsultaAluno(new ArrayList(0));
        setMensagemID("msg_dados_consultados");
    }

    public void limparDadosAluno() {
        setMatriculaVO(new MatriculaVO());
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();        
        itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Nome Curso"));
        return itens;
    }
    
    public void consultarTurma() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), 0, 0, 0, false, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        setTurmaVO(obj);
        obj = null;
        setValorConsultaTurma(null);
        setCampoConsultaTurma(null);
        Uteis.liberarListaMemoria(getListaConsultaTurma());
    }
    
    public void limparTurma() {
    	setTurmaVO(null);
    }
    
    
    private List<SelectItem> tipoConsultaComboTurma;

    public List getTipoConsultaComboTurma() {
        if (tipoConsultaComboTurma == null) {
            tipoConsultaComboTurma = new ArrayList(0);
            tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
        }
        return tipoConsultaComboTurma;
    }
	
	public List<SelectItem> getListaSelectItemSituacaoVinculoMatriculaVOs() {
		return SituacaoVinculoMatricula.getListaSelectItemSituacaoVinculoMatricula(true);
	}

	public SituacaoVinculoMatricula getSituacaoMatricula() {
		return situacaoMatricula;
	}

	public void setSituacaoMatricula(SituacaoVinculoMatricula situacaoMatricula) {
		this.situacaoMatricula = situacaoMatricula;
	}

	public MatriculaCamposAlteradosVO getMatriculaCamposAlteradosVO() {
		if (matriculaCamposAlteradosVO == null) {
			matriculaCamposAlteradosVO = new MatriculaCamposAlteradosVO();
		}
		return matriculaCamposAlteradosVO;
	}

	public void setMatriculaCamposAlteradosVO(MatriculaCamposAlteradosVO matriculaCamposAlteradosVO) {
		this.matriculaCamposAlteradosVO = matriculaCamposAlteradosVO;
	}

	public OpcaoTabelaLogMatriculaEnum getOpcaoTabelaLogMatricula() {
		if (opcaoTabelaLogMatricula == null) {
			opcaoTabelaLogMatricula = OpcaoTabelaLogMatriculaEnum.MATRICULA;
		}
		return opcaoTabelaLogMatricula;
	}

	public void setOpcaoTabelaLogMatricula(OpcaoTabelaLogMatriculaEnum opcaoTabelaLogMatricula) {
		this.opcaoTabelaLogMatricula = opcaoTabelaLogMatricula;
	}

	public Integer getCodigoMatriculaPeriodo() {
		if (codigoMatriculaPeriodo == null) {
			codigoMatriculaPeriodo = 0;
		}
		return codigoMatriculaPeriodo;
	}

	public void setCodigoMatriculaPeriodo(Integer codigoMatriculaPeriodo) {
		this.codigoMatriculaPeriodo = codigoMatriculaPeriodo;
	}
	
	public List<SelectItem> getListaSelectItemOpcaoTabelaMatriculaVOs() {
		return OpcaoTabelaLogMatriculaEnum.getListaSelectItemOpcaoTabelaLogMatricula();
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public String getAnoMatriculaPeriodo() {
		if (anoMatriculaPeriodo == null) {
			anoMatriculaPeriodo = "";
		}
		return anoMatriculaPeriodo;
	}

	public void setAnoMatriculaPeriodo(String anoMatriculaPeriodo) {
		this.anoMatriculaPeriodo = anoMatriculaPeriodo;
	}

	public String getSemestreMatriculaPeriodo() {
		if (semestreMatriculaPeriodo == null) {
			semestreMatriculaPeriodo = "";
		}
		return semestreMatriculaPeriodo;
	}

	public void setSemestreMatriculaPeriodo(String semestreMatriculaPeriodo) {
		this.semestreMatriculaPeriodo = semestreMatriculaPeriodo;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public Integer getCodigoTransacao() {
		if (codigoTransacao == null) {
			codigoTransacao = 0;
		}
		return codigoTransacao;
	}

	public void setCodigoTransacao(Integer codigoTransacao) {
		this.codigoTransacao = codigoTransacao;
	}
}
