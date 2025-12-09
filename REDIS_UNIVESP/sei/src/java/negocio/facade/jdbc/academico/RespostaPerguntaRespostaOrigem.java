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

import negocio.comuns.academico.RespostaPerguntaRespostaOrigemVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.RespostaPerguntaRespostaOrigemInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>RespostaPerguntaRespostaOrigemVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PerguntaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see RespostaPerguntaRespostaOrigemVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class RespostaPerguntaRespostaOrigem extends ControleAcesso implements RespostaPerguntaRespostaOrigemInterfaceFacade {
    protected static String idEntidade;

    public RespostaPerguntaRespostaOrigem() throws Exception {
        super();
        setIdEntidade("RespostaPerguntaRespostaOrigem");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PerguntaVO</code>.
     */
    public RespostaPerguntaRespostaOrigemVO novo() throws Exception {
    	RespostaPerguntaRespostaOrigem.incluir(getIdEntidade());
    	RespostaPerguntaRespostaOrigemVO obj = new RespostaPerguntaRespostaOrigemVO();
        return obj;
    }
    
    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>RespostaPerguntaRespostaOrigem</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>RespostaPerguntaRespostaOrigem</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final RespostaPerguntaRespostaOrigemVO obj, UsuarioVO usuario) throws Exception {
        try {        	
            //PerguntaVO.validarDados(obj);
        	//RespostaPerguntaRespostaOrigem.incluir(getIdEntidade(), usuario);
        	incluir(obj, "respostaPerguntaRespostaOrigem", new AtributoPersistencia()
					.add("respostaPergunta", obj.getRespostaPerguntaVO())
					.add("perguntaRespostaOrigem", obj.getPerguntaRespostaOrigemVO())
					.add("respostaAdicional", obj.getRespostaAdicional())
					.add("selecionado", obj.getSelecionado())
					, usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }
    
    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>RespostaPerguntaRespostaOrigemVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>RespostaPerguntaRespostaOrigemVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final RespostaPerguntaRespostaOrigemVO obj, UsuarioVO usuario) throws Exception {
        try {
           // PerguntaVO.validarDados(obj);
        	//RespostaPerguntaRespostaOrigem.alterar(getIdEntidade(), usuario);
        	alterar(obj, "respostaPerguntaRespostaOrigem", new AtributoPersistencia()
					.add("respostaPergunta", obj.getRespostaPerguntaVO())
					.add("perguntaRespostaOrigem", obj.getPerguntaRespostaOrigemVO())
					.add("respostaAdicional", obj.getRespostaAdicional())
					.add("selecionado", obj.getSelecionado())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);

        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
    	PerguntaRespostaOrigem.idEntidade = idEntidade;
    }
    
	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return PerguntaRespostaOrigem.idEntidade;
	}
	
	public List<RespostaPerguntaRespostaOrigemVO> consultarPorPerguntaRespostaOrigem(Integer codPerguntaRespostaOrigem, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		StringBuffer str = new StringBuffer();
		str.append(" select * from respostaperguntarespostaorigem ");
		str.append(" WHERE respostaperguntarespostaorigem.perguntarespostaorigem = ").append(codPerguntaRespostaOrigem);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<RespostaPerguntaRespostaOrigemVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<RespostaPerguntaRespostaOrigemVO> vetResultado = new ArrayList<RespostaPerguntaRespostaOrigemVO>(0);
		while (tabelaResultado.next()) {
			RespostaPerguntaRespostaOrigemVO obj = new RespostaPerguntaRespostaOrigemVO();
			montarDados(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	private void montarDados(RespostaPerguntaRespostaOrigemVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// Dados do PerguntaRespostaOrigemVO
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getRespostaPerguntaVO().setCodigo(dadosSQL.getInt("respostapergunta"));
		obj.getPerguntaRespostaOrigemVO().setCodigo(dadosSQL.getInt("perguntarespostaorigem"));
		obj.setRespostaAdicional(dadosSQL.getString("respostaadicional"));
		obj.setSelecionado(dadosSQL.getBoolean("selecionado"));	
		
		obj.setRespostaPerguntaVO(getFacadeFactory().getRespostaPerguntaFacade().consultarPorChavePrimaria(obj.getRespostaPerguntaVO().getCodigo(), nivelMontarDados, usuario));
						
	}	 
	
	public RespostaPerguntaRespostaOrigemVO consultarPorCodigoRespostaPerguntaCodigoPerguntaRespostaOrigem(Integer codigoRespostaPergunta, Integer codigoPerguntaRespostaOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM respostaPerguntaRespostaOrigem where respostaPergunta = " + codigoRespostaPergunta.intValue() + " and perguntaRespostaOrigem =  " + codigoPerguntaRespostaOrigem.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		RespostaPerguntaRespostaOrigemVO obj = new RespostaPerguntaRespostaOrigemVO();
		if(tabelaResultado.next()) {
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			obj.getRespostaPerguntaVO().setCodigo(tabelaResultado.getInt("respostapergunta"));
			obj.getPerguntaRespostaOrigemVO().setCodigo(tabelaResultado.getInt("perguntarespostaorigem"));
			obj.setRespostaAdicional(tabelaResultado.getString("respostaadicional"));
			obj.setSelecionado(tabelaResultado.getBoolean("selecionado"));	
			
			obj.setRespostaPerguntaVO(getFacadeFactory().getRespostaPerguntaFacade().consultarPorChavePrimaria(obj.getRespostaPerguntaVO().getCodigo(), nivelMontarDados, usuario));
			
		}
		return obj;

	}
	
}
