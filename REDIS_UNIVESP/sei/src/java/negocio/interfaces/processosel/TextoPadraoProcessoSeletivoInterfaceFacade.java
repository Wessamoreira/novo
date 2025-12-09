package negocio.interfaces.processosel;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.TextoPadraoProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.enumeradores.TipoRelatorioEstatisticoProcessoSeletivoEnum;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface TextoPadraoProcessoSeletivoInterfaceFacade {
	public TextoPadraoProcessoSeletivoVO novo() throws Exception;

	public void incluir(TextoPadraoProcessoSeletivoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public void alterar(TextoPadraoProcessoSeletivoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public void excluir(TextoPadraoProcessoSeletivoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public TextoPadraoProcessoSeletivoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorDataDefinicao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorResponsavelDefinicao(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List consultarPorMatricula(String matricula, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

    public List consultarPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorResponsavelDefinicao(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataDefinicao(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<TextoPadraoProcessoSeletivoVO> consultarPorTipo(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public String removerBordaDaPagina(String texto) throws Exception;
	
	public String substituirValorAtribuidoClass(String texto, String classe, String valor) throws Exception;
	
	public String adicionarStyleFormatoPaginaTextoPadrao(String texto, String orientacaoDaPagina) throws Exception;

	String consultaTextoDoTextoPadraoPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;
	
	public String imprimirTextoPadrao(TextoPadraoProcessoSeletivoVO texto, InscricaoVO inscricao, List<InscricaoVO> listaImpressao, String tipo, Boolean PDF, TipoRelatorioEstatisticoProcessoSeletivoEnum tipoLayout, ConfiguracaoGeralSistemaVO configGeralSistema,  String versaoSoftware, UsuarioVO usuarioLogado) throws Exception;
	
}