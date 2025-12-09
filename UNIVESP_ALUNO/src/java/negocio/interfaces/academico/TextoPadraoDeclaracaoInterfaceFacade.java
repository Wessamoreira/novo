package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface TextoPadraoDeclaracaoInterfaceFacade {
	public TextoPadraoDeclaracaoVO novo() throws Exception;

	public void incluir(TextoPadraoDeclaracaoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public void alterar(TextoPadraoDeclaracaoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public void excluir(TextoPadraoDeclaracaoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public TextoPadraoDeclaracaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorDataDefinicao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorResponsavelDefinicao(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List consultarPorMatricula(String matricula, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

    public List consultarPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean visaoTextoPadraoDeclaracao,  List<String>listaTipo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorResponsavelDefinicao(String valorConsulta, Integer unidadeEnsino,  List<String>listaTipo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, List<String>listaTipo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataDefinicao(Date prmIni, Date prmFim, Integer unidadeEnsino,  List<String>listaTipo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<TextoPadraoDeclaracaoVO> consultarPorTipo(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public String removerBordaDaPagina(String texto) throws Exception;
	
	public String substituirValorAtribuidoClass(String texto, String classe, String valor) throws Exception;
	
	public String adicionarStyleFormatoPaginaTextoPadrao(String texto, String orientacaoDaPagina) throws Exception;

	String consultaTextoDoTextoPadraoPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

	List<TextoPadraoDeclaracaoVO> consultarPorTipoRequerimento(String valorConsulta, Integer unidadeEnsino,String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public void adicionarArquivoIreport(List<ArquivoVO> lista, ArquivoVO obj,  String origemArquivo) throws Exception;
	
	public void removerArquivoIreport(ArquivoVO arquivoSelecionado, TextoPadraoDeclaracaoVO obj) throws Exception;
	
	public List consultarTipoAtaColacao(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Boolean consultarTextoPadraoAssinarDigitalmente(Integer textoPadraoDeclaracao) throws Exception;
}