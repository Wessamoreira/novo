package negocio.interfaces.processosel;

import java.util.Date;
import java.util.List;
import java.util.Map;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoGabaritoDataVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.ProcessoSeletivoProvaDataVO;

public interface ItemProcSeletivoDataProvaInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
     */
    public ItemProcSeletivoDataProvaVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(final ItemProcSeletivoDataProvaVO obj, UsuarioVO usuarioVO) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(final ItemProcSeletivoDataProvaVO obj, UsuarioVO usuarioVO) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(ItemProcSeletivoDataProvaVO obj, UsuarioVO usuarioVO) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>ProcSeletivoDisciplinasProcSeletivo</code> através do valor do atributo
     * <code>nome</code> da classe <code>DisciplinasProcSeletivo</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoProcSeletivo(Integer valorConsulta, int nivelMontarDados,boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>ProcSeletivoDisciplinasProcSeletivoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ProcSeletivoDisciplinasProcSeletivo</code>.
     * @param <code>procSeletivo</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List consultarPorDataProva(Date valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>ProcSeletivoDisciplinasProcSeletivoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirProcSeletivoDisciplinasProcSeletivos</code> e <code>incluirProcSeletivoDisciplinasProcSeletivos</code> disponíveis na classe <code>ProcSeletivoDisciplinasProcSeletivo</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>ProcSeletivoDisciplinasProcSeletivoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>processosel.ProcSeletivo</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirItemProcSeletivoDataProva(Integer procSeletivo, UsuarioVO usuarioVO) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public void alterarItemProcSeletivoDataProva(Integer procSeletivo, List objetos, UsuarioVO usuarioVO) throws Exception;

    public void incluirItemProcSeletivoDataProva(Integer procSeletivo, List<ItemProcSeletivoDataProvaVO> objetos, UsuarioVO usuarioVO) throws Exception;

//        public static List consultarItemProcSeletivoDataProva(Integer procSeletivo, int nivelMontarDados) throws Exception;
    public ItemProcSeletivoDataProvaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public List<ItemProcSeletivoDataProvaVO> consultarPorCodigoProcessoSeletivo(Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    void adicionarProcessoSeletivoProvaDataVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, ProcessoSeletivoProvaDataVO processoSeletivoProvaDataVO) throws Exception;

    void removerProcessoSeletivoProvaDataVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, ProcessoSeletivoProvaDataVO processoSeletivoProvaDataVO) throws Exception;
    
    public void adicionarProcessoSeletivoGabaritoDataVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, ProcSeletivoGabaritoDataVO procSeletivoGabaritoVO, UsuarioVO usuarioVO) throws Exception;
    
    public void removerProcessoSeletivoGabaritoDataVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, ProcSeletivoGabaritoDataVO procSeletivoGabaritoDataVO) throws Exception;
    
    public String consultarTipoProvaGabaritoPorInscricao(Integer inscricao, UsuarioVO usuarioVO);

	List<ItemProcSeletivoDataProvaVO> consultarPorProcSelectivoAptoInscricao(Integer valorConsulta, boolean visaoCandidato, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<ItemProcSeletivoDataProvaVO> consultarPorCodigoProcessoSeletivoProvaJaRealizada(Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ItemProcSeletivoDataProvaVO> consultarPorProcSelectivo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
        
    public void adicionarObjItemProcSeletivoDataProvaVOsAteDataFutura(ProcSeletivoVO procSeletivo, ItemProcSeletivoDataProvaVO itemDataProva, Boolean objReferenteGabarito, UsuarioVO usuario) throws Exception;
        
    public List<Date> consultarPorListaProcSeletivoComboBox(List<ProcSeletivoVO> listaProcessoSeletivoVOs, UsuarioVO usuarioVO);
        
   public void validarDados(ItemProcSeletivoDataProvaVO obj) throws Exception ;    
        
   public Integer consultarNumeroCandidatoNotificado(ProcSeletivoVO processoSeletivo, ItemProcSeletivoDataProvaVO itemProcessoSeletivoDataProva) throws Exception ; 
   
   public Date consultarDataProvaPorCodigo(Integer codigoItemProcSeletivoDataProva)throws Exception ;
   
   public ItemProcSeletivoDataProvaVO consultarMaiorDatas(Integer codigoItemProcSeletivoDataProva) throws Exception;

   ItemProcSeletivoDataProvaVO inicializarDadosItemImportacaoCandidatoInscricao(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, ProcSeletivoVO procSeletivoVO, Map<String, ItemProcSeletivoDataProvaVO> mapItemProcSeltivoDataProvaVOs, UsuarioVO usuario) throws Exception;

}
