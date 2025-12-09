package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public class RegistroNegativacaoCobrancaContaReceberVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Date dataGeracao;
	private UsuarioVO usuarioVO;
	private AgenteNegativacaoCobrancaContaReceberVO agente;
	private TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private String aluno;
	private String matricula;
	private Date dataInicioFiltro;
	private Date dataFimFiltro;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	private String centroReceitaApresentar;
	
	private List<RegistroNegativacaoCobrancaContaReceberItemVO> listaContasReceberCobranca;
	private boolean registrarNegativacaoContaReceberViaIntegracao = false;
	
	private List<ContaReceberNegativacaoVO> listaContasReceberNegativacao;
	
	public JRDataSource getListaContasReceberCobrancaJrDataSource() {
		JRDataSource jr = new JRBeanArrayDataSource(getListaContasReceberCobranca().toArray());
		return jr;
	}
	
	public Boolean getTipoAgenteNegativacao() {
		return getTipoAgente() != null && getTipoAgente().equals(TipoAgenteNegativacaoCobrancaContaReceberEnum.NEGATIVACAO);
	}
	
	public Boolean getTipoAgenteCobranca() {
		return getTipoAgente() != null && getTipoAgente().equals(TipoAgenteNegativacaoCobrancaContaReceberEnum.COBRANCA);
	}
	
	public Boolean getPossuiListaContasReceber() {
		return getListaContasReceberCobranca().size() > 0 || getListaContasReceberNegativacao().size() > 0;
	}
	
	public String getNomeAgenteComTipo() {
		return getAgente().getNome() + " - " + (getTipoAgenteCobranca()?"COBRANÇA":(getTipoAgenteNegativacao()?"NEGATIVAÇÃO":""));
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

	public String getAluno() {
		if (aluno == null) {
			aluno = "";
		}
		return aluno;
	}
	
	public void setAluno(String aluno) {
		this.aluno = aluno;
	}
	
	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}
	
	public void setmatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public Date getDataGeracao() {
		return dataGeracao;
	}
	
	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}
	
	public Date getDataInicioFiltro() {
		return dataInicioFiltro;
	}
	
	public void setDataInicioFiltro(Date dataInicioFiltro) {
		this.dataInicioFiltro = dataInicioFiltro;
	}
	
	public Date getDataFimFiltro() {
		return dataFimFiltro;
	}
	
	public void setDataFimFiltro(Date dataFimFiltro) {
		this.dataFimFiltro = dataFimFiltro;
	}

	public String getDataGeracao_Apresentar() {
		if (dataGeracao == null) {
			return "";
		}
		return (Uteis.getData(dataGeracao));
	}

	public String getDataInicioFiltro_Apresentar() {
		if (dataInicioFiltro == null) {
			return "";
		}
		return (Uteis.getData(dataInicioFiltro));
	}
	
	public String getDataFimFiltro_Apresentar() {
		if (dataFimFiltro == null) {
			return "";
		}
		return (Uteis.getData(dataFimFiltro));
	}
	
	public void setMatricula(String matricula) {
		this.matricula = matricula;
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
	
	public FiltroRelatorioFinanceiroVO getFiltroRelatorioFinanceiroVO() {
		if (filtroRelatorioFinanceiroVO == null) {
			filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(false);
		}
		return filtroRelatorioFinanceiroVO;
	}
	
	public void setFiltroRelatorioFinanceiroVO(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
		this.filtroRelatorioFinanceiroVO = filtroRelatorioFinanceiroVO;
	}
	
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}
	
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
	
	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}
	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
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
	
	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}
	
	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}
	
	public String getCentroReceitaApresentar() {
		if (centroReceitaApresentar == null) {
			centroReceitaApresentar = "";
		}
		return centroReceitaApresentar;
	}
	
	public void setCentroReceitaApresentar(String centroReceita) {
		this.centroReceitaApresentar = centroReceita;
	}

	public List<RegistroNegativacaoCobrancaContaReceberItemVO> getListaContasReceberCobranca() {
		if (listaContasReceberCobranca == null) {
			listaContasReceberCobranca = new ArrayList<RegistroNegativacaoCobrancaContaReceberItemVO>();
		}
		return listaContasReceberCobranca;
	}
	
	public Integer getTotalContasReceberCobranca() {
		return getListaContasReceberCobranca().size();
	}

	public void setListaContasReceberCobranca(List<RegistroNegativacaoCobrancaContaReceberItemVO> listaContasReceberCobranca) {
		this.listaContasReceberCobranca = listaContasReceberCobranca;
	}

	public AgenteNegativacaoCobrancaContaReceberVO getAgente() {
		if (agente == null) {
			agente = new AgenteNegativacaoCobrancaContaReceberVO();
		}
		return agente;
	}

	public void setAgente(AgenteNegativacaoCobrancaContaReceberVO agente) {
		this.agente = agente;
	}

	public TipoAgenteNegativacaoCobrancaContaReceberEnum getTipoAgente() {
		return tipoAgente;
	}

	public void setTipoAgente(TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente) {
		this.tipoAgente = tipoAgente;
	}

	public List<ContaReceberNegativacaoVO> getListaContasReceberNegativacao() {
		if (listaContasReceberNegativacao == null) {
			listaContasReceberNegativacao = new ArrayList<ContaReceberNegativacaoVO>();
		}
		return listaContasReceberNegativacao;
	}

	public void setListaContasReceberNegativacao(List<ContaReceberNegativacaoVO> listaContasReceberNegativacao) {
		this.listaContasReceberNegativacao = listaContasReceberNegativacao;
	}
	
	
	
	public boolean isRegistrarNegativacaoContaReceberViaIntegracao() {
		return registrarNegativacaoContaReceberViaIntegracao;
	}

	public void setRegistrarNegativacaoContaReceberViaIntegracao(boolean registrarNegativacaoContaReceberViaIntegracao) {
		this.registrarNegativacaoContaReceberViaIntegracao = registrarNegativacaoContaReceberViaIntegracao;
	}

	public void adicionarListaContaReceber (List lista) {
		Iterator i = lista.iterator();
		while (i.hasNext()) {
			boolean encontrou = false;
			RegistroNegativacaoCobrancaContaReceberItemVO reg = (RegistroNegativacaoCobrancaContaReceberItemVO)i.next();
			Iterator o = this.getListaContasReceberCobranca().iterator();
			while (o.hasNext()) {
				RegistroNegativacaoCobrancaContaReceberItemVO reg2 = (RegistroNegativacaoCobrancaContaReceberItemVO)o.next();
				if (reg.getNossoNumero().equals(reg2.getNossoNumero())) {
					encontrou = true;
				}
			}
			if (!encontrou) {
				this.getListaContasReceberCobranca().add(reg);
			}
		}
	}
	
	public Integer getQuantidadeContas() {
		return getListaContasReceberCobranca().size();
	}
	
	public Double valorTotalContas;
	public Double getValorTotalContas() {
		if(valorTotalContas == null) {
			valorTotalContas = getListaContasReceberCobranca().stream().mapToDouble(RegistroNegativacaoCobrancaContaReceberItemVO::getValor).sum();
		}
		return valorTotalContas;
	}
	
}