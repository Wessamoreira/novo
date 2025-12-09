/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.administrativo.CampanhaPublicoAlvoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.enumeradores.TipoDistribuicaoProspectCampanhaPublicoAlvoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CompromissoCampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.enumerador.TipoCompromissoEnum;
import negocio.comuns.crm.enumerador.TipoContatoEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.CampanhaPublicoAlvoProspectInterfaceFacade;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class CampanhaPublicoAlvoProspect extends ControleAcesso implements CampanhaPublicoAlvoProspectInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6592558955745510539L;
	protected static String idEntidade;
	
	@Override
    public void incluirCampanhaPublicoAlvoProspect(Integer campanhaPublicoAlvo, List<CampanhaPublicoAlvoProspectVO> objetos, UsuarioVO usuarioLogado) throws Exception {
        Iterator<CampanhaPublicoAlvoProspectVO> e = objetos.iterator();
        while (e.hasNext()) {
            CampanhaPublicoAlvoProspectVO obj = (CampanhaPublicoAlvoProspectVO) e.next();
            obj.getCampanhaPublicoAlvo().setCodigo(campanhaPublicoAlvo);
            incluir(obj, usuarioLogado);
        }

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CampanhaPublicoAlvoProspectVO obj, UsuarioVO usuarioLogado) throws Exception {
        try {
        	
        	if (obj.getProspect().getCodigo().equals(0) && !obj.getProspect().getNome().equals("")) {
        		if (obj.getProspect().getUnidadeEnsino().getCodigo().equals(0)) {
        			obj.getProspect().getUnidadeEnsino().setCodigo(obj.getCompromissoCampanhaPublicoAlvoProspectVO().getCampanha().getUnidadeEnsino().getCodigo());
        		}
        		getFacadeFactory().getProspectsFacade().incluirSemValidarDados(obj.getProspect(), false, null, null);
        	} else if (!obj.getProspect().getCodigo().equals(0)){
            	if (obj.getProspect().getUnidadeEnsino().getCodigo().equals(0)) {
            		getFacadeFactory().getProspectsFacade().realizarVinculoUnidadeEnsinoProspectSemUnidadeEnsinoPorPessoa(obj.getProspect().getPessoa().getCodigo(), obj.getCompromissoCampanhaPublicoAlvoProspectVO().getCampanha().getUnidadeEnsino().getCodigo(), usuarioLogado);
            	}
        		getFacadeFactory().getProspectsFacade().alterarResponsavelFinanceiroProspect(obj.getProspect(), null);
        	}
        	getFacadeFactory().getCompromissoCampanhaPublicoAlvoProspectFacade().incluirSemValidarDados(obj.getCompromissoCampanhaPublicoAlvoProspectVO(), null);
        	
            final String sql = "INSERT INTO CampanhaPublicoAlvoProspect (campanhapublicoalvo, prospect, pessoa, consultorDistribuicao, compromissoCampanhaPublicoAlvoProspect) VALUES (?, ?, ?, ?, ?) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getCampanhaPublicoAlvo().getCodigo() != 0) {
                        sqlInserir.setInt(1, obj.getCampanhaPublicoAlvo().getCodigo());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getProspect().getCodigo() != 0) {
                        sqlInserir.setInt(2, obj.getProspect().getCodigo());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    if (obj.getPessoa().getCodigo() != 0) {
                        sqlInserir.setInt(3, obj.getPessoa().getCodigo());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    if (obj.getConsultorDistribuicaoVO().getCodigo() != 0) {
                        sqlInserir.setInt(4, obj.getConsultorDistribuicaoVO().getCodigo());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    if (obj.getCompromissoCampanhaPublicoAlvoProspectVO() .getCodigo() != 0) {
                        sqlInserir.setInt(5, obj.getCompromissoCampanhaPublicoAlvoProspectVO().getCodigo());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
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
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
			if(e.getMessage().contains("Prospect_unique_nomeEmail")){
			    throw new Exception("O Prospect ("  + obj.getProspect().getNome() + ") Já Existe Com o Mesmo Nome e E-mail (" + obj.getProspect().getEmailPrincipal() + ").");
			}
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CampanhaPublicoAlvoProspectVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {
			getFacadeFactory().getCompromissoCampanhaPublicoAlvoProspectFacade().alterarSemValidarDados(obj.getCompromissoCampanhaPublicoAlvoProspectVO(), null);
			final String sql = "UPDATE campanhaPublicoAlvoProspect SET campanhapublicoalvo=?, prospect=?, pessoa=?, consultorDistribuicao=?, compromissoCampanhaPublicoAlvoProspect=? WHERE ((codigo =? ))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getCampanhaPublicoAlvo().getCodigo() != 0) {
						sqlAlterar.setInt(1, obj.getCampanhaPublicoAlvo().getCodigo());
                    } else {
                    	sqlAlterar.setNull(1, 0);
                    }
                    if (obj.getProspect().getCodigo() != 0) {
                    	sqlAlterar.setInt(2, obj.getProspect().getCodigo());
                    } else {
                    	sqlAlterar.setNull(2, 0);
                    }
                    if (obj.getPessoa().getCodigo() != 0) {
                    	sqlAlterar.setInt(3, obj.getPessoa().getCodigo());
                    } else {
                    	sqlAlterar.setNull(3, 0);
                    }
                    if (obj.getConsultorDistribuicaoVO().getCodigo() != 0) {
                    	sqlAlterar.setInt(4, obj.getConsultorDistribuicaoVO().getCodigo());
                    } else {
                    	sqlAlterar.setNull(4, 0);
                    }
                    if (obj.getCompromissoCampanhaPublicoAlvoProspectVO() .getCodigo() != 0) {
                    	sqlAlterar.setInt(5, obj.getCompromissoCampanhaPublicoAlvoProspectVO().getCodigo());
                    } else {
                    	sqlAlterar.setNull(5, 0);
                    }
					sqlAlterar.setInt(6, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}

	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirCampanhaPublicoAlvoProspectPorCodigoCampanhaPublicoAlvo(Integer codigo, UsuarioVO usuarioLogado) throws Exception {
        try {
            CampanhaPublicoAlvoProspect.excluir(getIdEntidade());
            String sql = "DELETE FROM campanhapublicoalvoprospect WHERE ((campanhapublicoalvo= ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(sql, new Object[]{codigo});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirCampanhaPublicoAlvoProspectPorCodigoProspect(Integer codigo, UsuarioVO usuarioLogado) throws Exception {
        try {
            CampanhaPublicoAlvoProspect.excluir(getIdEntidade());
            String sql = "DELETE FROM campanhapublicoalvoprospect WHERE ((prospect= ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(sql, new Object[]{codigo});
        } catch (Exception e) {
            throw e;
        }
    }

    public List<CampanhaPublicoAlvoProspectVO> consultarPorCampanhaPublicoAlvo(Integer campanhaPublicoAlvo) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("	select");
    	sb.append("		CampanhaPublicoAlvoProspect.codigo,");
    	sb.append("		CampanhaPublicoAlvoProspect.campanhapublicoalvo ,");
    	sb.append("		CampanhaPublicoAlvoProspect.pessoa ,");
    	sb.append("		CampanhaPublicoAlvoProspect.prospect,");
    	sb.append("		CampanhaPublicoAlvoProspect.consultordistribuicao ,");
    	sb.append("		compromissocampanhapublicoalvoprospect ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.descricao ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.hora ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.tipocompromisso ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.observacao ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.origem ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.urgente ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.datacadastro ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.tipocontato ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.campanha ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.codigo ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.datacompromisso ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.etapaworkflow ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.tiposituacaocompromissoenum ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.prospectado ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.horafim ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.preinscricao ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.historicoreagendamentocompromisso ,");
    	sb.append("		compromissoCampanhaPublicoAlvoProspect.datainicialcompromisso ,");
    	sb.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" ");
    	sb.append(" FROM CampanhaPublicoAlvoProspect ");
    	sb.append(" left join funcionario on funcionario.codigo = CampanhaPublicoAlvoProspect.consultorDistribuicao ");
    	sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
    	sb.append(" left join compromissoCampanhaPublicoAlvoProspect on CampanhaPublicoAlvoProspect.compromissoCampanhaPublicoAlvoProspect = compromissoCampanhaPublicoAlvoProspect.codigo ");
        sb.append(" WHERE CampanhaPublicoAlvo = ").append(campanhaPublicoAlvo);
        sb.append("and ( ");
        sb.append("compromissoCampanhaPublicoAlvoProspect.codigo is null or  compromissoCampanhaPublicoAlvoProspect.codigo in (");
        sb.append("		select ");
        sb.append("			ccpap.codigo ");
        sb.append("			from compromissoCampanhaPublicoAlvoProspect as ccpap");
        sb.append("			where ccpap.tipocompromisso= compromissoCampanhaPublicoAlvoProspect.tipocompromisso");
        sb.append("			and ccpap.hora = compromissoCampanhaPublicoAlvoProspect.hora");
        sb.append("			and ccpap.datacadastro = compromissoCampanhaPublicoAlvoProspect.datacadastro");
        sb.append("			and ccpap.tipocontato = compromissoCampanhaPublicoAlvoProspect.tipocontato");
        sb.append("			and ccpap.prospect = compromissoCampanhaPublicoAlvoProspect.prospect");
        sb.append("			and ccpap.campanha = compromissoCampanhaPublicoAlvoProspect.campanha");
        sb.append("			and ccpap.datacompromisso = compromissoCampanhaPublicoAlvoProspect.datacompromisso");
        sb.append("			and coalesce(ccpap.etapaworkflow, 0) = coalesce(compromissoCampanhaPublicoAlvoProspect.etapaworkflow, 0)");
        sb.append("			and ccpap.tiposituacaocompromissoenum = compromissoCampanhaPublicoAlvoProspect.tiposituacaocompromissoenum");
        sb.append("			and ccpap.datainicialcompromisso = compromissoCampanhaPublicoAlvoProspect.datainicialcompromisso");
        sb.append("			order by ccpap.codigo desc limit 1  )");
        sb.append(")	order by compromissoCampanhaPublicoAlvoProspect.hora");
        
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return montarDadosConsulta(tabelaResultado);
    }

    public static List<CampanhaPublicoAlvoProspectVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List<CampanhaPublicoAlvoProspectVO> vetResultado = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    public static CampanhaPublicoAlvoProspectVO montarDados(SqlRowSet dadosSQL) throws Exception {
        CampanhaPublicoAlvoProspectVO obj = new CampanhaPublicoAlvoProspectVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getCampanhaPublicoAlvo().setCodigo(dadosSQL.getInt("campanhaPublicoAlvo"));
        obj.getProspect().setCodigo(dadosSQL.getInt("prospect"));
        if (!obj.getProspect().getCodigo().equals(0)) {
            obj.setProspect(getFacadeFactory().getProspectsFacade().consultarPorChavePrimaria(obj.getProspect().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
        }
        obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
        obj.getConsultorDistribuicaoVO().setCodigo(dadosSQL.getInt("consultorDistribuicao"));
        obj.getConsultorDistribuicaoVO().getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
        obj.getConsultorDistribuicaoVO().getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setCodigo(dadosSQL.getInt("compromissoCampanhaPublicoAlvoProspect"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setDescricao(dadosSQL.getString("descricao"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setHora(dadosSQL.getString("hora"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setTipoCompromisso(TipoCompromissoEnum.valueOf(dadosSQL.getString("tipoCompromisso")));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setObservacao(dadosSQL.getString("observacao"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setUrgente(new Boolean("urgente"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setOrigem(dadosSQL.getString("origem"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setDataCompromisso(dadosSQL.getDate("dataCompromisso"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setDataCadastro(dadosSQL.getDate("dataCadastro"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setDataCompromissoAnterior(dadosSQL.getDate("dataCompromisso"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setDataInicialCompromisso(dadosSQL.getDate("dataInicialCompromisso"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().getCampanha().setCodigo(dadosSQL.getInt("campanha"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().getEtapaWorkflowVO().setCodigo(dadosSQL.getInt("etapaworkflow"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setHistoricoReagendamentoCompromisso(dadosSQL.getString("historicoReagendamentoCompromisso"));
        obj.getCompromissoCampanhaPublicoAlvoProspectVO().setTipoContato(TipoContatoEnum.valueOf(dadosSQL.getString("tipoContato")));
		if (dadosSQL.getString("tipoSituacaoCompromissoEnum") != null && !dadosSQL.getString("tipoSituacaoCompromissoEnum").isEmpty()) {
			obj.getCompromissoCampanhaPublicoAlvoProspectVO().setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.valueOf(dadosSQL.getString("tipoSituacaoCompromissoEnum")));
		}
		obj.getCompromissoCampanhaPublicoAlvoProspectVO().getPreInscricao().setCodigo(new Integer(dadosSQL.getInt("preinscricao")));
        if (!obj.getCompromissoCampanhaPublicoAlvoProspectVO().getCodigo().equals(0) ) {
//        	obj.setCompromissoCampanhaPublicoAlvoProspectVO(getFacadeFactory().getCompromissoCampanhaPublicoAlvoProspectFacade().consultarPorChavePrimaria(obj.getCompromissoCampanhaPublicoAlvoProspectVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
        	obj.setSituacaoAtualCompromissoAgendaEnum(obj.getCompromissoCampanhaPublicoAlvoProspectVO().getTipoSituacaoCompromissoEnum());
        	if(Uteis.isAtributoPreenchido(obj.getCompromissoCampanhaPublicoAlvoProspectVO().getCompromissoAgendaPessoaHorarioVO().getCodigo()) 
        			&&  Uteis.isAtributoPreenchido(obj.getCompromissoCampanhaPublicoAlvoProspectVO().getConsultorAtual().getCodigo())
        			&&  !obj.getCompromissoCampanhaPublicoAlvoProspectVO().getConsultorAtual().getCodigo().equals(obj.getConsultorDistribuicaoVO().getCodigo())){
        		obj.setConsultorDistribuicaoVO(obj.getCompromissoCampanhaPublicoAlvoProspectVO().getConsultorAtual());
        		getFacadeFactory().getCampanhaPublicoAlvoProspectFacade().alterarConsultorDistribuicao(obj.getCodigo(), obj.getConsultorDistribuicaoVO().getCodigo());
        	}
        }
        return obj;
    }

    public static String getIdEntidade() {
        return CampanhaPublicoAlvoProspect.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        CampanhaPublicoAlvoProspect.idEntidade = idEntidade;
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirCampanhaPublicoAlvoProspect(Integer campanha, Integer campanhaPublicoAlvo, List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs, UsuarioVO usuarioVO) throws Exception {
    	List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectExcluirVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
    	for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : listaCampanhaPublicoAlvoProspectVOs) {
    		
    		if ((campanhaPublicoAlvoProspectVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR)
    				|| campanhaPublicoAlvoProspectVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REMOVER_AGENDA))
    				&& (campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)
    				|| campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.NAO_POSSUI_AGENDA))) {
    			//Adiciona os prospects que precisam ser regerados com as novas distribuições
    			
    			listaCampanhaPublicoAlvoProspectExcluirVOs.add(campanhaPublicoAlvoProspectVO);
    		} else if (!campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)
    				&& !campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.NAO_POSSUI_AGENDA)) {
    			//Se entrar aqui nós iremos atualizar o consultor responsável por realizar a interação com o prospect
    			alterarConsultorResponsavelProspect(campanhaPublicoAlvoProspectVO.getProspect().getCodigo(), campanhaPublicoAlvo, campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getCodigo(), usuarioVO);
    		}
    	}
    	if (listaCampanhaPublicoAlvoProspectExcluirVOs.isEmpty()) {
			return;
		}
    	
    	StringBuilder sb = new StringBuilder();
		sb.append("delete from campanhaPublicoAlvoProspect where campanhaPublicoAlvo = ?");
		sb.append(" and codigo in(");
		boolean primeiraVez = true;
		for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : listaCampanhaPublicoAlvoProspectExcluirVOs) {
			if (primeiraVez) {
				sb.append(campanhaPublicoAlvoProspectVO.getCodigo());
				primeiraVez = false;
			} else {
				sb.append(", ").append(campanhaPublicoAlvoProspectVO.getCodigo());
			}
		}
		sb.append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sb.toString(), new Object[] { campanhaPublicoAlvo });
		
		//Exclui os compromissos da campanha publico alvo
		for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : listaCampanhaPublicoAlvoProspectExcluirVOs) {
			getFacadeFactory().getCompromissoCampanhaPublicoAlvoProspectFacade().excluirCompromissoCampanhaPublicoAlvoPorCampanhaProspect(campanha, campanhaPublicoAlvoProspectVO.getProspect().getCodigo(), usuarioVO);
			
		}
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarCampanhaPublicoAlvoProspectPorCampanhaPublicoAlvo(Integer campanha, Integer campanhaPublicoAlvo, List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs, UsuarioVO usuarioVO) throws Exception {
    	excluirCampanhaPublicoAlvoProspect(campanha, campanhaPublicoAlvo, listaCampanhaPublicoAlvoProspectVOs, usuarioVO);
    	for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : listaCampanhaPublicoAlvoProspectVOs) {
    		if ((campanhaPublicoAlvoProspectVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REDISTRIBUIR)
    				|| campanhaPublicoAlvoProspectVO.getTipoDistribuicaoProspectCampanhaPublicoAlvo().equals(TipoDistribuicaoProspectCampanhaPublicoAlvoEnum.REMOVER_AGENDA))
    				&& (campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO)
    				|| campanhaPublicoAlvoProspectVO.getSituacaoAtualCompromissoAgendaEnum().equals(TipoSituacaoCompromissoEnum.NAO_POSSUI_AGENDA))) {
    			campanhaPublicoAlvoProspectVO.getCampanhaPublicoAlvo().setCodigo(campanhaPublicoAlvo);
				incluir(campanhaPublicoAlvoProspectVO, usuarioVO);
			} 
		}
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarConsultorResponsavelProspect(final Integer prospect, final Integer campanhaPublicoAlvo, final Integer consultor, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE campanhapublicoalvoprospect set consultorDistribuicao=? WHERE campanhaPublicoAlvo = ? and prospect = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setInt(++i, consultor);
					sqlAlterar.setInt(++i, campanhaPublicoAlvo.intValue());
					sqlAlterar.setInt(++i, prospect.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
    
    @Override
    public List<CampanhaPublicoAlvoProspectVO> realizarCarregamentoProspectIniciouAgendaParaVisualizacao(CampanhaVO campanhaVO, List<CompromissoAgendaPessoaHorarioVO> listaCompromissoIniciouAgendaVOs, Boolean publicoAlvoEspecifico, List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs) {
    	List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectIniciouAgendaVOs = new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
    	if (publicoAlvoEspecifico) {
    		for (CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO : listaCompromissoIniciouAgendaVOs) {
    			
    			for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : listaCampanhaPublicoAlvoProspectVOs) {
    				if (compromissoAgendaPessoaHorarioVO.getProspect().getCodigo().equals(campanhaPublicoAlvoProspectVO.getProspect().getCodigo())) {
    					try {
							CampanhaPublicoAlvoProspectVO clone = (CampanhaPublicoAlvoProspectVO) campanhaPublicoAlvoProspectVO.clone();	
							clone.setCompromissoCampanhaPublicoAlvoProspectVO((CompromissoCampanhaPublicoAlvoProspectVO) campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().clone());
							clone.getCompromissoCampanhaPublicoAlvoProspectVO().setDataCompromisso(compromissoAgendaPessoaHorarioVO.getDataCompromisso());
							clone.getCompromissoCampanhaPublicoAlvoProspectVO().setHora(compromissoAgendaPessoaHorarioVO.getHora());
							clone.getCompromissoCampanhaPublicoAlvoProspectVO().setTipoSituacaoCompromissoEnum(compromissoAgendaPessoaHorarioVO.getTipoSituacaoCompromissoEnum());
							clone.setSituacaoAtualCompromissoAgendaEnum(compromissoAgendaPessoaHorarioVO.getTipoSituacaoCompromissoEnum());
							listaCampanhaPublicoAlvoProspectIniciouAgendaVOs.add(clone);
						} catch (CloneNotSupportedException e) {
						}
    					break;
    				}
    			}
    		}
    	} else {
    		for (CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO : listaCompromissoIniciouAgendaVOs) {
    			
    			for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : campanhaVO.getListaCampanhaPublicoAlvo()) {
    				for (CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO : campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs()) {
        				if (compromissoAgendaPessoaHorarioVO.getProspect().getCodigo().equals(campanhaPublicoAlvoProspectVO.getProspect().getCodigo())) {
        					try {
        						CampanhaPublicoAlvoProspectVO clone = (CampanhaPublicoAlvoProspectVO) campanhaPublicoAlvoProspectVO.clone();
								clone.setCompromissoCampanhaPublicoAlvoProspectVO((CompromissoCampanhaPublicoAlvoProspectVO) campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().clone());
								clone.getCompromissoCampanhaPublicoAlvoProspectVO().setDataCompromisso(compromissoAgendaPessoaHorarioVO.getDataCompromisso());
								clone.getCompromissoCampanhaPublicoAlvoProspectVO().setHora(compromissoAgendaPessoaHorarioVO.getHora());
								clone.getCompromissoCampanhaPublicoAlvoProspectVO().setTipoSituacaoCompromissoEnum(compromissoAgendaPessoaHorarioVO.getTipoSituacaoCompromissoEnum());
								clone.setSituacaoAtualCompromissoAgendaEnum(compromissoAgendaPessoaHorarioVO.getTipoSituacaoCompromissoEnum());
								listaCampanhaPublicoAlvoProspectIniciouAgendaVOs.add(clone);
        					} catch (CloneNotSupportedException e) {        					
        					}	
        					break;
        				}
        			}
    			}
    		}
    	}
    	return listaCampanhaPublicoAlvoProspectIniciouAgendaVOs;
    }
    
    @Override
    public List<CampanhaPublicoAlvoProspectVO> consultarProspectsVinculadoCampanhaSemVinculoPublicoAlvo(CampanhaVO campanhaVO, UsuarioVO usuarioVO) throws Exception{
		StringBuilder sql = new StringBuilder(" select distinct prospects.codigo, prospects.nome, prospects.cpf, prospects.emailprincipal, prospects.pessoa,  ");
		sql.append(" pessoa.nome as pessoa_nome, pessoa.cpf as pessoa_cpf, pessoa.email as pessoa_email, ");
		sql.append(" consultor.codigo as consultor, consultor.nome as consultor_nome, consultor.cpf as consultor_cpf, consultor.email as consultor_email, ");		
		sql.append(" funcionario.codigo as funcionario_codigo,  ");
		sql.append(" compromissoagendapessoahorario.descricao as compromisso_descricao, compromissoagendapessoahorario.hora as compromisso_hora, ");
		sql.append(" compromissoagendapessoahorario.horaFim as compromisso_horaFim, compromissoagendapessoahorario.tipoContato as compromisso_tipoContato, ");		
		sql.append(" compromissoagendapessoahorario.origem as compromisso_origem, compromissoagendapessoahorario.observacao as compromisso_observacao, ");
		sql.append(" compromissoagendapessoahorario.urgente as compromisso_urgente, compromissoagendapessoahorario.dataCadastro as compromisso_dataCadastro, ");
		sql.append(" compromissoagendapessoahorario.dataCompromisso as compromisso_dataCompromisso,  ");
		sql.append(" compromissoagendapessoahorario.etapaworkflow as compromisso_etapaworkflow, compromissoagendapessoahorario.tipoSituacaoCompromissoEnum as compromisso_tipoSituacaoCompromissoEnum, ");
		sql.append(" compromissoagendapessoahorario.preInscricao as compromisso_preInscricao, compromissoagendapessoahorario.dataInicialCompromisso as compromisso_dataInicialCompromisso, ");
		sql.append(" compromissoagendapessoahorario.historicoReagendamentoCompromisso as compromisso_historicoReagendamentoCompromisso, ");
		sql.append(" compromissoagendapessoahorario.codigo as compromisso_codigo  ");
		sql.append(" from prospects ");		
		sql.append(" inner join compromissoagendapessoahorario on compromissoagendapessoahorario.prospect = prospects.codigo ");
		sql.append(" and compromissoagendapessoahorario.codigo = ( ");
		sql.append(" select caph.codigo from compromissoagendapessoahorario caph where caph.prospect = prospects.codigo ");
		sql.append(" and caph.campanha =").append(campanhaVO.getCodigo()).append(" order by case when caph.tiposituacaocompromissoenum in ('AGUARDANDO_CONTATO', 'PARALIZADO') then 0 else 1 end, caph.datacompromisso desc, caph.hora desc, caph.codigo desc limit 1) ");
		sql.append(" inner join agendaPessoaHorario on  agendaPessoaHorario.codigo = compromissoagendapessoahorario.agendaPessoaHorario ");
		sql.append(" inner join agendaPessoa on  agendaPessoaHorario.agendaPessoa = agendaPessoa.codigo ");
		sql.append(" inner join pessoa as consultor on  agendaPessoa.pessoa = consultor.codigo ");
		sql.append(" inner join funcionario on  funcionario.pessoa = consultor.codigo ");
		sql.append(" left join pessoa on pessoa.codigo = prospects.pessoa ");
		sql.append(" where compromissoagendapessoahorario.campanha = ").append(campanhaVO.getCodigo());
		sql.append(" and not exists ( ");
		sql.append(" select compromissocampanhapublicoalvoprospect.codigo from compromissocampanhapublicoalvoprospect where compromissocampanhapublicoalvoprospect.prospect = prospects.codigo ");
		sql.append(" and compromissocampanhapublicoalvoprospect.campanha = ").append(campanhaVO.getCodigo());
		sql.append(" ) order by prospects.nome ");
		List<CampanhaPublicoAlvoProspectVO> campanhaPublicoAlvoProspectVOs =  new ArrayList<CampanhaPublicoAlvoProspectVO>(0);
		CampanhaPublicoAlvoProspectVO campanhaPublicoAlvoProspectVO = null;
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while(rs.next()){
			campanhaPublicoAlvoProspectVO =  new CampanhaPublicoAlvoProspectVO();
			campanhaPublicoAlvoProspectVO.getProspect().setCodigo(rs.getInt("codigo"));
			campanhaPublicoAlvoProspectVO.getProspect().setNome(rs.getString("nome"));
			campanhaPublicoAlvoProspectVO.getProspect().setCpf(rs.getString("cpf"));
			campanhaPublicoAlvoProspectVO.getProspect().setEmailPrincipal(rs.getString("emailprincipal"));
			campanhaPublicoAlvoProspectVO.getProspect().getPessoa().setCodigo(rs.getInt("pessoa"));
			campanhaPublicoAlvoProspectVO.getProspect().getPessoa().setNome(rs.getString("pessoa_nome"));
			campanhaPublicoAlvoProspectVO.getProspect().getPessoa().setCPF(rs.getString("pessoa_cpf"));
			campanhaPublicoAlvoProspectVO.getProspect().getPessoa().setEmail(rs.getString("pessoa_email"));			
			campanhaPublicoAlvoProspectVO.getPessoa().setCodigo(rs.getInt("pessoa"));
			campanhaPublicoAlvoProspectVO.getPessoa().setNome(rs.getString("pessoa_nome"));
			campanhaPublicoAlvoProspectVO.getPessoa().setCPF(rs.getString("pessoa_cpf"));
			campanhaPublicoAlvoProspectVO.getPessoa().setEmail(rs.getString("pessoa_email"));
			campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().setCodigo(rs.getInt("funcionario_codigo"));
			campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getPessoa().setCodigo(rs.getInt("consultor"));
			campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getPessoa().setNome(rs.getString("consultor_nome"));
			campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getPessoa().setCPF(rs.getString("consultor_cpf"));
			campanhaPublicoAlvoProspectVO.getConsultorDistribuicaoVO().getPessoa().setEmail(rs.getString("consultor_email"));
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setProspect(campanhaPublicoAlvoProspectVO.getProspect());			
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setCampanha(campanhaVO);
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setDescricao(rs.getString("compromisso_descricao"));
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setHora(rs.getString("compromisso_hora"));
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setHoraFim(rs.getString("compromisso_horaFim"));
			if(rs.getString("compromisso_tipoContato") != null){
				campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setTipoContato(TipoContatoEnum.valueOf(rs.getString("compromisso_tipoContato")));
			}
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setOrigem(rs.getString("compromisso_origem"));
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setObservacao(rs.getString("compromisso_observacao"));
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setUrgente(rs.getBoolean("compromisso_urgente"));
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setDataCadastro(rs.getDate("compromisso_dataCadastro"));
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setDataCompromisso(rs.getDate("compromisso_dataCompromisso"));
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getEtapaWorkflowVO().setCodigo(rs.getInt("compromisso_etapaworkflow"));
			if(rs.getString("compromisso_tipoSituacaoCompromissoEnum") != null){
				campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.getEnum(rs.getString("compromisso_tipoSituacaoCompromissoEnum")));
			}
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getPreInscricao().setCodigo(rs.getInt("compromisso_preInscricao"));			
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().setDataInicialCompromisso(rs.getDate("compromisso_dataInicialCompromisso"));
			campanhaPublicoAlvoProspectVO.getCompromissoCampanhaPublicoAlvoProspectVO().getCompromissoAgendaPessoaHorarioVO().setCodigo(rs.getInt("compromisso_codigo"));
			campanhaPublicoAlvoProspectVOs.add(campanhaPublicoAlvoProspectVO);
		}
		return campanhaPublicoAlvoProspectVOs;
	}
    
    /**
     * Este metodo tem a finalidade de corrigir o consultor vinculado a tabela campanhapublicoalvoprospect, pois se o sistema gerar o compromisso para o consultor x 
     * e na tela de agenda do consultor este compromisso for alterado para outro consultor o sistema já realiza a correção na campanha
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAlteracaoConsultorAgendaCampanhaPublicoAlvoProspect(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO, UsuarioVO usuarioVO) throws Exception{
    	if(Uteis.isAtributoPreenchido(compromissoAgendaPessoaHorarioVO.getCampanha()) && Uteis.isAtributoPreenchido(compromissoAgendaPessoaHorarioVO.getProspect()) && Uteis.isAtributoPreenchido(compromissoAgendaPessoaHorarioVO.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo())){    		
    		StringBuilder sql  = new StringBuilder("update campanhapublicoalvoprospect ");
    		sql.append(" set consultorDistribuicao =  funcionario.codigo ");
    		sql.append(" from compromissocampanhapublicoalvoprospect, funcionario where ");
    		sql.append(" funcionario.pessoa = ").append(compromissoAgendaPessoaHorarioVO.getAgendaPessoaHorario().getAgendaPessoa().getPessoa().getCodigo());
    		sql.append(" and campanhapublicoalvoprospect.consultorDistribuicao != funcionario.codigo ");
    		sql.append(" and compromissocampanhapublicoalvoprospect.codigo = campanhapublicoalvoprospect.compromissocampanhapublicoalvoprospect ");
    		sql.append(" and compromissocampanhapublicoalvoprospect.prospect = ").append(compromissoAgendaPessoaHorarioVO.getProspect().getCodigo());
    		sql.append(" and compromissocampanhapublicoalvoprospect.campanha = ").append(compromissoAgendaPessoaHorarioVO.getCampanha().getCodigo());
    		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    		getConexao().getJdbcTemplate().update(sql.toString());    		    		
    	}
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarConsultorDistribuicao(Integer campanhaPublicoAlvoPropect, Integer funcionarioConsultor) throws Exception{
    	if(Uteis.isAtributoPreenchido(campanhaPublicoAlvoPropect) && Uteis.isAtributoPreenchido(funcionarioConsultor)){
    		getConexao().getJdbcTemplate().update(" update campanhapublicoalvoprospect set consultorDistribuicao = ? where codigo =? ", funcionarioConsultor, campanhaPublicoAlvoPropect);
    	}
    }
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCampanhaPublicoAlvoProspectUnificacaoFuncionario(Integer funcManter, Integer funcRemover , UsuarioVO usuarioVO) throws Exception {
		final String sqlStr = "UPDATE campanhapublicoalvoprospect set  consultordistribuicao = ? WHERE ((consultordistribuicao = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);;
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { funcManter, funcRemover });
	}
}
