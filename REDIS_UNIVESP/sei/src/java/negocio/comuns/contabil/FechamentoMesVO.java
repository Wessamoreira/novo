package negocio.comuns.contabil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.utilitarias.Uteis;

public class FechamentoMesVO extends SuperVO {

    protected Integer codigo;
    protected Integer mes;
    protected Integer ano;
    protected Boolean fechado;
    protected Date dataFechamento;
    protected UsuarioVO usuario;
    protected String observacao;
    protected Boolean gerarFechamentoMesTodasUnidades;
    protected List<FechamentoMesUnidadeEnsinoVO> listaUnidadesEnsinoVOs;
    protected List<FechamentoMesContaCaixaVO> listaContaCaixaVOs;
    protected List<FechamentoMesHistoricoModificacaoVO> listaHistoricoModificacaoVOs;
    protected String nomeUnidadesEnsinos;
    protected Boolean politicaFechamentoMesPadrao;
    protected Boolean fecharParcialmenteMes;
    protected Date dataInicioFechamentoMes;
    protected Date dataFimFechamentoMes;
    protected Boolean fecharCompetenciaUltimoDiaMes;
    protected Boolean fecharCompetenciaDiaEspecifico;
    protected Integer diaEspecificoFechaCompetencia;
    /**
     * Utilizado para controlar quantas competencias deverão ficar em aberto 
     * (permitindo a edicao dos dados financeiros) antes da mesma ser fechada
     * automaticamente. Por exemplo, se informar 2 e a dia do fechamento automatico
     * for dia 30. Quando estivemos no dia 30/06, a rotina irá fechar dois meses para
     * trás, ou seja, o sistema irá fechar e bloquear alterações no mês 04/18.
     * Se este campo for zero, então, o fechamento automatico será no mês corrente.
     */
    protected Integer nrMesesManterAbertoAntesFechamentoAutomatico;
     
    protected Boolean gerarPrevisaoReceitaCompetenciaAutomatimente;
    protected Boolean gerarPrevisaoReceitaCompetenciaUltimoDiaMes;
    protected Boolean gerarPrevisaoReceitaCompetenciaDiaEspecifico;
    protected Integer diaEspecificoGerarPrevisaoReceitaCompetencia;
    protected Boolean bloquearInclusaoAlteracaoContaReceber;
    protected Boolean bloquearRecebimentoContaReceber;
    protected Boolean bloquearInclusaoAlteracaoContaPagar;
    protected Boolean bloquearPagamentoContaPagar;
    protected Boolean bloquearInclusaoAlteracaoNotaFiscalEntrada;
    protected Boolean bloquearInclusaoAlteracaoNotaFiscalSaida;
    protected Boolean bloquearMovimentacoesFinanceiras;
    protected Boolean bloquearAberturaTodasContaCaixa;
    protected Boolean bloquearAberturaContasCaixasEspecificas;
    protected String dataUtilizarVerificarBloqueioContaPagar;
    protected String dataUtilizarVerificarBloqueioContaReceber;

    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>fechamentoMes</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public FechamentoMesVO() {
        super();
        inicializarDados();
    }


    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        
    }

    public Boolean getFechado() {
    	if (fechado == null) { 
    		fechado = Boolean.FALSE;
    	}
        return (fechado);
    }

    public Boolean isFechado() {
        if (fechado == null) {
            fechado = Boolean.FALSE;
        }
        return (fechado);
    }

    public void setFechado(Boolean fechado) {
        this.fechado = fechado;
    }
    
    public String getAno_Apresentar() {
    	String anoStr = getAno().toString();
    	return anoStr;
    }

    public Integer getAno() {
        if (ano == null) {
            ano = Uteis.getAnoData(new Date());
        }
        return (ano);
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }
    
    public String getMes_Apresentar() {
    	String mesStr = getMes().toString();
    	if (mesStr.length() == 1) {
    		mesStr = "0" + mesStr;
    	}
    	return mesStr;
    }

    public Integer getMes() {
        if (mes == null) {
            mes = Uteis.getMesData(new Date());
        }
        return (mes);
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

	public Date getDataFechamento() {
		return dataFechamento;
	}
	
	public String getDataFechamento_Apresentar() {
		if (dataFechamento == null) {
			return "";
		}
		return (Uteis.getDataComHora(dataFechamento));
	}	

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public UsuarioVO getUsuario() {
		if (usuario == null) { 
			usuario = new UsuarioVO();
		}
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public List<FechamentoMesUnidadeEnsinoVO> getListaUnidadesEnsinoVOs() {
		if (listaUnidadesEnsinoVOs == null) { 
			listaUnidadesEnsinoVOs = new ArrayList<FechamentoMesUnidadeEnsinoVO>();
		}
		return listaUnidadesEnsinoVOs;
	}

	public void setListaUnidadesEnsinoVOs(List<FechamentoMesUnidadeEnsinoVO> listaUnidadesEnsinoVOs) {
		this.listaUnidadesEnsinoVOs = listaUnidadesEnsinoVOs;
	}

	public String getObservacao() {
		if (observacao == null) { 
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Boolean getPoliticaFechamentoMesPadrao() {
		if (politicaFechamentoMesPadrao == null) { 
			politicaFechamentoMesPadrao = Boolean.FALSE;
		}
		return politicaFechamentoMesPadrao;
	}

	public void setPoliticaFechamentoMesPadrao(Boolean politicaFechamentoMesPadrao) {
		this.politicaFechamentoMesPadrao = politicaFechamentoMesPadrao;
	}

	public Date getDataInicioFechamentoMes() {
		if (dataInicioFechamentoMes == null) { 
			dataInicioFechamentoMes = new Date();
		}		
		return dataInicioFechamentoMes;
	}
	
	public String getDataInicioFechamentoMes_Apresentar() {
		if (dataInicioFechamentoMes == null) {
			return "";
		}
		return (Uteis.getData(dataInicioFechamentoMes));
	}		

	public void setDataInicioFechamentoMes(Date dataInicioFechamentoMes) {
		this.dataInicioFechamentoMes = dataInicioFechamentoMes;
	}

	public Date getDataFimFechamentoMes() {
		if (dataFimFechamentoMes == null) { 
			dataFimFechamentoMes = new Date();
		}
		return dataFimFechamentoMes;
	}
	
	public String getDataFimFechamentoMes_Apresentar() {
		if (dataFimFechamentoMes == null) {
			return "";
		}
		return (Uteis.getData(dataFimFechamentoMes));
	}

	public void setDataFimFechamentoMes(Date dataFimFechamentoMes) {
		this.dataFimFechamentoMes = dataFimFechamentoMes;
	}

	public Boolean getFecharParcialmenteMes() {
		if (fecharParcialmenteMes == null) { 
			fecharParcialmenteMes = Boolean.FALSE;
		}
		return fecharParcialmenteMes;
	}

	public void setFecharParcialmenteMes(Boolean fecharParcialmenteMes) {
		this.fecharParcialmenteMes = fecharParcialmenteMes;
	}

	public Boolean getFecharCompetenciaUltimoDiaMes() {
		if (fecharCompetenciaUltimoDiaMes == null) { 
			fecharCompetenciaUltimoDiaMes = Boolean.TRUE;
		}
		return fecharCompetenciaUltimoDiaMes;
	}

	public void setFecharCompetenciaUltimoDiaMes(Boolean fecharCompetenciaUltimoDiaMes) {
		this.fecharCompetenciaUltimoDiaMes = fecharCompetenciaUltimoDiaMes;
	}

	public Boolean getFecharCompetenciaDiaEspecifico() {
		if (fecharCompetenciaDiaEspecifico == null) { 
			fecharCompetenciaDiaEspecifico = Boolean.FALSE;
		}
		return fecharCompetenciaDiaEspecifico;
	}

	public void setFecharCompetenciaDiaEspecifico(Boolean fecharCompetenciaDiaEspecifico) {
		this.fecharCompetenciaDiaEspecifico = fecharCompetenciaDiaEspecifico;
	}

	public Integer getDiaEspecificoFechaCompetencia() {
		if (diaEspecificoFechaCompetencia == null) { 
			diaEspecificoFechaCompetencia = 0;
		}
		return diaEspecificoFechaCompetencia;
	}

	public void setDiaEspecificoFechaCompetencia(Integer diaEspecificoFechaCompetencia) {
		this.diaEspecificoFechaCompetencia = diaEspecificoFechaCompetencia;
	}

	public Integer getNrMesesManterAbertoAntesFechamentoAutomatico() {
		if (nrMesesManterAbertoAntesFechamentoAutomatico == null) { 
			nrMesesManterAbertoAntesFechamentoAutomatico = 1;
		}			
		return nrMesesManterAbertoAntesFechamentoAutomatico;
	}

	public void setNrMesesManterAbertoAntesFechamentoAutomatico(Integer nrMesesManterAbertoAntesFechamentoAutomatico) {
		this.nrMesesManterAbertoAntesFechamentoAutomatico = nrMesesManterAbertoAntesFechamentoAutomatico;
	}

	public Boolean getGerarPrevisaoReceitaCompetenciaAutomatimente() {
		if (gerarPrevisaoReceitaCompetenciaAutomatimente == null) { 
			gerarPrevisaoReceitaCompetenciaAutomatimente = Boolean.TRUE;
		}		
		return gerarPrevisaoReceitaCompetenciaAutomatimente;
	}

	public void setGerarPrevisaoReceitaCompetenciaAutomatimente(Boolean gerarPrevisaoReceitaCompetenciaAutomatimente) {
		this.gerarPrevisaoReceitaCompetenciaAutomatimente = gerarPrevisaoReceitaCompetenciaAutomatimente;
	}

	public Boolean getGerarPrevisaoReceitaCompetenciaUltimoDiaMes() {
		if (gerarPrevisaoReceitaCompetenciaUltimoDiaMes == null) { 
			gerarPrevisaoReceitaCompetenciaUltimoDiaMes = Boolean.TRUE;
		}			
		return gerarPrevisaoReceitaCompetenciaUltimoDiaMes;
	}

	public void setGerarPrevisaoReceitaCompetenciaUltimoDiaMes(Boolean gerarPrevisaoReceitaCompetenciaUltimoDiaMes) {
		this.gerarPrevisaoReceitaCompetenciaUltimoDiaMes = gerarPrevisaoReceitaCompetenciaUltimoDiaMes;
	}

	public Boolean getGerarPrevisaoReceitaCompetenciaDiaEspecifico() {
		if (gerarPrevisaoReceitaCompetenciaDiaEspecifico == null) { 
			gerarPrevisaoReceitaCompetenciaDiaEspecifico = Boolean.FALSE;
		}		
		return gerarPrevisaoReceitaCompetenciaDiaEspecifico;
	}

	public void setGerarPrevisaoReceitaCompetenciaDiaEspecifico(Boolean gerarPrevisaoReceitaCompetenciaDiaEspecifico) {
		this.gerarPrevisaoReceitaCompetenciaDiaEspecifico = gerarPrevisaoReceitaCompetenciaDiaEspecifico;
	}

	public Integer getDiaEspecificoGerarPrevisaoReceitaCompetencia() {
		if (diaEspecificoGerarPrevisaoReceitaCompetencia == null) { 
			diaEspecificoGerarPrevisaoReceitaCompetencia = 0;
		}			
		return diaEspecificoGerarPrevisaoReceitaCompetencia;
	}

	public void setDiaEspecificoGerarPrevisaoReceitaCompetencia(Integer diaEspecificoGerarPrevisaoReceitaCompetencia) {
		this.diaEspecificoGerarPrevisaoReceitaCompetencia = diaEspecificoGerarPrevisaoReceitaCompetencia;
	}

	public Boolean getBloquearInclusaoAlteracaoContaReceber() {
		if (bloquearInclusaoAlteracaoContaReceber == null) { 
			bloquearInclusaoAlteracaoContaReceber = Boolean.TRUE;
		}				
		return bloquearInclusaoAlteracaoContaReceber;
	}

	public void setBloquearInclusaoAlteracaoContaReceber(Boolean bloquearInclusaoAlteracaoContaReceber) {
		this.bloquearInclusaoAlteracaoContaReceber = bloquearInclusaoAlteracaoContaReceber;
	}

	public Boolean getBloquearRecebimentoContaReceber() {
		if (bloquearRecebimentoContaReceber == null) { 
			bloquearRecebimentoContaReceber = Boolean.TRUE;
		}			
		return bloquearRecebimentoContaReceber;
	}

	public void setBloquearRecebimentoContaReceber(Boolean bloquearRecebimentoContaReceber) {
		this.bloquearRecebimentoContaReceber = bloquearRecebimentoContaReceber;
	}

	public Boolean getBloquearInclusaoAlteracaoContaPagar() {
		if (bloquearInclusaoAlteracaoContaPagar == null) { 
			bloquearInclusaoAlteracaoContaPagar = Boolean.TRUE;
		}			
		return bloquearInclusaoAlteracaoContaPagar;
	}

	public void setBloquearInclusaoAlteracaoContaPagar(Boolean bloquearInclusaoAlteracaoContaPagar) {
		this.bloquearInclusaoAlteracaoContaPagar = bloquearInclusaoAlteracaoContaPagar;
	}

	public Boolean getBloquearPagamentoContaPagar() {
		if (bloquearPagamentoContaPagar == null) { 
			bloquearPagamentoContaPagar = Boolean.TRUE;
		}		
		return bloquearPagamentoContaPagar;
	}

	public void setBloquearPagamentoContaPagar(Boolean bloquearPagamentoContaPagar) {
		this.bloquearPagamentoContaPagar = bloquearPagamentoContaPagar;
	}

	public Boolean getBloquearInclusaoAlteracaoNotaFiscalEntrada() {
		if (bloquearInclusaoAlteracaoNotaFiscalEntrada == null) { 
			bloquearInclusaoAlteracaoNotaFiscalEntrada = Boolean.TRUE;
		}			
		return bloquearInclusaoAlteracaoNotaFiscalEntrada;
	}

	public void setBloquearInclusaoAlteracaoNotaFiscalEntrada(Boolean bloquearInclusaoAlteracaoNotaFiscalEntrada) {
		this.bloquearInclusaoAlteracaoNotaFiscalEntrada = bloquearInclusaoAlteracaoNotaFiscalEntrada;
	}

	public Boolean getBloquearInclusaoAlteracaoNotaFiscalSaida() {
		if (bloquearInclusaoAlteracaoNotaFiscalSaida == null) { 
			bloquearInclusaoAlteracaoNotaFiscalSaida = Boolean.TRUE;
		}			
		return bloquearInclusaoAlteracaoNotaFiscalSaida;
	}

	public void setBloquearInclusaoAlteracaoNotaFiscalSaida(Boolean bloquearInclusaoAlteracaoNotaFiscalSaida) {
		this.bloquearInclusaoAlteracaoNotaFiscalSaida = bloquearInclusaoAlteracaoNotaFiscalSaida;
	}

	public Boolean getBloquearMovimentacoesFinanceiras() {
		if (bloquearMovimentacoesFinanceiras == null) { 
			bloquearMovimentacoesFinanceiras = Boolean.TRUE;
		}		
		return bloquearMovimentacoesFinanceiras;
	}

	public void setBloquearMovimentacoesFinanceiras(Boolean bloquearMovimentacoesFinanceiras) {
		this.bloquearMovimentacoesFinanceiras = bloquearMovimentacoesFinanceiras;
	}

	public Boolean getBloquearAberturaTodasContaCaixa() {
		if (bloquearAberturaTodasContaCaixa == null) { 
			bloquearAberturaTodasContaCaixa = Boolean.TRUE;
		}		
		return bloquearAberturaTodasContaCaixa;
	}

	public void setBloquearAberturaTodasContaCaixa(Boolean bloquearAberturaTodasContaCaixa) {
		this.bloquearAberturaTodasContaCaixa = bloquearAberturaTodasContaCaixa;
	}

	public Boolean getBloquearAberturaContasCaixasEspecificas() {
		if (bloquearAberturaContasCaixasEspecificas == null) { 
			bloquearAberturaContasCaixasEspecificas = Boolean.FALSE;
		}
		return bloquearAberturaContasCaixasEspecificas;
	}

	public void setBloquearAberturaContasCaixasEspecificas(Boolean bloquearAberturaContasCaixasEspecificas) {
		this.bloquearAberturaContasCaixasEspecificas = bloquearAberturaContasCaixasEspecificas;
	}

	public List<FechamentoMesContaCaixaVO> getListaContaCaixaVOs() {
		if (listaContaCaixaVOs == null) { 
			listaContaCaixaVOs = new ArrayList<FechamentoMesContaCaixaVO>();
		}
		return listaContaCaixaVOs;
	}

	public void setListaContaCaixaVOs(List<FechamentoMesContaCaixaVO> listaContaCaixaVOs) {
		this.listaContaCaixaVOs = listaContaCaixaVOs;
	}
	 
	public List<FechamentoMesHistoricoModificacaoVO> getListaHistoricoModificacaoVOs() {
		if (listaHistoricoModificacaoVOs == null) { 
			listaHistoricoModificacaoVOs = new ArrayList<FechamentoMesHistoricoModificacaoVO>();
		}
		return listaHistoricoModificacaoVOs;
	}

	public void setListaHistoricoModificacaoVOs(List<FechamentoMesHistoricoModificacaoVO> listaHistoricoModificacaoVOs) {
		this.listaHistoricoModificacaoVOs = listaHistoricoModificacaoVOs;
	}	
	
	public String getCompetencia() {
		if (getPoliticaFechamentoMesPadrao()) {
			return "Cfg Padrão";
		} 
		if (getAno() > 0) {
			return getMes_Apresentar() + "/" + getAno_Apresentar();
		}
		return "";
	}
	
	public String getSituacao() {
		if (getPoliticaFechamentoMesPadrao()) { 
			return "";
		}
		if (getFechado()) {
			return "Fechado";
		} else {
			return "Não Fechado";
		}
	}
	
	public String getNomeUnidadesEnsinos() {
		if (nomeUnidadesEnsinos == null) {
			StringBuilder str = new StringBuilder();
			String virgula = "";
			for (FechamentoMesUnidadeEnsinoVO obj : getListaUnidadesEnsinoVOs()) {
				str.append(virgula);
				str.append(obj.getUnidadeEnsino().getAbreviatura());
				virgula = ", ";
			}
			nomeUnidadesEnsinos = str.toString();
			return nomeUnidadesEnsinos;
		} else {
			return nomeUnidadesEnsinos;
		}
	}

	public Boolean getGerarFechamentoMesTodasUnidades() {
		if (gerarFechamentoMesTodasUnidades == null) { 
			gerarFechamentoMesTodasUnidades = Boolean.TRUE;
		}
		return gerarFechamentoMesTodasUnidades;
	}

	public void setGerarFechamentoMesTodasUnidades(Boolean gerarFechamentoMesTodasUnidades) {
		this.gerarFechamentoMesTodasUnidades = gerarFechamentoMesTodasUnidades;
	}
	
	public String getDataUtilizarVerificarBloqueioContaPagar_Apresentar() {
		if (getDataUtilizarVerificarBloqueioContaPagar().equals("DV")) { 
			return "Data Vencimento";
		}
		if (getDataUtilizarVerificarBloqueioContaPagar().equals("DC")) {
			return "Data Competência";
		}
		return "";
	}

	public String getDataUtilizarVerificarBloqueioContaPagar() {
		if (dataUtilizarVerificarBloqueioContaPagar == null) { 
			dataUtilizarVerificarBloqueioContaPagar = "DV";
		}
		return dataUtilizarVerificarBloqueioContaPagar;
	}


	public void setDataUtilizarVerificarBloqueioContaPagar(String dataUtilizarVerificarBloqueioContaPagar) {
		this.dataUtilizarVerificarBloqueioContaPagar = dataUtilizarVerificarBloqueioContaPagar;
	}


	public String getDataUtilizarVerificarBloqueioContaReceber() {
		if (dataUtilizarVerificarBloqueioContaReceber == null) { 
			dataUtilizarVerificarBloqueioContaReceber = "DV";
		}
		return dataUtilizarVerificarBloqueioContaReceber;
	}
	
	public String getDataUtilizarVerificarBloqueioContaReceber_Apresentar() {
		if (getDataUtilizarVerificarBloqueioContaReceber().equals("DV")) { 
			return "Data Vencimento";
		}
		if (getDataUtilizarVerificarBloqueioContaReceber().equals("DC")) {
			return "Data Competência";
		}
		return "";
	}	

	public void setDataUtilizarVerificarBloqueioContaReceber(String dataUtilizarVerificarBloqueioContaReceber) {
		this.dataUtilizarVerificarBloqueioContaReceber = dataUtilizarVerificarBloqueioContaReceber;
	}
	
	public String getCompletoDescricaoDataUtilizadaVerificacao(TipoOrigemHistoricoBloqueioEnum tipoOrigemHistoricoBloqueio) {
		if (tipoOrigemHistoricoBloqueio.equals(TipoOrigemHistoricoBloqueioEnum.APAGAR)) {
			if (this.getDataUtilizarVerificarBloqueioContaPagar().equals("DV")) {
				return "Vencimento";
			} else {
				return "Competência";
			}
		}
		if  (tipoOrigemHistoricoBloqueio.equals(TipoOrigemHistoricoBloqueioEnum.ARECEBER)) {
			if (this.getDataUtilizarVerificarBloqueioContaReceber().equals("DV")) {
				return "Vencimento";
			} else {
				return "Competência";
			}
		}
		return "";
	}
	
}
