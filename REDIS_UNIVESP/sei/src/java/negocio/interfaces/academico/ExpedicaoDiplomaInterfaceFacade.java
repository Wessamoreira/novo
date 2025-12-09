package negocio.interfaces.academico;

import java.io.File;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoHistoricoVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.utilitarias.ControleConsultaExpedicaoDiploma;
import negocio.comuns.utilitarias.ProgressBarVO;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ExpedicaoDiplomaInterfaceFacade {

    public ExpedicaoDiplomaVO novo() throws Exception;

    public void incluir(ExpedicaoDiplomaVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(ExpedicaoDiplomaVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception;

    public void excluir(ExpedicaoDiplomaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
    
    public void validarDados(ExpedicaoDiplomaVO obj,Boolean gerarExessao , UsuarioVO usuario) throws Exception;

    public ExpedicaoDiplomaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataExpedicao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorVia(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void validarImprimirDiploma(ExpedicaoDiplomaVO expedicaoDiploma, Integer codFuncPrincipal, Integer codFuncSecundario, Integer codCargoFuncPrincipal, Integer codCargoFuncSecundario,Integer codFuncTerceiro, Integer codCargoFuncTerceiro, String tipoLayout, UsuarioVO usuario, Boolean gerarXMLDiploma) throws Exception ;

    public List<ExpedicaoDiplomaVO> consultarPorMatriculaAluno(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public ExpedicaoDiplomaVO consultarPorMatriculaMatriculaPrimeiraVia(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void incluirDataConclusaoCursoMatricula(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeAluno(String valorConsulta, boolean controlarAcesso, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

    public void alterarFuncionarioResponsavel(final ExpedicaoDiplomaVO obj, UsuarioVO usuario) throws Exception;

    public ExpedicaoDiplomaVO consultarFuncionarioResponsavelExpedicao(ExpedicaoDiplomaVO expedicaoDiploma, UsuarioVO usuario) throws Exception;

	ExpedicaoDiplomaVO consultarPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
    public void consultarObservacaoComplementarUltimaExpedicaoMatricula(ExpedicaoDiplomaVO expedicaoDiploma, UsuarioVO usuario) throws Exception;
        
    public ExpedicaoDiplomaVO consultarPorMatriculaPrimeiraVia(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
	public void alterarDadosBasicosPrimeiraVia(final ExpedicaoDiplomaVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception;
	
    public void incluir(final ExpedicaoDiplomaVO obj, UsuarioVO usuario, Boolean validarDados, Boolean alterarObsMatricula) throws Exception;
    
    public boolean verificarExisteRegistroExpedicaoDiplomaDiferenteAtual(ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception;
    
    public void alterarDataExpedicaoDiploma(Integer codigo, Date data, UsuarioVO usuario, Boolean controlarAcesso) throws Exception;
    
    public void validarRegraEmissao(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO) throws Exception;

	public void montarNumeroProcessoERegistroDiplomaVindoMascaraConfiguracaoAcademico(ExpedicaoDiplomaVO expedicaoDiplomaVO, Integer unidadeEnsino, Integer curso, UsuarioVO usuarioLogadoClone) throws Exception;
	
    ExpedicaoDiplomaVO consultarPorMatriculaPrimeiraViaMontandoFuncionarioCargos(String matricula, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public void montarListaExpedicaoDiplomaEmitirPorProgramacaoFormatura(ProgramacaoFormaturaVO programacaoFormaturaVO,	List<ExpedicaoDiplomaVO> listaExpedicaoDiplomaVOs, List<ExpedicaoDiplomaVO> listaExpedicaoDiplomaVOsErro, Boolean trazerAlunosDiplomaEmitido, UsuarioVO usuarioLogado)throws Exception;

	
	Date consultarDataExpedicaoDiplomaPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados,	UsuarioVO usuario) throws Exception;

	

	public void realizarGeracaoXMLDiplomaDigital(ExpedicaoDiplomaVO expedicaoDiplomaVO, ConfiguracaoGEDVO configGEDVO, ConfiguracaoGeralSistemaVO config, File arquivoVisual, UsuarioVO usuarioVO, File arquivoVisualHistorico, TipoOrigemDocumentoAssinadoEnum tipoXmlGeracao, Boolean persistirDocumentoAssinado, Boolean adicionarSeloQrCode, HistoricoAlunoRelVO histAlunoRelVO) throws Exception;
    
	public String realizarGeracaoHashCodigoValidacao(ExpedicaoDiplomaVO expedicaoDiplomaVO, String livroRegistro, String numeroFolhaDoDiploma, String numeroSequenciaDoDiploma, UsuarioVO usuarioVO) throws Exception;
	public void realizarGeracaoRepresentacaoVisualDiplomaDigital(ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception;

	public String realizarImpressaoExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, Boolean assinarDigitalmente, Boolean gerarDiplomaXML, Boolean gerarDiplomaXmlLote, SuperParametroRelVO superParametroRelVO, SuperControleRelatorio superControleRelatorio, String tipoLayout, List<String> listaMensagemErro, String caminhoPastaWeb, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void carregarDadosExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, Boolean expedicaoDiplomaLote, UsuarioVO usuario) throws Exception;
	
	public void realizarAnulacaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO) throws Exception;

	public void realizarEstornoAnulacaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO, UsuarioVO usuarioVO) throws Exception;

	public void consultarExpedicaoDiplomaGenericoOtimizado(ControleConsultaExpedicaoDiploma controleConsultaExpedicaoDiploma) throws Exception;

	public ExpedicaoDiplomaVO consultarUnicaExpedicaoDiplomaGenerico(List<String> fields, List<String> condicaoWhere, List<String> joins, List<Object> valoresFiltros, List<String> orderBY, Boolean retornaException) throws Exception;

	public ExpedicaoDiplomaVO carregarDadosCompletoExpedicaoDiploma(Integer codigoExpedicaoDiploma, UsuarioVO usuarioVO) throws Exception;

	public void realizarGeracaoHistoricoDigital(SuperParametroRelVO superParametroRelVO, SuperControleRelatorio superControleRelatorio, ExpedicaoDiplomaVO expedicaoDiplomaVO, String tipoLayout, Boolean layoutPersonalizado, Boolean assinarDigitalmente, ConfiguracaoHistoricoVO configuracaoHistoricoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	public void realizarGeracaoDocumentacaoAcademica(ExpedicaoDiplomaVO expedicaoDiplomaVO, SuperControleRelatorio superControleRelatorio, UsuarioVO usuarioVO) throws Exception;

	public String realizarGeracaoNomeArquivoExpedicaoDiploma(String origemUtilizar, MatriculaVO matriculaVO, UsuarioVO usuarioVO);

	public void carregarViaAnteriorExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO);

	public Integer consultarExpedicaoDiplomaUtilizarHistoricoDigital(String matricula);

	public void realizarFiltragemAlunosPermitirGerarXML(List<ExpedicaoDiplomaVO> expedicaoDiplomaVOs, String filtroSituacaoDiplomaDigital, String filtroSituacaoDocumentacaoAcademica);

	public void validarViaExpedicaoDiplomaValida(ExpedicaoDiplomaVO expedicaoDiplomaVO);

	public void realizarCorrecaoDocumentacaoMatriculaPorExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiploma, ProgressBarVO progressBar) throws Exception;

}
