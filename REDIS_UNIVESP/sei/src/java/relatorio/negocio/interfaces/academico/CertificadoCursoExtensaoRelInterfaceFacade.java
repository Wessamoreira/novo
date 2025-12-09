package relatorio.negocio.interfaces.academico;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.protocolo.RequerimentoVO;
import relatorio.negocio.comuns.academico.CertificadoCursoExtensaoRelVO;

public interface CertificadoCursoExtensaoRelInterfaceFacade {

	public List<CertificadoCursoExtensaoRelVO> criarObjeto(CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO, MatriculaVO matriculaVO, PeriodoLetivoVO periodoLetivoVO, TurmaVO turmaVO, String tipoLayout, String filtro, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO , boolean trazerApenasDisciplinaPeriodoSelecionado, DisciplinaVO disciplinaVO, UsuarioVO usuarioVO, Boolean trazerTodasSituacoesAprovadas, ProgramacaoFormaturaVO programacaoFormaturaVO , List<CertificadoCursoExtensaoRelVO> listaCertificadoErro,  List<CertificadoCursoExtensaoRelVO> listaCertificadoCursoExtensaoRelVOGerar) throws Exception;
	
	public List<CertificadoCursoExtensaoRelVO> executarConsultaParametrizada(List<MatriculaVO> listaMatricula, CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO, String tipoLayout) throws Exception;
	
    void montarCargaHorariaPorExtenso(List<CertificadoCursoExtensaoRelVO> listaCertificadoCursoExtensaoRelVO);

    public CertificadoCursoExtensaoRelVO montarDados(SqlRowSet dadosSQL, CertificadoCursoExtensaoRelVO obj) throws Exception;
    
    public List<CertificadoCursoExtensaoRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO) throws Exception;

    public List<CertificadoCursoExtensaoRelVO> montarDadosConsultaLayout2(SqlRowSet dadosSQL, CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO) throws Exception;
    
    void montarDadosFuncionarios(CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO, UsuarioVO usuarioVO) throws Exception;

    public void setDescricaoFiltros(String string);

    public String getDescricaoFiltros();

	List<CertificadoCursoExtensaoRelVO> executarConsultaParametrizadaLayout2(List<MatriculaVO> listaMatricula, CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO, String tipoLayout, boolean trazerApenasDisciplinaPeriodoSelecionado, 	DisciplinaVO disciplinaVO, String situacaoHistorioco, Boolean trazerTodasSituacoesAprovadas) throws Exception;

	public void montarDadosLayout2(SqlRowSet dadosSQL, List<CertificadoCursoExtensaoRelVO> vetResultado, CertificadoCursoExtensaoRelVO obj) throws Exception;
	
	public List<CertificadoCursoExtensaoRelVO> executarValidarSituacaoDisciplinaAprovada(List<CertificadoCursoExtensaoRelVO> certificados) throws Exception;
	
	public void validarDadosPeriodoLetivoEmissaoCertificado(Integer periodoLetivo) throws Exception;
	
	HashMap<String, Object> realizarMontagemRelatorioPorTextoPadrao(List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs, List<File> listaArquivos, TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, PlanoEnsinoVO plano, GradeDisciplinaVO gradeDisciplinaVO, RequerimentoVO requerimentoVO, ConfiguracaoGeralSistemaVO config, Boolean gerarNovoArquivoAssinado, Boolean persistirDocumento, UsuarioVO usuario) throws Exception;
	
	public void validarRegraEmissao(List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs, List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOsErro ,UsuarioVO usuarioVO, String filtro) throws Exception;

	

	List<CertificadoCursoExtensaoRelVO> realizarMontagemDadosRelatorioLayoutCertificadoCursoExtensao(
			List<CertificadoCursoExtensaoRelVO> listaCertificadoErro,
			CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO, MatriculaVO matriculaVO,
			PeriodoLetivoVO periodoLetivoVO, TurmaVO turmaVO, String tipoLayout, String filtro,
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, boolean trazerApenasDisciplinaPeriodoSelecionado,
			GradeDisciplinaVO gradeDisciplinaVO, UsuarioVO usuarioVO, Boolean trazerTodasSituacoesAprovadas,
			ProgramacaoFormaturaVO programacaoFormaturaVO, UnidadeEnsinoVO unidadeEnsinoVO, PlanoEnsinoVO plano , List<CertificadoCursoExtensaoRelVO> listaCertificadoCursoExtensaoRelVOGerar)
			throws Exception;
	
}