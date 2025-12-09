package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.TextoPadraoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface TextoPadraoInterfaceFacade {
	public TextoPadraoVO novo() throws Exception;

	public void incluir(TextoPadraoVO obj,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public void alterar(TextoPadraoVO obj,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public void excluir(TextoPadraoVO obj,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public TextoPadraoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoSituacao(Integer valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorDataDefinicao(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorResponsavelDefinicao(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List consultarPorTipo(String nomePrm, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

	public List consultarPorMatricula(String matricula, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

        public List consultarPorTipoNivelComboBox(String valorConsulta, UnidadeEnsinoVO unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

		List consultarPorTipoEUnidadeEnsino(String valorConsulta, Integer codigoUnidade, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		Boolean verificarTextoPadraoAssinaDigitalmentePorMatricula(String valorConsulta, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception;

		TextoPadraoVO consultarTextoPadraoContratoMatriculaPorMatriculaPeriodo(Integer valorConsulta,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		TextoPadraoVO consultarTextoPadraoContratoMatriculaPorCurso(Integer valorConsulta, boolean controlarAcesso,	int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List consultaSimplesTextoPadraoFinanceiro(int nivelMontarDados, UsuarioVO usuario) throws Exception;
}