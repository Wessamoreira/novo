package negocio.facade.jdbc.academico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TextoPadraoDeclaracaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>TextoPadraoDeclaracaoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>TextoPadraoDeclaracaoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see TextoPadraoDeclaracaoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class TextoPadraoDeclaracao extends ControleAcesso implements TextoPadraoDeclaracaoInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4999702874333682643L;
	protected static String idEntidade;

    public TextoPadraoDeclaracao() throws Exception {
        super();
        setIdEntidade("TextoPadraoDeclaracao");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>TextoPadraoDeclaracaoVO</code>.
     */
    public TextoPadraoDeclaracaoVO novo() throws Exception {
        incluir(getIdEntidade());
        TextoPadraoDeclaracaoVO obj = new TextoPadraoDeclaracaoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>TextoPadraoDeclaracaoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>TextoPadraoDeclaracaoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TextoPadraoDeclaracaoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
        try {
            incluir(getIdEntidade(), true, usuario);
            TextoPadraoDeclaracaoVO.validarDados(obj);
            if(obj.getTipo().equals("DQ") && existeTextoPadraoDeclaracaoAnualQuitacaoDebito(obj)){
            	throw new ConsistirException("Já existe um Texto Padrão Declaração cadastrado para esta Unidade de Ensino e Nível Educacional");
            }
            final String sql = "INSERT INTO TextoPadraoDeclaracao( descricao, dataDefinicao, responsavelDefinicao, texto, controlarDocumentacaoPendente, validarTodosDocumentosCurso, validarApenasDocumentoSuspensao, unidadeEnsino, tipo, orientacaoDaPagina, nivelEducacional, margemDireita, margemEsquerda, margemSuperior, margemInferior, tipodesignetextoenum, arquivoireport, assinardigitalmentetextopadrao, alinhamentoAssinaturaDigitalEnum, corAssinaturaDigitalmente, alturaassinatura, larguraassinatura , modelopadraovisaoaluno  ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataDefinicao()));
                    if (obj.getResponsavelDefinicao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getResponsavelDefinicao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    sqlInserir.setString(4, obj.getTexto());
                    sqlInserir.setBoolean(5, obj.getControlarDocumentacaoPendente());
                    sqlInserir.setBoolean(6, obj.getValidarTodosDocumentosCurso());
                    sqlInserir.setBoolean(7, obj.getValidarApenasDocumentoSuspensao());
                    sqlInserir.setInt(8, obj.getUnidadeEnsino().getCodigo());
                    sqlInserir.setString(9, obj.getTipo());
                    sqlInserir.setString(10, obj.getOrientacaoDaPagina());
                    sqlInserir.setString(11, obj.getNivelEducacional());
                    sqlInserir.setString(12, obj.getMargemDireita());
                    sqlInserir.setString(13, obj.getMargemEsquerda());
                    sqlInserir.setString(14, obj.getMargemSuperior());
                    sqlInserir.setString(15, obj.getMargemInferior());
                    sqlInserir.setString(16, obj.getTipoDesigneTextoEnum().name());
//					if(Uteis.isAtributoPreenchido(obj.getArquivoIreport())){
//						sqlInserir.setInt(17, obj.getArquivoIreport().getCodigo());
//					}else{
						sqlInserir.setNull(17, 0);
//					}
					sqlInserir.setBoolean(18, obj.getAssinarDigitalmenteTextoPadrao());
					sqlInserir.setString(19, obj.getAlinhamentoAssinaturaDigitalEnum().name());
					sqlInserir.setString(20, obj.getCorAssinaturaDigitalmente());
					sqlInserir.setFloat(21, obj.getAlturaAssinatura());
					sqlInserir.setFloat(22, obj.getLarguraAssinatura());
					int x = 23;
					Uteis.setValuePreparedStatement(obj.getModeloPadraoVisaoAluno(), x++, sqlInserir);
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            if(obj.getTipoDesigneTextoEnum().isPdf()){
            	for(ArquivoVO arquivoVO :obj.getListaArquivoIreport()) {
            		arquivoVO.setValidarDados(false);
            		arquivoVO.setCodOrigem(obj.getCodigo());
    				getFacadeFactory().getArquivoFacade().persistir(arquivoVO, false, usuario, configuracaoGeralSistemaVO);
            	}
			}
			new TextoPadraoDeclaracaoFuncionario().incluirTextoPadraoDeclaracaoFuncionario(obj, obj.getTextoPadraoDeclaracaofuncionarioVOs());            			
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }
    
    
    @Override
	public void adicionarArquivoIreport(List<ArquivoVO> lista, ArquivoVO obj, String origemArquivo) throws Exception {
    	obj.setOrigem(origemArquivo);
    	obj.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IREPORT_TMP);
    	validarDadosArquivo(obj);
		if(Uteis.isAtributoPreenchido(lista)) {
			int cont = 0;
			for(ArquivoVO arquivo : lista) {
				if(arquivo.getCodigo().equals(obj.getCodigo()) && arquivo.getNome().equals(obj.getNome())) {
					lista.set(cont, obj);
					return;
				}
				cont ++;
			}
		}
		lista.add(obj);
	}

	private void validarDadosArquivo(ArquivoVO arquivo) throws Exception {
		 if (arquivo.getNome().isEmpty()) {
	        	throw new ConsistirException("O arquivo do Ireport (Texto Padrao) deve ser adicionado.");
	        }
	}
    
    @Override
    public void removerArquivoIreport(ArquivoVO arquivoSelecionado, TextoPadraoDeclaracaoVO obj) throws Exception {
		Iterator<ArquivoVO> listaArquivo = obj.getListaArquivoIreport().iterator();
		while(listaArquivo.hasNext()) {
			ArquivoVO arquivoExistente = listaArquivo.next();
			if(arquivoExistente.getCodigo().equals(arquivoSelecionado.getCodigo()) && arquivoExistente.getNome().equals(arquivoSelecionado.getNome())) {
				listaArquivo.remove();
			}
		}
	}
    
    private boolean existeTextoPadraoDeclaracaoAnualQuitacaoDebito(TextoPadraoDeclaracaoVO obj) {
	        StringBuilder sqlStr = new StringBuilder("SELECT * FROM TextoPadraoDeclaracao ");
	        
	      	sqlStr.append(" WHERE tipo = '").append(obj.getTipo()).append("' ");
	      	
	      	if(Uteis.isAtributoPreenchido(obj.getNivelEducacional())) {
		       	 sqlStr.append(" AND niveleducacional = '").append(obj.getNivelEducacional()).append("' ");
		        }
	        
	        if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsino().getCodigo())) {
	        	sqlStr.append(" AND unidadeensino = ").append(obj.getUnidadeEnsino().getCodigo());
	        }else {
	        	sqlStr.append(" AND unidadeensino = 0 ");
	        }
	        
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	        if(tabelaResultado.next()) {
	       	 	return true;
	        }else {
	        	return false;
	        }
	}

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>TextoPadraoDeclaracaoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>TextoPadraoDeclaracaoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TextoPadraoDeclaracaoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
        try {
            alterar(getIdEntidade(), true, usuario);
            TextoPadraoDeclaracaoVO.validarDados(obj);
            final String sql = "UPDATE TextoPadraoDeclaracao set descricao=?, dataDefinicao=?, responsavelDefinicao=?, texto=?, controlarDocumentacaoPendente=?, validarTodosDocumentosCurso=?, validarApenasDocumentoSuspensao=?, unidadeEnsino=?, tipo=?, orientacaoDaPagina=?, nivelEducacional=?, margemDireita = ?, margemEsquerda = ?, margemSuperior = ?, margemInferior = ?, tipodesignetextoenum = ?, arquivoireport = ?, assinardigitalmentetextopadrao=?, alinhamentoAssinaturaDigitalEnum=?, corAssinaturaDigitalmente=?, alturaassinatura=?, larguraassinatura=?,  modeloPadraoVisaoAluno=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataDefinicao()));
                    if (obj.getResponsavelDefinicao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getResponsavelDefinicao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    sqlAlterar.setString(4, obj.getTexto());
                    sqlAlterar.setBoolean(5, obj.getControlarDocumentacaoPendente());
                    sqlAlterar.setBoolean(6, obj.getValidarTodosDocumentosCurso());
                    sqlAlterar.setBoolean(7, obj.getValidarApenasDocumentoSuspensao());
                    sqlAlterar.setInt(8, obj.getUnidadeEnsino().getCodigo().intValue());
                    sqlAlterar.setString(9, obj.getTipo());
                    sqlAlterar.setString(10, obj.getOrientacaoDaPagina());
                    sqlAlterar.setString(11, obj.getNivelEducacional());
                    sqlAlterar.setString(12, obj.getMargemDireita());
                    sqlAlterar.setString(13, obj.getMargemEsquerda());
                    sqlAlterar.setString(14, obj.getMargemSuperior());
                    sqlAlterar.setString(15, obj.getMargemInferior());
                    sqlAlterar.setString(16, obj.getTipoDesigneTextoEnum().name());
//					if(Uteis.isAtributoPreenchido(obj.getArquivoIreport())){
//						sqlAlterar.setInt(17, obj.getArquivoIreport().getCodigo());
//					}else{
						sqlAlterar.setNull(17, 0);
//					}
					sqlAlterar.setBoolean(18, obj.getAssinarDigitalmenteTextoPadrao());
					sqlAlterar.setString(19, obj.getAlinhamentoAssinaturaDigitalEnum().name());
					sqlAlterar.setString(20, obj.getCorAssinaturaDigitalmente());
					sqlAlterar.setFloat(21, obj.getAlturaAssinatura());
					sqlAlterar.setFloat(22, obj.getLarguraAssinatura());
					
					int x = 23;
					Uteis.setValuePreparedStatement(obj.getModeloPadraoVisaoAluno(), x++, sqlAlterar);
					
                    sqlAlterar.setInt(x++, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
    
            if(obj.getTipoDesigneTextoEnum().isPdf()){
            	for(ArquivoVO arquivoVO : obj.getListaArquivoIreport()) {
            		arquivoVO.setValidarDados(false);
            		arquivoVO.setCodOrigem(obj.getCodigo());
            		if(Uteis.isAtributoPreenchido(arquivoVO.getCodigo())) {
            			arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IREPORT);
            		}else {
            			arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IREPORT_TMP);
            		}
            		getFacadeFactory().getArquivoFacade().persistir(arquivoVO, false, usuario, configuracaoGeralSistemaVO);
            	}		
			}  
            new TextoPadraoDeclaracaoFuncionario().alterarTextoPadraoDeclaracaoFuncionario(obj, obj.getTextoPadraoDeclaracaofuncionarioVOs());            			
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>TextoPadraoDeclaracaoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>TextoPadraoDeclaracaoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TextoPadraoDeclaracaoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
        try {
            excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM TextoPadraoDeclaracao WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            if(Uteis.isAtributoPreenchido(obj.getListaArquivoIreport())){
            	for(ArquivoVO arquivoVO : obj.getListaArquivoIreport()) {
            		getFacadeFactory().getArquivoFacade().excluir(arquivoVO, usuario, configuracaoGeralSistemaVO);
    				getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(arquivoVO, configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + obj.getArquivoIreport().getPastaBaseArquivo());
            	}
			}
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>TextoPadrao</code> através do valor do atributo 
     * <code>situacao</code> da classe <code>Colaborador</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoDeclaracaoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorResponsavelDefinicao(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarPorResponsavelDefinicao(valorConsulta, 0, Arrays.asList(), nivelMontarDados, usuario);
    }

    public List consultarPorResponsavelDefinicao(String valorConsulta, Integer unidadeEnsino,  List<String>listaTipo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), true, usuario);
        String sqlStr = "select TextoPadraoDeclaracao.* from TextoPadraoDeclaracao inner join 	Usuario on Usuario.codigo = TextoPadraoDeclaracao.responsavelDefinicao WHERE upper( Usuario.nome ) like('" + valorConsulta.toUpperCase() + "%') ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND (TextoPadraoDeclaracao.unidadeEnsino = " + unidadeEnsino + " or TextoPadraoDeclaracao.unidadeEnsino is null) ";
        }
        if(Uteis.isAtributoPreenchido(listaTipo)) {
        	sqlStr += "AND (TextoPadraoDeclaracao.tipo in(" + UteisTexto.converteListaStringParaString(listaTipo) + ")) ";
        }
        sqlStr += " ORDER BY Usuario.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>TextoPadrao</code> através do valor do atributo 
     * <code>Date dataDefinicao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoDeclaracaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataDefinicao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarPorDataDefinicao(prmIni, prmFim, 0, Arrays.asList(), controlarAcesso, nivelMontarDados, usuario);
    }

    public List consultarPorDataDefinicao(Date prmIni, Date prmFim, Integer unidadeEnsino,  List<String>listaTipo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TextoPadraoDeclaracao WHERE ((dataDefinicao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataDefinicao <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND (TextoPadraoDeclaracao.unidadeEnsino = " + unidadeEnsino + " or TextoPadraoDeclaracao.unidadeEnsino is null) ";
        }
        if(Uteis.isAtributoPreenchido(listaTipo)) {
        	sqlStr += "AND (TextoPadraoDeclaracao.tipo in(" + UteisTexto.converteListaStringParaString(listaTipo) + ")) ";
        }
        sqlStr += " ORDER BY dataDefinicao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>TextoPadrao</code> através do valor do atributo 
     * <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoDeclaracaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarPorDescricao(valorConsulta, 0,  false, Arrays.asList(), controlarAcesso, nivelMontarDados, usuario);
    }

    public List consultarPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean visaoTextoPadraoDeclaracao,  List<String>listaTipo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TextoPadraoDeclaracao WHERE upper( descricao ) like('" + valorConsulta.toUpperCase() + "%') ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND (unidadeEnsino = " + unidadeEnsino + " or unidadeEnsino is null or unidadeEnsino = 0) ";
        }
        if(!visaoTextoPadraoDeclaracao) {
            sqlStr += "AND tipo <> 'DQ' ";
        }
        if(Uteis.isAtributoPreenchido(listaTipo)) {
        	sqlStr += "AND (TextoPadraoDeclaracao.tipo in(" + UteisTexto.converteListaStringParaString(listaTipo) + ")) ";
        } else {
//        	sqlStr += "AND (TextoPadraoDeclaracao.tipo not in('AC')) ";
        }
        sqlStr += "ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public List<TextoPadraoDeclaracaoVO> consultarPorTipoRequerimento(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT TextoPadraoDeclaracao.* FROM TextoPadraoDeclaracao ";
    	sqlStr +=" inner join tiporequerimento on tiporequerimento.textopadrao = TextoPadraoDeclaracao.codigo ";
    	sqlStr +=" WHERE tiporequerimento.tipo = '" + valorConsulta + "' ";
    	if (unidadeEnsino != 0) {
    		sqlStr += " AND (TextoPadraoDeclaracao.unidadeEnsino = " + unidadeEnsino + " or TextoPadraoDeclaracao.unidadeEnsino is null or TextoPadraoDeclaracao.unidadeEnsino = 0) ";
    	}
    	if (!nivelEducacional.equals("")) {
    		sqlStr += " AND (TextoPadraoDeclaracao.nivelEducacional = '"+nivelEducacional+"' OR TextoPadraoDeclaracao.nivelEducacional is null OR TextoPadraoDeclaracao.nivelEducacional = '') ";
    	}
    	sqlStr += " ORDER BY TextoPadraoDeclaracao.descricao";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public List<TextoPadraoDeclaracaoVO> consultarPorTipo(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT * FROM TextoPadraoDeclaracao WHERE tipo = '" + valorConsulta + "' ";
    	if (unidadeEnsino != 0) {
    		sqlStr += "AND (unidadeEnsino = " + unidadeEnsino + " or unidadeEnsino is null or unidadeEnsino = 0) ";
    	}
    	if (!nivelEducacional.equals("")) {
    		sqlStr += "AND (nivelEducacional = '"+nivelEducacional+"' OR nivelEducacional is null OR nivelEducacional = '') ";
    	}
    	sqlStr += "ORDER BY descricao";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>TextoPadrao</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoDeclaracaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarPorCodigo(valorConsulta,0, Arrays.asList(), controlarAcesso,nivelMontarDados, usuario);
    }

    public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, List<String>listaTipo,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TextoPadraoDeclaracao WHERE codigo = " + valorConsulta.intValue() + " ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND (TextoPadraoDeclaracao.unidadeEnsino = " + unidadeEnsino + " or TextoPadraoDeclaracao.unidadeEnsino is null) ";
        }
        if(Uteis.isAtributoPreenchido(listaTipo)) {
        	sqlStr += "AND (TextoPadraoDeclaracao.tipo in(" + UteisTexto.converteListaStringParaString(listaTipo) + ")) ";
        }
        sqlStr += " ORDER BY codigo";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>TextoPadrao</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoDeclaracaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT DISTINCT (TextoPadraoDeclaracao.*) FROM TextoPadraoDeclaracao ");
        sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.contratoMatricula = TextoPadraoDeclaracao.codigo");
        sqlStr.append(" OR matriculaPeriodo.contratoFiador = TextoPadraoDeclaracao.codigo");
        sqlStr.append(" WHERE matriculaPeriodo.matricula = '" + valorConsulta.toUpperCase() + "' ;");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoDeclaracaoVO</code> resultantes da consulta.
     */
    public  List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>TextoPadraoDeclaracaoVO</code>.
     * @return  O objeto da classe <code>TextoPadraoDeclaracaoVO</code> com os dados devidamente montados.
     */
    public  TextoPadraoDeclaracaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ////System.out.println(">> Montar dados(TextoPadrao) - " + new Date());
        TextoPadraoDeclaracaoVO obj = new TextoPadraoDeclaracaoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setDataDefinicao(dadosSQL.getDate("dataDefinicao"));
        obj.getResponsavelDefinicao().setCodigo(new Integer(dadosSQL.getInt("responsavelDefinicao")));
        obj.setTexto(dadosSQL.getString("texto"));
        obj.setControlarDocumentacaoPendente(dadosSQL.getBoolean("controlarDocumentacaoPendente"));
        obj.setValidarTodosDocumentosCurso(dadosSQL.getBoolean("validarTodosDocumentosCurso"));
        obj.setValidarApenasDocumentoSuspensao(dadosSQL.getBoolean("validarApenasDocumentoSuspensao"));
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
        obj.setTipo(dadosSQL.getString("tipo"));
        obj.setOrientacaoDaPagina(dadosSQL.getString("orientacaoDaPagina"));
        obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
        obj.setMargemDireita(dadosSQL.getString("margemDireita"));
		obj.setMargemEsquerda(dadosSQL.getString("margemEsquerda"));
		obj.setMargemSuperior(dadosSQL.getString("margemSuperior"));
		obj.setMargemInferior(dadosSQL.getString("margemInferior"));
		obj.setCorAssinaturaDigitalmente(dadosSQL.getString("corAssinaturaDigitalmente"));
		obj.setAlturaAssinatura(dadosSQL.getFloat("alturaAssinatura"));
		obj.setLarguraAssinatura(dadosSQL.getFloat("larguraAssinatura"));
		obj.setAssinarDigitalmenteTextoPadrao(dadosSQL.getBoolean("assinardigitalmentetextopadrao"));
		obj.setAlinhamentoAssinaturaDigitalEnum(AlinhamentoAssinaturaDigitalEnum.valueOf(dadosSQL.getString("alinhamentoAssinaturaDigitalEnum")));
		obj.setTipoDesigneTextoEnum(TipoDesigneTextoEnum.valueOf(dadosSQL.getString("tipodesignetextoenum")));
		obj.setModeloPadraoVisaoAluno(dadosSQL.getBoolean("modeloPadraoVisaoAluno"));
		if(obj.getTipoDesigneTextoEnum().isPdf()){
			obj.setListaArquivoIreport(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigem(obj.getCodigo(), OrigemArquivo.TEXTO_PADRAO_DECLARACAO.getValor(), nivelMontarDados, usuario));
		}
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        montarDadosResponsavelDefinicao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
//        obj.setTextoPadraoDeclaracaofuncionarioVOs(.consultarTextoPadraoDeclaracaoFuncionario(obj.getCodigo(), false, usuario));		
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ColaboradorVO</code> relacionado ao objeto <code>TextoPadraoDeclaracaoVO</code>.
     * Faz uso da chave primária da classe <code>ColaboradorVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosResponsavelDefinicao(TextoPadraoDeclaracaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelDefinicao().getCodigo().intValue() == 0) {
            obj.setResponsavelDefinicao(new UsuarioVO());
            return;
        }
        obj.setResponsavelDefinicao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelDefinicao().getCodigo(), nivelMontarDados, usuario));
    }

    public  void montarDadosUnidadeEnsino(TextoPadraoDeclaracaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>TextoPadraoDeclaracaoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public TextoPadraoDeclaracaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM TextoPadraoDeclaracao WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public String consultaTextoDoTextoPadraoPorChavePrimaria(Integer codigoPrm,  UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT texto FROM TextoPadraoDeclaracao WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado.getString("texto");
    }
    
    
    public String removerBordaDaPagina(String texto) throws Exception {
    	try {
    		texto = texto.replaceAll("padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;", "");
    		texto = texto.replaceAll("border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;", "");
    		
    		texto = texto.replaceAll("width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;", "");
    		texto = texto.replaceAll("border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;", "");
    		
    		texto = texto.replaceAll("padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;", "");
    		texto = texto.replaceAll("border: 1px #CCCCCC dashed; height: 190mm; padding-left: 1cm; padding-right: 1cm;", "");
    		
		} catch (Exception e) {
			throw e;
		}
    	return texto;
    }
    
    public String adicionarStyleFormatoPaginaTextoPadrao(String texto, String orientacaoDaPagina) throws Exception {
    	try {
    		StringBuilder sb = new StringBuilder();
			if (!texto.contains("<style type='text/css'>")) {
				if (orientacaoDaPagina.equals("RE")) {
			        sb.append("<head>");
			        sb.append("<style type='text/css'>");
			        sb.append(" body { margin: 0; padding: 0; font-size:11px; } ");
			        sb.append(" th { font-weight: normal; } ");
			        sb.append(" * { box-sizing: border-box; -moz-box-sizing: border-box; } ");
			        sb.append(" .page { width: 21cm; min-height: 29.7cm; padding: 2cm; margin: 1cm auto; } ");
			        sb.append(" .subpage { padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 237mm; } ");
			        sb.append(" @page { size: A4; margin: 0; } ");
			        sb.append(" @media print { .page { margin: 0; border: initial; border-radius: initial; width: initial; min-height: initial; box-shadow: initial; background: initial; page-break-after: always; } } ");
			        sb.append("</style>");
			        
					texto = texto.replace("<head>", sb.toString());
				} else {
			        sb.append("<head>");
			        sb.append("<style type='text/css'>");
			        sb.append(" body { margin: 0; padding: 0; font-size:11px; } ");
			        sb.append(" th { font-weight: normal; } ");
			        sb.append(" * { box-sizing: border-box; -moz-box-sizing: border-box; } ");
			        sb.append(" .page { width: 29.7cm; min-height: 21cm; padding: 2cm; margin: 1cm auto; } ");
			        sb.append(" .subpage { padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 150mm; } ");
			        sb.append(" @page { size: A4 landscape; margin: 0; } ");
			        sb.append(" @media print { .page { margin: 0; border: initial; border-radius: initial; width: initial; min-height: initial; box-shadow: initial; background: initial; page-break-after: always; } } ");
			        sb.append("</style>");
			        
					texto = texto.replace("<head>", sb.toString());
				}
			}
		} catch (Exception e) {
			throw e;
		}
    	
    	return texto;
    }
    
	public String substituirValorAtribuidoClass(String texto, String classe, String valor) throws Exception {
		String parte1, parte2, novaClasse  = "";
		try {
			parte1 = texto.substring(texto.indexOf(classe), texto.length());
			parte2 = parte1.substring(0, parte1.indexOf("}") + 1);
			novaClasse = classe + " { " + valor + " } ";
		} catch (Exception e) {
			throw e;
		}
		return texto.replace(parte2, novaClasse);
	}
	
    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return TextoPadraoDeclaracao.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        TextoPadraoDeclaracao.idEntidade = idEntidade;
    }

	@Override
	public List consultarTipoAtaColacao(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TextoPadraoDeclaracao WHERE ";
        sqlStr += "TextoPadraoDeclaracao.tipo in('AC') ";
        sqlStr += "ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
    
    @Override
    public Boolean consultarTextoPadraoAssinarDigitalmente(Integer codigoTextoPadrao) throws Exception {
    	StringBuilder sql = new StringBuilder("SELECT assinarDigitalmenteTextoPadrao FROM textoPadraoDeclaracao WHERE codigo = ? ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoTextoPadrao);
    	if (!tabelaResultado.next()) {
    		throw new Exception("Dados não encontrados (Texto Padrão Declaração)");
    	}
    	return tabelaResultado.getBoolean("assinarDigitalmenteTextoPadrao");
    }

	
}
