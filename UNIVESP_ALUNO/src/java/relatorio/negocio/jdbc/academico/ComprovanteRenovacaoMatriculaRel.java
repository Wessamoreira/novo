package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.DadosComerciaisVO;

import negocio.comuns.utilitarias.Extenso;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.academico.ComprovanteRenovacaoMatriculaVO;
import relatorio.negocio.interfaces.academico.ComprovanteRenovacaoMatriculaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ComprovanteRenovacaoMatriculaRel extends SuperRelatorio implements ComprovanteRenovacaoMatriculaRelInterfaceFacade {

    private ComprovanteRenovacaoMatriculaVO comprovanteRenovacaoMatriculaVO;

    public ComprovanteRenovacaoMatriculaRel() {
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * relatorio.negocio.jdbc.academico.ComprovanteRenovacaoMatriculaRelInterfaceFacade#criarObjeto(java.lang.Integer,
     * java.lang.String)
     */
    public List<ComprovanteRenovacaoMatriculaVO> criarObjeto(Integer matriculaPeriodo, String matricula,  UsuarioVO usuarioVO) throws Exception {
        setComprovanteRenovacaoMatriculaVO(new ComprovanteRenovacaoMatriculaVO());
        List<ComprovanteRenovacaoMatriculaVO> listaResultado = new ArrayList<ComprovanteRenovacaoMatriculaVO>(0);
        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().setCodigo(matriculaPeriodo);
//        getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO(), NivelMontarDados.BASICO, usuarioVO);
        getComprovanteRenovacaoMatriculaVO().setAutorizacaoCurso(getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getCurso().getCodigo(), getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getData(), Uteis.NIVELMONTARDADOS_COMBOBOX));
        getFacadeFactory().getPessoaFacade().carregarDados(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno(), usuarioVO);
//        DadosComerciaisVO dados = getFacadeFactory().getDadosComerciaisFacade().consultarEmpregoAtualPorCodigoPessoa(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCodigo(), usuarioVO);
//        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().setNomeEmpresa(dados.getNomeEmpresa());
//        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().setEnderecoEmpresa(dados.getEnderecoEmpresa());
//        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().setCepEmpresa(dados.getCepEmpresa());
//        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().setCidadeEmpresa(dados.getCidadeEmpresa());
//        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().setSetorEmpresa(dados.getSetorEmpresa());
//        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().setCargoPessoaEmpresa(dados.getCargoPessoaEmpresa());
//        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().setComplementoEmpresa(dados.getComplementoEmpresa());
//        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().setTelefoneComer(dados.getTelefoneComer());
//        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().setSexo(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().getSexo_Apresentar());
//        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().setEstadoCivil(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().getEstadoCivil_Apresentar());
//        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().setCidade(getFacadeFactory().getCidadeFacade().consultarCidadeUnidadeEnsinoPorMatricula(matricula, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
//        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().setMatriculaPeriodoTumaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaPeriodo(matriculaPeriodo, false, usuarioVO));
//        if(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo() > 0){
//        	getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
//        }
        getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().setTurma(getFacadeFactory().getTurmaFacade().carregarDadosTurmaAgrupada(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getTurma(), usuarioVO));
//        if(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo() > 0){
//        	getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO));
//
//        	getFacadeFactory().getMatriculaPeriodoFacade().realizarCalculoValorMatriculaEMensalidade(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO(), getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO(), usuarioVO);
//			
//        }
        if (!getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNaturalidade().getCodigo().equals(0)) {
            getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNaturalidade().setEstado(getFacadeFactory().getEstadoFacade().consultarPorCodigoCidade(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNaturalidade().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        }
        if (!getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().getCodigo().equals(0)) {
            getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().setEstado(getFacadeFactory().getEstadoFacade().consultarPorCodigoCidade(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCidade().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        }
        getComprovanteRenovacaoMatriculaVO().setDocumetacaoMatriculaVOs(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculaPorMatriculaAlunoEntregue(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, false, usuarioVO));
        consultarContaReceberDataPagamento(getComprovanteRenovacaoMatriculaVO());
        if (!getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getResponsavelLiberacaoMatricula().getCodigo().equals(0)) {
            getComprovanteRenovacaoMatriculaVO().setDataPagtoMatricula(Uteis.getData(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getDataLiberacaoMatricula(), "dd/MM/yyyy"));
        }
        Extenso ext = new Extenso();
        getComprovanteRenovacaoMatriculaVO().setCidadeDataAtual(Uteis.getDataCidadeDiaMesPorExtensoEAno(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCidade().getNome(), new Date(), false));
		getComprovanteRenovacaoMatriculaVO().setValorMatricula(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getValorMatriculaCheio());
		ext.setNumber(getComprovanteRenovacaoMatriculaVO().getValorMatricula());
		getComprovanteRenovacaoMatriculaVO().setValorMatriculaExtenso(ext.toString());
		ext.setNumber(getComprovanteRenovacaoMatriculaVO().getValorRecebidoMatricula());
		getComprovanteRenovacaoMatriculaVO().setValorRecebidoMatriculaExtenso(ext.toString());
		UnidadeEnsinoVO u = getFacadeFactory().getUnidadeEnsinoFacade().obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		getComprovanteRenovacaoMatriculaVO().setPeriodo(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getPeridoLetivo().getDescricao());
		listaResultado.add(getComprovanteRenovacaoMatriculaVO());
		return listaResultado;
    }

    public void consultarContaReceberDataPagamento(ComprovanteRenovacaoMatriculaVO comprovanteRenovacaoMatricula) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT cr.situacao as cr_situacao, cr.valor as cr_valor, nr.valorTotalRecebimento as nr_valorTotalRecebimento, nr.data as nr_data FROM contaReceber cr ");
        sqlStr.append("LEFT JOIN contaReceberNegociacaoRecebimento crnr ON crnr.contaReceber = cr.codigo ");
        sqlStr.append("LEFT JOIN negociacaoRecebimento nr ON nr.codigo = crnr.negociacaoRecebimento ");
        sqlStr.append("WHERE cr.parcela = 'Matrícula' AND cr.matriculaPeriodo = ").append(getComprovanteRenovacaoMatriculaVO().getMatriculaPeriodoVO().getCodigo());
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (resultado.next()) {
            comprovanteRenovacaoMatricula.setValorMatricula(resultado.getDouble("cr_valor"));
            comprovanteRenovacaoMatricula.setValorRecebidoMatricula(Uteis.arredondar(resultado.getDouble("nr_valorTotalRecebimento"), 2, 0));
            comprovanteRenovacaoMatricula.setDataPagtoMatricula(Uteis.getData(resultado.getDate("nr_data"), "dd/MM/yyyy"));
        }
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getDesignComprovanteMatriculaIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeComprovanteMatricula() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("FichaInscricaoMatriculaRel");
    }

    public static String getIdEntidadeComprovanteMatricula() {
        return ("ComprovanteRenovacaoMatriculaRel");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * relatorio.negocio.jdbc.academico.ComprovanteRenovacaoMatriculaRelInterfaceFacade#getComprovanteRenovacaoMatriculaVO
     * ()
     */
    public ComprovanteRenovacaoMatriculaVO getComprovanteRenovacaoMatriculaVO() {
        if (comprovanteRenovacaoMatriculaVO == null) {
            comprovanteRenovacaoMatriculaVO = new ComprovanteRenovacaoMatriculaVO();
        }
        return comprovanteRenovacaoMatriculaVO;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * relatorio.negocio.jdbc.academico.ComprovanteRenovacaoMatriculaRelInterfaceFacade#setComprovanteRenovacaoMatriculaVO
     * (relatorio.negocio.comuns.academico.ComprovanteRenovacaoMatriculaVO)
     */
    public void setComprovanteRenovacaoMatriculaVO(ComprovanteRenovacaoMatriculaVO comprovanteRenovacaoMatriculaVO) {
        this.comprovanteRenovacaoMatriculaVO = comprovanteRenovacaoMatriculaVO;
    }
}
