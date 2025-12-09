package negocio.facade.jdbc.academico;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;

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

import negocio.comuns.academico.DisciplinaMatriculaCRMVO;
import negocio.comuns.academico.DocumentacaoMatriculaCRMVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO;
import negocio.comuns.academico.MatriculaCRMVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.TurmaContratoVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.MatriculaCRMInterfaceFacade;
import relatorio.arquitetura.VisualizadorRelatorio;
import relatorio.negocio.jdbc.financeiro.BoletoBancarioRel;
import webservice.servicos.IntegracaoMatriculaCRMVO;
import webservice.servicos.IntegracaoPessoaVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MatriculaCRMVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>MatriculaCRMVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see MatriculaCRMVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class MatriculaCRM extends ControleAcesso implements MatriculaCRMInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3344455448724033411L;
	protected static String idEntidade;

    public MatriculaCRM() throws Exception {
        super();
        setIdEntidade("MatriculaCRM");
    }
    
    public Boolean verificaSeTemRegistro(Boolean matriculaCRMFinalizada, UsuarioVO usuarioVO) throws Exception {
        String sql = null;
        SqlRowSet resultado = null;
        try {
            sql = "SELECT codigo FROM MatriculaCRM WHERE matriculaFinalizada = ? and erro = false";
            resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{matriculaCRMFinalizada});
            return resultado.next();
        } catch (Exception e) {
            throw e;
        } finally {
            sql = null;
            resultado = null;
        }
    }
    
    public Boolean validarSeMatriculaFinalizada(MatriculaCRMVO matriculaCRM) throws Exception {
        String sql = null;
        SqlRowSet resultado = null;
        try {
            sql = "SELECT * FROM MatriculaCRM WHERE codigo = ? and (matricula is null or ";
            resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{matriculaCRM.getCodigo()});
            return resultado.next();
        } catch (Exception e) {
            throw e;
        } finally {
            sql = null;
            resultado = null;
        }
    }

    public Boolean verificaSeDeveProcessarJob() throws Exception {
        String sql = null;
        SqlRowSet resultado = null;
        try {
            sql = "SELECT executarJob FROM ConfiguracaoMatriculaCRM limit 1";
            resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
            return resultado.next();
        } catch (Exception e) {
            throw e;
        } finally {
            sql = null;
            resultado = null;
        }
    }

    public List<MatriculaCRMVO> consultarMatriculaCRMSituacao(Boolean matriculaCRMFinalizada, UsuarioVO usuarioVO) throws Exception {
//        List objetos = new ArrayList(0);
        ////System.out.println("===== FACADE : ENTROU METODO ======");
        String sql = "SELECT MatriculaCRM.codigo,MatriculaCRM.usuario,MatriculaCRM.data,MatriculaCRM.dataMatricula,MatriculaCRM.dataProcessamento,MatriculaCRM.unidadeensino,"
                + "MatriculaCRM.aluno,MatriculaCRM.localarmazenamentodocumentosmatricula,MatriculaCRM.turma,MatriculaCRM.processomatricula,MatriculaCRM.planofinanceirocurso,"
                + "MatriculaCRM.condicaopagamentoplanofinanceirocurso,MatriculaCRM.descontoProgressivoPrimeiraParcela,MatriculaCRM.diretorioboletomatricula,"
                + "MatriculaCRM.htmlcontratomatricula,MatriculaCRM.matriculafinalizada,MatriculaCRM.planoDesconto,MatriculaCRM.formacaoAcademica,MatriculaCRM.matricula,"
                + "MatriculaCRM.tipoMatricula,MatriculaCRM.bolsa,MatriculaCRM.processarApenasImpressaoContrato, MatriculaCRM.diretorioContratoPdf,"
                + "funcionario.codigo as consultor "
                + " FROM MatriculaCRM "
                + " left join pessoa on pessoa.codigo = matriculaCRM.consultor "
                + " left join funcionario on funcionario.pessoa = pessoa.codigo "
                + " WHERE matriculaFinalizada = ? and erro = false limit 1"  ;
        ////System.out.println("===== FACADE : VAI REALIZAR CONEXAO======");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{matriculaCRMFinalizada});
        ////System.out.println("===== FACADE : REALIZOU CONSULTA ======");
        return montarDadosConsulta(resultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho
     * para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>MatriculaCRMVO</code> resultantes da consulta.
     */
    public static List<MatriculaCRMVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        List<MatriculaCRMVO> vetResultado = new ArrayList<MatriculaCRMVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>MatriculaCRMVO</code>.
     *
     * @return O objeto da classe <code>MatriculaCRMVO</code> com os dados devidamente montados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public static MatriculaCRMVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
       // //System.out.println("MONTOU DADOS");
        MatriculaCRMVO obj = new MatriculaCRMVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
        obj.setData(dadosSQL.getDate("data"));
        obj.setDataMatricula(dadosSQL.getDate("dataMatricula"));
        obj.setData(dadosSQL.getTimestamp("dataProcessamento"));
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino"));
        obj.getAluno().setCodigo(dadosSQL.getInt("aluno"));
        obj.setLocalArmazenamentoDocumentosMatricula(dadosSQL.getString("localarmazenamentodocumentosmatricula"));
        obj.setDiretorioContratoPdf(dadosSQL.getString("diretorioContratoPdf"));
        obj.getTurma().setCodigo(dadosSQL.getInt("turma"));
        obj.getConsultor().setCodigo(dadosSQL.getInt("consultor"));
        obj.getProcessoMatricula().setCodigo(dadosSQL.getInt("processomatricula"));
        obj.getPlanoFinanceiroCurso().setCodigo(dadosSQL.getInt("planofinanceirocurso"));
        obj.getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(dadosSQL.getInt("condicaopagamentoplanofinanceirocurso"));
        obj.getDescontoProgressivoPrimeiraParcela().setCodigo(dadosSQL.getInt("descontoProgressivoPrimeiraParcela"));
        obj.setDiretorioBoletoMatricula(dadosSQL.getString("diretorioboletomatricula"));
        obj.setHtmlContratoMatricula(dadosSQL.getString("htmlcontratomatricula"));
        obj.setMatriculaFinalizada(dadosSQL.getBoolean("matriculafinalizada"));
        obj.getPlanoDesconto().setCodigo(dadosSQL.getInt("planoDesconto"));
        obj.getFormacaoAcademica().setCodigo(dadosSQL.getInt("formacaoAcademica"));
        obj.setMatricula(dadosSQL.getString("matricula"));
        obj.setPermitiMatricula4Modulo(dadosSQL.getBoolean("permitiMatricula4Modulo"));
        if (dadosSQL.getString("tipoMatricula").equals("")) {
            obj.setTipoMatricula("NO");
        } else {
            obj.setTipoMatricula(dadosSQL.getString("tipoMatricula"));
        }

        obj.setBolsa(dadosSQL.getBoolean("bolsa"));
        obj.setProcessarApenasImpressaoContrato(dadosSQL.getBoolean("processarApenasImpressaoContrato"));
        obj.setNovoObj(false);
        ////System.out.println("MONTOU DADOS 2");
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        ////System.out.println("MONTOU DADOS 3");
        montarDadosUsuario(obj, usuarioVO);
        montarDadosUnidadeEnsino(obj, usuarioVO);
        montarDadosPessoa(obj, usuarioVO);
        montarDadosTurma(obj, usuarioVO);
        montarDadosConsultor(obj, usuarioVO);
        montarDadosProcessoMatricula(obj, usuarioVO);
        montarDadosPlanoFinanceiroCurso(obj, usuarioVO);
        montarDadosCondicaoPagamentoPlanoFinanceiroCurso(obj, usuarioVO);
        montarDadosPlanoDesconto(obj, usuarioVO);
        montarDadosFormacaoAcademica(obj, usuarioVO);
        obj.setDocumentacaoMatriculaCRM(consultarDocumentacaoMatriculaCRM(obj.getCodigo(), false, usuarioVO));
        obj.setDisciplinaMatriculaCRM(consultarDisciplinaMatriculaCRM(obj.getCodigo(), false, usuarioVO));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }
        return obj;
    }

    public static void montarDadosFormacaoAcademica(MatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.getFormacaoAcademica().getCodigo().intValue() == 0) {
            return;
        }
        obj.setFormacaoAcademica(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorChavePrimaria(obj.getFormacaoAcademica().getCodigo(), usuarioVO));
    }

    public static void montarDadosPlanoDesconto(MatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.getPlanoDesconto().getCodigo().intValue() == 0) {
            return;
        }
        obj.setPlanoDesconto(getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(obj.getPlanoDesconto().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
    }

    public static List consultarDocumentacaoMatriculaCRM(Integer matriculaCRM, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM documentacaomatriculacrm WHERE matriculacrm = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{matriculaCRM});
        while (resultado.next()) {
            objetos.add(montarDadosDocumentacaoMatriculaCRM(resultado, usuarioVO));
        }
        return objetos;
    }

    public static List consultarDisciplinaMatriculaCRM(Integer matriculaCRM, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
    	List objetos = new ArrayList(0);
    	String sql = "SELECT * FROM DisciplinaMatriculaCRM WHERE matriculacrm = ?";
    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{matriculaCRM});
    	while (resultado.next()) {
    		objetos.add(montarDadosDisciplinaMatriculaCRM(resultado, usuarioVO));
    	}
    	return objetos;
    }

    public static DocumentacaoMatriculaCRMVO montarDadosDocumentacaoMatriculaCRM(SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
        DocumentacaoMatriculaCRMVO obj = new DocumentacaoMatriculaCRMVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setMatriculaCRM(new Integer(dadosSQL.getInt("matriculaCRM")));
        obj.setDataEntrega(dadosSQL.getDate("dataEntrega"));
        obj.setEntregue(dadosSQL.getBoolean("entregue"));
        obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
        obj.getTipoDeDocumentoVO().setCodigo(new Integer(dadosSQL.getInt("tipoDeDocumento")));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosTipoDeDocumento(obj, usuarioVO);
        return obj;
    }

    public static DisciplinaMatriculaCRMVO montarDadosDisciplinaMatriculaCRM(SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
    	DisciplinaMatriculaCRMVO obj = new DisciplinaMatriculaCRMVO();
    	obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
    	obj.setMatriculaCRM(new Integer(dadosSQL.getInt("matriculaCRM")));
    	obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina"));
    	obj.setNovoObj(Boolean.FALSE);
    	return obj;
    }

    public static void montarDadosTipoDeDocumento(DocumentacaoMatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.getTipoDeDocumentoVO().getCodigo().intValue() == 0) {
            obj.setTipoDeDocumentoVO(new TipoDocumentoVO());
            return;
        }
        obj.setTipoDeDocumentoVO(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorChavePrimaria(obj.getTipoDeDocumentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
    }

    public static void montarDadosCondicaoPagamentoPlanoFinanceiroCurso(MatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo().intValue() == 0) {
            return;
        }
        obj.setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(obj.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
    }

    public static void montarDadosPlanoFinanceiroCurso(MatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.getPlanoFinanceiroCurso().getCodigo().intValue() == 0) {
            return;
        }
        obj.setPlanoFinanceiroCurso(getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(obj.getPlanoFinanceiroCurso().getCodigo(), "", Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
    }

    public static void montarDadosProcessoMatricula(MatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.getProcessoMatricula().getCodigo().intValue() == 0) {
            return;
        }
        obj.setProcessoMatricula(getFacadeFactory().getProcessoMatriculaFacade().consultarPorChavePrimaria(obj.getProcessoMatricula().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
    }

    public static void montarDadosTurma(MatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.getTurma().getCodigo().intValue() == 0) {
            return;
        }
        obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
    }

    public static void montarDadosConsultor(MatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.getConsultor().getCodigo().intValue() == 0) {
            return;
        }
        try {
            obj.setConsultor(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(obj.getConsultor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        } catch (Exception e) {
            obj.setConsultor(new FuncionarioVO());
        }
    }

    public static void montarDadosPessoa(MatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.getAluno().getCodigo().intValue() == 0) {
            return;
        }
//        obj.setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        obj.setAluno(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(obj.getAluno().getCodigo(), false, true, false, usuarioVO));
    }

    public static void montarDadosUnidadeEnsino(MatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
    }

    public static void montarDadosUsuario(MatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception {
        if (obj.getUsuario().getCodigo().intValue() == 0) {
            return;
        }
        obj.setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuario().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>MatriculaCRMVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public MatriculaCRMVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
        String sql = "SELECT * FROM matriculaCRM WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return MatriculaCRM.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        MatriculaCRM.idEntidade = idEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void impressaoContratoMatricula(MatriculaCRMVO matriculaCRM, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void novaMatricula(MatriculaCRMVO matriculaCRM, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
            MatriculaVO matricula = new MatriculaVO();
            MatriculaPeriodoVO matriculaPeriodo = new MatriculaPeriodoVO();
            ConfiguracaoGeralSistemaVO configuracaoGeralSistema = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(matriculaCRM.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
            preencherAbaDadosBasicoMatricula(matricula, matriculaPeriodo, matriculaCRM, usuarioVO, configuracaoFinanceiroVO, configuracaoGeralSistema);
            preencherAbaDocumentacaoMatricula(matricula, matriculaPeriodo, matriculaCRM, usuarioVO);
            preencherAbaFinanceiroMatricula(matricula, matriculaPeriodo, matriculaCRM, usuarioVO, configuracaoFinanceiroVO);
            preencherDadosContratoMatricula(matricula, matriculaPeriodo, matriculaCRM, usuarioVO, configuracaoFinanceiroVO, configuracaoGeralSistema);
            if (!matriculaCRM.getProcessarApenasImpressaoContrato()) {
                gravarDadosMatriculaCRM(matriculaCRM, matricula, matriculaPeriodo, configuracaoFinanceiroVO, configuracaoGeralSistema, false ,usuarioVO);
                gerarBoletoMatriculaCRM(matriculaCRM, matriculaPeriodo, configuracaoFinanceiroVO, usuarioVO);
            }
            realizarImpressaoDadosContratoMatricula(matricula, matriculaPeriodo, matriculaCRM, usuarioVO, configuracaoFinanceiroVO, configuracaoGeralSistema);
            matriculaCRM.setMatriculaFinalizada(Boolean.TRUE);
            matriculaCRM.setErro(Boolean.FALSE);
            matriculaCRM.setMensagemErro("");
            matriculaCRM.setMatricula(matricula.getMatricula());
            alterar(matriculaCRM);
            matricula = null;
            matriculaCRM = null;
            matriculaPeriodo = null;
            configuracaoFinanceiroVO = null;
            usuarioVO = null;
        } catch (Exception e) {
            throw e;
        } 
    }
    
    private void preencherAbaDadosBasicoMatricula(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, MatriculaCRMVO matriculaCRM,  UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
    	matricula.setAluno(matriculaCRM.getAluno());
        matricula.setUnidadeEnsino(matriculaCRM.getUnidadeEnsino());
        matricula.setCurso(matriculaCRM.getTurma().getCurso());
        matricula.setTurno(matriculaCRM.getTurma().getTurno());
        matricula.setConsultor(matriculaCRM.getConsultor());
        matricula.setTipoMatricula(matriculaCRM.getTipoMatricula());
        matricula.setUsuario(usuarioVO);
        matriculaPeriodo.setResponsavelRenovacaoMatricula(usuarioVO);
        matriculaPeriodo.setProcessoMatricula(matriculaCRM.getProcessoMatricula().getCodigo());
        matriculaPeriodo.setProcessoMatriculaVO(matriculaCRM.getProcessoMatricula());
        matriculaPeriodo.setPlanoFinanceiroCurso(matriculaCRM.getPlanoFinanceiroCurso());
        matriculaPeriodo.setCondicaoPagamentoPlanoFinanceiroCurso(matriculaCRM.getCondicaoPagamentoPlanoFinanceiroCurso());
        matriculaPeriodo.setCategoriaCondicaoPagamento(matriculaCRM.getCondicaoPagamentoPlanoFinanceiroCurso().getCategoria());
        UnidadeEnsinoCursoVO uni = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoUnidadeEnsino(matriculaCRM.getTurma().getCurso().getCodigo(), matriculaCRM.getTurma().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        matriculaPeriodo.setUnidadeEnsinoCurso(uni.getCodigo());
        matriculaPeriodo.setUnidadeEnsinoCursoVO(uni);
        matriculaPeriodo.setData(new Date());            
        getFacadeFactory().getCursoFacade().carregarDados(matriculaCRM.getTurma().getCurso(), NivelMontarDados.BASICO, usuarioVO);
        matricula.getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(matriculaCRM.getTurma().getCurso().getConfiguracaoAcademico().getCodigo(), usuarioVO));

        if (!matriculaCRM.getMatricula().equalsIgnoreCase("")) {
            matricula.setMatricula(matriculaCRM.getMatricula());
            getFacadeFactory().getMatriculaFacade().carregarDados(matricula, NivelMontarDados.TODOS, usuarioVO);
            matricula.setConsultor(matriculaCRM.getConsultor());
            matricula.setTipoMatricula(matriculaCRM.getTipoMatricula());
            matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(matriculaCRM.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
            getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(matriculaPeriodo, NivelMontarDados.TODOS, configuracaoFinanceiroVO, usuarioVO);
            matricula.setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(matriculaPeriodo.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        }
        matriculaPeriodo.setData(matriculaCRM.getDataMatricula());
        if(Uteis.isAtributoPreenchido(matriculaCRM.getDataBaseGeracaoParcela())) {
        	matriculaPeriodo.setDataBaseGeracaoParcelas(matriculaCRM.getDataBaseGeracaoParcela());	
        }
        if(Uteis.isAtributoPreenchido(matriculaCRM.getDataVencimento())) {
        	matriculaPeriodo.setDataVencimentoMatriculaEspecifico(matriculaCRM.getDataVencimento());
        }
        matriculaPeriodo.setTurma(matriculaCRM.getTurma());
        getFacadeFactory().getTurmaFacade().carregarDados(matriculaPeriodo.getTurma(), NivelMontarDados.TODOS, usuarioVO);
        matriculaPeriodo.getTurma().setNivelMontarDados(NivelMontarDados.TODOS);
        preencherGradeCurricularMatriculaPeriodo(matricula, matriculaPeriodo, usuarioVO);
        getFacadeFactory().getMatriculaPeriodoFacade().definirPeriodoLetivoNovaMatriculaAluno(matricula, matriculaPeriodo, usuarioVO);
        matricula.setGradeCurricularAtual(matriculaPeriodo.getGradeCurricular());
        preencherDadosTipoMatricula(matricula, matriculaPeriodo, matriculaCRM, configuracaoGeralSistema);            
        // funcao MatriculaPeriodo.montarDadosMatriculaCalendarioVO
        getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(matricula, matriculaPeriodo, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        // funcao MatriculaPeriodo.definirSituacaoMatriculaPeriodoComBaseProcesso
        getFacadeFactory().getMatriculaPeriodoFacade().definirSituacaoMatriculaPeriodoComBaseProcesso(matricula, matriculaPeriodo, configuracaoFinanceiroVO, usuarioVO);                
        validarDadosPendenciaFinanceiraMatricula(matricula, matriculaPeriodo, matriculaCRM, usuarioVO, configuracaoFinanceiroVO);
    }

	private void validarDadosPendenciaFinanceiraMatricula(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, MatriculaCRMVO matriculaCRM, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		List<ContaReceberVO> listaContaReceber = getFacadeFactory().getContaReceberFacade().consultaRapidaPorAlunoEMatriculaContasReceberVencidas(matricula.getAluno().getCodigo(), configuracaoFinanceiroVO.getUtilizarIntegracaoFinanceira(), matriculaPeriodo, usuarioVO);
        if ( Uteis.isAtributoPreenchido(listaContaReceber) && Uteis.isAtributoPreenchido(matriculaCRM.getPermitiMatriculaInadipliente()) && !matriculaCRM.getPermitiMatriculaInadipliente()) {
        	matricula.setBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira(true);
        }else if ( Uteis.isAtributoPreenchido(listaContaReceber) && matriculaCRM.getPermitiMatriculaInadipliente() == null ) {
			Uteis.checkState(Uteis.isAtributoPreenchido(listaContaReceber), "Aluno com pendência financeira, não é possível realizar a matrícula. Os títulos devem ser renegociados ou quitados para que a matrícula possa ser efetivada.");
		}
	}

	private void preencherDadosTipoMatricula(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, MatriculaCRMVO matriculaCRM, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		if (matricula.getTipoMatricula().equals("EX")) {
			validarDadosParaBloqueioMatriculaExtensao(matricula, matriculaPeriodo, matriculaCRM, configuracaoGeralSistema);
		} else if (matricula.getCurso().getNivelEducacionalPosGraduacao() && !matricula.getTipoMatricula().equals("EX") && configuracaoGeralSistema.getControlaQtdDisciplinaRealizadaAteMatricula()) {
			validaDadosParaBloqueioOuPendenciaNaMatricula4Modulo(matricula, matriculaPeriodo, matriculaCRM, configuracaoGeralSistema);
		}
	}

	private void validarDadosParaBloqueioMatriculaExtensao(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, MatriculaCRMVO matriculaCRM, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) {
		if (configuracaoGeralSistema.getControlaQtdDisciplinaExtensao() && !Uteis.isAtributoPreenchido(matricula.getQtdDisciplinasExtensao())) {
			matricula.setQtdDisciplinasExtensao(configuracaoGeralSistema.getQtdDisciplinaExtensao());
		}
		Uteis.checkState(matricula.getTipoMatricula().equals("EX") && (matriculaCRM.getDisciplinaMatriculaCRM().isEmpty() || matriculaCRM.getDisciplinaMatriculaCRM().size() > matricula.getQtdDisciplinasExtensao()), "Não é possível realizar a matrícula do aluno (tipo EXTENSÃO), pois é permitido até "+ matricula.getQtdDisciplinasExtensao() +" disciplina(s) por matrícula de extensão.");
		List<TurmaDisciplinaVO> listaDisciplinaExtensao = new ArrayList<TurmaDisciplinaVO>();
		matriculaPeriodo.getTurma().getTurmaDisciplinaVOs().stream().forEach(p -> {
			if (matriculaCRM.getDisciplinaMatriculaCRM().stream().anyMatch(d -> d.getDisciplinaVO().getCodigo().equals(p.getDisciplina().getCodigo()))) {
				listaDisciplinaExtensao.add(p);
			}
		});
		matriculaPeriodo.getTurma().setTurmaDisciplinaVOs(listaDisciplinaExtensao);
	}

	private void validaDadosParaBloqueioOuPendenciaNaMatricula4Modulo(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, MatriculaCRMVO matriculaCRM, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		Integer qtdAulas = getFacadeFactory().getHorarioTurmaDiaFacade().consultarQtdAulaRealizadaAteDataAtual(matriculaPeriodo.getTurma().getCodigo());
		Uteis.checkState(qtdAulas > configuracaoGeralSistema.getQtdDisciplinaRealizadaAteMatricula() && !matriculaCRM.getPermitiMatricula4Modulo(), "A matrícula não pode ser realizada! Já ocorreram mais aulas que a quantidade máxima permitida para realização da mesma! (Qtd aulas realizada = " + qtdAulas + "; Qtd máx. aulas permitida = " + configuracaoGeralSistema.getQtdDisciplinaRealizadaAteMatricula() + ")");
		if (qtdAulas > configuracaoGeralSistema.getQtdDisciplinaRealizadaAteMatricula() && matriculaCRM.getPermitiMatricula4Modulo()) {
			matricula.setBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica(true);
		}
	}

	
	private void preencherGradeCurricularMatriculaPeriodo(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, UsuarioVO usuarioVO) throws Exception {
		/*Foi exposto o seguinte cenario pelo IPOG ao criar uma turma com matriz curricular ainda inativa no curso porem era para ser matriculado nessa matriz só que devido a regra do sistema nao permitir foi entao comentado o codigo abaixo que nao permitia ser incluido uma matricula para uma matriz curricular inativa*/
		List<GradeCurricularVO> listaGrades = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularAtivaPorCodigoCurso(matricula.getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        if (!listaGrades.isEmpty()) {
        	Optional<GradeCurricularVO> findFirst = listaGrades.stream().filter(p-> p.getCodigo().equals(matriculaPeriodo.getTurma().getGradeCurricularVO().getCodigo())).findFirst();
        	if(Uteis.isAtributoPreenchido(findFirst)) {
        		matriculaPeriodo.setGradeCurricular(findFirst.get());
        	}else {
        		throw new StreamSeiException("A matriz curricular informada na Turma não se encontra Ativa no cadastro do curso. Por favor verificar o cadastro.");
        	}
        }
        
	}
    
    private void preencherAbaDocumentacaoMatricula(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, MatriculaCRMVO matriculaCRM,  UsuarioVO usuarioVO) throws Exception {
        getFacadeFactory().getMatriculaPeriodoFacade().validarMatriculaPeriodoPodeSerRealizada(matricula, matriculaPeriodo, usuarioVO);
        getFacadeFactory().getMatriculaFacade().gerenciarEntregaDocumentoMatricula(matricula, usuarioVO);
        Iterator<DocumentacaoMatriculaCRMVO> j = matriculaCRM.getDocumentacaoMatriculaCRM().iterator();
        while (j.hasNext()) {
            DocumentacaoMatriculaCRMVO docCRM = (DocumentacaoMatriculaCRMVO) j.next();
            Iterator<DocumetacaoMatriculaVO> i = matricula.getDocumetacaoMatriculaVOs().iterator();
            while (i.hasNext()) {
                DocumetacaoMatriculaVO d = (DocumetacaoMatriculaVO) i.next();
                if (d.getTipoDeDocumentoVO().getCodigo().equals(docCRM.getTipoDeDocumentoVO().getCodigo())) {
                    if (docCRM.getEntregue()) {
                        d.setEntregue(Boolean.TRUE);
                        d.setDataEntrega(docCRM.getDataEntrega());
                        d.setUsuario(docCRM.getUsuario());
                        d.setSituacao("OK");
                    }
                }
            }
        }
    }
    
    private void preencherAbaFinanceiroMatricula(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, MatriculaCRMVO matriculaCRM,  UsuarioVO usuarioVO,  ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
    	getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosDefinirDisciplinasMatriculaPeriodo(matricula, matriculaPeriodo, usuarioVO, null, false, false);
    	matricula.getPlanoFinanceiroAluno().setItemPlanoFinanceiroAlunoVOs(new ArrayList<>(0));
        validarDadosPlanoDesconto(matricula, matriculaCRM, usuarioVO);
        Ordenacao.ordenarLista(matricula.getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs(), "ordenacao");
        getFacadeFactory().getMatriculaPeriodoFacade().inicializarPlanoFinanceiroAlunoMatriculaPeriodo(matricula, matriculaPeriodo, matricula.getPlanoFinanceiroAluno(), true, usuarioVO);
  		getFacadeFactory().getMatriculaPeriodoFacade().validarDisponibilidadeVagasMatriculaPeriodoTurmaDisciplina(matricula, matriculaPeriodo, false, usuarioVO);
  		getFacadeFactory().getMatriculaPeriodoFacade().verificarPreRequisitoDisciplina(matriculaPeriodo, matricula, false, usuarioVO);
        getFacadeFactory().getMatriculaPeriodoFacade().realizarCalculoValorMatriculaEMensalidade(matricula, matriculaPeriodo, usuarioVO);
        if (matriculaCRM.getBolsa()) {
            matricula.getPlanoFinanceiroAluno().setItemPlanoFinanceiroAlunoVOs(new ArrayList<>(0));
            matricula.getPlanoFinanceiroAluno().setTipoDescontoParcela("PO");
            matricula.getPlanoFinanceiroAluno().setPercDescontoParcela(new Double(100));
            matricula.getPlanoFinanceiroAluno().setItemPlanoFinanceiroAlunoVOs(new ArrayList<>(0));
            matriculaPeriodo.setBolsista(Boolean.TRUE);
        } else {
            matricula.getPlanoFinanceiroAluno().setPercDescontoParcela(0.0);
        }
        getFacadeFactory().getMatriculaFacade().calcularTotalDesconto(matricula, matriculaPeriodo, matricula.getPlanoFinanceiroAluno().obterOrdemAplicacaoDescontosPadraoAtual(), usuarioVO, configuracaoFinanceiroVO); 
         //adicionar matriculaperiodo
        if (!matricula.getMatricula().equals("")) {
            matriculaPeriodo.setMatricula(matricula.getMatricula());
        }
        matricula.adicionarObjMatriculaPeriodoVOs(matriculaPeriodo);
        matricula.setFormacaoAcademica(matriculaCRM.getFormacaoAcademica());
        matricula.getPlanoFinanceiroAluno().getDescontoProgressivoPrimeiraParcela().setCodigo(matriculaCRM.getDescontoProgressivoPrimeiraParcela().getCodigo());

    }

	private void validarDadosPlanoDesconto(MatriculaVO matricula, MatriculaCRMVO matriculaCRM, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(matriculaCRM.getPlanoDesconto().getCodigo())) {
			matriculaCRM.getListaPlanoDescontoVO().add(matriculaCRM.getPlanoDesconto());
        }
		for (PlanoDescontoVO p : matriculaCRM.getListaPlanoDescontoVO()) {
			ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAluno = new ItemPlanoFinanceiroAlunoVO();
            PlanoDescontoVO planoDesconto = getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(p.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
            itemPlanoFinanceiroAluno.setPlanoDesconto(planoDesconto);
            itemPlanoFinanceiroAluno.setTipoItemPlanoFinanceiro("PD");                    
            getFacadeFactory().getPlanoFinanceiroAlunoFacade().adicionarObjItemPlanoFinanceiroAlunoVOs(matricula.getPlanoFinanceiroAluno(), itemPlanoFinanceiroAluno);
		}
	}
    
	private void preencherDadosContratoMatricula(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, MatriculaCRMVO matriculaCRM,  UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		Boolean isObrigatorioContratoPorTurma = null;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MATRICULA_INFORMA_CONTRATO_MATRICULA_VINCULADO_TURMA, matriculaCRM.getUsuario());
			isObrigatorioContratoPorTurma = true;
		} catch (Exception e) {
			isObrigatorioContratoPorTurma = false;
		}
		List<TurmaContratoVO> turmaContratoVOs = new ArrayList<>();
		if (TipoContratoMatriculaEnum.getEnumPorValor(matricula.getTipoMatricula()).isContratoExtensao()) {
			if (Uteis.isAtributoPreenchido(matriculaCRM.getCodigoContrato())) {
				matriculaPeriodo.getContratoExtensao().setCodigo(matriculaCRM.getCodigoContrato());	
			}else if(!Uteis.isAtributoPreenchido(matriculaPeriodo.getContratoExtensao())) {
				turmaContratoVOs = getFacadeFactory().getTurmaContratoFacade().consultarTurmaTipoContratoMatricula(matriculaPeriodo.getTurma().getCodigo(), TipoContratoMatriculaEnum.getEnumPorValor(matricula.getTipoMatricula()), false, matriculaCRM.getUsuario());
				matriculaPeriodo.setContratoExtensao(turmaContratoVOs.isEmpty() ? new TextoPadraoVO() : turmaContratoVOs.get(0).getTextoPadraoVO());
				Uteis.checkState(isObrigatorioContratoPorTurma && !Uteis.isAtributoPreenchido(matriculaPeriodo.getContratoExtensao()), "O campo Modelo de Contrato deve ser informado! Deve ser definido na turma o CONTRATO DE EXTENSÃO.");
			}
			MatriculaPeriodo.montarDadosContratoExtensao(matriculaPeriodo, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		} else if (TipoContratoMatriculaEnum.getEnumPorValor(matricula.getTipoMatricula()).isContratoNormal()) {
			if (Uteis.isAtributoPreenchido(matriculaCRM.getCodigoContrato())) {
				matriculaPeriodo.getContratoMatricula().setCodigo(matriculaCRM.getCodigoContrato());	
			}else if(!Uteis.isAtributoPreenchido(matriculaPeriodo.getContratoMatricula())) {
				turmaContratoVOs = getFacadeFactory().getTurmaContratoFacade().consultarTurmaTipoContratoMatricula(matriculaPeriodo.getTurma().getCodigo(), TipoContratoMatriculaEnum.getEnumPorValor(matricula.getTipoMatricula()), false, matriculaCRM.getUsuario());
				matriculaPeriodo.setContratoMatricula(turmaContratoVOs.isEmpty() ? new TextoPadraoVO() : turmaContratoVOs.get(0).getTextoPadraoVO());
				Uteis.checkState(isObrigatorioContratoPorTurma && !Uteis.isAtributoPreenchido(matriculaPeriodo.getContratoMatricula()), "O campo Modelo de Contrato deve ser informado! Deve ser definido na turma o CONTRATO DE MATRÍCULA.");
			}
			MatriculaPeriodo.montarDadosContratoMatricula(matriculaPeriodo, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		} else if (TipoContratoMatriculaEnum.getEnumPorValor(matricula.getTipoMatricula()).isContratoFiador()) {
			if (Uteis.isAtributoPreenchido(matriculaCRM.getCodigoContrato())) {
				matriculaPeriodo.getContratoFiador().setCodigo(matriculaCRM.getCodigoContrato());	
			}else if(!Uteis.isAtributoPreenchido(matriculaPeriodo.getContratoFiador())) {
				turmaContratoVOs = getFacadeFactory().getTurmaContratoFacade().consultarTurmaTipoContratoMatricula(matriculaPeriodo.getTurma().getCodigo(), TipoContratoMatriculaEnum.getEnumPorValor(matricula.getTipoMatricula()), false, matriculaCRM.getUsuario());
				matriculaPeriodo.setContratoFiador(turmaContratoVOs.isEmpty() ? new TextoPadraoVO() : turmaContratoVOs.get(0).getTextoPadraoVO());
				Uteis.checkState(isObrigatorioContratoPorTurma && !Uteis.isAtributoPreenchido(matriculaPeriodo.getContratoFiador()), "O campo Modelo de Contrato deve ser informado! Deve ser definido na turma o CONTRATO DE FIADOR.");
			}
			MatriculaPeriodo.montarDadosContratoFiador(matriculaPeriodo, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		}
	}

	private void realizarImpressaoDadosContratoMatricula(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, MatriculaCRMVO matriculaCRM, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		matricula.getAluno().setFormacaoAcademicaVOs(new ArrayList<FormacaoAcademicaVO>(0));
		matricula.getAluno().getFormacaoAcademicaVOs().add(matriculaCRM.getFormacaoAcademica());
		String ano = "";
		String semestre = "";
		if (!matricula.getCurso().getNivelEducacionalPosGraduacao()) {
			ano = matriculaPeriodo.getAno();
			semestre = matriculaPeriodo.getSemestre();
		}
		matriculaPeriodo.setDataInicioAula(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaAgrupada(matriculaPeriodo.getTurma().getCodigo(), ano, semestre));
		matriculaPeriodo.setDataFinalAula(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAgrupada(matriculaPeriodo.getTurma().getCodigo(), ano, semestre));

		matriculaCRM.setHtmlContratoMatricula("");
		String diretorioContratoPdf = getFacadeFactory().getImpressaoContratoFacade().imprimirContratoRenovarMatricula("MA", matricula, matriculaPeriodo, configuracaoFinanceiroVO, configuracaoGeralSistema, usuarioVO);
		String diretorio = UteisJSF.getCaminhoWeb().replace("/", File.separator) + "relatorio" + File.separator + diretorioContratoPdf;
		matriculaCRM.setDiretorioContratoPdf(diretorio);
		if (Uteis.isSistemaOperacionalWindows() && diretorio.startsWith("/")) {
			matriculaCRM.setDiretorioContratoPdf(diretorio.substring(1));
		}

	}
    

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gravarDadosMatriculaCRM(MatriculaCRMVO matriculaCRM, MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean validarAlunoMatriculadoWebServiceMatriculaCrm ,UsuarioVO usuarioVO) throws Exception {    	
    	matricula.setBloqueioPorSolicitacaoLiberacaoMatricula(matricula.getBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira() || matricula.getBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica());
    	if (!matriculaCRM.getAluno().getFormacaoAcademicaVOs().isEmpty()) {
        	matricula.setFormacaoAcademica(matriculaCRM.getAluno().getFormacaoAcademicaVOs().get(0));
        }
    	if (!matricula.getMatriculaJaRegistrada()) {
            //getFacadeFactory().getMatriculaFacade().incluir(matricula, matriculaPeriodo, matriculaPeriodo.getProcessoMatriculaCalendarioVO(), matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso(), configuracaoFinanceiroVO, configuracaoGeralSistema, false, usuarioVO);
            getFacadeFactory().getMatriculaFacade().incluir(matricula, matriculaPeriodo,  matriculaPeriodo.getProcessoMatriculaCalendarioVO(),  matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso(), configuracaoFinanceiroVO, configuracaoGeralSistema, false, false, validarAlunoMatriculadoWebServiceMatriculaCrm,true,usuarioVO);
                                                    
        } else {
            matricula.setUsuario(usuarioVO);
            matricula.getPlanoFinanceiroAluno().setResponsavel(usuarioVO);
            getFacadeFactory().getMatriculaFacade().alterar(matricula, matriculaPeriodo, matriculaPeriodo.getProcessoMatriculaCalendarioVO(), matriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso(), configuracaoFinanceiroVO, configuracaoGeralSistema, usuarioVO, false);
        }
    }

	private void gerarBoletoMatriculaCRM(MatriculaCRMVO matriculaCRM, MatriculaPeriodoVO matriculaPeriodo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception, ServletException, IOException {
		//BY THYAGO// GERAR PDF BOLETO MATRICULA.
        if (matriculaPeriodo.getMatriculaPeriodoVencimentoReferenteMatricula().getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA)) {
            //System.out.println("++++++++++++++++++++++ GERAR BOLETO +++++++++++++++++++++++++++");
            getFacadeFactory().getMatriculaPeriodoFacade().emitirBoletoMatricula(matriculaPeriodo, usuarioVO, configuracaoFinanceiroVO);
            VisualizadorRelatorio ser = new VisualizadorRelatorio();
            String design = BoletoBancarioRel.getDesignIReportRelatorio();
            ser.setTipoRelatorio("PDF");
            ser.setNomeRelatorio(BoletoBancarioRel.getIdEntidade());
            ser.setNomeEmpresa("");
            ser.setMensagemRel("");
            ser.setTipoRelatorio("Recibo do Sacado");
            ser.setCaminhoParserXML("");
            ser.setNomeDesignIReport(design);
            ser.setListaObjetos(getFacadeFactory().getBoletoBancarioRelFacade().emitirRelatorioLista(false, matriculaPeriodo.getMatriculaPeriodoVencimentoReferenteMatricula().getContaReceber().getCodigo(), null, null, null, null, null, null, null, null, null, "", 0, usuarioVO, configuracaoFinanceiroVO, 0, false));
            ser.setCaminhoBaseAplicacao(BoletoBancarioRel.getCaminhoBaseRelatorio());
            ser.setCaminhoBase(UteisJSF.getCaminhoBase().replace("/", File.separator));
            ser.setCaminhoBaseWeb(UteisJSF.getCaminhoWeb().replace("/", File.separator));
            ser.setPersistirImpressaoBoleto("true");
            ser.visualizarRelatorioPDF(ser.gerarRelatorioJasperPrintObjeto(null, null, design), "MatriculaCRM - gravarDadosMatriculaCRM");
            matriculaCRM.setDiretorioBoletoMatricula(UteisJSF.getCaminhoWeb().replace("/", File.separator) + "relatorio" + File.separator + ser.getNomePDF());
            if(Uteis.isSistemaOperacionalWindows() && matriculaCRM.getDiretorioBoletoMatricula().startsWith("/")) {
            	matriculaCRM.setDiretorioBoletoMatricula(matriculaCRM.getDiretorioBoletoMatricula().substring(1));
            }
        }
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MatriculaCRMVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "INSERT INTO MatriculaCRM( usuario, data, unidadeEnsino, aluno, localArmazenamentoDocumentosMatricula,"
					+ " turma, consultor, processoMatricula, planoFinanceiroCurso, condicaoPagamentoPlanoFinanceiroCurso, descontoProgressivoPrimeiraParcela, "
					+ " planoDesconto, formacaoAcademica, tipoMatricula, matriculafinalizada, erro, dataMatricula, permitiMatricula4Modulo, "
					+ " dataVencimento, dataBaseGeracaoParcela, permitiMatriculaInadipliente "
					+ ") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);

					sqlInserir.setInt(1, obj.getUsuario().getCodigo());
					sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setInt(3, obj.getUnidadeEnsino().getCodigo());
	                sqlInserir.setInt(4, obj.getAluno().getCodigo());
	                sqlInserir.setString(5, obj.getLocalArmazenamentoDocumentosMatricula());
	                sqlInserir.setInt(6, obj.getTurma().getCodigo());
	                sqlInserir.setInt(7, obj.getConsultor().getCodigo());
	                sqlInserir.setInt(8, obj.getProcessoMatricula().getCodigo());
	                sqlInserir.setInt(9, obj.getPlanoFinanceiroCurso().getCodigo());
	                sqlInserir.setInt(10, obj.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo());
	                if (obj.getDescontoProgressivoPrimeiraParcela().getCodigo().intValue() != 0) {
	                	sqlInserir.setInt(11, obj.getDescontoProgressivoPrimeiraParcela().getCodigo());
					} else {
						sqlInserir.setNull(11, 0);
					}	                
	                if (obj.getPlanoDesconto().getCodigo() != 0) {
	                	sqlInserir.setInt(12, obj.getPlanoDesconto().getCodigo());
					} else {
						sqlInserir.setNull(12, 0);
					}		                
	                if (obj.getFormacaoAcademica().getCodigo().intValue() != 0) {
	                	sqlInserir.setInt(13, obj.getFormacaoAcademica().getCodigo());
					} else {
						sqlInserir.setNull(13, 0);
					}		                
	                sqlInserir.setString(14, obj.getTipoMatricula());					
	                sqlInserir.setBoolean(15, false);					
	                sqlInserir.setBoolean(16, false);		
	                sqlInserir.setTimestamp(17, Uteis.getDataJDBCTimestamp(obj.getDataMatricula()));
	                sqlInserir.setBoolean(18, obj.getPermitiMatricula4Modulo());
	                int i = 18;
	                Uteis.setValuePreparedStatement(obj.getDataVencimento(), ++i, sqlInserir);
	                Uteis.setValuePreparedStatement(obj.getDataBaseGeracaoParcela(), ++i, sqlInserir);
	                Uteis.setValuePreparedStatement(obj.getPermitiMatriculaInadipliente(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			incluirDisciplinaMatriculas(obj, obj.getDisciplinaMatriculaCRM());
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	public void incluirDisciplinaMatriculas(MatriculaCRMVO matriculaCrmVO, List<DisciplinaMatriculaCRMVO> objetos) throws Exception {
        Iterator<DisciplinaMatriculaCRMVO> e = objetos.iterator();
        while (e.hasNext()) {
            DisciplinaMatriculaCRMVO obj = (DisciplinaMatriculaCRMVO) e.next();
            obj.setMatriculaCRM(matriculaCrmVO.getCodigo());
            incluirDisciplinaMatricula(obj);
        }
    }

	
	public void incluirDisciplinaMatricula(final DisciplinaMatriculaCRMVO obj) throws Exception {
        try {
            final String sql = "INSERT INTO DisciplinaMatriculaCRM( disciplina, matriculaCRM ) VALUES ( ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (!obj.getDisciplinaVO().getCodigo().equals(0)) {
                        sqlInserir.setInt(1, obj.getDisciplinaVO().getCodigo());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (!obj.getMatriculaCRM().equals(0)) {
                    	sqlInserir.setInt(2, obj.getMatriculaCRM());
                    } else {
                    	sqlInserir.setNull(2, 0);
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            throw e;
        }
    }
	
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MatriculaCRMVO obj) throws Exception {
        //validarSeMatriculaFinalizada(obj);
        final String sql = "UPDATE MatriculaCRM set usuario=?, dataProcessamento=?, unidadeEnsino=?, aluno=?, localArmazenamentoDocumentosMatricula=? , turma=? , processoMatricula=? , planoFinanceiroCurso=? , condicaoPagamentoPlanoFinanceiroCurso=?, "
                + " diretorioBoletoMatricula=?, htmlContratoMatricula=?, matriculaFinalizada=?, erro=?, mensagemErro=?, matricula=?, bolsa=?, diretoriocontratopdf=?, "
                + " dataVencimento=?, dataBaseGeracaoParcela=?, permitiMatriculaInadipliente=? "
                + " WHERE (codigo = ?)";
        
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getUsuario().getCodigo());
                sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
                sqlAlterar.setInt(3, obj.getUnidadeEnsino().getCodigo());
                sqlAlterar.setInt(4, obj.getAluno().getCodigo());
                sqlAlterar.setString(5, obj.getLocalArmazenamentoDocumentosMatricula());
                sqlAlterar.setInt(6, obj.getTurma().getCodigo());
                sqlAlterar.setInt(7, obj.getProcessoMatricula().getCodigo());
                sqlAlterar.setInt(8, obj.getPlanoFinanceiroCurso().getCodigo());
                sqlAlterar.setInt(9, obj.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo());
                sqlAlterar.setString(10, obj.getDiretorioBoletoMatricula());
                sqlAlterar.setString(11, obj.getHtmlContratoMatricula());
                sqlAlterar.setBoolean(12, obj.getMatriculaFinalizada());
                sqlAlterar.setBoolean(13, obj.getErro());
                sqlAlterar.setString(14, obj.getMensagemErro());
                sqlAlterar.setString(15, obj.getMatricula());
                sqlAlterar.setBoolean(16, obj.getBolsa());
                sqlAlterar.setString(17, obj.getDiretorioContratoPdf());
                int i = 17;
                Uteis.setValuePreparedStatement(obj.getDataVencimento(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getDataBaseGeracaoParcela(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getPermitiMatriculaInadipliente(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
                return sqlAlterar;
            }
        });
    }

    public MatriculaCRMVO consultarAlunoASerMatriculadoPorCodigoPessoa(Integer codigoAluno, Boolean matriculaCRMFinalizada, UsuarioVO usuarioVO) throws Exception {
       ////System.out.println("ENTROU BUSCA");
        String sql = "SELECT * FROM MatriculaCRM WHERE matriculaFinalizada = ? and aluno = ? and erro = false";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{matriculaCRMFinalizada, codigoAluno});
        if (!resultado.next()) {
           // //System.out.println("RETORNOU NULL");
            return null;
        }
        return montarDados(resultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MatriculaCRMVO preencherNovaMatriculaAlunoWS(IntegracaoMatriculaCRMVO integracaoMatricula,boolean validarAlunoMatriculadoWebServiceMatriculaCrm) throws Exception {
		MatriculaCRMVO mat = new MatriculaCRMVO();
		mat.setCodigoContrato(integracaoMatricula.getCodigoContrato());
		mat.setPermitiMatricula4Modulo(integracaoMatricula.getPermitiMatricula4Modulo());
		if(integracaoMatricula.getPermitiMatriculaInadipliente() != null) {
			mat.setPermitiMatriculaInadipliente(integracaoMatricula.getPermitiMatriculaInadipliente());	
		}
		validarDadosTurmaParaMatricula(mat, integracaoMatricula);
		mat.setUsuario(validarDadosUsuarioResponsavel(mat.getUnidadeEnsino().getCodigo(), integracaoMatricula.getUsuarioResponsavel()));
		validarDadosAlunoParaMatricula(mat, integracaoMatricula);
		validarDatasParaMatricula(mat, integracaoMatricula);
		validarDadosTipoMatricula(mat, integracaoMatricula);
		validarCondicaoPagamentoPlanoFinanceiroCursoParaMatricula(mat, integracaoMatricula);
		validarProcessoMatriculaParaMatricula(mat, integracaoMatricula);
		ConfiguracaoGeralSistemaVO configuracaoGeralSistema = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, mat.getUsuario(), mat.getUnidadeEnsino().getCodigo());
		validarDadosConsultorParaMatricula(mat, integracaoMatricula, configuracaoGeralSistema);
		incluir(mat, mat.getUsuario());
		persistirNovaMatricula(mat, configuracaoGeralSistema,validarAlunoMatriculadoWebServiceMatriculaCrm);
		return mat;
	}

	private void persistirNovaMatricula(MatriculaCRMVO mat, ConfiguracaoGeralSistemaVO configuracaoGeralSistema ,boolean validarAlunoMatriculadoWebServiceMatriculaCrm) throws Exception {
		MatriculaVO matricula = new MatriculaVO();		
		MatriculaPeriodoVO matriculaPeriodo = new MatriculaPeriodoVO();
		ConfiguracaoFinanceiroVO confFinanceira = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, mat.getUnidadeEnsino().getCodigo(), mat.getUsuario());
		preencherAbaDadosBasicoMatricula(matricula, matriculaPeriodo, mat, mat.getUsuario(), confFinanceira, configuracaoGeralSistema);
		preencherAbaDocumentacaoMatricula(matricula, matriculaPeriodo, mat, mat.getUsuario());		
		preencherAbaFinanceiroMatricula(matricula, matriculaPeriodo, mat, mat.getUsuario(), confFinanceira);
		preencherDadosContratoMatricula(matricula, matriculaPeriodo, mat, mat.getUsuario(), confFinanceira, configuracaoGeralSistema);
		gravarDadosMatriculaCRM(mat, matricula, matriculaPeriodo, confFinanceira, configuracaoGeralSistema, validarAlunoMatriculadoWebServiceMatriculaCrm ,mat.getUsuario());
		mat.setUrlBoletoMatricula(configuracaoGeralSistema.getUrlAcessoExternoAplicacao() + File.separator+"/BoletoBancarioSV?codigoContaReceber="+matriculaPeriodo.getMatriculaPeriodoVencimentoReferenteMatricula().getContaReceber().getCodigo()+"&amp;codigoUsuario="+mat.getUsuario().getCodigo()+"&amp;titulo=matricula&amp;telaOrigem=visaoAluno/minhasContasPagarAluno.xhtml");
		mat.setMatriculaFinalizada(Boolean.TRUE);
		mat.setErro(Boolean.FALSE);
		mat.setMensagemErro("");
		mat.setMatricula(matricula.getMatricula());
		mat.setMatriculaPeriodo(matriculaPeriodo.getCodigo());
		alterar(mat);
	}
    
    private void validarDadosTurmaParaMatricula(MatriculaCRMVO mat, IntegracaoMatriculaCRMVO integracaoMatriculaCRMVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(integracaoMatriculaCRMVO.getCodigoTurma()), "O campo Turma Para Nova Matricula não foi informado.");
		mat.getTurma().setCodigo(integracaoMatriculaCRMVO.getCodigoTurma());
		getFacadeFactory().getTurmaFacade().carregarDados(mat.getTurma(), mat.getUsuario());
		mat.setUnidadeEnsino(mat.getTurma().getUnidadeEnsino());
	}
	
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public UsuarioVO validarDadosUsuarioResponsavel(Integer unidadeEnsino, Integer codigoUsuario) throws Exception {
		UsuarioVO usuario = null;
		if(Uteis.isAtributoPreenchido(codigoUsuario)) {
			usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(codigoUsuario, Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
		}else {
			usuario = getFacadeFactory().getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
		}
		usuario.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPerfilParaFuncionarioAdministrador(unidadeEnsino, usuario));
		if(!Uteis.isAtributoPreenchido(usuario.getPerfilAcesso())) {
			usuario.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPerfilAcessoDiretorMultiCampus(usuario));
		}
		return usuario;
	}
	
	private void validarDadosAlunoParaMatricula(MatriculaCRMVO mat, IntegracaoMatriculaCRMVO integracaoMatriculaCRMVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(integracaoMatriculaCRMVO.getCpf()), "O campo CPF do aluno não foi informado.");
		mat.setAluno(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorCPFUnico(integracaoMatriculaCRMVO.getCpf(), false, false, false, mat.getUsuario()));
		Uteis.checkState(!Uteis.isAtributoPreenchido(mat.getAluno()), "Não foi encontrado nennhum Aluno com o CPF informado.");
	}
	
	private void validarDadosConsultorParaMatricula(MatriculaCRMVO mat, IntegracaoMatriculaCRMVO integracaoMatriculaCRMVO, ConfiguracaoGeralSistemaVO confGeral) throws Exception {
		if (!Uteis.isAtributoPreenchido(integracaoMatriculaCRMVO.getCodigoConsultor())) {
			mat.setConsultor(confGeral.getFuncionarioRespAlteracaoDados());
		}else {
			mat.setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(integracaoMatriculaCRMVO.getCodigoConsultor(), false, mat.getUsuario()));
			Uteis.checkState(!Uteis.isAtributoPreenchido(mat.getConsultor()), "O código informado para o Consultor não foi encontrado.");
		}
	}
	
	private void validarDadosTipoMatricula(MatriculaCRMVO mat, IntegracaoMatriculaCRMVO integracaoMatriculaCRMVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(integracaoMatriculaCRMVO.getTipoMatricula()), "O campo Tipo Matricula não foi informado.");
		mat.setTipoMatricula(integracaoMatriculaCRMVO.getTipoMatricula());
		Uteis.checkState(mat.getTipoMatricula().equals("EX") && !Uteis.isAtributoPreenchido(integracaoMatriculaCRMVO.getDisciplinaMatriculaVOs()), "Tipo de Matricula igua a EX. Sendo assim é obrigatório a lista de discilinas a serem cursadas.");
		if(mat.getTipoMatricula().equals("EX") && Uteis.isAtributoPreenchido(integracaoMatriculaCRMVO.getDisciplinaMatriculaVOs())) {
			integracaoMatriculaCRMVO.getDisciplinaMatriculaVOs().stream().forEach(p->{
				DisciplinaMatriculaCRMVO d = new DisciplinaMatriculaCRMVO();
				d.getDisciplinaVO().setCodigo(p.getDisciplina());
				mat.getDisciplinaMatriculaCRM().add(d);
			});
		}
	}
	
	private void validarDatasParaMatricula(MatriculaCRMVO mat, IntegracaoMatriculaCRMVO integracaoMatriculaCRMVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(integracaoMatriculaCRMVO.getDataMatricula()), "O campo Data Matricula não foi informado.");
		String dataMatricula = integracaoMatriculaCRMVO.getDataMatricula();
		if (dataMatricula.indexOf("T") != -1) {
			dataMatricula = dataMatricula.substring(0, dataMatricula.indexOf("T"));
		}
		Uteis.checkState(!Uteis.getValidarDataFormatadaCorretamente(dataMatricula, "yyyy-MM-dd"), "O formato do campo data é invalido. yyyy-MM-dd");
		mat.setDataMatricula(Uteis.getDataYYYMMDD(dataMatricula));
		if(Uteis.isAtributoPreenchido(integracaoMatriculaCRMVO.getDataBaseGeracaoParcela())) {
			String dataBaseGeracaoParcela = integracaoMatriculaCRMVO.getDataBaseGeracaoParcela();
			if (dataBaseGeracaoParcela.indexOf("T") != -1) {
				dataBaseGeracaoParcela = dataBaseGeracaoParcela.substring(0, dataBaseGeracaoParcela.indexOf("T"));
			}
			Uteis.checkState(!Uteis.getValidarDataFormatadaCorretamente(dataBaseGeracaoParcela, "yyyy-MM-dd"), "O formato do campo data é invalido. yyyy-MM-dd");
			mat.setDataBaseGeracaoParcela(Uteis.getDataYYYMMDD(dataBaseGeracaoParcela));	
		}
		if(Uteis.isAtributoPreenchido(integracaoMatriculaCRMVO.getDataVencimentoMatricula())) {
			String dataVencimento = integracaoMatriculaCRMVO.getDataVencimentoMatricula();
			if (dataVencimento.indexOf("T") != -1) {
				dataVencimento = dataVencimento.substring(0, dataVencimento.indexOf("T"));
			}
			Uteis.checkState(!Uteis.getValidarDataFormatadaCorretamente(dataVencimento, "yyyy-MM-dd"), "O formato do campo data é invalido. yyyy-MM-dd");
			mat.setDataVencimento(Uteis.getDataYYYMMDD(dataVencimento));
		}
	}
	
	private void validarCondicaoPagamentoPlanoFinanceiroCursoParaMatricula(MatriculaCRMVO mat, IntegracaoMatriculaCRMVO integracaoMatriculaCRMVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(integracaoMatriculaCRMVO.getCodigoCondicaoPagamentoPlanoFinanceiroCurso()), "O campo Condição Pagamento Plano Financeiro Curso não foi informado.");
		mat.setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(integracaoMatriculaCRMVO.getCodigoCondicaoPagamentoPlanoFinanceiroCurso(), Uteis.NIVELMONTARDADOS_COMBOBOX, mat.getUsuario()));
		mat.getPlanoFinanceiroCurso().setCodigo(mat.getCondicaoPagamentoPlanoFinanceiroCurso().getPlanoFinanceiroCurso());
		integracaoMatriculaCRMVO.getPlanoDescontoVOs().stream().forEach(p->{
			PlanoDescontoVO planoDesconto= new PlanoDescontoVO();
			planoDesconto.setCodigo(p.getPlanoDesconto());
			mat.getListaPlanoDescontoVO().add(planoDesconto);
		});
	}
	
	private void validarProcessoMatriculaParaMatricula(MatriculaCRMVO mat, IntegracaoMatriculaCRMVO integracaoMatriculaCRMVO) throws Exception {
		if(Uteis.isAtributoPreenchido(integracaoMatriculaCRMVO.getCodigoProcessoMatricula())) {
			mat.getProcessoMatricula().setCodigo(integracaoMatriculaCRMVO.getCodigoProcessoMatricula());
			getFacadeFactory().getProcessoMatriculaFacade().carregarDados(mat.getProcessoMatricula(), NivelMontarDados.BASICO, mat.getUsuario());
		}else {
			mat.getProcessoMatricula().setCodigo(getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorCodigoTurmaAtivoEntrePeriodo(mat.getTurma().getCodigo(), mat.getUsuario(), TipoAlunoCalendarioMatriculaEnum.CALOURO));
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(mat.getProcessoMatricula().getCodigo()), "Não foi possível definir o processo de matricula, para conclusão do processo. Por favor verifica se existe um processo de matricula cadastrado que atenda as configurações de (turma, curso e periodo vigência) encaminhadas!");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void preencherNovaIntegracaoPessoaWS(IntegracaoPessoaVO integracaoPessoaVO) throws Exception {
		UsuarioVO usuResp = getFacadeFactory().getMatriculaCRMFacade().validarDadosUsuarioResponsavel(0, integracaoPessoaVO.getUsuarioResponsavel());
		ConfiguracaoGeralSistemaVO confGeral = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuResp, 0);
		PessoaVO p = new PessoaVO();
		p = p.getPessoaVO(integracaoPessoaVO);
		p.setAluno(Boolean.TRUE);
		PessoaVO.validarDados(p, true, false, false);
		p.setCidade(validarDadosParaCidade(p, p.getCidade().getCodigoIBGE(), p.getCidade().getNome(), p.getCidade().getEstado().getSigla(), usuResp));
		p.setNaturalidade(validarDadosParaCidade(p, p.getNaturalidade().getCodigoIBGE(), p.getNaturalidade().getNome(), p.getNaturalidade().getEstado().getSigla(), usuResp));
		preencherDadosFormacaoAcademica(usuResp, p);
		List<PaizVO> listaPaiz = getFacadeFactory().getPaizFacade().consultarPorNome(p.getNacionalidade().getNome(), false, usuResp);
		if (!listaPaiz.isEmpty()) {
			PaizVO paiz = (PaizVO) listaPaiz.get(0);
			p.setNacionalidade(paiz);
		}
		PessoaVO p2 = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(p.getCPF(), null, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuResp);
		if (!Uteis.isAtributoPreenchido(p2.getCodigo())) {
			getFacadeFactory().getPessoaFacade().incluirPessoaProspectMatriculaCRM(p, usuResp, confGeral, false);
		} else {
			p.setCodigo(p2.getCodigo().intValue());		
			getFacadeFactory().getPessoaFacade().alterarPessoaProspectMatriculaCRM(p, usuResp, confGeral, false);
		}
	}

	private void preencherDadosFormacaoAcademica(UsuarioVO usuResp, PessoaVO p) throws Exception {
		if (!p.getFormacaoAcademicaVOs().isEmpty()) {
			Iterator<FormacaoAcademicaVO> i = p.getFormacaoAcademicaVOs().iterator();
			while (i.hasNext()) {
				FormacaoAcademicaVO formacao = (FormacaoAcademicaVO) i.next();
				CidadeVO cidadeFor = getFacadeFactory().getCidadeFacade().consultarCidadePorCodigoIBGENomeCidade(formacao.getCidade().getCodigoIBGE(), "", usuResp);
				if (cidadeFor != null) {
					if (!cidadeFor.getCodigoIBGE().equals(formacao.getCidade().getCodigoIBGE()) || !cidadeFor.getNome().equals(formacao.getCidade().getNome())) {
						getFacadeFactory().getCidadeFacade().alterar(cidadeFor, usuResp);
					}
					formacao.setCidade(cidadeFor);
				}
				String descricaoArea = formacao.getDescricaoAreaConhecimento();
				if (!descricaoArea.isEmpty()) {
					try {
						Integer codigoArea = Integer.parseInt(descricaoArea);
						formacao.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(codigoArea, usuResp));
					} catch (Exception e) {

					}
				}
			}
		}
	}

	private CidadeVO validarDadosParaCidade(PessoaVO p, String codigoIbge, String nome, String siglaEstado, UsuarioVO usuResp) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(codigoIbge) && !Uteis.isAtributoPreenchido(nome), "O campo Codigo IBGE ou Campo Nome Cidade deve ser informado.");
		CidadeVO cidade = getFacadeFactory().getCidadeFacade().consultarCidadePorCodigoIBGENomeCidade(codigoIbge, nome, usuResp);
		if (cidade == null) {
			cidade = new CidadeVO();
			cidade.setNome(nome);
			cidade.setCodigoIBGE(codigoIbge);
			cidade.setEstado(getFacadeFactory().getEstadoFacade().consultarPorSigla(siglaEstado, usuResp));
			getFacadeFactory().getCidadeFacade().incluir(cidade, usuResp);
		} else if (!cidade.getCodigoIBGE().equals(codigoIbge) || !cidade.getNome().equals(nome)) {
			getFacadeFactory().getCidadeFacade().alterar(cidade, usuResp);
		}
		return cidade;
	}
		
		
    
    
    
    
    
    
    
    
    
    
    
    
}