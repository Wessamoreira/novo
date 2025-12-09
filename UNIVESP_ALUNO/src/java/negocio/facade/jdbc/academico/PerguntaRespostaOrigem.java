package negocio.facade.jdbc.academico;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

//import org.bouncycastle.crypto.tls.HashAlgorithm;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.PerguntaItemRespostaOrigemVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.RespostaPerguntaRespostaOrigemVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PerguntaRespostaOrigemInterfaceFacade;

/**
 * Classe de persist√™ncia que encapsula todas as opera√ß√µes de manipula√ß√£o dos dados da classe <code>PerguntaVO</code>.
 * Respons√°vel por implementar opera√ß√µes como incluir, alterar, excluir e consultar pertinentes a classe <code>PerguntaVO</code>.
 * Encapsula toda a intera√ß√£o com o banco de dados.
 * @see PerguntaRespostaOrigemVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class PerguntaRespostaOrigem extends ControleAcesso implements PerguntaRespostaOrigemInterfaceFacade {

	private static final long serialVersionUID = 890781495433227275L;

	protected static String idEntidade;

    public PerguntaRespostaOrigem() throws Exception {
        super();
        setIdEntidade("PerguntaRespostaOrigem");
    }

    /**
     * Opera√ß√£o respons√°vel por retornar um novo objeto da classe <code>PerguntaVO</code>.
     */
    public PerguntaRespostaOrigemVO novo() throws Exception {
    	PerguntaRespostaOrigem.incluir(getIdEntidade());
        PerguntaRespostaOrigemVO obj = new PerguntaRespostaOrigemVO();
        return obj;
    }
    
    /**
     * Opera√ß√£o respons√°vel por incluir no banco de dados um objeto da classe <code>PerguntaRespostaOrigem</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conex√£o com o banco de dados e a permiss√£o do usu√°rio
     * para realizar esta operac√£o na entidade.
     * Isto, atrav√©s da opera√ß√£o <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PerguntaRespostaOrigem</code> que ser√° gravado no banco de dados.
     * @exception Exception Caso haja problemas de conex√£o, restri√ß√£o de acesso ou valida√ß√£o de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PerguntaRespostaOrigemVO obj, UsuarioVO usuario) throws Exception {
        try {
        	validarDadosPerguntaRespostaOrigemVO(obj);
        	incluir(obj, "perguntaRespostaOrigem", new AtributoPersistencia()
					.add("questionarioRespostaOrigem", obj.getQuestionarioRespostaOrigemVO())
					.add("perguntaQuestionario", obj.getPerguntaQuestionarioVO())
					.add("respostaPergunta", obj.getRespostaPerguntaVO())
					.add("pergunta", obj.getPerguntaVO())
					.add("texto", obj.getTexto())
					.add("data", Uteis.getDataJDBC(obj.getData()))
					.add("verdadeiroFalse", obj.getVerdadeiroFalse())
					.add("numerico", obj.getNumerico())
					.add("hora", obj.getHora())
					.add("ordem", obj.getOrdem())
					, usuario);
            if(Uteis.isAtributoPreenchido(obj.getRespostaPerguntaRespostaOrigemVOs())) {
	        	for (RespostaPerguntaRespostaOrigemVO respostaPerguntaRespostaOrigemVO : obj.getRespostaPerguntaRespostaOrigemVOs()) {
	        		getFacadeFactory().getRespostaPerguntaRespostaOrigemInterfaceFacade().incluir(respostaPerguntaRespostaOrigemVO, usuario);
	        	}
			}
        	   
            if(Uteis.isAtributoPreenchido(obj.getPerguntaItemRespostaOrigemAdicionadaVOs())) {
	        	for (List<PerguntaItemRespostaOrigemVO> listPerguntaItemRespostaOrigemVO :  obj.getPerguntaItemRespostaOrigemAdicionadaVOs()) {
	        		for (PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigemVO : listPerguntaItemRespostaOrigemVO) {
	        			perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO().setQuestionarioRespostaOrigemVO(obj.getQuestionarioRespostaOrigemVO());
	        			incluir(perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO(), usuario);
	        			getFacadeFactory().getPerguntaItemRespostaOrigemInterfaceFacade().incluir(perguntaItemRespostaOrigemVO, usuario);
	        		}
	        	}
            }            
            getFacadeFactory().getPerguntaChecklistOrigemFacade().persistir(obj.getListaPerguntaChecklistOrigem(), usuario);
            if(Uteis.isAtributoPreenchido(obj.getListaArquivoVOs())) {
            	getFacadeFactory().getArquivoFacade().incluirArquivos(obj.getListaArquivoVOs(), obj.getCodigo(), OrigemArquivo.PERGUNTA_RESPOSTA_ORIGEM_ANEXO, usuario, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0,usuario));	
            }
            if(Uteis.isAtributoPreenchido(obj.getArquivoRespostaVO().getNome())) {
            	obj.getArquivoRespostaVO().setValidarDados(false);
            	obj.getArquivoRespostaVO().setCodOrigem(obj.getCodigo());
            	obj.getArquivoRespostaVO().setOrigem(OrigemArquivo.PERGUNTA_RESPOSTA_ORIGEM.getValor());
                getFacadeFactory().getArquivoFacade().persistir(obj.getArquivoRespostaVO(), false, usuario, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0,usuario));	
            }
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }
    
    /**
     * Opera√ß√£o respons√°vel por alterar no BD os dados de um objeto da classe <code>PerguntaRespostaOrigemVO</code>.
     * Sempre utiliza a chave prim√°ria da classe como atributo para localiza√ß√£o do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conex√£o com o banco de dados e a permiss√£o do usu√°rio
     * para realizar esta operac√£o na entidade.
     * Isto, atrav√©s da opera√ß√£o <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PerguntaRespostaOrigemVO</code> que ser√° alterada no banco de dados.
     * @exception Execption Caso haja problemas de conex√£o, restri√ß√£o de acesso ou valida√ß√£o de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PerguntaRespostaOrigemVO obj, UsuarioVO usuario) throws Exception {
        try {
           // PerguntaVO.validarDados(obj);
        	//PerguntaRespostaOrigem.alterar(getIdEntidade(), usuario);
        	validarDadosPerguntaRespostaOrigemVO(obj);
        	alterar(obj, "perguntaRespostaOrigem", new AtributoPersistencia()
					.add("questionarioRespostaOrigem", obj.getQuestionarioRespostaOrigemVO())
					.add("perguntaQuestionario", obj.getPerguntaQuestionarioVO())
					.add("respostaPergunta", obj.getRespostaPerguntaVO())
					.add("pergunta", obj.getPerguntaVO())
					.add("texto", obj.getTexto())
					.add("data", Uteis.getDataJDBC(obj.getData()))
					.add("verdadeiroFalse", obj.getVerdadeiroFalse())
					.add("numerico", obj.getNumerico())
					.add("hora", obj.getHora())
					.add("ordem", obj.getOrdem())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario); 
        	
            if(Uteis.isAtributoPreenchido(obj.getRespostaPerguntaRespostaOrigemVOs())) {
            	for (RespostaPerguntaRespostaOrigemVO respostaPerguntaRespostaOrigemVO : obj.getRespostaPerguntaRespostaOrigemVOs()) {
            		getFacadeFactory().getRespostaPerguntaRespostaOrigemInterfaceFacade().alterar(respostaPerguntaRespostaOrigemVO, usuario);
    			}
            }
            
            if(Uteis.isAtributoPreenchido(obj.getPerguntaItemRespostaOrigemAdicionadaVOs())) {
	        	List<PerguntaItemRespostaOrigemVO> listaPerguntaItemRespostaOrigem = new ArrayList<PerguntaItemRespostaOrigemVO>(0);    
	        	List<PerguntaItemRespostaOrigemVO> listaPerguntaItemRespostaOrigemAexcluir = new ArrayList<PerguntaItemRespostaOrigemVO>(0);  
	        	for (List<PerguntaItemRespostaOrigemVO> listPerguntaItemRespostaOrigemVO :  obj.getPerguntaItemRespostaOrigemAdicionadaVOs()) {       		
	        		for (PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigemVO : listPerguntaItemRespostaOrigemVO) {
	        			if(!Uteis.isAtributoPreenchido(perguntaItemRespostaOrigemVO.getCodigo())) {
	        				perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO().setQuestionarioRespostaOrigemVO(obj.getQuestionarioRespostaOrigemVO());
	            			incluir(perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO(), usuario);
	            			getFacadeFactory().getPerguntaItemRespostaOrigemInterfaceFacade().incluir(perguntaItemRespostaOrigemVO, usuario);
	        			}  
	        			listaPerguntaItemRespostaOrigem.add(perguntaItemRespostaOrigemVO);
	        		}           		
	        	}
	        	if (Uteis.isAtributoPreenchido(listaPerguntaItemRespostaOrigem)) {
	        		listaPerguntaItemRespostaOrigemAexcluir = getFacadeFactory().getPerguntaItemRespostaOrigemInterfaceFacade().consultarPerguntaItemRespostaOrigemAexcluir(listaPerguntaItemRespostaOrigem, obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
	        	}
	        	
	        	for (PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigemVO : listaPerguntaItemRespostaOrigemAexcluir) {
	        		excluir(perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO(), usuario);					
				}        	
	        	
	        	getFacadeFactory().getPerguntaItemRespostaOrigemInterfaceFacade().reorganizarOrdemPerguntaItemRespostaOrigem(obj.getPerguntaItemRespostaOrigemAdicionadaVOs(), usuario);
            }
            validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaPerguntaChecklistOrigem(), "perguntaChecklistOrigem", "perguntaRespostaOrigem", obj.getCodigo(), usuario);
            getFacadeFactory().getPerguntaChecklistOrigemFacade().persistir(obj.getListaPerguntaChecklistOrigem(), usuario);
            getFacadeFactory().getArquivoFacade().alterarArquivos(obj.getCodigo(), obj.getListaArquivoVOs(), OrigemArquivo.PERGUNTA_RESPOSTA_ORIGEM_ANEXO, usuario, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0,usuario));
            if(Uteis.isAtributoPreenchido(obj.getArquivoRespostaVO().getNome())) {
            	obj.getArquivoRespostaVO().setValidarDados(false);
            	obj.getArquivoRespostaVO().setCodOrigem(obj.getCodigo());
            	obj.getArquivoRespostaVO().setOrigem(OrigemArquivo.PERGUNTA_RESPOSTA_ORIGEM.getValor());            	
                getFacadeFactory().getArquivoFacade().persistir(obj.getArquivoRespostaVO(), false, usuario, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0,usuario));	
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
    /**
     * Opera√ß√£o repons√°vel por definir um novo valor para o identificador desta classe.
     * Esta altera√ß√£o deve ser poss√≠vel, pois, uma mesma classe de neg√≥cio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso √© realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
    	PerguntaRespostaOrigem.idEntidade = idEntidade;
    }
    
	/**
	 * Opera√ß√£o repons√°vel por retornar o identificador desta classe. Este identificar √© utilizado para verificar as permiss√µes de acesso as opera√ß√µes desta classe.
	 */
	public static String getIdEntidade() {
		return PerguntaRespostaOrigem.idEntidade;
	}

	@Override
	public void validarDadosPerguntaRespostaOrigemVO(PerguntaRespostaOrigemVO perguntaRespostaOrigemVO)  {
		if((perguntaRespostaOrigemVO.getQuestionarioRespostaOrigemVO().getSituacaoQuestionarioRespostaOrigemEnum().isEmAnalise()  
				|| perguntaRespostaOrigemVO.getQuestionarioRespostaOrigemVO().getSituacaoQuestionarioRespostaOrigemEnum().isDeferido())
				&& perguntaRespostaOrigemVO.getPerguntaVO().getTipoRespostaTextual()	
				&& perguntaRespostaOrigemVO.getPerguntaQuestionarioVO().getRespostaObrigatoria()
				&& !Uteis.isAtributoPreenchido(perguntaRespostaOrigemVO.getTexto())) {			
			throw new StreamSeiException("A Pergunta de n˙mero "+perguntaRespostaOrigemVO.getOrdem() +" - "+perguntaRespostaOrigemVO.getPerguntaVO().getDescricao()+" deve ser informado.");			
		}
		else if((perguntaRespostaOrigemVO.getQuestionarioRespostaOrigemVO().getSituacaoQuestionarioRespostaOrigemEnum().isEmAnalise()  
				|| perguntaRespostaOrigemVO.getQuestionarioRespostaOrigemVO().getSituacaoQuestionarioRespostaOrigemEnum().isDeferido())
				&& perguntaRespostaOrigemVO.getPerguntaVO().getTipoRespostaNumerico() 
				&& perguntaRespostaOrigemVO.getPerguntaQuestionarioVO().getRespostaObrigatoria()
				&& !Uteis.isAtributoPreenchido(perguntaRespostaOrigemVO.getNumerico())) {			
			throw new StreamSeiException("A Pergunta de n˙mero "+perguntaRespostaOrigemVO.getOrdem() +" - "+perguntaRespostaOrigemVO.getPerguntaVO().getDescricao()+" deve ser informado.");			
		}
		else if((perguntaRespostaOrigemVO.getQuestionarioRespostaOrigemVO().getSituacaoQuestionarioRespostaOrigemEnum().isEmAnalise()  
				|| perguntaRespostaOrigemVO.getQuestionarioRespostaOrigemVO().getSituacaoQuestionarioRespostaOrigemEnum().isDeferido())
				&& perguntaRespostaOrigemVO.getPerguntaVO().getTipoRespostaData() 
				&& perguntaRespostaOrigemVO.getPerguntaQuestionarioVO().getRespostaObrigatoria()
				&& !Uteis.isAtributoPreenchido(perguntaRespostaOrigemVO.getData())) {			
			throw new StreamSeiException("A Pergunta de n˙mero "+perguntaRespostaOrigemVO.getOrdem() +" - "+perguntaRespostaOrigemVO.getPerguntaVO().getDescricao()+" deve ser informado.");			
		}
		else if((perguntaRespostaOrigemVO.getQuestionarioRespostaOrigemVO().getSituacaoQuestionarioRespostaOrigemEnum().isEmAnalise()  
				|| perguntaRespostaOrigemVO.getQuestionarioRespostaOrigemVO().getSituacaoQuestionarioRespostaOrigemEnum().isDeferido())
				&& perguntaRespostaOrigemVO.getPerguntaVO().getTipoRespostaHora()
				&& perguntaRespostaOrigemVO.getPerguntaQuestionarioVO().getRespostaObrigatoria()
				&& !Uteis.isAtributoPreenchido(perguntaRespostaOrigemVO.getHora())) {			
			throw new StreamSeiException("A Pergunta de n˙mero "+perguntaRespostaOrigemVO.getOrdem() +" - "+perguntaRespostaOrigemVO.getPerguntaVO().getDescricao()+" deve ser informado.");			
		} 
		else if((perguntaRespostaOrigemVO.getQuestionarioRespostaOrigemVO().getSituacaoQuestionarioRespostaOrigemEnum().isEmAnalise()  
				|| perguntaRespostaOrigemVO.getQuestionarioRespostaOrigemVO().getSituacaoQuestionarioRespostaOrigemEnum().isDeferido())
				&& (perguntaRespostaOrigemVO.getPerguntaVO().getTipoRespostaSimplesEscolha() || perguntaRespostaOrigemVO.getPerguntaVO().getTipoRespostaMultiplaEscolha()) 
				&& perguntaRespostaOrigemVO.getPerguntaQuestionarioVO().getRespostaObrigatoria()
				&& !perguntaRespostaOrigemVO.getRespostaPerguntaRespostaOrigemVOs().stream().anyMatch(p-> p.getSelecionado())) {			
			throw new StreamSeiException("A Pergunta de n˙mero "+perguntaRespostaOrigemVO.getOrdem() +" - "+perguntaRespostaOrigemVO.getPerguntaVO().getDescricao()+" deve ser informado.");			
		}
		else if((perguntaRespostaOrigemVO.getQuestionarioRespostaOrigemVO().getSituacaoQuestionarioRespostaOrigemEnum().isEmAnalise()  
				|| perguntaRespostaOrigemVO.getQuestionarioRespostaOrigemVO().getSituacaoQuestionarioRespostaOrigemEnum().isDeferido())
				&& perguntaRespostaOrigemVO.getPerguntaVO().getTipoRespostaUpload()
				&& perguntaRespostaOrigemVO.getPerguntaQuestionarioVO().getRespostaObrigatoria()
				&& !Uteis.isAtributoPreenchido(perguntaRespostaOrigemVO.getArquivoRespostaVO().getDescricao())) {			
			throw new StreamSeiException("A Pergunta de n˙mero "+perguntaRespostaOrigemVO.getOrdem() +" - "+perguntaRespostaOrigemVO.getPerguntaVO().getDescricao()+" deve ser informado.");			
		}
	}
	
	public StringBuilder consultaPadrao() {
		StringBuilder str = new StringBuilder("");
		str.append(" select perguntarespostaorigem.*, ");
		
		str.append(" respostapergunta.codigo as \"respostapergunta.codigo\", respostapergunta.descricao as \"respostapergunta.descricao\", ");
		
		str.append(" questionariorespostaorigem.questionario as \"questionariorespostaorigem.questionario\", questionariorespostaorigem.escopo as \"questionariorespostaorigem.escopo\", ");
		str.append(" questionariorespostaorigem.planoEnsino as \"questionariorespostaorigem.planoEnsino\", questionariorespostaorigem.requisicao as \"questionariorespostaorigem.requisicao\", ");
		str.append(" questionariorespostaorigem.estagio as \"questionariorespostaorigem.estagio\", ");
		
		str.append(" aro.codigo as \"aro.codigo\", aro.nome as \"aro.nome\",  aro.descricao as \"aro.descricao\", ");
		str.append(" aro.origem as \"aro.origem\", aro.codorigem as \"aro.codorigem\",  aro.pastabasearquivo as \"aro.pastabasearquivo\" ");
		str.append(" from perguntarespostaorigem ");
		str.append(" inner join questionariorespostaorigem on perguntarespostaorigem.questionariorespostaorigem = questionariorespostaorigem.codigo");
		str.append(" left join respostapergunta on perguntarespostaorigem.respostapergunta = respostapergunta.codigo ");
		str.append(" left join arquivo as aro on aro.codorigem = perguntarespostaorigem.codigo and aro.origem = '").append(OrigemArquivo.PERGUNTA_RESPOSTA_ORIGEM.name()).append("' ");
		return str;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PerguntaRespostaOrigemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder str = new StringBuilder();
		str.append(" select * from perguntarespostaorigem ");
		str.append(" WHERE perguntarespostaorigem.codigo = ").append(codigoPrm);
		str.append(" order by perguntarespostaorigem.ordem ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados N„o Encontrados (PerguntaRespostaOrigemVO).");
		}
		return (montarDadosBasicos(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<PerguntaRespostaOrigemVO> consultarPorQuestionarioOrigem(Integer codQuestionario, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		StringBuilder str = new StringBuilder(consultaPadrao());
		str.append(" WHERE perguntarespostaorigem.questionariorespostaorigem = ").append(codQuestionario);
		str.append(" order by perguntarespostaorigem.ordem ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<PerguntaRespostaOrigemVO> consultarPorQuestionarioPlanoEnsino(Integer codQuestionario, Integer codPlanoEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		StringBuilder str = new StringBuilder(consultaPadrao());
		str.append(" WHERE questionariorespostaorigem.questionario = ").append(codQuestionario);
		str.append(" and questionariorespostaorigem.planoensino = ").append(codPlanoEnsino);
		str.append(" and perguntarespostaorigem.perguntaquestionario is not null");
		str.append(" order by perguntarespostaorigem.ordem ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<PerguntaRespostaOrigemVO> consultarPorQuestionarioRequisicao(Integer codQuestionario, Integer codRequisicao, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		StringBuilder str = new StringBuilder(consultaPadrao());
		str.append(" WHERE questionariorespostaorigem.questionario = ").append(codQuestionario);
		str.append(" and questionariorespostaorigem.requisicao = ").append(codRequisicao);
		str.append(" and perguntarespostaorigem.perguntaquestionario is not null");
		str.append(" order by perguntarespostaorigem.ordem ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<PerguntaRespostaOrigemVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<PerguntaRespostaOrigemVO> vetResultado = new ArrayList<PerguntaRespostaOrigemVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados( tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}
	
	private PerguntaRespostaOrigemVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// Dados do PerguntaRespostaOrigemVO
		PerguntaRespostaOrigemVO obj = new PerguntaRespostaOrigemVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getPerguntaVO().setCodigo(dadosSQL.getInt("pergunta"));
		obj.setTexto(dadosSQL.getString("texto"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setVerdadeiroFalse(dadosSQL.getBoolean("verdadeirofalse"));
		obj.setNumerico(dadosSQL.getDouble("numerico"));
		obj.setHora(dadosSQL.getString("hora"));
		obj.setOrdem(dadosSQL.getInt("ordem"));
		obj.getPerguntaQuestionarioVO().setCodigo(dadosSQL.getInt("perguntaquestionario"));
		obj.getRespostaPerguntaVO().setCodigo(dadosSQL.getInt("respostapergunta"));
		
		
		obj.getQuestionarioRespostaOrigemVO().setCodigo(dadosSQL.getInt("questionariorespostaorigem"));
		obj.getQuestionarioRespostaOrigemVO().getQuestionarioVO().setCodigo(dadosSQL.getInt("questionariorespostaorigem.questionario"));
		obj.getQuestionarioRespostaOrigemVO().getPlanoEnsinoVO().setCodigo(dadosSQL.getInt("questionariorespostaorigem.planoensino"));
		obj.getQuestionarioRespostaOrigemVO().setEscopo(EscopoPerguntaEnum.valueOf(dadosSQL.getString("questionariorespostaorigem.escopo")));		
	
		if(Uteis.isAtributoPreenchido(obj.getRespostaPerguntaVO().getCodigo())) {
			obj.getRespostaPerguntaVO().setDescricao(dadosSQL.getString("respostapergunta.descricao"));
		}
		
		if(dadosSQL.getInt("aro.codigo") != 0) {
			obj.getArquivoRespostaVO().setNovoObj(false);
			obj.getArquivoRespostaVO().setCodigo(dadosSQL.getInt("aro.codigo"));	
			obj.getArquivoRespostaVO().setCodOrigem(dadosSQL.getInt("aro.codorigem"));	
			obj.getArquivoRespostaVO().setNome(dadosSQL.getString("aro.nome"));	
			obj.getArquivoRespostaVO().setDescricao(dadosSQL.getString("aro.descricao"));	
			obj.getArquivoRespostaVO().setPastaBaseArquivo(dadosSQL.getString("aro.pastabasearquivo"));	
			obj.getArquivoRespostaVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.toString(dadosSQL.getString("aro.pastabasearquivo")));	
			obj.getArquivoRespostaVO().setOrigem(dadosSQL.getString("aro.origem"));
		}
		obj.setPerguntaQuestionarioVO(getFacadeFactory().getPerguntaQuestionarioFacade().consultarPorChavePrimaria(obj.getPerguntaQuestionarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario));
		obj.setRespostaPerguntaRespostaOrigemVOs(getFacadeFactory().getRespostaPerguntaRespostaOrigemInterfaceFacade().consultarPorPerguntaRespostaOrigem(obj.getCodigo(), nivelMontarDados, usuario));
		obj.setPerguntaItemRespostaOrigemVOs(getFacadeFactory().getPerguntaItemRespostaOrigemInterfaceFacade().consultarPorPerguntaRespostaOrigem(obj.getCodigo(), nivelMontarDados, usuario));	
		obj.setPerguntaVO(getFacadeFactory().getPerguntaFacade().consultarPorChavePrimaria(obj.getPerguntaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
		obj.setListaPerguntaChecklistOrigem(getFacadeFactory().getPerguntaChecklistOrigemFacade().consultarPorPerguntaRespostaOrigem(obj.getCodigo(), nivelMontarDados, usuario));
		obj.setListaArquivoVOs(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigem(obj.getCodigo(), OrigemArquivo.PERGUNTA_RESPOSTA_ORIGEM_ANEXO.getValor(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuario));
		if(!obj.getListaArquivoVOs().isEmpty()) {
			String urlExternoDownloadArquivo = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo().getUrlExternoDownloadArquivo();
			obj.getListaArquivoVOs().stream().forEach(p-> p.montarCampoDescricao(urlExternoDownloadArquivo));
		}		
		return obj;
	}
	
	private PerguntaRespostaOrigemVO montarDadosBasicos(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// Dados do PerguntaRespostaOrigemVO
		PerguntaRespostaOrigemVO obj = new PerguntaRespostaOrigemVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getPerguntaVO().setCodigo(dadosSQL.getInt("pergunta"));
		obj.setTexto(dadosSQL.getString("texto"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setVerdadeiroFalse(dadosSQL.getBoolean("verdadeirofalse"));
		obj.setNumerico(dadosSQL.getDouble("numerico"));
		obj.setHora(dadosSQL.getString("hora"));
		obj.setOrdem(dadosSQL.getInt("ordem"));
		obj.getQuestionarioRespostaOrigemVO().setCodigo(dadosSQL.getInt("questionariorespostaorigem"));
		obj.getPerguntaQuestionarioVO().setCodigo(dadosSQL.getInt("perguntaquestionario"));
		obj.getRespostaPerguntaVO().setCodigo(dadosSQL.getInt("respostapergunta"));	
		
		
		
		obj.setPerguntaVO(getFacadeFactory().getPerguntaFacade().consultarPorChavePrimaria(obj.getPerguntaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
		if(Uteis.isAtributoPreenchido(obj.getRespostaPerguntaVO())) {
			obj.setRespostaPerguntaVO(getFacadeFactory().getRespostaPerguntaFacade().consultarPorChavePrimaria(obj.getRespostaPerguntaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		obj.setListaPerguntaChecklistOrigem(getFacadeFactory().getPerguntaChecklistOrigemFacade().consultarPorPerguntaRespostaOrigem(obj.getCodigo(), nivelMontarDados, usuario));
		return obj;
	}
	
    /**
     * Opera√ß√£o respons√°vel por excluir no BD um objeto da classe <code>PerguntaRespostaOrigemVO</code>.
     * Sempre localiza o registro a ser exclu√≠do atrav√©s da chave prim√°ria da entidade.
     * Primeiramente verifica a conex√£o com o banco de dados e a permiss√£o do usu√°rio
     * para realizar esta operac√£o na entidade.
     * Isto, atrav√©s da opera√ß√£o <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PerguntaRespostaOrigemVO</code> que ser√° removido no banco de dados.
     * @exception Execption Caso haja problemas de conex√£o ou restri√ß√£o de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PerguntaRespostaOrigemVO obj, UsuarioVO usuario) throws Exception {
        try {
            String sql = "DELETE FROM PerguntaRespostaOrigem WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void corrigirEncondingEstagio(Date dataInicioCorrecaoEncode, Date dataFimCorrecaoEncode, UsuarioVO usuarioVO) throws Exception {    	
    	getConexao().getJdbcTemplate().update("create table if not exists palavraEncondeErrada(palavraErrada text, palavraCorreta text)");
    	getConexao().getJdbcTemplate().update("create table if not exists dicionarioPalavras(palavraOriginal text, palavraAlterada text)");
    	getConexao().getJdbcTemplate().update("delete from palavraEncondeErrada");
    	SqlRowSet rsd =  getConexao().getJdbcTemplate().queryForRowSet("select palavraOriginal, palavraAlterada from dicionarioPalavras limit 1 ");
    	if(!rsd.next()) {
    		String[] files =  new String[2];
    		files[0] = "palavras_portugues.txt";
    		files[1] = "palavras_portugues_2.txt";
    		Map<String, String> palavrasProcessadas = new HashMap<String, String>(0);
    		for(String name: files) {
    		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(UteisJSF.getCaminhoPastaArquivosCenso()+File.separator+name)));
    		
    		String line;
    		
            while ((line = br.readLine()) != null) {
            	List<String> palavras =  new ArrayList<String>(0);
            	line = new String(line.trim().getBytes(), Charset.forName("UTF-8"));
            	palavras.add(line.toLowerCase());
            	palavras.add(line.toUpperCase());            	
            	palavras.add(line.substring(0, 1).toUpperCase()+line.toLowerCase().substring(1));
            	for(String palavra: palavras) {
            		
            		String string = palavra.replaceAll("[¬¿¡ƒ√]", "?");
            		string = string.replaceAll("[‚„‡·‰]", "?");
            		string = string.replaceAll("[ »…À]", "?");
            		string = string.replaceAll("[ÍËÈÎ]", "?");
            		string = string.replaceAll("[ŒÕÃœ]", "?");
            		string = string.replaceAll("Õ", "?");
            		string = string.replaceAll("[ÓÌÏÔ]", "?");
            		string = string.replaceAll("[‘’“”÷]", "?");
            		string = string.replaceAll("[ÙıÚÛˆ]", "?");
            		string = string.replaceAll("[€Ÿ⁄‹]", "?");
            		string = string.replaceAll("[˚˙˘¸]", "?");
            		string = string.replaceAll("«", "?");
            		string = string.replaceAll("Á", "?");
            		string = string.replaceAll("[˝ˇ]", "?");
            		string = string.replaceAll("›", "?");
            		string = string.replaceAll("Ò", "?");
            		string = string.replaceAll("—", "?");            		            		            		
            		
            	final String palavraAlterada = string;	
            	if(palavraAlterada.contains("?") && !palavrasProcessadas.containsKey(palavra.hashCode()+"-"+palavra)) {
            	final String sqlInserir = "insert into dicionarioPalavras(palavraOriginal, palavraAlterada) values(?, ?) returning palavraOriginal";
            	
            	getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {				
    				@Override
    				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
    					PreparedStatement ps =  arg0.prepareStatement(sqlInserir);
    					ps.setString(1, palavra);
    					ps.setString(2, palavraAlterada);
    					return ps;
    				}
    			}, new ResultSetExtractor<String>() {
    				public String extractData(ResultSet arg0) {
    					return null;
    				}
    			});
            	palavrasProcessadas.put(palavra.hashCode()+"-"+palavra, palavraAlterada);
            	}
            	}
            }
            br.close();
    		}
            
    	}
    	
    	StringBuilder sql = new StringBuilder("");
    	sql.append(" select p.codigo, p.texto as texto from questionariorespostaorigem q ");
    	sql.append(" inner join perguntarespostaorigem p on p.questionariorespostaorigem = q.codigo ");
    	sql.append(" where ((p.created::date >= '").append(Uteis.getDataJDBC(dataInicioCorrecaoEncode)).append("' and p.created::date <= '").append(Uteis.getDataJDBC(dataFimCorrecaoEncode)).append("')");
    	sql.append(" or (p.updated::date >= '").append(Uteis.getDataJDBC(dataInicioCorrecaoEncode)).append("' and p.updated::date  <= '").append(Uteis.getDataJDBC(dataFimCorrecaoEncode)).append("'))");
    	sql.append(" and q.estagio is not null ");
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	List<String> palavras =  new ArrayList<String>(0);
    	while(rs.next()) { 
    		for(String linha:new String(rs.getString("texto").getBytes(), Charset.forName("UTF-8")).split("\\r?\\n")) {
    		for(String palavra1 : linha.split(" ")) {
    			if(palavra1.contains("?") && palavra1.trim().length() > 2) {
    			palavra1 = palavra1.replace(".", "").replace(",", "").replace("!", "").replace("&", " ").replace("(", " ").replace(")", " ").replace(";", "").replace("-", " ").replace("_", "").replace(":", " ").replace("[", " ").replace("~", " ").replace("=", " ").replace("]", " ").replace("\\", " ").replace("/", " ").replace("	", " ").replace("1", " ").replace("0", " ").replace("2", " ").replace("3", " ").replace("4", " ").replace("5", " ").replace("6", " ").replace("7", " ").replace("8", " ").replace("9", " ").trim();
    			for(String palavra : palavra1.split(" ")) {	
    				if(palavra.contains("?") && palavra.trim().length() > 2 && !palavras.contains(palavra)) {
    						palavras.add(new String(palavra.trim().getBytes(), Charset.forName("UTF-8")));
    					}
    				}    	
    			}
    			}
    		}
    	}
    	final String sqlInserir = "insert into palavraEncondeErrada(palavraErrada) values(?) returning palavraErrada";
    	for(final String palavra: palavras) {
    		getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {				
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps =  arg0.prepareStatement(sqlInserir);
					ps.setString(1, palavra);
					return ps;
				}
			}, new ResultSetExtractor<String>() {
				public String extractData(ResultSet arg0) {
					return null;
				}
			});
    	}
    	
    }
	
}
