/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.interfaces.biblioteca;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.biblioteca.ExemplarVO;
import relatorio.negocio.comuns.biblioteca.ExemplaresRelVO;

public interface ExemplaresRelInterfaceFacade {

	public List<ExemplaresRelVO> criarObjeto(ExemplarVO exemplarVO, Integer unidadeEnsino, String tipoCatalogoPeriodico, String tipoLayout,String tipoOrdenacaoRelatorio,Date dataInicioCompraExemplar,Date dataFimCompraExemplar ,Date dataInicioAquisicaoExemplar,Date dataFimAquisicaoExemplar, FuncionarioVO responsavelCriacao, FuncionarioVO responsavelAlteracao,
			Date periodoCriacaoInicio, Date peridoCriacaoFim, Date periodoAlteracaoInicio, Date periodoAlteracaoFim, Boolean apresentarResponsavelCriacaoCatalogo, Boolean apresentarResponsavelAlteracaoCatalogo, Boolean considerarSubTiposCatalogo) throws Exception;

	public void validarDados(ExemplarVO exemplarVO, Integer unidadeEnsino, String layoutRelatorio,Date dataInicioCompraExemplar,Date dataFimCompraExemplar , Date dataInicioAquisicaoExemplar,Date dataFimAquisicaoExemplar, Boolean apresentarResponsavelEdataCadastro, String responsavel, Date dataCadastroInicio, Date dataCadastroFim, Boolean apresentarUltimoResponsavelEdataCadastro, String responsavelUltimaAlteracao, Date dataAlteracaoCadastroInicio, Date dataAlteracaoCadastroFim) throws Exception;

    public String designIReportRelatorio();

    public String caminhoBaseRelatorio();

	public String designIReportRelatorioExemplaresRelAnaliticoPeriodico();

	public String designIReportRelatorioExemplaresRelSinteticoPorAssunto();
	
	public String designIReportRelatorioExemplaresRelAnaliticoCatalogo();
	
	
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo,String Situacao) throws Exception;

	public FuncionarioVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception;
}
