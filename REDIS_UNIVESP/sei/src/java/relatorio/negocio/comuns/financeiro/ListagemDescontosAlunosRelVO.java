package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ConvenioVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * @author Otimize-TI
 */
public class ListagemDescontosAlunosRelVO implements Cloneable {

	private MatriculaPeriodoVO matriculaPeriodoVO;
	private List<ConvenioVO> listaConvenio;
	private List<PlanoFinanceiroAlunoVO> listaPlanoFinanceiroAluno;
	private List<PlanoDescontoVO> listaPlanoDesconto;
	private List<DescontoProgressivoVO> listaDescontoProgressivo;
	private List<ContaReceberVO> listaContaReceber;
	private Map<Integer, String> hashMapDesconto;
	private String semestre;
	private String ano;
	private String campoFiltroPor;
	private Double valorTotalConvenioMatriculaCalculadoAluno;
	private Double valorTotalConvenioParcelaCalculadoAluno;
	private Double valorTotalPlanoFinanceiroAlunoMatriculaCalculadoAluno;
	private Double valorTotalPlanoFinanceiroAlunoParcelaCalculadoAluno;
	private Double valorTotalPlanoDescontoMatriculaCalculadoAluno;
	private Double valorTotalPlanoDescontoParcelaCalculadoAluno;
	private Double valorTotalDescontoProgressivoMatriculaCalculadoAluno;
	private Double valorTotalDescontoProgressivoParcelaCalculadoAluno;
	private Double valorTotalDescontoRecebimentoMatriculaCalculadoAluno;
	private Double valorTotalDescontoRecebimentoParcelaCalculadoAluno;
	private Double valorTotalDescontoRateioMatriculaCalculadoAluno;
	private Double valorTotalDescontoRateioParcelaCalculadoAluno;
	private Double valorContaReceberCalculado;

	private Integer totalAlunoConvenioMatricula;
	private Integer totalAlunoConvenioParcela;
	private Integer totalAlunoPlanoFinanceiroAlunoMatricula;
	private Integer totalAlunoPlanoFinanceiroAlunoParcela;
	private Integer totalAlunoPlanoDescontoMatricula;
	private Integer totalAlunoPlanoDescontoParcela;
	private Integer totalAlunoDescontoProgressivoMatricula;
	private Integer totalAlunoDescontoProgressivoParcela;
	private Integer totalAlunoDescontoRecebimentoMatricula;
	private Integer totalAlunoDescontoRecebimentoParcela;
	private Integer totalAlunoDescontoRateioMatricula;
	private Integer totalAlunoDescontoRateioParcela;
	private List<ListagemDescontosAlunosPorTipoDescontoRelVO> listagemDescontosAlunosPorTipoDescontoRelVOs;
	
	private List<String> matriculaComDescontoConvenio; 
	private List<String> matriculaComDescontoPlanoDesconto; 
	private List<String> matriculaComDescontoDescontoAluno; 
	private List<String> matriculaComDescontoDescontoRateio; 
	private List<String> matriculaComDescontoDescontoProgressivo; 
	private List<String> matriculaComDescontoRecebimento; 
	private List<String> matriculaComDesconto; 
	private String agrupador;
	
	public ListagemDescontosAlunosRelVO() {
		
	}

	public JRDataSource getListagemDescontosAlunosPorTipoDescontoRelVO() {
		return new JRBeanArrayDataSource(getListagemDescontosAlunosPorTipoDescontoRelVOs().toArray());
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

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getCampoFiltroPor() {
		if (campoFiltroPor == null) {
			campoFiltroPor = "";
		}
		return campoFiltroPor;
	}

	public void setCampoFiltroPor(String campoFiltroPor) {
		this.campoFiltroPor = campoFiltroPor;
	}

	public List<ContaReceberVO> getListaContaReceber() {
		if (listaContaReceber == null) {
			listaContaReceber = new ArrayList<ContaReceberVO>(0);
		}
		return listaContaReceber;
	}

	public void setListaContaReceber(List<ContaReceberVO> listaContaReceber) {
		this.listaContaReceber = listaContaReceber;
	}

	public List<ConvenioVO> getListaConvenio() {
		if (listaConvenio == null) {
			listaConvenio = new ArrayList<ConvenioVO>(0);
		}
		return listaConvenio;
	}

	public void setListaConvenio(List<ConvenioVO> listaConvenio) {
		this.listaConvenio = listaConvenio;
	}

	public List<DescontoProgressivoVO> getListaDescontoProgressivo() {
		if (listaDescontoProgressivo == null) {
			listaDescontoProgressivo = new ArrayList<DescontoProgressivoVO>(0);
		}
		return listaDescontoProgressivo;
	}

	public void setListaDescontoProgressivo(List<DescontoProgressivoVO> listaDescontoProgressivo) {
		this.listaDescontoProgressivo = listaDescontoProgressivo;
	}

	public Map<Integer, String> getHashMapDesconto() {
		if (hashMapDesconto == null) {
			hashMapDesconto = new HashMap<Integer, String>();
		}
		return hashMapDesconto;
	}

	public void setHashMapDesconto(Map<Integer, String> hashMapDesconto) {
		this.hashMapDesconto = hashMapDesconto;
	}

	public List<PlanoDescontoVO> getListaPlanoDesconto() {
		if (listaPlanoDesconto == null) {
			listaPlanoDesconto = new ArrayList<PlanoDescontoVO>(0);
		}
		return listaPlanoDesconto;
	}

	public void setListaPlanoDesconto(List<PlanoDescontoVO> listaPlanoDesconto) {
		this.listaPlanoDesconto = listaPlanoDesconto;
	}

	public List<PlanoFinanceiroAlunoVO> getListaPlanoFinanceiroAluno() {
		if (listaPlanoFinanceiroAluno == null) {
			listaPlanoFinanceiroAluno = new ArrayList<PlanoFinanceiroAlunoVO>(0);
		}
		return listaPlanoFinanceiroAluno;
	}

	public void setListaPlanoFinanceiroAluno(List<PlanoFinanceiroAlunoVO> listaPlanoFinanceiroAluno) {
		this.listaPlanoFinanceiroAluno = listaPlanoFinanceiroAluno;
	}

	public Double getValorContaReceberCalculado() {
		if (valorContaReceberCalculado == null) {
			valorContaReceberCalculado = 0.0;
		}
		return valorContaReceberCalculado;
	}

	public void setValorContaReceberCalculado(Double valorContaReceberCalculado) {
		this.valorContaReceberCalculado = valorContaReceberCalculado;
	}

	public Double getValorTotalConvenioMatriculaCalculadoAluno() {
		if (valorTotalConvenioMatriculaCalculadoAluno == null) {
			valorTotalConvenioMatriculaCalculadoAluno = 0.0;
		}
		return valorTotalConvenioMatriculaCalculadoAluno;
	}

	public void setValorTotalConvenioMatriculaCalculadoAluno(Double valorTotalConvenioMatriculaCalculadoAluno) {
		this.valorTotalConvenioMatriculaCalculadoAluno = valorTotalConvenioMatriculaCalculadoAluno;
	}

	public Double getValorTotalConvenioParcelaCalculadoAluno() {
		if (valorTotalConvenioParcelaCalculadoAluno == null) {
			valorTotalConvenioParcelaCalculadoAluno = 0.0;
		}
		return valorTotalConvenioParcelaCalculadoAluno;
	}

	public void setValorTotalConvenioParcelaCalculadoAluno(Double valorTotalConvenioParcelaCalculadoAluno) {
		this.valorTotalConvenioParcelaCalculadoAluno = valorTotalConvenioParcelaCalculadoAluno;
	}

	public Double getValorTotalDescontoProgressivoMatriculaCalculadoAluno() {
		if (valorTotalDescontoProgressivoMatriculaCalculadoAluno == null) {
			valorTotalDescontoProgressivoMatriculaCalculadoAluno = 0.0;
		}
		return valorTotalDescontoProgressivoMatriculaCalculadoAluno;
	}

	public void setValorTotalDescontoProgressivoMatriculaCalculadoAluno(Double valorTotalDescontoProgressivoMatriculaCalculadoAluno) {
		this.valorTotalDescontoProgressivoMatriculaCalculadoAluno = valorTotalDescontoProgressivoMatriculaCalculadoAluno;
	}

	public Double getValorTotalDescontoProgressivoParcelaCalculadoAluno() {
		if (valorTotalDescontoProgressivoParcelaCalculadoAluno == null) {
			valorTotalDescontoProgressivoParcelaCalculadoAluno = 0.0;
		}
		return valorTotalDescontoProgressivoParcelaCalculadoAluno;
	}

	public void setValorTotalDescontoProgressivoParcelaCalculadoAluno(Double valorTotalDescontoProgressivoParcelaCalculadoAluno) {
		this.valorTotalDescontoProgressivoParcelaCalculadoAluno = valorTotalDescontoProgressivoParcelaCalculadoAluno;
	}

	public Double getValorTotalPlanoDescontoMatriculaCalculadoAluno() {
		if (valorTotalPlanoDescontoMatriculaCalculadoAluno == null) {
			valorTotalPlanoDescontoMatriculaCalculadoAluno = 0.0;
		}
		return valorTotalPlanoDescontoMatriculaCalculadoAluno;
	}

	public void setValorTotalPlanoDescontoMatriculaCalculadoAluno(Double valorTotalPlanoDescontoMatriculaCalculadoAluno) {
		this.valorTotalPlanoDescontoMatriculaCalculadoAluno = valorTotalPlanoDescontoMatriculaCalculadoAluno;
	}

	public Double getValorTotalPlanoDescontoParcelaCalculadoAluno() {
		if (valorTotalPlanoDescontoParcelaCalculadoAluno == null) {
			valorTotalPlanoDescontoParcelaCalculadoAluno = 0.0;
		}
		return valorTotalPlanoDescontoParcelaCalculadoAluno;
	}

	public void setValorTotalPlanoDescontoParcelaCalculadoAluno(Double valorTotalPlanoDescontoParcelaCalculadoAluno) {
		this.valorTotalPlanoDescontoParcelaCalculadoAluno = valorTotalPlanoDescontoParcelaCalculadoAluno;
	}

	public Double getValorTotalPlanoFinanceiroAlunoMatriculaCalculadoAluno() {
		if (valorTotalPlanoFinanceiroAlunoMatriculaCalculadoAluno == null) {
			valorTotalPlanoFinanceiroAlunoMatriculaCalculadoAluno = 0.0;
		}
		return valorTotalPlanoFinanceiroAlunoMatriculaCalculadoAluno;
	}

	public void setValorTotalPlanoFinanceiroAlunoMatriculaCalculadoAluno(Double valorTotalPlanoFinanceiroAlunoMatriculaCalculadoAluno) {
		this.valorTotalPlanoFinanceiroAlunoMatriculaCalculadoAluno = valorTotalPlanoFinanceiroAlunoMatriculaCalculadoAluno;
	}

	public Double getValorTotalPlanoFinanceiroAlunoParcelaCalculadoAluno() {
		if (valorTotalPlanoFinanceiroAlunoParcelaCalculadoAluno == null) {
			valorTotalPlanoFinanceiroAlunoParcelaCalculadoAluno = 0.0;
		}
		return valorTotalPlanoFinanceiroAlunoParcelaCalculadoAluno;
	}

	public void setValorTotalPlanoFinanceiroAlunoParcelaCalculadoAluno(Double valorTotalPlanoFinanceiroAlunoParcelaCalculadoAluno) {
		this.valorTotalPlanoFinanceiroAlunoParcelaCalculadoAluno = valorTotalPlanoFinanceiroAlunoParcelaCalculadoAluno;
	}

	public JRDataSource getListaConvenioJR() {
		JRDataSource jr = new JRBeanArrayDataSource(getListaConvenio().toArray());
		return jr;
	}

	public JRDataSource getListaPlanoDescontoJR() {
		JRDataSource jr = new JRBeanArrayDataSource(getListaPlanoDesconto().toArray());
		return jr;
	}

	public JRDataSource getListaPlanoFinanceiroJR() {
		JRDataSource jr = new JRBeanArrayDataSource(getListaPlanoFinanceiroAluno().toArray());
		return jr;
	}

	public JRDataSource getListaDescontoProgressivoJR() {
		JRDataSource jr = new JRBeanArrayDataSource(getListaDescontoProgressivo().toArray());
		return jr;
	}

	public Integer getTotalAlunoConvenioMatricula() {
		if (totalAlunoConvenioMatricula == null) {
			totalAlunoConvenioMatricula = 0;
		}
		return totalAlunoConvenioMatricula;
	}

	public void setTotalAlunoConvenioMatricula(Integer totalAlunoConvenioMatricula) {
		this.totalAlunoConvenioMatricula = totalAlunoConvenioMatricula;
	}

	public Integer getTotalAlunoConvenioParcela() {
		if (totalAlunoConvenioParcela == null) {
			totalAlunoConvenioParcela = 0;
		}
		return totalAlunoConvenioParcela;
	}

	public void setTotalAlunoConvenioParcela(Integer totalAlunoConvenioParcela) {
		this.totalAlunoConvenioParcela = totalAlunoConvenioParcela;
	}

	public Integer getTotalAlunoPlanoFinanceiroAlunoMatricula() {
		if (totalAlunoPlanoFinanceiroAlunoMatricula == null) {
			totalAlunoPlanoFinanceiroAlunoMatricula = 0;
		}
		return totalAlunoPlanoFinanceiroAlunoMatricula;
	}

	public void setTotalAlunoPlanoFinanceiroAlunoMatricula(Integer totalAlunoPlanoFinanceiroAlunoMatricula) {
		this.totalAlunoPlanoFinanceiroAlunoMatricula = totalAlunoPlanoFinanceiroAlunoMatricula;
	}

	public Integer getTotalAlunoPlanoFinanceiroAlunoParcela() {
		if (totalAlunoPlanoFinanceiroAlunoParcela == null) {
			totalAlunoPlanoFinanceiroAlunoParcela = 0;
		}
		return totalAlunoPlanoFinanceiroAlunoParcela;
	}

	public void setTotalAlunoPlanoFinanceiroAlunoParcela(Integer totalAlunoPlanoFinanceiroAlunoParcela) {
		this.totalAlunoPlanoFinanceiroAlunoParcela = totalAlunoPlanoFinanceiroAlunoParcela;
	}

	public Integer getTotalAlunoPlanoDescontoMatricula() {
		if (totalAlunoPlanoDescontoMatricula == null) {
			totalAlunoPlanoDescontoMatricula = 0;
		}
		return totalAlunoPlanoDescontoMatricula;
	}

	public void setTotalAlunoPlanoDescontoMatricula(Integer totalAlunoPlanoDescontoMatricula) {
		this.totalAlunoPlanoDescontoMatricula = totalAlunoPlanoDescontoMatricula;
	}

	public Integer getTotalAlunoPlanoDescontoParcela() {
		if (totalAlunoPlanoDescontoParcela == null) {
			totalAlunoPlanoDescontoParcela = 0;
		}
		return totalAlunoPlanoDescontoParcela;
	}

	public void setTotalAlunoPlanoDescontoParcela(Integer totalAlunoPlanoDescontoParcela) {
		this.totalAlunoPlanoDescontoParcela = totalAlunoPlanoDescontoParcela;
	}

	public Integer getTotalAlunoDescontoProgressivoMatricula() {
		if (totalAlunoDescontoProgressivoMatricula == null) {
			totalAlunoDescontoProgressivoMatricula = 0;
		}
		return totalAlunoDescontoProgressivoMatricula;
	}

	public void setTotalAlunoDescontoProgressivoMatricula(Integer totalAlunoDescontoProgressivoMatricula) {
		this.totalAlunoDescontoProgressivoMatricula = totalAlunoDescontoProgressivoMatricula;
	}

	public Integer getTotalAlunoDescontoProgressivoParcela() {
		if (totalAlunoDescontoProgressivoParcela == null) {
			totalAlunoDescontoProgressivoParcela = 0;
		}
		return totalAlunoDescontoProgressivoParcela;
	}

	public void setTotalAlunoDescontoProgressivoParcela(Integer totalAlunoDescontoProgressivoParcela) {
		this.totalAlunoDescontoProgressivoParcela = totalAlunoDescontoProgressivoParcela;
	}

	public List<ListagemDescontosAlunosPorTipoDescontoRelVO> getListagemDescontosAlunosPorTipoDescontoRelVOs() {
		if (listagemDescontosAlunosPorTipoDescontoRelVOs == null) {
			listagemDescontosAlunosPorTipoDescontoRelVOs = new ArrayList<ListagemDescontosAlunosPorTipoDescontoRelVO>(0);
		}
		return listagemDescontosAlunosPorTipoDescontoRelVOs;
	}

	public void setListagemDescontosAlunosPorTipoDescontoRelVOs(List<ListagemDescontosAlunosPorTipoDescontoRelVO> listagemDescontosAlunosPorTipoDescontoRelVOs) {
		this.listagemDescontosAlunosPorTipoDescontoRelVOs = listagemDescontosAlunosPorTipoDescontoRelVOs;
	}

	public Double getValorTotalDescontoRecebimentoMatriculaCalculadoAluno() {
		if (valorTotalDescontoRecebimentoMatriculaCalculadoAluno == null) {
			valorTotalDescontoRecebimentoMatriculaCalculadoAluno = 0.0;
		}
		return valorTotalDescontoRecebimentoMatriculaCalculadoAluno;
	}

	public void setValorTotalDescontoRecebimentoMatriculaCalculadoAluno(Double valorTotalDescontoRecebimentoMatriculaCalculadoAluno) {
		this.valorTotalDescontoRecebimentoMatriculaCalculadoAluno = valorTotalDescontoRecebimentoMatriculaCalculadoAluno;
	}

	public Double getValorTotalDescontoRecebimentoParcelaCalculadoAluno() {
		if (valorTotalDescontoRecebimentoParcelaCalculadoAluno == null) {
			valorTotalDescontoRecebimentoParcelaCalculadoAluno = 0.0;
		}
		return valorTotalDescontoRecebimentoParcelaCalculadoAluno;
	}

	public void setValorTotalDescontoRecebimentoParcelaCalculadoAluno(Double valorTotalDescontoRecebimentoParcelaCalculadoAluno) {
		this.valorTotalDescontoRecebimentoParcelaCalculadoAluno = valorTotalDescontoRecebimentoParcelaCalculadoAluno;
	}

	public Double getValorTotalDescontoRateioMatriculaCalculadoAluno() {
		if (valorTotalDescontoRateioMatriculaCalculadoAluno == null) {
			valorTotalDescontoRateioMatriculaCalculadoAluno = 0.0;
		}
		return valorTotalDescontoRateioMatriculaCalculadoAluno;
	}

	public void setValorTotalDescontoRateioMatriculaCalculadoAluno(Double valorTotalDescontoRateioMatriculaCalculadoAluno) {
		this.valorTotalDescontoRateioMatriculaCalculadoAluno = valorTotalDescontoRateioMatriculaCalculadoAluno;
	}

	public Double getValorTotalDescontoRateioParcelaCalculadoAluno() {
		if (valorTotalDescontoRateioParcelaCalculadoAluno == null) {
			valorTotalDescontoRateioParcelaCalculadoAluno = 0.0;
		}
		return valorTotalDescontoRateioParcelaCalculadoAluno;
	}

	public void setValorTotalDescontoRateioParcelaCalculadoAluno(Double valorTotalDescontoRateioParcelaCalculadoAluno) {
		this.valorTotalDescontoRateioParcelaCalculadoAluno = valorTotalDescontoRateioParcelaCalculadoAluno;
	}

	public Integer getTotalAlunoDescontoRecebimentoMatricula() {
		if (totalAlunoDescontoRecebimentoMatricula == null) {
			totalAlunoDescontoRecebimentoMatricula = 0;
		}
		return totalAlunoDescontoRecebimentoMatricula;
	}

	public void setTotalAlunoDescontoRecebimentoMatricula(Integer totalAlunoDescontoRecebimentoMatricula) {
		this.totalAlunoDescontoRecebimentoMatricula = totalAlunoDescontoRecebimentoMatricula;
	}

	public Integer getTotalAlunoDescontoRecebimentoParcela() {
		if (totalAlunoDescontoRecebimentoParcela == null) {
			totalAlunoDescontoRecebimentoParcela = 0;
		}
		return totalAlunoDescontoRecebimentoParcela;
	}

	public void setTotalAlunoDescontoRecebimentoParcela(Integer totalAlunoDescontoRecebimentoParcela) {
		this.totalAlunoDescontoRecebimentoParcela = totalAlunoDescontoRecebimentoParcela;
	}

	public Integer getTotalAlunoDescontoRateioMatricula() {
		if (totalAlunoDescontoRateioMatricula == null) {
			totalAlunoDescontoRateioMatricula = 0;
		}
		return totalAlunoDescontoRateioMatricula;
	}

	public void setTotalAlunoDescontoRateioMatricula(Integer totalAlunoDescontoRateioMatricula) {
		this.totalAlunoDescontoRateioMatricula = totalAlunoDescontoRateioMatricula;
	}

	public Integer getTotalAlunoDescontoRateioParcela() {
		if (totalAlunoDescontoRateioParcela == null) {
			totalAlunoDescontoRateioParcela = 0;
		}
		return totalAlunoDescontoRateioParcela;
	}

	public void setTotalAlunoDescontoRateioParcela(Integer totalAlunoDescontoRateioParcela) {
		this.totalAlunoDescontoRateioParcela = totalAlunoDescontoRateioParcela;
	}		
	
	public List<String> getMatriculaComDescontoConvenio() {
		if(matriculaComDescontoConvenio == null){
			matriculaComDescontoConvenio = new ArrayList<String>();
		}
		return matriculaComDescontoConvenio;
	}

	public void setMatriculaComDescontoConvenio(List<String> matriculaComDescontoConvenio) {
		this.matriculaComDescontoConvenio = matriculaComDescontoConvenio;
	}

	public List<String> getMatriculaComDescontoPlanoDesconto() {
		if(matriculaComDescontoPlanoDesconto == null){
			matriculaComDescontoPlanoDesconto = new ArrayList<String>();
		}
		return matriculaComDescontoPlanoDesconto;
	}

	public void setMatriculaComDescontoPlanoDesconto(List<String> matriculaComDescontoPlanoDesconto) {
		this.matriculaComDescontoPlanoDesconto = matriculaComDescontoPlanoDesconto;
	}

	public List<String> getMatriculaComDescontoDescontoAluno() {
		if(matriculaComDescontoDescontoAluno == null){
			matriculaComDescontoDescontoAluno = new ArrayList<String>();
		}
		return matriculaComDescontoDescontoAluno;
	}

	public void setMatriculaComDescontoDescontoAluno(List<String> matriculaComDescontoDescontoAluno) {
		this.matriculaComDescontoDescontoAluno = matriculaComDescontoDescontoAluno;
	}

	public List<String> getMatriculaComDescontoDescontoRateio() {
		if(matriculaComDescontoDescontoRateio == null){
			matriculaComDescontoDescontoRateio = new ArrayList<String>();
		}
		return matriculaComDescontoDescontoRateio;
	}

	public void setMatriculaComDescontoDescontoRateio(List<String> matriculaComDescontoDescontoRateio) {
		this.matriculaComDescontoDescontoRateio = matriculaComDescontoDescontoRateio;
	}

	public List<String> getMatriculaComDescontoDescontoProgressivo() {
		if(matriculaComDescontoDescontoProgressivo == null){
			matriculaComDescontoDescontoProgressivo = new ArrayList<String>();
		}
		return matriculaComDescontoDescontoProgressivo;
	}
	
	public void setMatriculaComDescontoDescontoProgressivo(List<String> matriculaComDescontoDescontoProgressivo) {
		this.matriculaComDescontoDescontoProgressivo = matriculaComDescontoDescontoProgressivo;
	}
	
	public List<String> getMatriculaComDescontoRecebimento() {
		if(matriculaComDescontoRecebimento == null){
			matriculaComDescontoRecebimento = new ArrayList<String>();
		}
		return matriculaComDescontoRecebimento;
	}
	
	public void setMatriculaComDescontoRecebimento(List<String> matriculaComDescontoRecebimento) {
		this.matriculaComDescontoRecebimento = matriculaComDescontoRecebimento;
	}
	
	public List<String> getMatriculaComDesconto() {
		if(matriculaComDesconto == null){
			matriculaComDesconto = new ArrayList<String>();
		}
		return matriculaComDesconto;
	}

	public void setMatriculaComDesconto(List<String> matriculaComDesconto) {
		this.matriculaComDesconto = matriculaComDesconto;
	}

	public Integer getQtdeMatriculaComDescontoConvenio() {
		return getMatriculaComDescontoConvenio().size();
	}
	
	public Integer getQtdeMatriculaComDescontoPlanoDesconto() {
		return getMatriculaComDescontoPlanoDesconto().size();
	}
	
	public Integer getQtdeMatriculaComDescontoRateio() {
		return getMatriculaComDescontoDescontoRateio().size();
	}
	
	public Integer getQtdeMatriculaComDescontoAluno() {
		return getMatriculaComDescontoDescontoAluno().size();
	}
	
	public Integer getQtdeMatriculaComDescontoProgressivo() {
		return getMatriculaComDescontoDescontoProgressivo().size();
	}
	
	public Integer getQtdeMatriculaComDescontoRecebimento() {
		return getMatriculaComDescontoRecebimento().size();
	}
	
	public Integer getQtdeMatriculaComDesconto() {
		return getMatriculaComDesconto().size();
	}

	public String getAgrupador() {
		if(agrupador == null){
			agrupador = "";
		}
		return agrupador;
	}

	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}

	
}
