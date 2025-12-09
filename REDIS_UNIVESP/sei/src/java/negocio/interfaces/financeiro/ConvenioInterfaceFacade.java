package negocio.interfaces.financeiro;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.ParceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ConvenioInterfaceFacade {
	

    public ConvenioVO novo() throws Exception;
    public void incluir(ConvenioVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(ConvenioVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(ConvenioVO obj, UsuarioVO usuario) throws Exception;
    public List consultarPorAtivoVigente(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;
    public ConvenioVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorDataAssinatura(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeParceiro(String valorConsulta, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    /**
     * Responsável por realizar uma consulta de <code>Convenio</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Parceiro</code> que esteja ativo ou não no sistema, de acordo com o
     * atributo <code>boolean ativo</code>.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @author Paulo Taucci
     * @param valorConsulta
     * @param ativo
     * @param controlarAcesso
     * @param nivelMontarDados
     * @return  List Contendo vários objetos da classe <code>ConvenioVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeParceiroESituacao(String valorConsulta, boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    /**
     * Responsável por realizar uma consulta de <code>Convenio</code> através do valor do atributo 
     * <code>String descricao</code> que esteja ativo ou não no sistema, de acordo com o
     * atributo <code>boolean ativo</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @author Paulo Taucci
     * @param valorConsulta
     * @param ativo
     * @param controlarAcesso
     * @param nivelMontarDados
     * @return List Contendo vários objetos da classe <code>ConvenioVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricaoESituacao(String valorConsulta, boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    /**
     * Responsável por realizar uma consulta de <code>Convenio</code> por ativo vigente que esteja ativo ou não 
     * no sistema, de acordo com o atributo <code>boolean ativo</code>. Retorna os objetos, com início do valor
     * do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @author Paulo Taucci
     * @param ativo
     * @param controlarAcesso
     * @param nivelMontarDados
     * @return List Contendo vários objetos da classe <code>ConvenioVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorAtivoVigenteESituacao(boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    /**
     * Responsável por realizar uma consulta de <code>Convenio</code> através do valor do atributo 
     * <code>Integer codigo</code> que esteja ativo ou não no sistema, de acordo com o atributo <code>boolean ativo</code>.
     * Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @author Paulo Taucci
     * @param valorConsulta
     * @param ativo
     * @param controlarAcesso
     * @param nivelMontarDados
     * @return List Contendo vários objetos da classe <code>ConvenioVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoESituacao(Integer valorConsulta, boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    /**
     * Método responsável por INATIVAR um ConvenioVO. Altera campos ativo, dataInativacao e responsavelInativacao
     * @author Paulo Taucci
     * @param obj
     * @throws Exception
     */
    public void realizarAlterarConvenioParaInativo(final ConvenioVO obj, UsuarioVO usuario) throws Exception;
    
    /**
     * Método responsável por ATIVAR um ConvenioVO. Altera campos ativo, dataAtivacao e responsavelAtivacao
     * @author Paulo Taucci
     * @param obj
     * @throws Exception
     */
    public void realizarAlterarConvenioParaAtivo(final ConvenioVO obj, UsuarioVO usuario) throws Exception;
    
    /**
     * Método responsável por ativar um convênio inativado ou vice versa.
     * @author Paulo Taucci
     * @param convenioVO
     * @return
     * @throws Exception
     */
    public String realizarAtivacaoInativacao(ConvenioVO convenioVO, UsuarioVO usuario) throws Exception;
    /**
     * Responsável por realizar uma consulta de <code>Convenio</code> através do valor do atributo 
     * <code>boolean ativo</code>.
     * Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @author Paulo Taucci
     * @param ativo
     * @param controlarAcesso
     * @param nivelMontarDados
     * @return List Contendo vários objetos da classe <code>ConvenioVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorSituacao(boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public void setIdEntidade(String aIdEntidade);
    public void validarPorcentagensDescontosProgressivos(ConvenioVO convenioVO, UsuarioVO usuarioLogado) throws Exception;
    
    public List consultarPorPlanoFinanceiroAluno(Integer planoFinanceiroAluno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    List<ConvenioVO> consultarConvenioAptoUsarNaMatricula(Integer unidadeEnsino, Integer curso, Integer turno, boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void alterarSituacao(final ConvenioVO obj, UsuarioVO usuario) throws Exception;
    public Boolean consultarPorCodigoConvenioAbateValorCusteadoContaAReceber(Integer valorConsulta, boolean contaAReceberDeParcela, UsuarioVO usuario) throws Exception;

	List<ConvenioVO> consultarPorParceiro(Integer parceiro, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	List<ConvenioVO> consultarConvenioFiltrarRenovacaoTurmaNivelCombobox(Integer turma, Integer gradeCurricular, String ano, String semestre) throws Exception;
	void realizarPersistenciaPorRenovacaoMatricula(ConvenioVO obj, ParceiroVO parceiro, UsuarioVO usuario) throws Exception;
	ConvenioVO consultarConvenioPadrao(ParceiroVO parceiro, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	void persistir(ConvenioVO obj, UsuarioVO usuarioLogado) throws Exception;
	List<ConvenioVO> consultarConvenioAptoUsarNaMatriculaPorParceiro(Integer unidadeEnsino, Integer curso, Integer turno, Integer parceiro, boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	List<ConvenioVO> consultarConvenioPorSituacaoNivelCombobox(String situacao, UsuarioVO usuarioVO) throws Exception;
}