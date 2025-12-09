package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberAgrupadaVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface BoletoBancarioRelInterfaceFacade {

//	public String emitirRelatorio(Integer codigo, UsuarioVO usuarioVO) throws Exception;

	public List<BoletoBancarioRelVO> emitirRelatorioLista(Boolean trazerApenasAlunosAtivos, Integer codigo, String matricula, String ano, String semestre, String parcela, Integer curso, Integer turma, Date dataInicio, Date dataFim, Integer unidadeEnsino, String valorConsultaFiltro, Integer codigoRenegociacao, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer responsavelFinanceiro, Boolean permiteEmitirBoletoRecebido) throws Exception;
	
	public List<BoletoBancarioRelVO> emitirRelatorioLista(Boolean trazerApenasAlunosAtivos, Integer codigo, String matricula, String ano, String semestre, String parcela, Integer curso, Integer turma, Date dataInicio, Date dataFim, Integer unidadeEnsino, String valorConsultaFiltro, Integer centroReceita, UsuarioVO usuarioVO, String tipoImpressaoBoleto, Integer codigoRenegociacao, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer responsavelFinanceiro, Boolean permiteEmitirBoletoRecebido, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception;

	public List<BoletoBancarioRelVO> executarConsultaParametrizada(Boolean trazerApenasAlunosAtivos, Integer contaReceber, List<Integer> listaContaReceber, String matricula, String ano, String semestre, String parcela, Integer curso, Integer turma, Date dataInicio, Date dataFim, Integer unidadeEnsino, UsuarioVO usuarioVO, String tipoImpressaoBoleto, Integer codigoRenegociacao, Integer responsavelFinanceiro, Integer centroReceita, Boolean permiteEmitirBoletoRecebido, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception;
	
	public List<String> executarConsultaParcelasFiltro(String filtrarPor, Integer contaReceber, String matricula, String ano, String semestre, Integer curso, Integer turma, Integer unidadeEnsino, Integer responsavelFinanceiro, Integer centroReceita, Date dataInicio, Date dataTermino, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception;
	
	public List<String> executarConsultaParcelasFiltro(Integer codigoSacado, Date dataIni, Date dataFim, Integer unidadeEnsino) throws Exception;
	
	public List<String> executarConsultaParcelasTurma(Integer codigoTurma, Date dataIni, Date dataFim) throws Exception;
	
	public List<BoletoBancarioRelVO> emitirRelatorioListaContaAgrupada(Boolean trazerApenasAlunosAtivos, ContaReceberAgrupadaVO contaReceberAgrupadaVO,  String matricula, String ano, String semestre, String parcela, Integer curso, Integer turma, Date dataInicio, Date dataFim, Integer unidadeEnsino, String valorConsultaFiltro, Integer centroReceita, UsuarioVO usuarioVO, String tipoImpressaoBoleto, Integer codigoRenegociacao, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer responsavelFinanceiro, Boolean permiteEmitirBoletoRecebido) throws Exception;

	void realizarImpressaoPDF(List<BoletoBancarioRelVO> boletoBancarioRelVOs, SuperParametroRelVO superParametroRelVO, String versaoSistema, String valorModelo, UsuarioVO usuarioVO) throws Exception;

	String getObterDesign(String tipoBoleto, BoletoBancarioRelVO boletoBancarioRelVO);
	
	public void validarImpressaoBoletoAluno(Integer codigoContaReceber, BoletoBancarioRelVO boletoBancarioRelVO, String origemRotina, Integer codigoUsuario) throws Exception;


}