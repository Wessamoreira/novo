package negocio.facade.jdbc.arquitetura;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.AplicacaoControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.SpringUtil;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.utilitarias.Conexao;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioContaReceberVO;
import relatorio.negocio.jdbc.crm.FiltroRelatorioCompromissoAgendaVO;

/**
 * Responsável por implementar características comuns relativas a conexão com o banco de dados.
 */
@SuppressWarnings("rawtypes")
public abstract class SuperFacadeJDBC  implements Serializable {

    public String diretorioPastaWeb;
    private static FacadeFactory facadeFactory;
    protected final Lock bloqueio = new ReentrantLock();
    public static final long serialVersionUID = 1L;
    private ResultaSetExtractorImp resultaSetExtractorImp;

    @Autowired
    public void setFacadeFactory(FacadeFactory facadeFactory) {
        SuperFacadeJDBC.facadeFactory = facadeFactory;
    }

    public static FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    private static Conexao conexao;

    public static Conexao getConexao() {
    	if(conexao == null)
    		conexao = new Conexao();
        return conexao;
    }

    /**
     * @see negocio.facade.jdbc.arquitetura.SuperFacadeJDBCInterfaceFacade#setConexao(negocio.facade.jdbc.utilitarias.Conexao)
     */
    @Autowired
    public void setConexao(Conexao conexao) {
        SuperFacadeJDBC.conexao = conexao;
    }

    public SuperFacadeJDBC() {
    }

    public static void incluir(String idEntidade) throws Exception {
    }

    public static void alterar(String idEntidade) throws Exception {
        // ControleAcesso.alterar(idEntidade);
    }

    public static void excluir(String idEntidade) throws Exception {
    }

    public static void consultar(String idEntidade) throws Exception {
    }

    public static void emitirRelatorio(String idEntidade) throws Exception {
    }

//    public static void verificarPermissaoUsuarioFuncionalidade(String funcionalidade) throws Exception {
//    }

    public static String getIdEntidade() {
        return "Entidade";
    }

    protected FacesContext context() {
        return (FacesContext.getCurrentInstance());
    }

    public String getCaminhoPastaWeb() {
        if (diretorioPastaWeb == null || diretorioPastaWeb.equals("")) {
            if(context() == null){
        	try {
		    diretorioPastaWeb = UteisJSF.getCaminhoWeb();
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
            }else{
            ServletContext servletContext = (ServletContext) this.context().getExternalContext().getContext();
            diretorioPastaWeb = servletContext.getRealPath("");
            }
        }
        return diretorioPastaWeb;
    }

    public String getCaminhoClassesAplicacao() throws Exception {
        String caminhoBaseAplicacaoPrm = getCaminhoPastaWeb() + File.separator + "WEB-INF" + File.separator + "classes";
        return caminhoBaseAplicacaoPrm;
    }

    public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(UsuarioVO usuario) throws Exception {
        ConfiguracaoGeralSistemaVO cfg = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(
                       usuario.getUnidadeEnsinoLogado().getCodigo(), false ,
                       Uteis.NIVELMONTARDADOS_TODOS, usuario);
        if ((cfg != null) && (!cfg.getCodigo().equals(0))) {
            return cfg;
        }
        return getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, 0);
    }

    public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		ConfiguracaoFinanceiroVO cfg = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorUnidadeEnsino(unidadeEnsino, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		if ((cfg != null) && (!cfg.getCodigo().equals(0))) {
			return cfg;
		}
		return getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, 0);
	}

    /**
     * Método responsável pela verificação da consistência do estado de um dado {@code VO} (aqui representando um registro no banco de dados).
     *
     * @param <T> Parâmetro de tipo que restringe validação em instâncias que herdam a classe {@link SuperVO}.
     * @param vo Instância do objeto onde será validada a consistência do estado.
     * @param criterioClausaWhere Condição a ser considerada na clausula WHERE do comando SQL.
     * @param parametrosSQL Parâmetros que substituirão os (?) no código SQL.
     * @since 15.04.2011
     * @author Vinicius Bueno
     */
    // @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    // protected <T extends SuperVO> void validarConsitenciaDoEstadoDoVO(T vo, String criterioClausulaWhere, final Object... parametrosSQL) {
    // Date updated = null;
    // StringBuilder sql = new StringBuilder(0);
    // try {
    // // SELECT FOR SHARE - Bloqueio Compartilhado. Um bloqueio compartilhado não impede que outras transações
    // // adquiram o mesmo bloqueio compartilhado. No entanto, nenhuma transação está autorizada a atualizar, apagar
    // // ou bloquear exclusivamente (SELECT FOR UPDATE) em qualquer outra transação que mantém um bloqueio compartilhado.
    // // Qualquer tentativa de fazê-lo irá bloquear até que o bloqueio compartilhado seja liberado.
    // sql.append("SELECT updated ");
    // sql.append("FROM ").append(vo.getClass().getSimpleName().replace("VO", "").toLowerCase()).append(" ");
    // sql.append("WHERE ").append(criterioClausulaWhere).append(" FOR SHARE ");
    //
    // updated = getConexao().getJdbcTemplate().execute(sql.toString(), new PreparedStatementCallback<Date>() {
    //
    // @Override
    // public Date doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
    // synchronized (ps) {
    // // Passa os parâmetros para o SQL.
    // int indice = 1;
    // for (Object parametro : parametrosSQL) {
    // ps.setObject(indice++, parametro);
    // }
    // indice = 0;
    // // Executa o SQL.
    // ResultSet rs = ps.executeQuery();
    // try {
    // if (rs.next()) {
    // return rs.getTimestamp("updated");
    // } else {
    // return null;
    // }
    // } finally {
    // rs.close();
    // rs = null;
    // }
    // }
    // }
    //
    // });
    // // Verifica consistência do estado do objeto.
    // // if (!vo.getUpdated().equals(updated)) {
    // // throw new RuntimeException(UteisJSF.internacionalizar("msg_RegistroDesatualizado"));
    // // }
    // } finally {
    // sql = null;
    // updated = null;
    // }
    // }
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    protected <T extends SuperVO> boolean realizarAtualizacaoDoCampoUpdated(Class<T> cls, String criterioClausulaWhere, final Date updated, final Object... parametrosSQL) throws Exception {
        boolean sucesso = false;
        StringBuilder sql = new StringBuilder(0);
        try {
            sql.append("UPDATE ").append(cls.getSimpleName().replaceAll("VO", "").toLowerCase()).append(" SET");
            sql.append(" updated = ? ");
            sql.append("WHERE ").append(criterioClausulaWhere);

            sucesso = getConexao().getJdbcTemplate().execute(sql.toString(), new PreparedStatementCallback<Boolean>() {

                @Override
                public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    // Passa os parâmetros para o SQL.
                    int indice = 1;
                    ps.setTimestamp(indice++, Uteis.getDataJDBCTimestamp(updated));
                    for (Object parametro : parametrosSQL) {
                        ps.setObject(indice++, parametro);
                    }
                    indice = 0;
                    // Executa o SQL.
                    return (ps.executeUpdate() > 0);
                }
            });
            return sucesso;
        } finally {
            sql = null;
        }
    }

    public Lock getBloqueio() {
        return bloqueio;
    }

    public void removerObjetoMemoria(Object object) {
        if (object != null) {
			Class classe = object.getClass();
            for (Field field : classe.getDeclaredFields()) {
                field.setAccessible(true);
                if (field != null) {
                    try {
                        if (field.getType().getClass().getSuperclass().getSimpleName().equals("SuperVO")
                                || field.getType().getClass().getSuperclass().getSimpleName().equals("SuperControle")
                                || field.getType().getClass().getSuperclass().getSimpleName().equals("SuperControleRelatorio")) {
                            removerObjetoMemoria(field);
                        }
                        if (field.getType().getName().equals("java.lang.Integer")
                                || field.getType().getName().equals("java.lang.String")
                                || field.getType().getName().equals("java.lang.Double")
                                || field.getType().getName().equals("java.lang.Boolean")
                                || field.getType().getName().equals("java.lang.Long")) {
                            field.set(object, null);
                        } else if (field.getType().getName().equals("java.util.List")
                                || field.getType().getName().equals("java.util.ArrayList")) {
                            List<?> lista = (List<?>) field.get(object);
                            // for(Object o: lista){
                            // removerObjetoMemoria(o);
                            // }
                            if(lista != null) {
                            	lista.clear();
                            	field.set(object, null);
                            }
                        } else {
                            field.set(object, null);
                        }
                        field = null;
                    } catch (Exception e) {

                    }
                }
            }
            classe = null;
        }
    }

    public Integer getTotalRegistro(SqlRowSet rs) {
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    /**
     * Gera a clausula sql 'IN' de acordo com a quantidade informada.
     * Exemplo: IN (?, ?, ...)
     *
     * @param quantidade - Quantidade de '?'
     * @return Exemplo: IN (?)
     */
    public String realizarGeracaoIn(int quantidade) {
		String sql = " IN (";
		StringBuilder sqlBuilder = new StringBuilder(sql);
		for( int i = 0; i< quantidade; i++){
			sqlBuilder.append(" ?");
			if(i != quantidade -1) {
				sqlBuilder.append(",");
			}
		}
		sqlBuilder.append(")");
		return sqlBuilder.toString();
	}
    
    public String realizarGeracaoInValor(int quantidade, String[] valores) {
    	return this.realizarGeracaoInValor(quantidade, valores, "");
    }
    
    public String realizarGeracaoInValor(int quantidade, String[] valores, String campo) {
    	String sql = campo + " IN (";
    	StringBuilder sqlBuilder = new StringBuilder(sql);
    	for( int i = 0; i< quantidade; i++){
    		sqlBuilder.append("'"+valores[i]+"'");
    		if(i != quantidade -1) {
    			sqlBuilder.append(",");
    		}
    	}
    	sqlBuilder.append(")");
    	return sqlBuilder.toString();
    }
 	public String[] gerarArray(List<?> lista) {
		int contador = 0;
		String[] array  = new String[lista.size()];		for (Object object : lista) {
			array[contador] = String.valueOf(UtilReflexao.invocarMetodoGet(object, "codigo"));
			contador++;
		}
		return array;
	}
	public String realizarGeracaoWhereDataInicioDataTermino(Date dataInicio, Date dataTermino, String campoInicio, String campoTermino, boolean dataComHora) {
        if (!dataComHora) {
            if (dataInicio != null && dataTermino != null) {
            	return campoInicio + " >= '" + Uteis.getDataJDBC(dataInicio) + " 00:00:00.000' and "+campoTermino+ " <= '" + Uteis.getDataJDBC(dataTermino) + " 23:59:59.059'";
                //return campo + "::DATE >= '" + Uteis.getDataJDBC(dataInicio) + "' and "+campo+ "::DATE <= '" + Uteis.getDataJDBC(dataTermino) + "'";
            }
            if (dataInicio != null) {
            	return campoInicio + " >= '" + Uteis.getDataJDBC(dataInicio) + " 00:00:00.000'";
                //return campo + "::DATE >= '" + Uteis.getDataJDBC(dataInicio) + "'";
            } else {
                if (dataTermino != null) {
                	return campoTermino + " <= '" + Uteis.getDataJDBC(dataTermino) + " 23:59:59.059'";
                    //return campo + "::DATE <= '" + Uteis.getDataJDBC(dataTermino) + "'";
                }
            }
        }else{
            if (dataInicio != null && dataTermino != null) {
//                return campo + " >= '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0)) + "' and "+campo+ " <= '" + Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino)) + "'";
                return campoInicio + " >= '" + Uteis.getDataJDBC(dataInicio) + " 00:00:00.000' and "+campoTermino+ " <= '" + Uteis.getDataJDBC(dataTermino) + " 23:59:59.059'";
            }
            if (dataInicio != null) {
//                return campo + " >= '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0)) + "'";
                return campoInicio + " >= '" + Uteis.getDataJDBC(dataInicio) + " 00:00:00.000'";
            }
            if (dataTermino != null) {
                return campoTermino + " <= '" + Uteis.getDataJDBC(dataTermino) + " 23:59:59.059'";
            }
        }
        return " 1 = 1 ";
    }
    
    public String realizarGeracaoWherePeriodo(Date dataInicio, Date dataTermino, String campo, boolean dataComHora) {
    	if (!dataComHora) {
    		if (dataInicio != null && dataTermino != null) {
    			return campo + " >= '" + Uteis.getDataJDBC(dataInicio) + " 00:00:00.000' and "+campo+ " <= '" + Uteis.getDataJDBC(dataTermino) + " 23:59:59.059'";
    			//return campo + "::DATE >= '" + Uteis.getDataJDBC(dataInicio) + "' and "+campo+ "::DATE <= '" + Uteis.getDataJDBC(dataTermino) + "'";
    		}
    		if (dataInicio != null) {
    			return campo + " >= '" + Uteis.getDataJDBC(dataInicio) + " 00:00:00.000'";
    			//return campo + "::DATE >= '" + Uteis.getDataJDBC(dataInicio) + "'";
    		} else {
    			if (dataTermino != null) {
    				return campo + " <= '" + Uteis.getDataJDBC(dataTermino) + " 23:59:59.059'";
    				//return campo + "::DATE <= '" + Uteis.getDataJDBC(dataTermino) + "'";
    			}
    		}
    	}else{
    		if (dataInicio != null && dataTermino != null) {
//                return campo + " >= '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0)) + "' and "+campo+ " <= '" + Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino)) + "'";
    			return campo + " >= '" + Uteis.getDataJDBC(dataInicio) + " 00:00:00.000' and "+campo+ " <= '" + Uteis.getDataJDBC(dataTermino) + " 23:59:59.059'";
    		}
    		if (dataInicio != null) {
//                return campo + " >= '" + Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0)) + "'";
    			return campo + " >= '" + Uteis.getDataJDBC(dataInicio) + " 00:00:00.000'";
    		}
    		if (dataTermino != null) {
    			return campo + " <= '" + Uteis.getDataJDBC(dataTermino) + " 23:59:59.059'";
    		}
    	}
    	return " 1 = 1 ";
    }

    public String realizarGeracaoWherePeriodoConsiderandoMesAno(Date dataInicio, Date dataTermino, String campo) {

            if (dataInicio != null && dataTermino != null) {
                return " to_char("+campo + ", 'yyyy/MM') >= to_char('"+ Uteis.getDataJDBC(dataInicio)+"'::DATE, 'yyyy/MM') and to_char("+campo+ ", 'yyyy/MM') <= to_char('" + Uteis.getDataJDBC(dataTermino) + "'::DATE, 'yyyy/MM')";
            }
            if (dataInicio != null) {
                return " to_char("+campo + ", 'yyyy/MM') >= to_char('"+ Uteis.getDataJDBC(dataInicio)+"'::DATE, 'yyyy/MM')";
            } else {
                if (dataTermino != null) {
                    return " to_char("+campo + ", 'yyyy/MM') <= to_char('"+ Uteis.getDataJDBC(dataInicio)+"'::DATE, 'yyyy/MM')";
                }
            }

        return " 1 = 1 ";
    }

    public String realizarGeracaoWherePeriodoConsiderandoMesAnoExtract(Date dataInicio, Date dataTermino, String campo) {
    	if (dataInicio != null && dataTermino != null) {
    		return " and extract(month from " +campo + ") >= "+ Uteis.getMesData(dataInicio) +" and extract(year from " +campo + ") >= "+ Uteis.getAno(dataInicio)
    				+ " and extract(month from " +campo + ") <= "+ Uteis.getMesData(dataTermino) +" and extract(year from " +campo + ") <= "+ Uteis.getAno(dataTermino);
    	}

    	if (dataInicio != null) {
    		return " and extract(month from " +campo + ") >= "+ Uteis.getMesData(dataInicio) +" and extract(year from " +campo + ") >= "+ Uteis.getDataJDBC(dataInicio) ;
    	}
    	if (dataTermino != null) {
    		return " and extract(month from " +campo + ") <= "+ Uteis.getMesData(dataTermino) +" and extract(year from " +campo + ") <= "+ Uteis.getDataJDBC(dataTermino) ;
    	}
    	
    	return " 1 = 1 ";
    }

    public StringBuilder adicionarFiltroSituacaoAcademicaMatriculaPeriodo(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String keyEntidade) {
		StringBuilder sqlStr = new StringBuilder("");
		if(filtroRelatorioAcademicoVO == null){
			return sqlStr.append(" 1=1 ");
		}
		keyEntidade = keyEntidade.trim();
		sqlStr.append(" ").append(keyEntidade).append(".situacaomatriculaperiodo in (");
		if (filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getFormado() || filtroRelatorioAcademicoVO.getConcluido() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() || filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getCancelado() || filtroRelatorioAcademicoVO.getJubilado()) {
			boolean virgula = false;
			if (filtroRelatorioAcademicoVO.getAtivo()) {
				sqlStr.append(virgula ? "," : "").append("'AT'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getFormado()) {
				sqlStr.append(virgula ? "," : "").append("'FO'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getConcluido()) {
				sqlStr.append(virgula ? "," : "").append("'FI'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getPreMatricula()) {
				sqlStr.append(virgula ? "," : "").append("'PR'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getPreMatriculaCancelada()) {
				sqlStr.append(virgula ? "," : "").append("'PC'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getTrancado()) {
				sqlStr.append(virgula ? "," : "").append("'TR'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getTransferenciaExterna()) {
				sqlStr.append(virgula ? "," : "").append("'TS'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getTransferenciaInterna()) {
				sqlStr.append(virgula ? "," : "").append("'TI'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getAbandonado()) {
				sqlStr.append(virgula ? "," : "").append("'AC'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getCancelado()) {
				sqlStr.append(virgula ? "," : "").append("'CA'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getJubilado()) {
				sqlStr.append(virgula ? "," : "").append("'JU'");
				virgula = true;
			}
		} else {
			return new StringBuilder(" 1 = 1 ");
		}
		sqlStr.append(") ");
		return sqlStr;

	}

	public StringBuilder adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String keyEntidade) {
		StringBuilder sqlStr = new StringBuilder();
		if(filtroRelatorioAcademicoVO == null){
			return sqlStr.append(" 1 = 1 ");
		}
		keyEntidade = keyEntidade.trim();
		sqlStr.append(" ").append(keyEntidade).append(".situacao in (");
		if (filtroRelatorioAcademicoVO.getPendenteFinanceiro() || filtroRelatorioAcademicoVO.getConfirmado()) {
			boolean virgula = false;
			if (filtroRelatorioAcademicoVO.getPendenteFinanceiro()) {
				sqlStr.append("'PF'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getConfirmado()) {
				sqlStr.append(virgula ? "," : "").append("'CO', 'AT'");
				virgula = true;
			}
		}else{
			return new StringBuilder(" 1 = 1 ");
		}
		sqlStr.append(") ");
		return sqlStr;

	}

	public StringBuilder adicionarFiltroTipoOrigemContaPagar(FiltroRelatorioFinanceiroVO obj, String  keyEntidade) {
		StringBuilder sqlStr = new StringBuilder("") ;
		if(obj == null){
			return sqlStr.append(" 1=1 ");
		}
		sqlStr.append(" and ").append(keyEntidade).append(" .tipoOrigem in (");
		boolean virgula = false;
		if (obj.isTipoOrigemPagarCompra()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.COMPRA.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarContratoDespesa()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.CONTRATO_DESPESA.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarMulta()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.MULTA.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarPrevisaoCusto()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.PROVISAO_DE_CUSTO.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarRegistroManual()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.REGISTRO_MANUAL.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarRequisicao()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.REQUISICAO.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarRestituicaoConvenio()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.RESTITUICAO_CONVENIO_MATRICULA_PERIODO.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarServico()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.SERVICO.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarProcessamentoRetornoParceiro()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.PROCESSAMENTO_ARQUIVO_RETORNO_PARCEIRO.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarNotaFiscalEntrada()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarAdiantamento()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.ADIANTAMENTO.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarRecebimentoCompra()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.RECEBIMENTO_COMPRA.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarConciliacaoBancaria()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.CONCILIACAO_BANCARIA.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarTaxaCartao()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.TAXA_CARTAO.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarNegociacaoContaPagar()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.NEGOCICACAO_CONTA_PAGAR.getValor()).append("' ");
			virgula = true;
		}
		if (obj.isTipoOrigemPagarFolhaPagamento()) {
			sqlStr.append(virgula ? ", " : "").append(" '").append(OrigemContaPagar.FOLHA_PAGAMENTO.getValor()).append("' ");
			virgula = true;
		}
		sqlStr.append(") ");
		if (virgula) {
			return sqlStr;
		} else {
			return new StringBuilder("");
		}
	}


	public StringBuilder adicionarFiltroTipoOrigemContaReceber(FiltroRelatorioFinanceiroVO obj, String  keyEntidade) {
		return adicionarFiltroTipoOrigemContaReceber(obj, keyEntidade, "tipoOrigem");			
	}
	
	public StringBuilder adicionarFiltroTipoOrigemContaReceber(FiltroRelatorioFinanceiroVO obj, String  keyEntidade, String keyCampo) {
		StringBuilder sqlStr = new StringBuilder("") ;
		if(obj == null){
			return sqlStr;
		}

		sqlStr.append(keyEntidade).append(".").append(keyCampo).append(" in (''");
		boolean existe = false;
		if (obj.getTipoOrigemBiblioteca()) {
			sqlStr.append(", 'BIB'");
			existe = true;
		}
		if (obj.getTipoOrigemBolsaCusteadaConvenio()) {
			sqlStr.append(", 'BCC'");
			existe = true;
		}
		if (obj.getTipoOrigemContratoReceita()) {
			sqlStr.append(", 'CTR'");
			existe = true;
		}
		if (obj.getTipoOrigemDevolucaoCheque()) {
			sqlStr.append(", 'DCH'");
			existe = true;
		}
		if (obj.getTipoOrigemInclusaoReposicao()) {
			sqlStr.append(", 'IRE'");
			existe = true;
		}
		if (obj.getTipoOrigemInscricaoProcessoSeletivo()) {
			sqlStr.append(", 'IPS'");
			existe = true;
		}
		if (obj.getTipoOrigemMatricula()) {
			sqlStr.append(", 'MAT'");
			existe = true;
		}
		if (obj.getTipoOrigemMensalidade()) {
			sqlStr.append(", 'MEN'");
			existe = true;
		}
		if (obj.getTipoOrigemNegociacao()) {
			sqlStr.append(", 'NCR'");
			existe = true;
		}
		if (obj.getTipoOrigemOutros()) {
			sqlStr.append(", 'OUT'");
			existe = true;
		}
		if (obj.getTipoOrigemMaterialDidatico()) {
			sqlStr.append(", 'MDI'");
			existe = true;
		}
		if (obj.getTipoOrigemRequerimento()) {
			sqlStr.append(", 'REQ'");
			existe = true;
		}
		sqlStr.append(" ) ");
		if(!existe) {
			sqlStr = new StringBuilder(" 1 = 1");
		}
		return sqlStr;
	}

	public StringBuilder adicionarFiltroSituacaoAcademicaMatricula(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String keyEntidade) {
		StringBuilder sqlStr = new StringBuilder("");
		if (filtroRelatorioAcademicoVO == null) {
			return sqlStr.append(" 1=1 ");
		}
		keyEntidade = keyEntidade.trim();
		sqlStr.append(" ").append(keyEntidade).append(".situacao IN (");
		if (filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getFormado() || filtroRelatorioAcademicoVO.getConcluido() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getCancelado() || filtroRelatorioAcademicoVO.getJubilado()) {
			boolean virgula = false;
			if (filtroRelatorioAcademicoVO.getAtivo()) {
				sqlStr.append(virgula ? "," : "").append("'AT'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getFormado()) {
				sqlStr.append(virgula ? "," : "").append("'FO'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getConcluido()) {
				sqlStr.append(virgula ? "," : "").append("'FI'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getPreMatricula()) {
				sqlStr.append(virgula ? "," : "").append("'PR'");
				virgula = true;
			}

			if (filtroRelatorioAcademicoVO.getTrancado()) {
				sqlStr.append(virgula ? "," : "").append("'TR'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getTransferenciaExterna()) {
				sqlStr.append(virgula ? "," : "").append("'TS'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getTransferenciaInterna()) {
				sqlStr.append(virgula ? "," : "").append("'TI'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getAbandonado()) {
				sqlStr.append(virgula ? "," : "").append("'AC'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getCancelado()) {
				sqlStr.append(virgula ? "," : "").append("'CA'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getJubilado()) {
				sqlStr.append(virgula ? "," : "").append("'JU'");
				virgula = true;
			}
		} else {
			return new StringBuilder(" 1 = 1 ");
		}
		sqlStr.append(") ");
		return sqlStr;
	}
	
	public StringBuilder adicionarFiltroSituacaoAcademicaHistorico(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String keyEntidade) {
		StringBuilder sqlStr = new StringBuilder("");
		if (filtroRelatorioAcademicoVO == null) {
			return sqlStr.append(" 1=1 ");
		}
		keyEntidade = keyEntidade.trim();
		sqlStr.append(" ").append(keyEntidade).append(".situacao IN (");
		if (filtroRelatorioAcademicoVO.getReprovado() || filtroRelatorioAcademicoVO.getAprovado() || filtroRelatorioAcademicoVO.getNaoCursada() 
				|| filtroRelatorioAcademicoVO.getCursando() || filtroRelatorioAcademicoVO.getTrancamentoHistorico()
				|| filtroRelatorioAcademicoVO.getAbandonoHistorico() || filtroRelatorioAcademicoVO.getCanceladoHistorico()
				|| filtroRelatorioAcademicoVO.getTransferidoHistorico() || filtroRelatorioAcademicoVO.getConcessaoCreditoHistorico() 
				|| filtroRelatorioAcademicoVO.getConcessaoCargaHorariaHistorico() || filtroRelatorioAcademicoVO.getPreMatriculaHistorico()
				|| filtroRelatorioAcademicoVO.getJubilado()) {
			boolean virgula = false;
			if (filtroRelatorioAcademicoVO.getReprovado()) {
				sqlStr.append(virgula ? "," : "").append(UteisTexto.converteListaSituacaoHistoricoParaCondicaoIn(SituacaoHistorico.getSituacoesDeReprovacao()));
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getAprovado()) {
				sqlStr.append(virgula ? "," : "").append(UteisTexto.converteListaSituacaoHistoricoParaCondicaoIn(SituacaoHistorico.getSituacoesDeAprovacao()));
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getCursando()) {
				sqlStr.append(virgula ? "," : "").append(UteisTexto.converteListaSituacaoHistoricoParaCondicaoIn(SituacaoHistorico.getSituacoesDeCursando()));
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getNaoCursada()) {
				sqlStr.append(virgula ? "," : "").append("'NC'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getTrancamentoHistorico()) {
				sqlStr.append(virgula ? "," : "").append("'TR'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getAbandonoHistorico()) {
				sqlStr.append(virgula ? "," : "").append("'AC'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getCanceladoHistorico()) {
				sqlStr.append(virgula ? "," : "").append("'CA'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getTransferidoHistorico()) {
				sqlStr.append(virgula ? "," : "").append("'TF'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getConcessaoCreditoHistorico()) {
				sqlStr.append(virgula ? "," : "").append("'CC'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getConcessaoCargaHorariaHistorico()) {
				sqlStr.append(virgula ? "," : "").append("'CH'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getPreMatriculaHistorico()) {
				sqlStr.append(virgula ? "," : "").append("'PR'");
				virgula = true;
			}
			if (filtroRelatorioAcademicoVO.getJubilado()) {
				sqlStr.append(virgula ? "," : "").append("'JU'");
				virgula = true;
			}
		} else {
			return new StringBuilder(" 1 = 1 ");
		}
		sqlStr.append(") ");
		return sqlStr;
	}

	public StringBuilder adicionarFiltroRelatorioContaReceberVO(FiltroRelatorioContaReceberVO  obj, String  keyEntidade, boolean isValor) {
		StringBuilder sqlStr = new StringBuilder("") ;
		if(obj == null){
			return sqlStr.append(" 1=1 ");
		}
		if (obj.getTipoOrigemContaReceberMatricula() || obj.getTipoOrigemContaReceberMaterialDidatico()
				|| obj.getTipoOrigemContaReceberMensalidade() || obj.getTipoOrigemContaReceberBiblioteca()
				|| obj.getTipoOrigemContaReceberBolsaCusteadaConvenio() || obj.getTipoOrigemContaReceberContratoReceita()
				|| obj.getTipoOrigemContaReceberDevolucaoCheque() || obj.getTipoOrigemContaReceberInclusaoReposicao()
				|| obj.getTipoOrigemContaReceberInscricaoProcessoSeletivo() || obj.getTipoOrigemContaReceberNegociacao()
				|| obj.getTipoOrigemContaReceberOutros() || obj.getTipoOrigemContaReceberRequerimento()) {
			sqlStr.append(" ").append(keyEntidade).append(" in (");
			boolean virgula = false;
			if (obj.getTipoOrigemContaReceberMatricula()) {
				sqlStr.append(virgula ? "," : "").append(" '").append(isValor ? TipoOrigemContaReceber.MATRICULA.valor : TipoOrigemContaReceber.MATRICULA).append("' ");
				virgula = true;
			}
			if (obj.getTipoOrigemContaReceberMaterialDidatico()) {
				sqlStr.append(virgula ? "," : "").append(" '").append(isValor ? TipoOrigemContaReceber.MATERIAL_DIDATICO.valor : TipoOrigemContaReceber.MATERIAL_DIDATICO).append("' ");
				virgula = true;
			}
			if (obj.getTipoOrigemContaReceberMensalidade()) {
				sqlStr.append(virgula ? "," : "").append(" '").append(isValor ? TipoOrigemContaReceber.MENSALIDADE.valor : TipoOrigemContaReceber.MENSALIDADE).append("' ");
				virgula = true;
			}
			if (obj.getTipoOrigemContaReceberBiblioteca()) {
				sqlStr.append(virgula ? "," : "").append("'BIB'");
				virgula = true;
			}
			if (obj.getTipoOrigemContaReceberBolsaCusteadaConvenio()) {
				sqlStr.append(virgula ? "," : "").append("'BCC'");
				virgula = true;
			}
			if (obj.getTipoOrigemContaReceberContratoReceita()) {
				sqlStr.append(virgula ? "," : "").append("'CTR'");
				virgula = true;
			}
			if (obj.getTipoOrigemContaReceberDevolucaoCheque()) {
				sqlStr.append(virgula ? "," : "").append("'DCH'");
				virgula = true;
			}
			if (obj.getTipoOrigemContaReceberInclusaoReposicao()) {
				sqlStr.append(virgula ? "," : "").append("'IRE'");
				virgula = true;
			}
			if (obj.getTipoOrigemContaReceberInscricaoProcessoSeletivo()) {
				sqlStr.append(virgula ? "," : "").append("'IPS'");
				virgula = true;
			}
			if (obj.getTipoOrigemContaReceberNegociacao()) {
				sqlStr.append(virgula ? "," : "").append("'NCR'");
				virgula = true;
			}
			if (obj.getTipoOrigemContaReceberOutros()) {
				sqlStr.append(virgula ? "," : "").append("'OUT'");
				virgula = true;
			}
			if (obj.getTipoOrigemContaReceberRequerimento()) {
				sqlStr.append(virgula ? "," : "").append("'REQ'");
				virgula = true;
			}
			sqlStr.append(") ");
		}else{
			sqlStr.append(" 1=1 ");
		}
		return sqlStr;
	}
	
	public StringBuilder adicionarFiltroRelatorioCompromissoAgendaVO(FiltroRelatorioCompromissoAgendaVO  obj, String  keyEntidade) {
		StringBuilder sqlStr = new StringBuilder("") ;
		if(obj == null){
			return sqlStr.append(" and 1=1 ");
		}
		if (obj.getAguardandoContato() || obj.getParalizado()
				|| obj.getRealizado()|| obj.getRealizadoComInsucessoContato()
				|| obj.getRealizadoComRemarcacao() || obj.getNaoPossuiAgenda()) {
			sqlStr.append(" and ").append(keyEntidade).append(" in (");
			boolean virgula = false;
			if (obj.getAguardandoContato()) {
				sqlStr.append(virgula ? "," : "").append("'").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO).append("'");
				virgula = true;
			}
			if (obj.getParalizado()) {
				sqlStr.append(virgula ? "," : "").append("'").append(TipoSituacaoCompromissoEnum.PARALIZADO).append("'");
				virgula = true;
			}
			if (obj.getRealizado()) {
				sqlStr.append(virgula ? "," : "").append("'").append(TipoSituacaoCompromissoEnum.REALIZADO).append("'");
				virgula = true;
			}
			if (obj.getRealizadoComInsucessoContato()) {
				sqlStr.append(virgula ? "," : "").append("'").append(TipoSituacaoCompromissoEnum.REALIZADO_COM_INSUCESSO_CONTATO).append("'");
				virgula = true;
			}
			if (obj.getRealizadoComRemarcacao()) {
				sqlStr.append(virgula ? "," : "").append("'").append(TipoSituacaoCompromissoEnum.REALIZADO_COM_REMARCACAO).append("'");
				virgula = true;
			}
			if (obj.getNaoPossuiAgenda()) {
				sqlStr.append(virgula ? "," : "").append("'").append(TipoSituacaoCompromissoEnum.NAO_POSSUI_AGENDA).append("'");
				virgula = true;
			}
			sqlStr.append(") ");
		}else{
			sqlStr.append(" and 1=1 ");
		}
		return sqlStr;
	}

	public ResultaSetExtractorImp getResultaSetExtractorImp() {
		if(resultaSetExtractorImp == null){
			resultaSetExtractorImp = new ResultaSetExtractorImp();
		}
		return resultaSetExtractorImp;
	}

	public void setResultaSetExtractorImp(ResultaSetExtractorImp resultaSetExtractorImp) {
		this.resultaSetExtractorImp = resultaSetExtractorImp;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public <T extends SuperVO> void  realizarVisualizacaoSeiLog( T obj, String nomeTabela, String key, String value) throws SQLException {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select created, codigoCreated, nomeCreated, ");
		sqlStr.append(" updated, codigoUpdated, nomeUpdated ");
		sqlStr.append(" from ");
		sqlStr.append(nomeTabela);		
		sqlStr.append(" WHERE ").append(key).append("='").append(value).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {			
			obj.setCreated(tabelaResultado.getTimestamp("created"));
			obj.setCodigoCreated(tabelaResultado.getInt("codigoCreated"));
			obj.setNomeCreated(tabelaResultado.getString("nomeCreated"));
			obj.setUpdated(tabelaResultado.getTimestamp("updated"));
			obj.setCodigoUpdated(tabelaResultado.getInt("codigoUpdated"));
			obj.setNomeUpdated(tabelaResultado.getString("nomeUpdated"));
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public static <T extends SuperVO> void setIncluirSeiLog(String nomeTabela, Integer codigo,  UsuarioVO usuarioVO) throws SQLException {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" UPDATE ").append(nomeTabela).append(" set ");
		sqlStr.append(" created= '").append(Uteis.getDataJDBCTimestamp(new Date())).append("', ");
		if(usuarioVO != null && usuarioVO.getCodigo() != 0) {
			sqlStr.append("codigoCreated= ").append(usuarioVO.getCodigo()).append(", ");
		}
		if(usuarioVO != null && usuarioVO.getNome() != "") {
			sqlStr.append("nomeCreated='").append(Uteis.removeCaractersEspeciaisParaNomePessoa(usuarioVO.getNome())).append("' ");
		}
		sqlStr.append(" where codigo =  ").append(codigo);
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public static <T extends SuperVO> void setAlterarSeiLog(String nomeTabela, Integer codigo,  UsuarioVO usuarioVO) throws SQLException {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" UPDATE ").append(nomeTabela).append(" set ");
		sqlStr.append(" updated= '").append(Uteis.getDataJDBCTimestamp(new Date())).append("', ");
		if(usuarioVO != null && usuarioVO.getNome() != "") {
			sqlStr.append("nomeUpdated='").append(Uteis.removeCaractersEspeciaisParaNomePessoa(usuarioVO.getNome())).append("', ");
		}
		if(usuarioVO != null && usuarioVO.getCodigo() != 0) {
			sqlStr.append("codigoUpdated= ").append(usuarioVO.getCodigo()).append(" ");
		}
		sqlStr.append(" where codigo =  ").append(codigo);
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final SuperVO objetoPersistencia, final String nomeTabela, final AtributoPersistencia atributoPersistencia, final UsuarioVO usuarioVO) {	
		atributoPersistencia.add("created", new Date())
		.add("codigoCreated", usuarioVO != null && usuarioVO.getCodigo() != 0 ? usuarioVO.getCodigo() : 0) 
		.add("nomeCreated", usuarioVO != null && usuarioVO.getNome() != "" ? Uteis.removeCaractersEspeciaisParaNomePessoa(usuarioVO.getNome()) : ""); 
		UtilReflexao.invocarMetodo(objetoPersistencia, "setCodigo", getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = null;
				StringBuilder sqlInterrogacoes = null;
				try {
					sql = new StringBuilder("INSERT INTO ");
					sqlInterrogacoes = new StringBuilder(" values ( ");
					boolean adicionarVigula = false;
					sql.append(nomeTabela);
					sql.append(" ( ");
					for (String campo : atributoPersistencia.keySet()) {
						if (adicionarVigula) {
							sql.append(", ");
							sqlInterrogacoes.append(", ");
						}
						sql.append(" ").append(campo).append(" ");
						sqlInterrogacoes.append(" ? ");
						adicionarVigula = true;
					}
					sql.append(" ) ");
					sqlInterrogacoes.append(") ");
					sql.append(sqlInterrogacoes);

					sql.append(" returning codigo ");

					if (usuarioVO != null && usuarioVO.getCodigo() != null) {
						sql.append(" --ul:").append(usuarioVO.getCodigo());
					} else {
						sql.append(" --ul:0");
					}
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					int x = 1;
					for (String campo : atributoPersistencia.keySet()) {
						Uteis.setValuePreparedStatement(atributoPersistencia.get(campo), x++, sqlInserir, arg0);
					}
					return sqlInserir;
				} catch (Exception e) {
					throw e;
				} finally {
					sqlInterrogacoes =  null;
					sql =  null;
				}
			}
		}, getResultaSetExtractorImp()));
		objetoPersistencia.setNovoObj(false);
	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer alterar(final SuperVO objetoPersistencia, final String nomeTabela, final AtributoPersistencia atributoPersistencia, final AtributoPersistencia atributoComparacao,  final UsuarioVO usuarioVO) {
		if(atualizar(nomeTabela, atributoPersistencia, atributoComparacao, usuarioVO) == 0) {
			incluir(objetoPersistencia, nomeTabela, atributoPersistencia,  usuarioVO);
			return  1;
		}else{
			return 1;
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer atualizar(final String nomeTabela, final AtributoPersistencia atributoPersistencia, final AtributoPersistencia atributoComparacao,  final UsuarioVO usuarioVO) {
		atributoPersistencia.add("updated", new Date())
		.add("codigoUpdated", usuarioVO != null && usuarioVO.getCodigo() != 0 ? usuarioVO.getCodigo() : 0) 
		.add("nomeUpdated", usuarioVO != null && usuarioVO.getNome() != "" ? Uteis.removeCaractersEspeciaisParaNomePessoa(usuarioVO.getNome()) : ""); 
		return getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = null;
				try {
					sql = new StringBuilder("UPDATE ");
					boolean adicionarVigula = false;
					sql.append(nomeTabela);
					sql.append(" set ");
					for (String campo : atributoPersistencia.keySet()) {
						if (adicionarVigula) {
							sql.append(", ");
							
						}
						sql.append(" ").append(campo).append(" = ? ");
						adicionarVigula = true;
					}
					sql.append(" where ");
					adicionarVigula = false;
					for (String campo : atributoComparacao.keySet()) {
						if (adicionarVigula) {
							sql.append(" and ");
							
						}
						sql.append(" ").append(campo).append(" = ? ");
						adicionarVigula = true;
					}
					
					executarInicializacaoUsuarioLogado(usuarioVO,  sql);
					
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					int x = 1;
					for (String campo : atributoPersistencia.keySet()) {
						Uteis.setValuePreparedStatement(atributoPersistencia.get(campo), x++, sqlInserir, arg0);
					}
					for (String campo : atributoComparacao.keySet()) {
						Uteis.setValuePreparedStatement(atributoComparacao.get(campo), x++, sqlInserir, arg0);
					}
					return sqlInserir;
				} catch (Exception e) {
					throw e;
				} finally {
					sql = null;
				}
			}
		}); 
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final String nomeTabela, final AtributoPersistencia atributoComparacao, UsuarioVO usuarioLogado) {
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = null;
				try {
					sql = new StringBuilder(" DELETE from ");
					boolean adicionarVigula = false;
					sql.append(nomeTabela);
					sql.append(" where ");
					for (String campo : atributoComparacao.keySet()) {
						if (adicionarVigula) {
							sql.append(" and ");
						}
						sql.append(" ").append(campo).append(" = ? ");
						adicionarVigula = true;
					}
					executarInicializacaoUsuarioLogado(usuarioLogado,  sql);
					PreparedStatement sqlDelete = arg0.prepareStatement(sql.toString());
					int x = 1;
					for (String campo : atributoComparacao.keySet()) {
						Uteis.setValuePreparedStatement(atributoComparacao.get(campo), x++, sqlDelete, arg0);
					}
					return sqlDelete;
				} catch (Exception e) {
					throw e;
				} finally {
					sql = null;
				}
			}
		});
	}
	
	private void executarInicializacaoUsuarioLogado(final UsuarioVO usuarioLogado, StringBuilder sql) {
		if (usuarioLogado != null && usuarioLogado.getCodigo() != null) {
			sql.append(" --ul:").append(usuarioLogado.getCodigo());
		} else {
			sql.append(" --ul:0");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirListaSubordinada(final List<? extends SuperVO> listaManter, final String nomeTabela, final AtributoPersistencia atributoComparacao,  final UsuarioVO usuarioVO) {
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {				
					StringBuilder sql = new StringBuilder("delete from ");
					sql.append(nomeTabela);
					sql.append(" where ");
					boolean adicionarVigula = false;
					for (String campo : atributoComparacao.keySet()) {
						if (adicionarVigula) {
							sql.append(" and ");
						}
						sql.append(" ").append(campo).append(" = ? ");
						adicionarVigula = true;
					}
					if (adicionarVigula) {
						sql.append(" and ");
					}					
					sql.append(" codigo not in (0 ");
					for(SuperVO superVO: listaManter) {
						sql.append(", ").append(UtilReflexao.invocarMetodoGet(superVO, "codigo"));
					}
					sql.append(" ) ");
					if (usuarioVO != null && usuarioVO.getCodigo() != null) {
						sql.append(" --ul:").append(usuarioVO.getCodigo());
					} else {
						sql.append(" --ul:0");
					}
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					int x = 1;
					for (String campo : atributoComparacao.keySet()) {
						Uteis.setValuePreparedStatement(atributoComparacao.get(campo), x++, sqlInserir, arg0);
					}
					return sqlInserir;
			}
		});
	}
	
//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	public List<Class> list(final Class objetoRetorno, final String nomeTabela, final AtributoRetorno atributoRetorno, final JoinTable joinTable, final AtributoPersistencia atributoComparacao) {
//		StringBuilder sql =  new StringBuilder();
//		StringBuilder orderBy = new StringBuilder();
//		sql.append("select ");
//		boolean virgula = false;
//		for(AtributoRetorno.Retorno retorno: atributoRetorno) {
//			if(virgula) {
//				sql.append(", ");
//			}
//			sql.append(retorno.getCampoBase()).append(" as ").append(retorno.getApelidoRetorno());
//			virgula = true;
//		}
//		sql.append(" from ").append(nomeTabela);
//		if(joinTable != null && !joinTable.isEmpty()) {
//		for(JoinTable.Join join: joinTable) {
//			if(join.innerJoin) {
//				sql.append(" inner ");
//			}else {
//				sql.append(" left ");
//			}
//			sql.append(" join ").append(join.tableName).append(" on ").append(join.condicaoOn);
//		}
//		}
//		if(atributoComparacao != null && !atributoComparacao.isEmpty()) {
//			sql.append(" where ");
//			for (String campo : atributoComparacao.keySet()) {
//				sql.append(" and ");
//			}
//		}
//	}

	public AplicacaoControle aplicacaoControle;
	
	public AplicacaoControle getAplicacaoControle() {
    	if(aplicacaoControle == null) {
    		if(SpringUtil.getApplicationContext() == null) {
    			aplicacaoControle = new AplicacaoControle();
    			aplicacaoControle.setFacadeFactory(getFacadeFactory());
    		}else {
    			aplicacaoControle = (AplicacaoControle) SpringUtil.getApplicationContext().getBean(AplicacaoControle.class);
	}

}
    	return aplicacaoControle;
	}
    
	public StringBuilder realizarGeracaoWhereUnidadeEnsinoSelecionada(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String campo) {
    	StringBuilder selectStr = new StringBuilder(" ");
    	selectStr.append(campo).append(" in (0");
    	int x = 0;
    	for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
    		if (ue.getFiltrarUnidadeEnsino()) {
    			selectStr.append(", ").append(ue.getCodigo());
    			x++;
    		}
    	}
    	if(x == 1) {
    		selectStr = new StringBuilder(selectStr.toString().replace(" in (0,", " = ("));
    	}
    	if(x == 0) {
    		for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
        		selectStr.append(", ").append(ue.getCodigo());        		        	
        	}
    	}
    	selectStr.append(") ");
    	return selectStr;
    }
	
	public StringBuilder realizarGeracaoWhereCursoSelecionada(List<CursoVO> cursoVOs, String campo) {
		StringBuilder selectStr = new StringBuilder(" ");
		selectStr.append(campo).append(" in (0");
		int x = 0;
		for (CursoVO curso : cursoVOs) {
			if (curso.getFiltrarCursoVO()) {
				selectStr.append(", ").append(curso.getCodigo());
				x++;
			}
		}
		if(x == 1) {
			selectStr = new StringBuilder(selectStr.toString().replace(" in (0,", " = ("));
		}
		if(x == 0) {
			for (CursoVO curso : cursoVOs) {
				selectStr.append(", ").append(curso.getCodigo());        		        	
			}
		}
		selectStr.append(") ");
		return selectStr;	
	}
	
	public StringBuilder realizarGeracaoWhereTurnoSelecionada(List<TurnoVO> turnoVOs, String campo) {
		StringBuilder selectStr = new StringBuilder(" ");
		selectStr.append(campo).append(" in (0");
		int x = 0;
		for (TurnoVO turno : turnoVOs) {
			if (turno.getFiltrarTurnoVO()) {
				selectStr.append(", ").append(turno.getCodigo());
				x++;
			}
		}
		if(x == 1) {
			selectStr = new StringBuilder(selectStr.toString().replace(" in (0,", " = ("));
		}
		if(x == 0) {
			for (TurnoVO turno : turnoVOs) {
				selectStr.append(", ").append(turno.getCodigo());        		        	
			}
		}
		selectStr.append(") ");
		return selectStr;	
	}

	public void registrarExceptionSentry(Throwable e, List<String> listaInformacaoAdicional, Boolean enviarSentry, UsuarioVO usuarioVO) {
		try {
			if (Objects.isNull(getAplicacaoControle())) {
				return;
			}
			getFacadeFactory().getServicoIntegracaoSentryInterfaceFacade().registrarExceptionSentry(e, listaInformacaoAdicional, enviarSentry, getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null), usuarioVO);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}