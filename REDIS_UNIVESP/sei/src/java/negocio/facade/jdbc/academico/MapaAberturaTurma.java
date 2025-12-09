package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.TurmaAberturaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.MapaAberturaTurmaInterfaceFacade;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MapaAberturaTurma extends ControleAcesso implements MapaAberturaTurmaInterfaceFacade {

    public MapaAberturaTurma() {
    }

    public void persistirAberturaTurma(TurmaVO obj, TurmaAberturaVO turmaAberturaVO, Date dataTemp, UsuarioVO usuario) throws Exception {
        executarMudancaSituacaoTurmaAbertura(obj, obj.getTurmaAberturaVOs(), turmaAberturaVO, dataTemp, usuario);
    }

    public void executarMudancaSituacaoTurmaAbertura(TurmaVO turmaVO, List<TurmaAberturaVO> turmaAberturaVOs, TurmaAberturaVO obj, Date dataTemp, UsuarioVO usuarioVO) throws Exception {
        Date novaData = new Date();
        for (TurmaAberturaVO turmaAberturaVO : turmaAberturaVOs) {

            if (turmaAberturaVO.getSituacao().equals("AD")) {
                novaData = turmaAberturaVO.getData();
                turmaAberturaVO.setData(dataTemp);
                turmaAberturaVO.setDataAdiada(novaData);
                getFacadeFactory().getTurmaAberturaFacade().alterar(turmaAberturaVO);
                TurmaAberturaVO novoObj = getFacadeFactory().getTurmaAberturaFacade().inicializarDadosTurmaAbertura(turmaAberturaVO.getTurma().getCodigo(), usuarioVO);
                novoObj.setData(novaData);
                getFacadeFactory().getTurmaAberturaFacade().incluir(novoObj);
            } else {
                if (turmaAberturaVO.getSituacao().equals("CO")) {
                    obj.setAbrirModalEnviarEmail(Boolean.TRUE);
                }
                getFacadeFactory().getTurmaAberturaFacade().alterar(turmaAberturaVO);
            }
        }
    }

    public List<TurmaAberturaVO> consultarAberturaTurma(Integer turma, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, String situacao, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select turmaabertura.codigo, turmaabertura.data, turmaabertura.dataAdiada, turmaabertura.situacao, turmaabertura.turma, turmaabertura.usuario, ");
        sqlStr.append("(select count(matriculaperiodo.matricula) from matriculaperiodo where matriculaperiodo.turma = turmaabertura.turma ");
        sqlStr.append("and matriculaperiodo.situacaoMatriculaperiodo = 'AT' and matriculaperiodo.bolsista = false ");
        sqlStr.append("AND (matriculaperiodo.situacao = 'PF' OR matriculaperiodo.situacao = 'AT' OR matriculaperiodo.situacao = 'CO') ) as qtdeAlunoMatriculado, ");
        sqlStr.append("(select count(matriculaperiodo.matricula) from matriculaperiodo where matriculaperiodo.turma = turmaabertura.turma ");
        sqlStr.append("and matriculaperiodo.situacaoMatriculaperiodo = 'PR' and matriculaperiodo.bolsista = false ");
        //sqlStr.append("AND (matriculaperiodo.situacao = 'PF' OR matriculaperiodo.situacao = 'AT' OR matriculaperiodo.situacao = 'CO') ) as qtdeAlunoMatriculado ");
        sqlStr.append(") as qtdeAlunoPreMatriculado ");
        sqlStr.append("from turmaabertura ");
        sqlStr.append("inner join turma on turma.codigo = turmaabertura.turma ");
        sqlStr.append("inner join curso on curso.codigo = turma.curso ");
        sqlStr.append("inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
        sqlStr.append("WHERE 1=1 ");
        if (dataInicio != null) {
            sqlStr.append("AND turmaabertura.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
        }
        if (dataFim != null) {
            sqlStr.append("AND turmaabertura.data <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
        }
//        if (unidadeEnsino != null && unidadeEnsino != 0) {
//            sqlStr.append("AND unidadeensino.codigo = ").append(unidadeEnsino).append(" ");
//        }
        int x = 0;
        for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
            if (unidadeEnsinoVO.getEscolhidaParaFazerCotacao()) {
                if (x == 0) {
                    sqlStr.append("and unidadeEnsino.codigo in ( ");
                }
                if (x > 0) {
                    sqlStr.append(", ");
                }
                sqlStr.append(unidadeEnsinoVO.getCodigo());
                x++;
            }
        }
        if (x > 0) {
            sqlStr.append(" ) ");
        }
        if (turma != null && turma != 0) {
            sqlStr.append("AND turma.codigo = ").append(turma).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("AND curso.codigo = ").append(curso).append(" ");
        }
        if (!situacao.equals("")) {
            sqlStr.append("AND turmaabertura.situacao = '").append(situacao).append("' ");
        }
        sqlStr.append("GROUP BY turmaabertura.codigo, turmaabertura.data, turmaabertura.dataAdiada, turmaabertura.situacao, turmaabertura.turma, turmaabertura.usuario ");
        sqlStr.append("ORDER BY  turmaabertura.data, turmaabertura.turma");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public static List<TurmaAberturaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<TurmaAberturaVO> vetResultado = new ArrayList<TurmaAberturaVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    public static TurmaAberturaVO montarDados(SqlRowSet dadosSql, UsuarioVO usuario) throws Exception {
        TurmaAberturaVO obj = new TurmaAberturaVO();
        obj.setCodigo(dadosSql.getInt("codigo"));
        obj.setData(dadosSql.getDate("data"));
        obj.setDataAdiada(dadosSql.getDate("dataAdiada"));
        obj.setSituacao(dadosSql.getString("situacao"));
        obj.setQtdeAlunoMatriculado(dadosSql.getInt("qtdeAlunoMatriculado"));
        obj.setQtdeAlunoPreMatriculado(dadosSql.getInt("qtdeAlunoPreMatriculado"));
        obj.getTurma().setCodigo(dadosSql.getInt("turma"));
        getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(), NivelMontarDados.BASICO, usuario);
        return obj;
    }

    public String montarGraficoPizza(List<TurmaAberturaVO> listaTurmaAberturaVO) throws Exception {
        Map<String, Integer> hashSituacaoTurmaAbertura = new HashMap<String, Integer>(0);
        int somaValor = 0;
        for (TurmaAberturaVO turmaAbertura : listaTurmaAberturaVO) {
            if (hashSituacaoTurmaAbertura.containsKey(turmaAbertura.getSituacao_Apresentar())) {
                somaValor = hashSituacaoTurmaAbertura.get(turmaAbertura.getSituacao_Apresentar());
                somaValor++;
                hashSituacaoTurmaAbertura.put(turmaAbertura.getSituacao_Apresentar(), somaValor);
            } else {
                hashSituacaoTurmaAbertura.put(turmaAbertura.getSituacao_Apresentar(), 1);
            }
        }
        StringBuilder dataGrafico = new StringBuilder();
		Boolean virgula = false;
		dataGrafico.append("data: [");
        for (Entry<String, Integer> turmaAberturaVO : hashSituacaoTurmaAbertura.entrySet()) {
        	if (virgula) {
        		dataGrafico.append(", ");
			}
        	dataGrafico.append("{ name: '").append(turmaAberturaVO.getKey()).append("', y:").append(turmaAberturaVO.getValue()).append("}");
        	virgula = true;
		}
		dataGrafico.append("]");
        return dataGrafico.toString();
    }

    public List consultarUsuario(String campoConsultaUsuario, UnidadeEnsinoVO unidadeEnsinoVO, String valorConsultaUsuario, UsuarioVO usuarioLogado) throws Exception {
        if (valorConsultaUsuario.length() < 2) {
            throw new Exception("Informe pelo menos 2 (dois) parâmetros para consulta.");
        }
        if (campoConsultaUsuario.equals("nome")) {
            return getFacadeFactory().getUsuarioFacade().consultarPorNome(valorConsultaUsuario, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioLogado);
        }
        if (campoConsultaUsuario.equals("username")) {
            return getFacadeFactory().getUsuarioFacade().consultarPorUsername(valorConsultaUsuario, false,
                    Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioLogado);
        }
        return new ArrayList(0);
    }

    public List consultarTurma(String campoConsultaTurma, UnidadeEnsinoVO unidadeEnsinoVO, String valorConsultaTurma, UsuarioVO usuarioLogado) throws Exception {

        if (campoConsultaTurma.equals("identificadorTurma")) {
            return getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(valorConsultaTurma, unidadeEnsinoVO.getCodigo(), false, false, false,
                    Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
        }
        if (campoConsultaTurma.equals("nomeTurno")) {
            return getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(valorConsultaTurma, unidadeEnsinoVO.getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
        }
        if (campoConsultaTurma.equals("nomeCurso")) {
            return getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(valorConsultaTurma, unidadeEnsinoVO.getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
        }
        return new ArrayList(0);
    }

    public List consultarCurso(String campoConsultaCurso, UnidadeEnsinoVO unidadeEnsinoVO, String valorConsultaCurso, UsuarioVO usuarioLogado) throws Exception {

        if (campoConsultaCurso.equals("nome")) {
            return getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(valorConsultaCurso,
                    unidadeEnsinoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
        }
        return new ArrayList(0);
    }

    public void validarDadosEnviarComunicado(UsuarioVO usuarioDestinatarioVO, UsuarioVO usuarioLogado) throws Exception {
        if (usuarioLogado.getPessoa() == null || usuarioLogado.getPessoa().getCodigo().equals(0)) {
            throw new Exception("Este usuáro não pode enviar Comunicação Interna, pois não possui nenhuma pessoa vinculada a ele.");
        }
        if (usuarioDestinatarioVO == null || usuarioDestinatarioVO.getCodigo() == 0) {
            throw new Exception("O campo USUÁRIO DESTINATÁRIO deve ser informado.");
        }
    }

    public void enviarEmailUsuario(UsuarioVO usuarioDestinatarioVO, UnidadeEnsinoVO unidadeEnsinoVO, Boolean enviarComunicadoPorEmail, String mensagemPadraoNotificacao, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        validarDadosEnviarComunicado(usuarioDestinatarioVO, usuarioLogado);
        ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();

        comunicacaoInternaVO.setResponsavel(usuarioLogado.getPessoa());
        comunicacaoInternaVO.setUnidadeEnsino(unidadeEnsinoVO);
        comunicacaoInternaVO.setAssunto("Confirmação Abertura de Turma");
        comunicacaoInternaVO.setEnviarEmail(enviarComunicadoPorEmail);
        comunicacaoInternaVO.setTipoDestinatario(usuarioDestinatarioVO.getTipoUsuario());
        comunicacaoInternaVO.setTipoMarketing(false);
        comunicacaoInternaVO.setTipoLeituraObrigatoria(false);
        comunicacaoInternaVO.setDigitarMensagem(true);
        comunicacaoInternaVO.setRemoverCaixaSaida(false);
        comunicacaoInternaVO.setPessoa(usuarioDestinatarioVO.getPessoa());
        comunicacaoInternaVO.setMensagem(comunicacaoInternaVO.getMensagemComLayout(mensagemPadraoNotificacao));
        adicionarDestinatarioResponsavelComunicado(comunicacaoInternaVO, comunicacaoInternaVO.getPessoa());
        getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, true, usuarioLogado, configuracaoGeralSistemaVO,null);

    }

    public void adicionarDestinatarioResponsavelComunicado(ComunicacaoInternaVO comunicacaoInternaVO, PessoaVO responsavel) throws Exception {
        ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
        if (responsavel != null && responsavel.getCodigo() != 0) {
            comunicadoInternoDestinatarioVO.setDestinatario(responsavel);
            comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
            comunicadoInternoDestinatarioVO.setDataLeitura(null);
            comunicadoInternoDestinatarioVO.setCiJaRespondida(false);
            comunicadoInternoDestinatarioVO.setCiJaLida(false);
            comunicadoInternoDestinatarioVO.setRemoverCaixaEntrada(false);
            comunicadoInternoDestinatarioVO.setMensagemMarketingLida(false);
            comunicacaoInternaVO.adicionarObjComunicadoInternoDestinatarioVOs(comunicadoInternoDestinatarioVO);

        }
    }
}
