package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.PerguntaChecklistOrigemVO;
import negocio.comuns.academico.QuestionarioRespostaOrigemMotivosPadroesEstagioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.QuestionarioRespostaOrigemMotivosPadroesEstagioInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PerguntaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PerguntaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see QuestionarioRespostaOrigemMotivosPadroesEstagioVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class QuestionarioRespostaOrigemMotivosPadroesEstagio extends ControleAcesso implements QuestionarioRespostaOrigemMotivosPadroesEstagioInterfaceFacade {

	private static final long serialVersionUID = 890781495433227275L;

	protected static String idEntidade;

    public QuestionarioRespostaOrigemMotivosPadroesEstagio() throws Exception {
        super();
        setIdEntidade("QuestionarioRespostaOrigem");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PerguntaVO</code>.
     */
    public QuestionarioRespostaOrigemMotivosPadroesEstagioVO novo() throws Exception {
    	QuestionarioRespostaOrigemMotivosPadroesEstagio.incluir(getIdEntidade());
        QuestionarioRespostaOrigemMotivosPadroesEstagioVO obj = new QuestionarioRespostaOrigemMotivosPadroesEstagioVO();
        return obj;
    }
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<QuestionarioRespostaOrigemMotivosPadroesEstagioVO> lista, UsuarioVO usuarioVO) {
		try {
			for (QuestionarioRespostaOrigemMotivosPadroesEstagioVO obj : lista) {
				if (obj.getCodigo() == 0) {
					incluir(obj, usuarioVO);
				} else {
					alterar(obj, usuarioVO);
				}	
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
    
    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PerguntaRespostaOrigem</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PerguntaRespostaOrigem</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final QuestionarioRespostaOrigemMotivosPadroesEstagioVO obj, UsuarioVO usuario) throws Exception {
        try {
        	incluir(obj, "questionarioRespostaOrigemMotivosPadroesEstagio", new AtributoPersistencia()
					.add("questionarioRespostaOrigem", obj.getQuestionarioRespostaOrigemVO())
					.add("motivosPadroesEstagio", obj.getMotivosPadroesEstagioVO())
					.add("selecionado", obj.getSelecionado())
					, usuario);            
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
        }
    }
    
    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>QuestionarioRespostaOrigemMotivosPadroesEstagioVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>QuestionarioRespostaOrigemMotivosPadroesEstagioVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final QuestionarioRespostaOrigemMotivosPadroesEstagioVO obj, UsuarioVO usuario) throws Exception {
        try {
        	alterar(obj, "questionarioRespostaOrigemMotivosPadroesEstagio", new AtributoPersistencia()
					.add("questionarioRespostaOrigem", obj.getQuestionarioRespostaOrigemVO())
					.add("motivosPadroesEstagio", obj.getMotivosPadroesEstagioVO())
					.add("selecionado", obj.getSelecionado())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario); 
        	            
        } catch (Exception e) {
			throw new StreamSeiException(e);
        }
    }
    
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
    	QuestionarioRespostaOrigemMotivosPadroesEstagio.idEntidade = idEntidade;
    }
    
	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return QuestionarioRespostaOrigemMotivosPadroesEstagio.idEntidade;
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public QuestionarioRespostaOrigemMotivosPadroesEstagioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder str = new StringBuilder();
		str.append(" select * from questionarioRespostaOrigemMotivosPadroesEstagio ");
		str.append(" WHERE questionarioRespostaOrigemMotivosPadroesEstagio.codigo = ").append(codigoPrm);
		str.append(" order by questionarioRespostaOrigemMotivosPadroesEstagio.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (QuestionarioRespostaOrigemMotivosPadroesEstagioVO).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<QuestionarioRespostaOrigemMotivosPadroesEstagioVO> consultarPorQuestionarioOrigem(Integer codQuestionario, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		StringBuilder str = new StringBuilder();
		str.append(" select * from questionarioRespostaOrigemMotivosPadroesEstagio ");
		str.append(" WHERE questionarioRespostaOrigemMotivosPadroesEstagio.questionariorespostaorigem = ").append(codQuestionario);
		str.append(" order by questionarioRespostaOrigemMotivosPadroesEstagio.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}	
	
	public List<QuestionarioRespostaOrigemMotivosPadroesEstagioVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<QuestionarioRespostaOrigemMotivosPadroesEstagioVO> vetResultado = new ArrayList<QuestionarioRespostaOrigemMotivosPadroesEstagioVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados( tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}
	
	private QuestionarioRespostaOrigemMotivosPadroesEstagioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// Dados do QuestionarioRespostaOrigemMotivosPadroesEstagioVO
		QuestionarioRespostaOrigemMotivosPadroesEstagioVO obj = new QuestionarioRespostaOrigemMotivosPadroesEstagioVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));		
		obj.getQuestionarioRespostaOrigemVO().setCodigo(dadosSQL.getInt("questionarioRespostaOrigem"));			
		obj.getMotivosPadroesEstagioVO().setCodigo(dadosSQL.getInt("motivosPadroesEstagio"));
		if(Uteis.isAtributoPreenchido(obj.getMotivosPadroesEstagioVO())) {
			obj.setMotivosPadroesEstagioVO(getFacadeFactory().getMotivosPadroesEstagioFacade().consultarPorChavePrimaria(obj.getMotivosPadroesEstagioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		obj.setSelecionado(dadosSQL.getBoolean("selecionado"));			
		return obj;
	}
	
	
}
