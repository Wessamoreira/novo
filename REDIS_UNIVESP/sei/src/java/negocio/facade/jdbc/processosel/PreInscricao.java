package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.CampanhaColaboradorCursoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.AgendaPessoaVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.HistoricoFollowUpVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.crm.enumerador.TipoCompromissoEnum;
import negocio.comuns.crm.enumerador.TipoContatoEnum;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.processosel.PreInscricaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.processosel.PreInscricaoInterfaceFacade;
import webservice.servicos.objetos.LeadRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe
 * <code>PreInscricaoVO</code>. Responsável por implementar operações como
 * incluir, alterar, excluir e consultar pertinentes a classe
 * <code>PreInscricaoVO</code>. Encapsula toda a interação com o banco de dados.
 *
 * @see PreInscricaoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class PreInscricao extends ControleAcesso implements PreInscricaoInterfaceFacade {

    protected static String idEntidade;

    public PreInscricao() throws Exception {
        super();
        setIdEntidade("PreInscricao");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>PreInscricaoVO</code>.
     */
    public PreInscricaoVO novo() throws Exception {
        PreInscricao.incluir(getIdEntidade());
        PreInscricaoVO obj = new PreInscricaoVO();
        return obj;
    }

    public static void validarDados(PreInscricaoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws ConsistirException {
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Inscrição) deve ser informado.");
        }
        if ((obj.getCurso() == null) || (obj.getCurso().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CURSO (Inscrição) deve ser informado.");
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PreInscricaoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption Se uma inconsistência for encontrada
     * aumaticamente é gerada uma exceção descrevendo o atributo e o erro
     * ocorrido.
     */
    public static void validarDados(PreInscricaoVO obj, UsuarioVO usuario) throws ConsistirException {
        validarDados(obj, true, usuario);
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>PreInscricaoVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação
     * <code>incluir</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>PreInscricaoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso
     * ou validação de dados.
     */
    public void incluir(PreInscricaoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
        incluir(obj, true, configuracaoFinanceiro, configuracaoGeralSistemaVO, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean consultaProspectPorEmail(PreInscricaoVO obj) throws Exception {
        ProspectsVO pro = getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(obj.getProspect().getEmailPrincipal(), false, null);
        if (pro.getCodigo().intValue() != 0) {
            obj.setProspect(pro);
            return true;
        }
        return false;
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean consultaProspectPorCpf(PreInscricaoVO obj) throws Exception {
        ProspectsVO pro = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(obj.getProspect(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
        if (pro.getCodigo().intValue() != 0) {
            obj.setProspect(pro);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean consultaPessoaPorEmail(PreInscricaoVO obj) throws Exception {
        PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorEmailUnico(obj.getProspect().getEmailPrincipal(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
        if (pessoaVO.getCodigo().intValue() != 0) {
            obj.getProspect().setPessoa(pessoaVO);
            return true;
        }
        return false;
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean consultaPessoaPorCpf(PreInscricaoVO obj) throws Exception {
        PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(obj.getProspect().getCpf(), 0, "", false, Uteis.NIVELMONTARDADOS_TODOS, null);
        if (pessoaVO.getCodigo().intValue() != 0) {
            obj.getProspect().setPessoa(pessoaVO);
            return true;
        }
        return false;
    }    

    /**
     * Se entrarmos aqui é por que existe a pessoa, logo temos que gerar o prospect para a mesma. Seguindo como
     * os dados básicos da pessoa, mantendo assim coerência entre os dados da pessoa e do prospect.
     * Com intuito de permitir uma gestão de eventuais problemas, como por exemplo, no cadastro da pre-inscricao
     * por equivoco informar o e-mail errado e acabar coincidindo com o e-mail de uma pessoa do SEI,
     * então vamos gravar no histórico do prospect, os dados conforme informado na pré. 
     * @param pessoaVO
     * @param prospect
     * @throws Exception 
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistirProspectUtilizandoDadosDaPessoa(PessoaVO pessoaVO, PreInscricaoVO obj, 
        ConfiguracaoGeralSistemaVO configuracaoVO, List<CursoInteresseVO> cursosInteressePre,
        List<FormacaoAcademicaVO> formacoesInteressePre) throws Exception {
        String historicoProspect = obj.getHistoricoCriacaoProspect();
        obj.getProspect().setNome(pessoaVO.getNome());
        obj.getProspect().setEmailPrincipal(pessoaVO.getEmail());
        obj.getProspect().setCpf(pessoaVO.getCPF());
        obj.getProspect().setPessoa(pessoaVO);
//        obj.getProspect().setCelular(pessoaVO.getCelular());
//        obj.getProspect().setTelefoneComercial(pessoaVO.getTelefoneComer());
//        obj.getProspect().setTelefoneResidencial(pessoaVO.getTelefoneRes());
        //obj.getProspect().setSexo(pessoaVO.getSexo());
        //obj.getProspect().setPessoa(pessoaVO);
        // Persistindo o prospect com dados básicos. 
        //
        getFacadeFactory().getProspectsFacade().incluirSemValidarDados(obj.getProspect(), false, null, configuracaoVO);
        // Refletindo no prospect todos os dados já informados na Pessoa
        getFacadeFactory().getProspectsFacade().alterarProspectConformePessoa(pessoaVO, false, null);
        // Atualizar curso de interesse e formacao informada durante a pre-inscricao
        adicionarCursosInteressePreInscricao(obj, cursosInteressePre);
        adicionarFormacoesAcademicasPreInscricao(obj, formacoesInteressePre);
        
        // Gravando o histórico do prospect, com dados originais da pré-inscrição
        HistoricoFollowUpVO historicoFollowUpVO = new HistoricoFollowUpVO();
        historicoFollowUpVO.setObservacao(historicoProspect);
        historicoFollowUpVO.setProspect(obj.getProspect());
        getFacadeFactory().getHistoricoFollowUpFacade().incluir(historicoFollowUpVO, null);
        // Uma vez que os dados do prospect foram gravados atualizados com os dados das pessoas, temos carregá-los
        // conforme estão na base, pois os mesmos serão refletivos novamente para o aluno, quando a pré-inscrição é gravada
        // logo precisam estar atualizados
        getFacadeFactory().getProspectsFacade().carregarDados(obj.getProspect(), null);
    }
    
    public void adicionarFormacoesAcademicasPreInscricao(PreInscricaoVO obj, List<FormacaoAcademicaVO> formacoesPre) throws Exception {
        for (FormacaoAcademicaVO formacao : formacoesPre) {
            Boolean jaAdicionado = false;
            for (FormacaoAcademicaVO formacaoExistente : obj.getProspect().getFormacaoAcademicaVOs()) {
                if (formacaoExistente.getCurso().equals(formacao.getCurso())) {
                    jaAdicionado = true;
                }
            }
            if (!jaAdicionado) {
                obj.getProspect().getFormacaoAcademicaVOs().add(formacao);
            }
        }        
    }
    
    private void atualizarDadosProspectPorPreInscricao(PreInscricaoVO obj) {
	//	if (!Uteis.isAtributoPreenchido(obj.getProspect().getEmailPrincipal()) && Uteis.isAtributoPreenchido(obj.getEmail())) {
			obj.getProspect().setEmailPrincipal(obj.getEmail());
	//	}		
	//	if(!Uteis.isAtributoPreenchido(obj.getProspect().getTelefoneComercial()) && Uteis.isAtributoPreenchido(obj.getTelefoneComercial())) {
			obj.getProspect().setTelefoneComercial(obj.getTelefoneComercial());
	//	}
//    	if(!Uteis.isAtributoPreenchido(obj.getProspect().getTelefoneResidencial()) && Uteis.isAtributoPreenchido(obj.getTelefoneResidencial())) {
			obj.getProspect().setTelefoneResidencial(obj.getTelefoneResidencial());
	//	}
	//	if(!Uteis.isAtributoPreenchido(obj.getProspect().getCelular()) && Uteis.isAtributoPreenchido(obj.getCelular())) {
			obj.getProspect().setCelular(obj.getCelular());
	//	}  
    }
    
    public void adicionarCursosInteressePreInscricao(PreInscricaoVO obj, List<CursoInteresseVO> cursosInteressePre) throws Exception {
        for (CursoInteresseVO cursoInteressePre : cursosInteressePre) {
            Boolean cursoJaAdicionado = false;
            for (CursoInteresseVO cursoExistente : obj.getProspect().getCursoInteresseVOs()) {
                if (cursoExistente.getCurso().getCodigo().equals(cursoInteressePre.getCurso().getCodigo())) {
                    cursoJaAdicionado = true;
                }
            }
            if (!cursoJaAdicionado) {
                obj.getProspect().getCursoInteresseVOs().add(cursoInteressePre);
            }
        }
    }
    
    /**
     * Método responsável por validar e persistir uma preinscricao feita por meio do site da instituicao
     * cliente ou por meio do formulario homePreInscricao2.jsp do próprio SEI. Mas trata-se de uma pré-inscrição
     * na qual não possui um usuário logado, apenas dados básicos do prospect, unidade e curso de interesse.
     * @throws Exception 
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirPreInscricaoAPartirSiteOuHomePreInscricao(final PreInscricaoVO obj, ConfiguracaoGeralSistemaVO configuracaoVO) throws Exception {
		if (configuracaoVO == null) {
			configuracaoVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, 0);
		}
		PreInscricaoVO preInscricao = (PreInscricaoVO) obj.clone();
		getFacadeFactory().getProspectsFacade().validarDadosPreInscricao(obj.getProspect(), configuracaoVO);
		obj.setNome(obj.getProspect().getNome());
		obj.setTelefoneResidencial(obj.getProspect().getTelefoneResidencial());
		obj.setTelefoneComercial(obj.getProspect().getTelefoneComercial());
		obj.setCelular(obj.getProspect().getCelular());
		obj.setEmail(obj.getProspect().getEmailPrincipal());
		Boolean prospectExistente = false;
		List<CursoInteresseVO> cursosInteressePre = obj.getProspect().getCursoInteresseVOs();
		List<FormacaoAcademicaVO> formacoesInteressePre = obj.getProspect().getFormacaoAcademicaVOs();
		if (!preInscricao.getProspect().getCpf().equals("")) {
			prospectExistente = consultaProspectPorCpf(preInscricao);
		}
		if (!prospectExistente) {
			prospectExistente = consultaProspectPorEmail(preInscricao);
		}
		if (prospectExistente) {
			obj.getProspect().setCodigo(preInscricao.getProspect().getCodigo());
			obj.getProspect().setNovoObj(false);
			atualizarDadosProspectPorPreInscricao(obj);
			adicionarCursosInteressePreInscricao(obj, cursosInteressePre);
			adicionarFormacoesAcademicasPreInscricao(obj, formacoesInteressePre);
		} else {
			Boolean pessoaExistente = false;
			if (!obj.getProspect().getCpf().equals("")) {
				pessoaExistente = consultaPessoaPorCpf(obj);
			}
			if (!pessoaExistente) {
				pessoaExistente = consultaPessoaPorEmail(obj);
			}
			if (pessoaExistente) {
				persistirProspectUtilizandoDadosDaPessoa(obj.getProspect().getPessoa(), obj, configuracaoVO, cursosInteressePre, formacoesInteressePre);
			}
		}
		if (existePreInscricaoProspect(obj, null)) {
			return;
		}
		getFacadeFactory().getPreInscricaoFacade().incluir(obj, false, null, configuracaoVO, null);
	}
    
    public Boolean existePreInscricaoProspect(PreInscricaoVO obj, UsuarioVO usuarioVO) throws Exception {
    	if (consultarProspectJaInscrito(obj, usuarioVO) && obj.getReenvioPreInscricao()) {
    		return true;
    	}
    	return false;
    }

    @SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PreInscricaoVO obj, boolean verificarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
        try {
            PreInscricao.incluir(getIdEntidade(), verificarAcesso, usuario);
            validarDados(obj, verificarAcesso, usuario);
            if (consultarProspectJaInscrito(obj, usuario)) {
                throw new ConsistirException("Já existe uma inscrição realizada para esse curso e unidade de ensino!");
            }
            realizarPersistenciaProspectPreInscricao(obj, configuracaoGeralSistemaVO, true, verificarAcesso, usuario);
            realizarPersistenciaCompromissoPreInscricao(obj, verificarAcesso, usuario);

            // Gerar Agenda Aqui!!!

            final String sql = "INSERT INTO PreInscricao( unidadeEnsino, curso, prospect, data, responsavel, nome, telefoneResidencial, telefoneComercial, celular, email, nomeBatismo ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    try {
                        sqlInserir.setInt(1, obj.getUnidadeEnsino().getCodigo().intValue());
                        if (obj.getCurso().getCodigo().intValue() != 0) {
                            sqlInserir.setInt(2, obj.getCurso().getCodigo().intValue());
                        } else {
                            sqlInserir.setNull(2, 0);
                        }
                        if (obj.getProspect().getCodigo().intValue() != 0) {
                            sqlInserir.setInt(3, obj.getProspect().getCodigo().intValue());
                        } else {
                            sqlInserir.setNull(3, 0);
                        }
                        sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getData()));
                        sqlInserir.setInt(5, obj.getResponsavel().getCodigo().intValue());
                        sqlInserir.setString(6, obj.getNome());
                        sqlInserir.setString(7, obj.getTelefoneResidencial());
                        sqlInserir.setString(8, obj.getTelefoneComercial());
                        sqlInserir.setString(9, obj.getCelular());
                        sqlInserir.setString(10, obj.getEmail());
                        sqlInserir.setString(11, obj.getNomeBatismo());
                        
                    } catch (Exception x) {
                        return null;
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));
            getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().gravarPreInscricaoCompromissoAgendaPessoaHorario(obj.getCompromissoAgendaPessoaHorarioVO().getCodigo(), obj.getCodigo(), usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            obj.setCodigo(0);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarPersistenciaProspectPreInscricao(final PreInscricaoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean apenasDadosPreenchidos, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		if (apenasDadosPreenchidos) {
			if (obj.getProspect().isNovoObj().booleanValue()) {
				getFacadeFactory().getProspectsFacade().incluirSemValidarDados(obj.getProspect(), verificarAcesso, usuario, configuracaoGeralSistemaVO);
			} else {
				getFacadeFactory().getProspectsFacade().alterarApenasDadosPreenchidos(obj.getProspect(), verificarAcesso, usuario);
			}
		} else {
			if (obj.getProspect().isNovoObj().booleanValue()) {
				getFacadeFactory().getProspectsFacade().incluirSemValidarDados(obj.getProspect(), verificarAcesso, usuario, configuracaoGeralSistemaVO);
			} else {
				getFacadeFactory().getProspectsFacade().alterarSemValidarDados(obj.getProspect(), verificarAcesso, usuario, configuracaoGeralSistemaVO, true);
			}
		}
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarPersistenciaCompromissoPreInscricao(final PreInscricaoVO obj,
            boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        realizarPersistenciaCompromissoPreInscricao(obj, verificarAcesso, usuario, null);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarPersistenciaCompromissoPreInscricao(final PreInscricaoVO obj, boolean verificarAcesso,
            UsuarioVO usuario, FuncionarioVO funcionarioAgenda) throws Exception {
        CampanhaColaboradorCursoVO campanhaColaboradorCursoVO = getFacadeFactory().getCampanhaColaboradorCursoFacade().consultarCampanhaAndResponsavel(obj.getCurso().getCodigo(), obj.getUnidadeEnsino().getCodigo(), "AT", TipoCampanhaEnum.PRE_INSCRICAO, false, usuario);
        if (campanhaColaboradorCursoVO.getCampanhaColaboradorVO().getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo().intValue() == 0) {
        	throw new ConsistirException("Não existe nenhum funcionário definido como responsável pelo curso para o Prospect "+obj.getNome().toUpperCase()+"!");
        }
        AgendaPessoaVO agenda = null;
        if (funcionarioAgenda == null) {
            obj.setResponsavel(campanhaColaboradorCursoVO.getCampanhaColaboradorVO().getFuncionarioCargoVO().getFuncionarioVO());
            agenda = realizarValidacaoAgenda(campanhaColaboradorCursoVO.getCampanhaColaboradorVO().getFuncionarioCargoVO().getFuncionarioVO().getPessoa(), usuario);
        } else {
            // trata-se de uma situacao na qual o gestor está definindo manualmente o
            // consultor responsavel o prospect. 
            obj.setResponsavel(funcionarioAgenda);
            agenda = realizarValidacaoAgenda(funcionarioAgenda.getPessoa(), usuario);
        }
        obj.getCompromissoAgendaPessoaHorarioVO().setProspect(obj.getProspect());//      
        obj.getCompromissoAgendaPessoaHorarioVO().setCampanha(campanhaColaboradorCursoVO.getCampanhaColaboradorVO().getCampanha());
        obj.getCompromissoAgendaPessoaHorarioVO().setDataCadastro(new Date());
        obj.getCompromissoAgendaPessoaHorarioVO().setDescricao("Atendimento Pré-Inscrição");
        obj.getCompromissoAgendaPessoaHorarioVO().setObservacao("");
        obj.getCompromissoAgendaPessoaHorarioVO().setOrigem("");
        obj.getCompromissoAgendaPessoaHorarioVO().setPreInscricao(obj);
        obj.getCompromissoAgendaPessoaHorarioVO().setTipoCompromisso(TipoCompromissoEnum.CONTATO);
        obj.getCompromissoAgendaPessoaHorarioVO().setTipoContato(TipoContatoEnum.TELEFONE);
        obj.getCompromissoAgendaPessoaHorarioVO().setTipoSituacaoCompromissoEnum(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO);
        obj.getCompromissoAgendaPessoaHorarioVO().setUrgente(Boolean.TRUE);
        gerarCompromissoUrgente(obj.getCompromissoAgendaPessoaHorarioVO(), agenda);
        obj.getCompromissoAgendaPessoaHorarioVO().setDataCompromisso(obj.getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getDataCompromisso());
        obj.getCompromissoAgendaPessoaHorarioVO().setDataInicialCompromisso(obj.getCompromissoAgendaPessoaHorarioVO().getAgendaPessoaHorario().getDataCompromisso());
        getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().incluirCompromissoPorAgendaHorarioPessoa(obj.getCompromissoAgendaPessoaHorarioVO(), usuario);
    }

    public AgendaPessoaVO realizarValidacaoAgenda(PessoaVO pessoa, UsuarioVO usuario) throws Exception {
        AgendaPessoaVO agenda = getFacadeFactory().getAgendaPessoaFacade().realizarValidacaoSeExisteAgendaPessoaParaUsuarioLogado(pessoa, usuario);
        if (agenda.getCodigo() == 0) {
            agenda.setPessoa(pessoa);
            getFacadeFactory().getAgendaPessoaFacade().persistir(agenda, usuario);
        }
        return agenda;
    }

    public ConfiguracaoFinanceiroVO consultarConfiguracaoFinanceiro(UsuarioVO usuario, Integer codigoUnidadeEnsino) throws Exception {
        ConfiguracaoFinanceiroVO config = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, usuario, codigoUnidadeEnsino);
        if (!Uteis.isAtributoPreenchido(config.getContaCorrentePadraoProcessoSeletivo())) {
            throw new ConsistirException("Não existe Conta Corrente padrão para processo seletivo. Vá até Configurações/Financeiro e configure uma.");
        }
        if (!Uteis.isAtributoPreenchido(config.getCentroReceitaInscricaoProcessoSeletivoPadrao())) {
            throw new ConsistirException("Não existe Centro de Receita padrão para processo seletivo. Vá até Configurações/Financeiro e configure uma.");
        }
        return config;
    }

    public PerfilAcessoVO consultarPerfilAcessoPadrao(Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, unidadeEnsino);
        if (Uteis.isAtributoPreenchido(conf) && Uteis.isAtributoPreenchido(conf.getPerfilPadraoAluno())) {
            return conf.getPerfilPadraoAluno();
        }
        throw new Exception("Não Existe nenhum perfil de acesso padrão cadastrado para efetuar a matricula.");
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>PreInscricaoVO</code>. Sempre utiliza a chave primária da classe
     * como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação
     * <code>alterar</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>PreInscricaoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso
     * ou validação de dados.
     */
    public void alterar(PreInscricaoVO obj, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
        alterar(obj, true, usuario, configuracaoFinanceiroVO);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PreInscricaoVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
        try {
            boolean valor = false;
            PreInscricao.alterar(getIdEntidade(), verificarAcesso, usuario);
            final String sql = "UPDATE PreInscricao set unidadeEnsino=?, curso=?, prospect=?, data=?, responsavel=?, nome=?, telefoneResidencial=?, telefoneComercial=?, celular=?, email=?, nomeBatismo=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getUnidadeEnsino().getCodigo().intValue());
                    if (obj.getCurso().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getCurso().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    if (obj.getProspect().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getProspect().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getData()));
                    sqlAlterar.setInt(5, obj.getResponsavel().getCodigo().intValue());
                    
                    sqlAlterar.setString(6, obj.getNome());
                    sqlAlterar.setString(7, obj.getTelefoneResidencial());
                    sqlAlterar.setString(8, obj.getTelefoneComercial());
                    sqlAlterar.setString(9, obj.getCelular());
                    sqlAlterar.setString(10, obj.getEmail());
                    sqlAlterar.setString(11, obj.getNomeBatismo());
                    
                    sqlAlterar.setInt(12, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarProspectPreInscricao(final Integer codProspectManter, final Integer codProspectRemover) throws Exception {
        try {
            final String sql = "UPDATE PreInscricao set prospect=? WHERE ((prospect = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setInt(1, codProspectManter);
                    sqlAlterar.setInt(2, codProspectRemover);
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>PreInscricaoVO</code>. Sempre localiza o registro a ser excluído
     * através da chave primária da entidade. Primeiramente verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>PreInscricaoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PreInscricaoVO obj, UsuarioVO usuario) throws Exception {
        try {
            PreInscricao.excluir(getIdEntidade(), usuario);
            String sql = "DELETE FROM PreInscricao WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    public void gerarCompromissoUrgente(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO, AgendaPessoaVO agenda) throws Exception {
        AgendaPessoaHorarioVO agendaPessoaHorarioVO = new AgendaPessoaHorarioVO();
        agendaPessoaHorarioVO = new AgendaPessoaHorarioVO(agenda, Uteis.getDiaMesData(new Date()), Uteis.getMesData(new Date()), Uteis.getAnoData(new Date()), Uteis.getDiaSemanaEnum(new Date()), true);
        compromissoAgendaPessoaHorarioVO.setAgendaPessoaHorario(agendaPessoaHorarioVO);
        String horaAvancada15min = Uteis.getHoraMinutoComMascara(Uteis.getDataFutura(new Date(), Calendar.MINUTE, 15));
        gerarHoraValidaCompromisso(horaAvancada15min, compromissoAgendaPessoaHorarioVO, agendaPessoaHorarioVO, agenda);
    }

    public void gerarHoraValidaCompromisso(String hora, CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO, AgendaPessoaHorarioVO agendaPessoaHorarioVO, AgendaPessoaVO agenda) throws Exception {
        if (!consultarHoraCompromissoAgendaPessoaHora(compromissoAgendaPessoaHorarioVO.getAgendaPessoaHorario(), hora, Uteis.getDiaMesData(new Date()))) {
            compromissoAgendaPessoaHorarioVO.setHora(hora);
        } else {
            compromissoAgendaPessoaHorarioVO.setHora(atualizarCompromissoHoraInicial(compromissoAgendaPessoaHorarioVO, compromissoAgendaPessoaHorarioVO.getAgendaPessoaHorario(), hora));
        }
        if (Integer.parseInt(hora.substring(0, 2)) >= 18) {
            Date data = Uteis.obterDataAvancadaPorDiaPorMesPorAno(agendaPessoaHorarioVO.getDia(), agendaPessoaHorarioVO.getMes(), agendaPessoaHorarioVO.getAno(), 1);
            agendaPessoaHorarioVO = new AgendaPessoaHorarioVO(agenda, Uteis.getDiaMesData(Uteis.obterDataAvancada(data, 1)), Uteis.getMesData(Uteis.obterDataAvancada(data, 1)), Uteis.getAnoData(Uteis.obterDataAvancada(data, 1)), Uteis.getDiaSemanaEnum(Uteis.obterDataAvancada(data, 1)), true);
            compromissoAgendaPessoaHorarioVO.setHora("08:00");
            gerarHoraValidaCompromisso(compromissoAgendaPessoaHorarioVO.getHora(), compromissoAgendaPessoaHorarioVO, agendaPessoaHorarioVO, agenda);
        }
        if (Integer.parseInt(hora.substring(0, 2)) >= 12 && Integer.parseInt(hora.substring(0, 2)) < 14) {
            compromissoAgendaPessoaHorarioVO.setHora("14:00");
            gerarHoraValidaCompromisso(compromissoAgendaPessoaHorarioVO.getHora(), compromissoAgendaPessoaHorarioVO, agendaPessoaHorarioVO, agenda);
        }
    }

    public String atualizarCompromissoHoraInicial(CompromissoAgendaPessoaHorarioVO novoCompromisso, AgendaPessoaHorarioVO agendaPessoaHorario, String horaAvancada15min) throws Exception {
        String horarioInicialTentativaInsercao = horaAvancada15min;
        while (consultarHoraCompromissoAgendaPessoaHora(agendaPessoaHorario, horarioInicialTentativaInsercao, agendaPessoaHorario.getDia())) {
            horarioInicialTentativaInsercao = Uteis.obterHoraAvancada(horarioInicialTentativaInsercao, 1);
        }
        return horarioInicialTentativaInsercao;
    }

    public boolean consultarHoraCompromissoAgendaPessoaHora(AgendaPessoaHorarioVO agendaPessoaHorario, String hora, Integer dia) throws Exception {
        String sql = "SELECT * FROM CompromissoAgendaPessoaHorario AS caph INNER JOIN "
                + "AgendaPessoaHorario AS aph ON aph.codigo = caph.agendapessoahorario WHERE caph.agendapessoahorario = " + agendaPessoaHorario.getCodigo() + " and caph.hora = '" + hora + "' and aph.dia = " + dia;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            return false;
        }
        return true;
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>Inscricao</code> através do valor do atributo
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao
     * período informado por parâmetro. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>PreInscricaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List consultarPorData(Date prmIni, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PreInscricao WHERE (data = '" + Uteis.getDataJDBC(prmIni) + "') ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public Boolean consultarProspectJaInscrito(PreInscricaoVO obj, UsuarioVO usuario) throws Exception {
	StringBuilder sql = new StringBuilder();
	sql.append(" SELECT preinscricao.codigo ");
	sql.append(" FROM PreInscricao  as preinscricao ");
	//sql.append(" 	left join compromissoagendapessoahorario cap on cap.preinscricao = preinscricao.codigo ");
	sql.append(" WHERE preinscricao.prospect = ").append(obj.getProspect().getCodigo());
	sql.append(" 	and preinscricao.curso = ").append(obj.getCurso().getCodigo());
	sql.append(" 	and preinscricao.unidadeensino = ").append(obj.getUnidadeEnsino().getCodigo().intValue());
	sql.append(" 	and preinscricao.data::date >= (CURRENT_DATE - 3) ");
	//sql.append(" 	and cap.datacompromisso::date >= (CURRENT_DATE - 3) ");
	//sql.append(" 	and cap.tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO' ");
	sql.append(" 	ORDER BY preinscricao.data ");
	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	if (tabelaResultado.next()) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>Inscricao</code> através do valor do atributo
     * <code>nome</code> da classe
     * <code>Curso</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @return List Contendo vários objetos da classe
     * <code>PreInscricaoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PreInscricao.* FROM PreInscricao, Curso WHERE PreInscricao.curso = Curso.codigo and  Curso.nome ilike('" + valorConsulta + "%') ORDER BY Curso.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>Inscricao</code> através do valor do atributo
     * <code>nome</code> da classe
     * <code>Pessoa</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @return List Contendo vários objetos da classe
     * <code>PreInscricaoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PreInscricao.* FROM PreInscricao, Pessoa WHERE PreInscricao.candidato = Pessoa.codigo and Pessoa.nome ilike('" + valorConsulta + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorCodigoPreInscricao(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PreInscricao.* FROM PreInscricao WHERE codigo = " + valorConsulta;
        if (unidadeEnsino != 0) {
            sqlStr += " and unidadeEnsino = " + unidadeEnsino;
        }
        sqlStr += " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>Inscricao</code> através do valor do atributo
     * <code>nome</code> da classe
     * <code>UnidadeEnsino</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @return List Contendo vários objetos da classe
     * <code>PreInscricaoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT PreInscricao.* FROM PreInscricao, UnidadeEnsino WHERE PreInscricao.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.nome ilike('" + valorConsulta + "%') ORDER BY UnidadeEnsino.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de
     * <code>Inscricao</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
     * superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>PreInscricaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PreInscricao WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe
     * <code>PreInscricaoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>PessoaVO</code> relacionado ao objeto
     * <code>PreInscricaoVO</code>. Faz uso da chave primária da classe
     * <code>PessoaVO</code> para realizar a consulta.
     *
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(PreInscricaoVO obj, int nivelMontarDados) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new FuncionarioVO());
            return;
        }
        obj.setResponsavel(obj.getResponsavel());
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>UnidadeEnsinoVO</code> relacionado ao objeto
     * <code>PreInscricaoVO</code>. Faz uso da chave primária da classe
     * <code>UnidadeEnsinoVO</code> para realizar a consulta.
     *
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(PreInscricaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        //obj.setUnidadeEnsino(obj.getUnidadeEnsino());
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosCurso(PreInscricaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
    }

    public static void montarDadosProspect(PreInscricaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setProspect(getFacadeFactory().getProspectsFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>PreInscricaoVO</code> através de sua chave primária.
     *
     * @exception Exception Caso haja problemas de conexão ou localização do
     * objeto procurado.
     */
    public PreInscricaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM PreInscricao WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (
     * <code>ResultSet</code>) em um objeto da classe
     * <code>PreInscricaoVO</code>.
     *
     * @return O objeto da classe
     * <code>PreInscricaoVO</code> com os dados devidamente montados.
     */
    public static PreInscricaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        PreInscricaoVO obj = new PreInscricaoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
        obj.getProspect().setCodigo(new Integer(dadosSQL.getInt("prospect")));
        obj.setData(dadosSQL.getDate("data"));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        
        obj.setNome(dadosSQL.getString("nome"));
        obj.setNomeBatismo(dadosSQL.getString("nomeBatismo"));
        obj.setTelefoneResidencial(dadosSQL.getString("telefoneResidencial"));
        obj.setTelefoneComercial(dadosSQL.getString("telefoneComercial"));
        obj.setCelular(dadosSQL.getString("celular"));
        obj.setEmail(dadosSQL.getString("email"));
        
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosProspect(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
        return obj;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return PreInscricao.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        PreInscricao.idEntidade = idEntidade;
    }
    
    /**
  	 * @author Victor Hugo de Paula Costa
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirPreInscricaoAPartirMatriculaExternaOnline(final PreInscricaoVO obj) throws Exception {
        ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null);
        obj.setNome(obj.getProspect().getNome());
        obj.setNomeBatismo(obj.getProspect().getNomeBatismo());
        obj.setTelefoneResidencial(obj.getProspect().getTelefoneResidencial());
        obj.setTelefoneComercial(obj.getProspect().getTelefoneComercial());
        obj.setCelular(obj.getProspect().getCelular());
        obj.setEmail(obj.getProspect().getEmailPrincipal());        
        Boolean prospectExistente = false;
        // É importante manter os dados abaixo (cursos interesse) pois iremos buscar por um prospect já existente na base. 
        // Caso um prospect seja encontrado na base, estes dados serão adicionados aos dados existentes para
        // persistência
        List<CursoInteresseVO> cursosInteressePre = obj.getProspect().getCursoInteresseVOs();
        List<FormacaoAcademicaVO> formacoesInteressePre = obj.getProspect().getFormacaoAcademicaVOs();
        // Caso exista um cpf vamos privilegiar a busca por cpd, pois é mais forte que o e-mail.
        if (!obj.getProspect().getCpf().equals("")) {
            prospectExistente = consultaProspectPorCpf(obj);
        } 
        if (!prospectExistente)  {
            prospectExistente = consultaProspectPorEmail(obj);
        }
        // Primeiro passo é verificar se o prospect já está cadastrado no SEI, evitando assim duplicidade
        // de dados. 
        if (prospectExistente) {
            // Se o mesmo existe, os dados do mesmo são carregados para que quando ele for persistido
            // com novas informações da pré-inscrição (por exemplo, curso de interesse), nenhum dado seja perdido.
            getFacadeFactory().getProspectsFacade().carregarDados(obj.getProspect(), null);
            adicionarCursosInteressePreInscricao(obj, cursosInteressePre);
            adicionarFormacoesAcademicasPreInscricao(obj, formacoesInteressePre);
            if(Uteis.isAtributoPreenchido(obj.getProspect().getPessoa())){
            	getFacadeFactory().getPessoaFacade().carregarDados(obj.getProspect().getPessoa(), null);
            }
            obj.getProspect().setNivelMontarDados(NivelMontarDados.TODOS);
        } else {
            // Caso o prospect não exista, temos que verificar se nao existe uma pessoa no SEI, com o e-mail
            // informado na pré-inscrição. Caso sim, temos que gerar um prospect associado a esta pessoa e utilizá-lo
            // para dar continuidade na Pré-inscrição. 
            Boolean pessoaExistente = false;
            if (!obj.getProspect().getCpf().equals("")) {
                pessoaExistente = consultaPessoaPorCpf(obj);
            }
            if (!pessoaExistente) {
                pessoaExistente = consultaPessoaPorEmail(obj);
            }
            if (pessoaExistente) {
            	formacoesInteressePre = obj.getProspect().getPessoa().getFormacaoAcademicaVOs();
                // Se entrarmos aqui é por que existe a pessoa, logo temos que gerar o prospect para a mesma. Seguindo como
                // os dados básicos da pessoa, mantendo assim coerência entre os dados da pessoa e do prospect.                
                persistirProspectUtilizandoDadosDaPessoa(obj.getProspect().getPessoa(), obj, configuracaoGeralSistemaVO,
                        cursosInteressePre, formacoesInteressePre);
            }
        }
        CampanhaColaboradorCursoVO campanhaColaboradorCursoVO = getFacadeFactory().getCampanhaColaboradorCursoFacade().consultarCampanhaAndResponsavel(obj.getCurso().getCodigo(), obj.getUnidadeEnsino().getCodigo(), "AT", TipoCampanhaEnum.PRE_INSCRICAO, false, null);
	    if (campanhaColaboradorCursoVO.getCampanhaColaboradorVO().getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo().intValue() == 0) {
	    	realizarPersistenciaProspectPreInscricao(obj, configuracaoGeralSistemaVO, false, false, null);
	    }else {
	    	incluirSemValidarDados(obj, false, null, configuracaoGeralSistemaVO, null);
	    }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirSemValidarDados(final PreInscricaoVO obj, boolean verificarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
        try {
            PreInscricao.incluir(getIdEntidade(), verificarAcesso, usuario);
            if (consultarProspectJaInscrito(obj, usuario)) {
                throw new ConsistirException("Já existe uma inscrição realizada para esse curso e unidade de ensino!");
            }
            realizarPersistenciaProspectPreInscricao(obj, configuracaoGeralSistemaVO, false, verificarAcesso, usuario);
            realizarPersistenciaCompromissoPreInscricao(obj, verificarAcesso, usuario);
            if(!obj.getProspect().getNivelMontarDados().equals(NivelMontarDados.TODOS)){
            	obj.setProspect(getFacadeFactory().getProspectsFacade().consultarPorChavePrimaria(obj.getProspect().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
            	if(Uteis.isAtributoPreenchido(obj.getProspect().getPessoa().getCodigo())){
            		getFacadeFactory().getPessoaFacade().carregarDados(obj.getProspect().getPessoa(), usuario);
            	}
            }
            // Gerar Agenda Aqui!!!

            final String sql = "INSERT INTO PreInscricao( unidadeEnsino, curso, prospect, data, responsavel, nome, telefoneResidencial, telefoneComercial, celular, email, nomeBatismo ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    try {
                        if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        	sqlInserir.setInt(1, obj.getUnidadeEnsino().getCodigo().intValue());
                        } else {
                            sqlInserir.setNull(1, 0);
                        }
                        if (obj.getCurso().getCodigo().intValue() != 0) {
                            sqlInserir.setInt(2, obj.getCurso().getCodigo().intValue());
                        } else {
                            sqlInserir.setNull(2, 0);
                        }
                        if (obj.getProspect().getCodigo().intValue() != 0) {
                            sqlInserir.setInt(3, obj.getProspect().getCodigo().intValue());
                        } else {
                            sqlInserir.setNull(3, 0);
                        }
                        sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getData()));
                        sqlInserir.setInt(5, obj.getResponsavel().getCodigo().intValue());
                        sqlInserir.setString(6, obj.getNome());
                        sqlInserir.setString(7, obj.getTelefoneResidencial());
                        sqlInserir.setString(8, obj.getTelefoneComercial());
                        sqlInserir.setString(9, obj.getCelular());
                        sqlInserir.setString(10, obj.getEmail());
                        sqlInserir.setString(11, obj.getNomeBatismo());
                        
                    } catch (Exception x) {
                        return null;
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            obj.setCodigo(0);
            throw e;
        }
    }
    
    
    @Override
    public PreInscricaoVO montarDadosPreInscricaoPessoa(PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, UsuarioVO usuarioVO) throws Exception {
    	PreInscricaoVO pre = new PreInscricaoVO();
    	pre.setUnidadeEnsino(unidadeEnsinoVO);
        pre.setCurso(cursoVO);
        pre.setData(new Date());
        pre.setProspect(getFacadeFactory().getProspectsFacade().consultarPorCodigoPessoa(pessoaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		if (pre.getProspect().getCodigo().equals(0)) {
			getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorPessoa(pessoaVO.getCPF(), pre.getProspect(), usuarioVO);
		}
        pre.getProspect().getPessoa().setCodigo(pessoaVO.getCodigo());
        pre.getProspect().setTipoProspect(TipoProspectEnum.FISICO);
        pre.getProspect().setCEP(pessoaVO.getCEP());
        pre.getProspect().setUnidadeEnsino(unidadeEnsinoVO);
        pre.getProspect().setCpf(pessoaVO.getCPF());
        pre.getProspect().setCelular(pessoaVO.getCelular());
        pre.getProspect().setTelefoneResidencial(pessoaVO.getTelefoneRes());
        pre.getProspect().setTelefoneComercial(pessoaVO.getTelefoneComer());
        pre.getProspect().setNaturalidade(pessoaVO.getNaturalidade());
        pre.getProspect().setEstadoCivil(pessoaVO.getEstadoCivil());
        pre.getProspect().setNome(pessoaVO.getNome());
        pre.getProspect().setNomeBatismo(pessoaVO.getNomeBatismo());
        pre.getProspect().setEmailPrincipal(pessoaVO.getEmail());
        pre.getProspect().setSexo(pessoaVO.getSexo());
        pre.getProspect().setRg(pessoaVO.getRG());
        pre.getProspect().setDataExpedicao(pessoaVO.getDataEmissaoRG());
        pre.getProspect().setOrgaoEmissor(pessoaVO.getOrgaoEmissor());
        pre.getProspect().setEstadoEmissor(pessoaVO.getEstadoEmissaoRG());
        pre.getProspect().setSetor(pessoaVO.getSetor());
        pre.getProspect().setEndereco(pessoaVO.getEndereco());
        pre.getProspect().setDataNascimento(pessoaVO.getDataNasc());
        pre.getProspect().setCidade(pessoaVO.getCidade());
        return pre;
    }
    
    
    @Override
    public PreInscricaoVO montarDadosAPartirDaLeadIntegracaoRDStation(LeadRSVO lead) throws Exception {

		// Unidade
		// Consultar unidade pelo nome
		UnidadeEnsinoVO unidadeEnsinoVO = (UnidadeEnsinoVO) getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeDeEnsinoPorNome(
				lead.getCamposPersonalizados().getUnidadeEnsino(), 
				false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
		
		// Curso
		// Consultar Curso
		CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultarPorNomeDoCursoEUnidadeDeEnsino(
				lead.getCamposPersonalizados().getCursoInteresse(), unidadeEnsinoVO.getCodigo(), 
				Uteis.NIVELMONTARDADOS_COMBOBOX, false, null);
		
		//Curso de interesse
		CursoInteresseVO cursoDeInteresse = new CursoInteresseVO();
		cursoDeInteresse.setCurso(cursoVO);
		cursoDeInteresse.setDataCadastro(new Date());		
		
		//Setar campos no prospect
		// Prospect
		ProspectsVO prospect = new ProspectsVO();
		prospect.setTipoProspect(TipoProspectEnum.FISICO);
		prospect.setNome(lead.getNome());
		prospect.setNomeBatismo(lead.getNomeBatismo());
		prospect.setEmailPrincipal(lead.getEmail());
		prospect.setCurso(cursoVO.getNome());
		
		//sincronizado com RD Station
		prospect.setSincronizadoRDStation(true);
		
		//Validação telefone para nao estourar o tamanho do campo na base de dados
		prospect.setCelular(lead.getCelular().replace(" ", ""));
		prospect.setTelefoneResidencial(lead.getTelefone().replace(" ", ""));
		prospect.getCidade().setCodigo(unidadeEnsinoVO.getCidade().getCodigo());
		prospect.setUnidadeEnsino(unidadeEnsinoVO);
		getFacadeFactory().getProspectsFacade().adicionarObjCursoInteresseVOs(prospect, cursoDeInteresse);
		
		
		// Preinscricao
		PreInscricaoVO preinscricaoVO = new PreInscricaoVO();
		preinscricaoVO.setUnidadeEnsino(unidadeEnsinoVO);
		preinscricaoVO.setCurso(cursoVO);
		preinscricaoVO.setData(new Date());
		preinscricaoVO.setProspect(prospect);
		
		
		return preinscricaoVO;
		
	}
    
    public void montarDadosCursoInteresseCompromissoAgendaPessoaHorarioPreInscricao(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO) throws Exception {
		String sql = "SELECT PreInscricao.codigo as \"PreInscricao.codigo\", Curso.codigo as \"Curso.codigo\", Curso.nome as \"Curso.nome\" "
				+ " FROM PreInscricao INNER JOIN Compromissoagendapessoahorario ON compromissoagendapessoahorario.PreInscricao = PreInscricao.codigo "
				+ " INNER JOIN Curso on PreInscricao.Curso = Curso.codigo "
				+ " WHERE Compromissoagendapessoahorario.codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, compromissoAgendaPessoaHorarioVO.getCodigo());
		if (tabelaResultado.next()) {
			compromissoAgendaPessoaHorarioVO.getPreInscricao().setCodigo(tabelaResultado.getInt("PreInscricao.codigo"));
			compromissoAgendaPessoaHorarioVO.getPreInscricao().getCurso().setCodigo(tabelaResultado.getInt("Curso.codigo"));
			compromissoAgendaPessoaHorarioVO.getPreInscricao().getCurso().setNome(tabelaResultado.getString("Curso.nome"));
			compromissoAgendaPessoaHorarioVO.getCursoInteresseProspect().setCodigo(tabelaResultado.getInt("Curso.codigo"));
			compromissoAgendaPessoaHorarioVO.getCursoInteresseProspect().setNome(tabelaResultado.getString("Curso.nome"));
		}
	}
}