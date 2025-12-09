package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.text.ParseException;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AniversariantesDoMesRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Controller("AniversariantesDoMesRelControle")
@Scope("viewScope")
@Lazy
public class AniversariantesDoMesRelControle extends SuperControleRelatorio {
    
	private static final long serialVersionUID = 1L;
	
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private List<TurmaVO> listaConsultaTurma;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private TurmaVO turma;
    private String mes;
    private String dia;
    private String diaFim;
    private String emails;
    private Integer codigoTipoPessoa;
    private Boolean acessaTurma;
    private Boolean funcionario;
    private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private LayoutEtiquetaVO layoutEtiquetaVO;
	private List<SelectItem> listaSelectItemlayoutEtiqueta;
	private List<SelectItem> listaSelectItemColuna;
	private List<SelectItem> listaSelectItemLinha;
	private Integer numeroCopias;
	private Integer coluna;
	private Integer linha;
	private Integer quantidadeDestinatarioNotificado;
	private Boolean enviarSms;
	private Boolean enviarEmail;
	private ComunicacaoInternaVO comunicacaoInternaVO;
	private Boolean manterModalAberto;
	private Boolean fecharModalGerarEtiqueta;

    public AniversariantesDoMesRelControle() throws Exception {
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List<AniversariantesDoMesRelVO> listaAlunos = null;
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AniversariantesDoMesRelControle", "Inicializando Geração de Relatório Aniversariantes Do Mês", "Emitindo Relatório");
            validarDados();
            listaAlunos = getFacadeFactory().getAniversariantesDoMesRelFacade().criarObjeto(getUnidadeEnsinoVO().getCodigo(), getTurma(), getMes(), getDia(), getDiaFim(), getUsuarioLogado(), getAcessaTurma(), getFuncionario(),getFiltroRelatorioAcademicoVO());
            if (!listaAlunos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAniversariantesDoMesRelFacade().getDesignIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAniversariantesDoMesRelFacade().getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Aniversariantes do Mês");
                getSuperParametroRelVO().setListaObjetos(listaAlunos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAniversariantesDoMesRelFacade().getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setQuantidade(listaAlunos.size());
                getSuperParametroRelVO().setTurma(getTurma().getIdentificadorTurma());
                getSuperParametroRelVO().setDataInicio("");
                getSuperParametroRelVO().setDataFim("");
                if (getAcessaTurma()) {
                    getSuperParametroRelVO().setProfessor("false");
                } else {
                    getSuperParametroRelVO().setProfessor("true");
                }
                if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
			setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
		}
                realizarImpressaoRelatorio(getSuperParametroRelVO());
                inicializarListasSelectItemTodosComboBox();
                setMensagemID("msg_relatorio_ok");

            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "AniversariantesDoMesRelControle", "Finalizando Geração de Relatório Aniversariantes Do Mês", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaAlunos);
        }
    }

    public void consultarEmails() {
        List<AniversariantesDoMesRelVO> listaAlunos = null;
        try {            
            setEmails("");
            validarDados();
            listaAlunos = getFacadeFactory().getAniversariantesDoMesRelFacade().criarObjeto(getUnidadeEnsinoVO().getCodigo(), getTurma(), getMes(), getDia(), getDiaFim(), getUsuarioLogado(), getAcessaTurma(), getFuncionario(),getFiltroRelatorioAcademicoVO());
            for (AniversariantesDoMesRelVO a : listaAlunos) {
                if (!a.getEmail().equals("")) {
                    setEmails(getEmails() + a.getEmail() + ";");
                }
                if (!a.getEmail2().equals("")) {
                    setEmails(getEmails() + a.getEmail2() + ";");
                }
            }
            setMensagemID("msg_relatorio_ok");
        } catch (Exception e) {
            setEmails("");
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            //Uteis.liberarListaMemoria(listaAlunos);
        }
    }

    public void validarDados() throws Exception {
        if (getDia().equals("")) {
            throw new Exception("O campo DIA deve ser informado.");
        }
        if (getDiaFim().equals("")) {
            throw new Exception("O campo DIA FIM deve ser informado.");
        }
    }

    public List<SelectItem> getListaSelectItemMes() {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        lista.add(new SelectItem("01", "Janeiro"));
        lista.add(new SelectItem("02", "Fevereiro"));
        lista.add(new SelectItem("03", "Março"));
        lista.add(new SelectItem("04", "Abril"));
        lista.add(new SelectItem("05", "Maio"));
        lista.add(new SelectItem("06", "Junho"));
        lista.add(new SelectItem("07", "Julho"));
        lista.add(new SelectItem("08", "Agosto"));
        lista.add(new SelectItem("09", "Setembro"));
        lista.add(new SelectItem("10", "Outubro"));
        lista.add(new SelectItem("11", "Novembro"));
        lista.add(new SelectItem("12", "Dezembro"));
        return lista;
    }

    public List<SelectItem> getListaSelectItemTipoPessoa() {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        lista.add(new SelectItem(1, "Aluno"));
        lista.add(new SelectItem(2, "Professor"));
        lista.add(new SelectItem(3, "Funcionário"));
        return lista;
    }

    public String consultarTurma() {
        try {
            super.consultar();
            if (getUnidadeEnsinoVO().getCodigo() == 0) {
                throw new Exception("Selecione a Unidade de Ensino.");
            }
            List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(),
                        getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false,
                        false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeCurso")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeTurno")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList<TurmaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }

    }

    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        getTurma().setCodigo(obj.getCodigo());
        getTurma().setIdentificadorTurma(obj.getIdentificadorTurma());
        getTurma().setCurso(obj.getCurso());
        setCampoConsultaTurma("");
        setValorConsultaTurma("");
        setListaConsultaTurma(null);
    }

    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void limparDadosTurma() {
    	setTurma(null);
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
            removerObjetoMemoria(resultadoConsulta);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
                super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /**
     * @return the listaConsultaTurma
     */
    public List<TurmaVO> getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList<TurmaVO>(0);
        }
        return listaConsultaTurma;
    }

    /**
     * @param listaConsultaTurma
     *            the listaConsultaTurma to set
     */
    public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    /**
     * @return the valorConsultaTurma
     */
    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    /**
     * @param valorConsultaTurma
     *            the valorConsultaTurma to set
     */
    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    /**
     * @return the campoConsultaTurma
     */
    public String getCampoConsultaTurma() {
        if (campoConsultaTurma == null) {
            campoConsultaTurma = "";
        }
        return campoConsultaTurma;
    }

    /**
     * @param campoConsultaTurma
     *            the campoConsultaTurma to set
     */
    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    /**
     * @return the unidadeEnsinoVO
     */
    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    /**
     * @param unidadeEnsinoVO
     *            the unidadeEnsinoVO to set
     */
    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getMes() {
        if (mes == null) {
            mes = String.valueOf(Uteis.getMesDataAtual());
        }
        return mes;
    }

    /**
     * @return the emails
     */
    public String getEmails() {
        if (emails == null) {
            emails = "";
        }
        return emails;
    }

    /**
     * @param emails the emails to set
     */
    public void setEmails(String emails) {
        this.emails = emails;
    }

    /**
     * @return the dia
     */
    public String getDia() {
        if (dia == null) {
            dia = String.valueOf(Uteis.getDiaMesData(Uteis.getDataPrimeiroDiaMes(new Date())));
        }
        return dia;
    }

    /**
     * @param dia the dia to set
     */
    public void setDia(String dia) {
        this.dia = dia;
    }

    /**
     * @return the diaFim
     */
    public String getDiaFim() {
        if (diaFim == null) {
            diaFim = String.valueOf(Uteis.getDiaMesData(Uteis.getDataUltimoDiaMes(new Date())));
        }
        return diaFim;
    }

    /**
     * @param diaFim the diaFim to set
     */
    public void setDiaFim(String diaFim) {
        this.diaFim = diaFim;
    }

    public Integer getCodigoTipoPessoa() {
        if (codigoTipoPessoa == null) {
            codigoTipoPessoa = 1;
        }
        return codigoTipoPessoa;
    }

    public void setCodigoTipoPessoa(Integer codigoTipoPessoa) {
        this.codigoTipoPessoa = codigoTipoPessoa;
    }

    public Boolean getAcessaTurma() {
        if (acessaTurma == null) {
            acessaTurma = true;
        }
        return acessaTurma;
    }

    public void setAcessaTurma(Boolean acessaTurma) {
        this.acessaTurma = acessaTurma;
    }

    public void verificarSituacaoTurma() {
        if (getCodigoTipoPessoa().intValue() == 3) {
            setFuncionario(true);
            setAcessaTurma(false);
        } else if (getCodigoTipoPessoa().intValue() == 2) {
            setFuncionario(false);
            setAcessaTurma(false);
        } else {
            setFuncionario(false);
            setAcessaTurma(true);
        }
    }

    public void verificarSituacaoFuncionario() {
        if (getCodigoTipoPessoa().intValue() == 3) {
            setFuncionario(true);
        } else {
            setFuncionario(false);
        }
    }

    /**
     * @return the funcionario
     */
    public Boolean getFuncionario() {
        if (funcionario == null) {
            funcionario = false;
        }
        return funcionario;
    }

    /**
     * @param funcionario the funcionario to set
     */
    public void setFuncionario(Boolean funcionario) {
        this.funcionario = funcionario;
    }
    
    public boolean getIsApresentarFiltroFormacaoAcademica() {
		return getCodigoTipoPessoa() == 1 ? true : false;
	}
	
	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}
	
	public String obterUltimoDiaMesConformeMesInformado() throws ParseException, java.text.ParseException{
		   StringBuilder dataPrm = new StringBuilder();
		   dataPrm.append(getDia()).append("/").append(getMes()).append("/").append(UteisData.getAnoDataAtual());
           setDiaFim(String.valueOf(Uteis.getDiaMesData(Uteis.getDataUltimoDiaMes(Uteis.getData(dataPrm.toString(), "dd/MM/yyyy")))));
		return "";
	}
	public void consultarLayoutEtiquetaPorModulo() {
		try {
			getListaSelectItemlayoutEtiqueta().clear();
			List<LayoutEtiquetaVO> layoutEtiquetaVOs = getFacadeFactory().getLayoutEtiquetaFacade().consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum.ANIVERSARIANTE, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			listaSelectItemlayoutEtiqueta.add(new SelectItem(0, ""));
			for (LayoutEtiquetaVO layoutEtiquetaVO : layoutEtiquetaVOs) {
				listaSelectItemlayoutEtiqueta.add(new SelectItem(layoutEtiquetaVO.getCodigo(), layoutEtiquetaVO.getDescricao()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public LayoutEtiquetaVO getLayoutEtiquetaVO() {
		if (layoutEtiquetaVO == null) {
			layoutEtiquetaVO = new LayoutEtiquetaVO();
		}
		return layoutEtiquetaVO;
	}

	public void setLayoutEtiquetaVO(LayoutEtiquetaVO layoutEtiquetaVO) {
		this.layoutEtiquetaVO = layoutEtiquetaVO;
	}

	public List<SelectItem> getListaSelectItemlayoutEtiqueta() {
		if (listaSelectItemlayoutEtiqueta == null) {
			listaSelectItemlayoutEtiqueta = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemlayoutEtiqueta;
	}

	public void setListaSelectItemlayoutEtiqueta(List<SelectItem> listaSelectItemlayoutEtiqueta) {
		this.listaSelectItemlayoutEtiqueta = listaSelectItemlayoutEtiqueta;
	}

	public List<SelectItem> getListaSelectItemColuna() {
		if (listaSelectItemColuna == null) {
			listaSelectItemColuna = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemColuna;
	}

	public void setListaSelectItemColuna(List<SelectItem> listaSelectItemColuna) {
		this.listaSelectItemColuna = listaSelectItemColuna;
	}

	public List<SelectItem> getListaSelectItemLinha() {
		if (listaSelectItemLinha == null) {
			listaSelectItemLinha = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemLinha;
	}

	public void setListaSelectItemLinha(List<SelectItem> listaSelectItemLinha) {
		this.listaSelectItemLinha = listaSelectItemLinha;
	}

	public Integer getNumeroCopias() {
		if(numeroCopias == null){
			numeroCopias = 1;
		}
		return numeroCopias;
	}

	public void setNumeroCopias(Integer numeroCopias) {
		this.numeroCopias = numeroCopias;
	}

	public Integer getColuna() {
		if(coluna == null){
			coluna = 1;
		}
		return coluna;
	}

	public void setColuna(Integer coluna) {
		this.coluna = coluna;
	}

	public Integer getLinha() {
		if(linha == null){
			linha = 1;
		}
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}
   
	
	public void inicializarDadosLayoutEtiqueta() {
		try {
			getListaSelectItemColuna().clear();
			getListaSelectItemLinha().clear();
			if (getLayoutEtiquetaVO().getCodigo() > 0) {
				setLayoutEtiquetaVO(getFacadeFactory().getLayoutEtiquetaFacade().consultarPorChavePrimaria(getLayoutEtiquetaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
				for (int x = 1; x <= getLayoutEtiquetaVO().getNumeroLinhasEtiqueta(); x++) {
					getListaSelectItemLinha().add(new SelectItem(x, String.valueOf(x)));
				}
				for (int y = 1; y <= getLayoutEtiquetaVO().getNumeroColunasEtiqueta(); y++) {
					getListaSelectItemColuna().add(new SelectItem(y, String.valueOf(y)));
				}
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarGeracaoEtiqueta() {
		setFazerDownload(false);
		setFecharModalGerarEtiqueta(false);
		try {
			if (!Uteis.isAtributoPreenchido(getLayoutEtiquetaVO().getCodigo())) {
				throw new Exception("Selecione Um Layout de Impressão");
			}
        	setCaminhoRelatorio(getFacadeFactory().getEtiquetaAniversarianteRelFacade().realizarImpressaoEtiqueta(getUnidadeEnsinoVO().getCodigo(), getTurma(), getMes(), getDia(), getDiaFim(), getAcessaTurma(), getFuncionario(), getLayoutEtiquetaVO(), getFiltroRelatorioAcademicoVO(), getNumeroCopias(), getLinha(), getColuna(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			super.setFazerDownload(true);
            this.limparDadosGeracaoEtiqueta();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparDadosGeracaoEtiqueta() {
		setListaSelectItemlayoutEtiqueta(null);
		setLayoutEtiquetaVO(null);
		setMensagemDetalhada(null);
		setFecharModalGerarEtiqueta(true);
	}

	public Integer getQuantidadeDestinatarioNotificado() {
		if(quantidadeDestinatarioNotificado == null){
			quantidadeDestinatarioNotificado = 0;
		}
		return quantidadeDestinatarioNotificado;
	}

	public void setQuantidadeDestinatarioNotificado(Integer quantidadeDestinatarioNotificado) {
		this.quantidadeDestinatarioNotificado = quantidadeDestinatarioNotificado;
	}
	
	public Boolean getEnviarSms() {
		if(enviarSms == null){
			enviarSms = false;
		}
		return enviarSms;
	}

	public void setEnviarSms(Boolean enviarSms) {
		this.enviarSms = enviarSms;
	}

	public Boolean getEnviarEmail() {
		if(enviarEmail == null){
			enviarEmail = false;
		}
		return enviarEmail;
	}

	public void setEnviarEmail(Boolean enviarEmail) {
		this.enviarEmail = enviarEmail;
	}
	
	public Boolean getManterModalAberto() {
		if (manterModalAberto == null) {
			manterModalAberto = Boolean.FALSE;
		}
		return manterModalAberto;
	}

	public void setManterModalAberto(Boolean manterModalAberto) {
		this.manterModalAberto = manterModalAberto;
	}

	public void realizarConsultaQuantidadeDestinatarioNotificado() throws Exception {
		setQuantidadeDestinatarioNotificado(getFacadeFactory().getAniversariantesDoMesRelFacade().consultarNumeroDestinatario(getUnidadeEnsinoVO().getCodigo(), getTurma(), getMes(), getDia(), getDiaFim(), getUsuarioLogado(), getAcessaTurma(), getFuncionario(),getFiltroRelatorioAcademicoVO()));
		if(getQuantidadeDestinatarioNotificado() > 0){
			setManterModalAberto(Boolean.TRUE);
		}else{
			setManterModalAberto(Boolean.FALSE);
			setMensagemID("msg_relatorio_sem_dados");
		}
	}

	public ComunicacaoInternaVO getComunicacaoInternaVO() {
		if(comunicacaoInternaVO == null){
			comunicacaoInternaVO = new ComunicacaoInternaVO();
		}
		return comunicacaoInternaVO;
	}

	public void setComunicacaoInternaVO(ComunicacaoInternaVO comunicacaoInternaVO) {
		this.comunicacaoInternaVO = comunicacaoInternaVO;
	}
	
	
	public String getShowHideModalComunicado() {
		if (getManterModalAberto()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			try {
				PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getAniversariantesDoMesRelFacade().carregarDadosMensagemPersonalizada(getUsuarioLogadoClone());
				if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
					comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
					comunicacaoEnviar.setMensagem(mensagemTemplate.getMensagem());
					if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
						comunicacaoEnviar.setMensagemSMS(mensagemTemplate.getMensagemSMS());
						setEnviarSms(Boolean.TRUE);
					} 
				} else {
					comunicacaoEnviar.setAssunto("Feliz Aniversário");
					getComunicacaoInternaVO().setMensagem(getComunicacaoInternaVO().getMensagemComLayout(getConfiguracaoGeralPadraoSistema().getTextoComunicacaoInterna()));
				}
				if(Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())){
					comunicacaoEnviar.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				} 
				getFacadeFactory().getComunicacaoInternaFacade().realizarTrocarLogoEmailPorUnidadeEnsino(comunicacaoEnviar, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				setComunicacaoInternaVO(comunicacaoEnviar);
			} catch(Exception e) {
				setComunicacaoInternaVO(comunicacaoEnviar);				
			}
			return "RichFaces.$('panelEnvioEmailSms').show()";
		}
		setEnviarEmail(Boolean.FALSE);
		setEnviarSms(Boolean.FALSE);
		return "RichFaces.$('panelEnvioEmailSms').hide()";
	}
	
	public String getIsRenderizarFormularioModalEmailSms() {
		if (!getManterModalAberto()) {
			return "formEnvioEmailSms, form";
		}
		return "formEnvioEmailSms:mensagemAniversariante";
	}
	
	
	public void realizarEnvioComunicadoInternoAniversariante(){
		List<AniversariantesDoMesRelVO> listaObjetos = new ArrayList<AniversariantesDoMesRelVO>(0);
		try {
			if (getComunicacaoInternaVO().getMensagemSMS().length() > 150 && getEnviarSms()) {
				throw new Exception(getMensagemInternalizacao("msg_LimiteCampoTextoSms"));
			}
			if (getComunicacaoInternaVO().getMensagemSMS().isEmpty() && getEnviarSms()) {
				throw new Exception(getMensagemInternalizacao("msg_EnviarSmsTextoVazio"));
			}
			listaObjetos = getFacadeFactory().getAniversariantesDoMesRelFacade().criarObjeto(getUnidadeEnsinoVO().getCodigo(), getTurma(), getMes(), getDia(), getDiaFim(), getUsuarioLogado(), getAcessaTurma(), getFuncionario(),getFiltroRelatorioAcademicoVO());			
			if(!listaObjetos.isEmpty()){
				getFacadeFactory().getAniversariantesDoMesRelFacade().executarEnvioComunicadoInternoAniversariante(listaObjetos, getComunicacaoInternaVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getEnviarSms(), getEnviarEmail());
				setMensagemID("msg_msg_enviados");
				setManterModalAberto(Boolean.FALSE);
			}else{
				setManterModalAberto(Boolean.TRUE);
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setManterModalAberto(Boolean.TRUE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}

	public Boolean getFecharModalGerarEtiqueta() {
		if (fecharModalGerarEtiqueta == null) {
			fecharModalGerarEtiqueta = Boolean.FALSE;
		}
		return fecharModalGerarEtiqueta;
	}

	public void setFecharModalGerarEtiqueta(Boolean fecharModalGerarEtiqueta) {
		this.fecharModalGerarEtiqueta = fecharModalGerarEtiqueta;
	}

	public String getIsFecharModalGeracaoEtiqueta() {
		if (getFecharModalGerarEtiqueta()) {
			return "#{rich:component('panelImprimirEtiqueta')}.hide()";
		}
		return "";
	}
}
