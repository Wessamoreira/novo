package negocio.interfaces.processosel;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.processosel.PreInscricaoVO;
import webservice.servicos.objetos.LeadRSVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface PreInscricaoInterfaceFacade {

    public PreInscricaoVO novo() throws Exception;

    public void incluir(PreInscricaoVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiro, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

    public void alterar(PreInscricaoVO obj, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception;

    public void excluir(PreInscricaoVO obj,UsuarioVO usuario) throws Exception;

    public PreInscricaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorData(Date prmIni, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public ConfiguracaoFinanceiroVO consultarConfiguracaoFinanceiro(UsuarioVO usuario, Integer codigoUnidadeEnsino) throws Exception;

    public PerfilAcessoVO consultarPerfilAcessoPadrao(Integer unidadeEnsino,UsuarioVO usuario) throws Exception;

    public void incluir(final PreInscricaoVO obj, boolean verificarAcesso,ConfiguracaoFinanceiroVO configuracaoFinanceiro, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

    public void alterar(final PreInscricaoVO obj, boolean verificarAcesso, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception;
    
    public void realizarPersistenciaCompromissoPreInscricao(final PreInscricaoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
    
    public void realizarPersistenciaCompromissoPreInscricao(final PreInscricaoVO obj, boolean verificarAcesso, UsuarioVO usuario, FuncionarioVO funcionarioAgenda) throws Exception;            
    
    public void incluirPreInscricaoAPartirSiteOuHomePreInscricao(final PreInscricaoVO obj, ConfiguracaoGeralSistemaVO configuracaoVO) throws Exception;

    public void alterarProspectPreInscricao(final Integer codProspectManter, final Integer codProspectRemover) throws Exception;

	void incluirPreInscricaoAPartirMatriculaExternaOnline(PreInscricaoVO obj) throws Exception;

	void incluirSemValidarDados(PreInscricaoVO obj, boolean verificarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 13 de jun de 2016 
	 * @param pessoaVO
	 * @param unidadeEnsinoVO
	 * @param cursoVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	PreInscricaoVO montarDadosPreInscricaoPessoa(PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * 
	 * Monta os dados da Pre Inscricao a partir de uma LEAD
	 * 
	 * Existe a possibilidade de existir mais de um CursoDeInteresse, por isso a lista de PreInscricaoVO
	 * 
	 * @param lead
	 * @return List<PreInscricaoVO> Lista de PreInscricaoVO populada com os dados enviados da lead
	 * @throws Exception
	 */
	public PreInscricaoVO montarDadosAPartirDaLeadIntegracaoRDStation(LeadRSVO lead) throws Exception;
	
	void montarDadosCursoInteresseCompromissoAgendaPessoaHorarioPreInscricao(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO) throws Exception;
}