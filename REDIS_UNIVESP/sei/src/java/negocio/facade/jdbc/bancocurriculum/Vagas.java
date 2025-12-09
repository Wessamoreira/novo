package negocio.facade.jdbc.bancocurriculum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.bancocurriculum.CandidatosVagasVO;
import negocio.comuns.bancocurriculum.OpcaoRespostaVagaQuestaoVO;
import negocio.comuns.bancocurriculum.VagaAreaVO;
import negocio.comuns.bancocurriculum.VagaContatoVO;
import negocio.comuns.bancocurriculum.VagaEstadoVO;
import negocio.comuns.bancocurriculum.VagaQuestaoVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.bancocurriculum.enumeradores.SituacaoReferenteVagaEnum;
import negocio.comuns.bancocurriculum.enumeradores.TipoVagaQuestaoEnum;
import negocio.comuns.basico.AreaProfissionalInteresseContratacaoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.bancocurriculum.VagasInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Vagas extends ControleAcesso implements VagasInterfaceFacade {

    private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public Vagas() throws Exception {
        super();
        setIdEntidade("Vagas");
    }

    public static void validarDados(VagasVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getCargo() == null) {
            throw new ConsistirException("O campo CARGO (etapa 1) deve ser informado.");
        }
        if (obj.getParceiro() == null || obj.getParceiro().getCodigo() == 0) {
            throw new ConsistirException("O campo PARCEIRO (etapa 1) deve ser informado.");
        }
        if (obj.getAreaProfissional() == null || obj.getAreaProfissional().getCodigo() == 0) {
            throw new ConsistirException("O campo AREA PROFISSIONAL (etapa 1) deve ser informado.");
        }
        if (obj.getNumeroVagas() == null) {
            throw new ConsistirException("O campo NUMERO DE VAGAS (etapa 1) deve ser informado.");
        }
//        if (obj.getSuperiorImediato() == null || obj.getSuperiorImediato().equals("")) {
//            throw new ConsistirException("O campo SUPERIOR IMEDIATO (etapa 1) deve ser informado.");
//        }
//        if (obj.getEstado().getCodigo() == null || obj.getEstado().getCodigo().equals(0)) {
//            throw new ConsistirException("O campo ESTADO (etapa 1) deve ser informado.");
//        }
        if (obj.getCidade().getCodigo() == null || obj.getCidade().getCodigo().equals(0)) {
            throw new ConsistirException("O campo CIDADE (etapa 1) deve ser informado.");
        }
        if (obj.getHorarioTrabalho() == null || obj.getHorarioTrabalho().equals("")) {
            throw new ConsistirException("O campo HORÁRIO DE TRABALHO (etapa 1) deve ser informado.");
        }
        if (obj.getSalario() == null) {
            throw new ConsistirException("O campo SALARIO (etapa 1) deve ser informado.");
        }
        if (!obj.getCLT() && !obj.getMensalista() && !obj.getHorista() && !obj.getEstagio() && !obj.getTemporario() && !obj.getOutro()) {
            throw new ConsistirException("O campo CONDIÇÃO DE CONTRATAÇÃO (etapa 1) deve ser informado.");
        }
        if (obj.getEscolaridadeRequerida() == null || obj.getEscolaridadeRequerida().equals("")) {
            throw new ConsistirException("O campo ESCOLARIDADE REQUERIDA (etapa 1) deve ser informado.");
        }
        if (obj.getNecessitaVeiculo().booleanValue() && obj.getTipoVeiculo().equals("")) {
            throw new ConsistirException("O campo VEÍCULO (etapa 1) deve ser informado.");
        }
        if (obj.getIngles()) {
            if (obj.getInglesNivel() == null || obj.getInglesNivel().equals("")) {
                throw new ConsistirException("O campo NÍVEL - IDIOMA INGLÊS (etapa 1) deve ser informado.");
            }
        } else {
            obj.setInglesNivel("");
        }
        if (obj.getFrances()) {
            if (obj.getFrancesNivel() == null || obj.getFrancesNivel().equals("")) {
                throw new ConsistirException("O campo NÍVEL - IDIOMA FRANCÊS (etapa 1) deve ser informado.");
            }
        } else {
            obj.setFrancesNivel("");
        }
        if (obj.getEspanhol()) {
            if (obj.getEspanholNivel() == null || obj.getEspanholNivel().equals("")) {
                throw new ConsistirException("O campo NÍVEL - IDIOMA ESPANHOL (etapa 1) deve ser informado.");
            }
        } else {
            obj.setEspanholNivel("");
        }
        if (!obj.getDeclaracaoValidade()) {
            throw new ConsistirException("O campo DECLARAÇÃO DE VALIDADE (etapa 3) deve ser informado.");
        }
        if (obj.getQtdDiasExpiracaoVaga() == 0) {
        	throw new ConsistirException("O campo QTD DIAS EXPIRA~]AO VAGA (etapa 3) deve ser informado.");
        }
        if (obj.getVagaEstadoVOs().isEmpty()) {
            throw new ConsistirException("Deve ser informado ao menos um estado para a vaga.");
        } else {
            Iterator i = obj.getVagaEstadoVOs().iterator();
            boolean entrou = false;
            while (i.hasNext()) {
                VagaEstadoVO v = (VagaEstadoVO) i.next();
                if (v.getSelecionado()) {
                    entrou = true;
                }
            }
            if (!entrou) {
                throw new ConsistirException("Deve ser informado ao menos um estado para a vaga.");
            }
        }
        if (obj.getVagaAreaVOs().isEmpty()) {
            throw new ConsistirException("Deve ser informado ao menos uma área profissional para a vaga.");
        } else {
            Iterator i = obj.getVagaAreaVOs().iterator();
            boolean entrou = false;
            while (i.hasNext()) {
                VagaAreaVO v = (VagaAreaVO) i.next();
                if (v.getSelecionado()) {
                    entrou = true;
                }
            }
            if (!entrou) {
                throw new ConsistirException("Deve ser informado ao menos uma área profissional para a vaga.");
            }
        }
    }

    public VagasVO novo() throws Exception {
        Vagas.incluir(getIdEntidade());
        VagasVO obj = new VagasVO();

        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final VagasVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            final String sql = "INSERT INTO Vagas(parceiro, cargo,  numeroVagas, superiorImediato, horarioTrabalho, salario, beneficios," // 1-9
                    + " horista, mensalista, clt, estagio, temporario, outro, escolaridadeRequerida, conhecimentoEspecifico, principalAtividadeCargo,"//10-19
                    + " necessitaVeiculo, tipoVeiculo, mudanca, transferencia, viagem, caracteristicaPessoalImprescindivel, declaracaoValidade,"//20-26
                    + " ingles, espanhol, frances, inglesNivel, espanholNivel, francesNivel, "//27-32
                    + " windows, word, excel, access, powerPoint, internet, sap, corelDraw, autoCad, photoshop, microsiga, dataCadastro, situacao, outrosSoftwares,"//33-46
                    + " motivoEncerramento, alunoContratado, empresaSigilosaParaVaga, cidade, areaProfissional, qtdDiasExpiracaoVaga ) "
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getParceiro().getCodigo());
                    sqlInserir.setString(2, obj.getCargo());
                    sqlInserir.setInt(3, obj.getNumeroVagas());
                    sqlInserir.setString(4, obj.getSuperiorImediato());
                    //sqlInserir.setInt(6, obj.getEstado().getCodigo());
                    sqlInserir.setString(5, obj.getHorarioTrabalho());
                    sqlInserir.setString(6, obj.getSalario());
                    sqlInserir.setString(7, obj.getBeneficios());
                    sqlInserir.setBoolean(8, obj.getHorista());
                    sqlInserir.setBoolean(9, obj.getMensalista());
                    sqlInserir.setBoolean(10, obj.getCLT());
                    sqlInserir.setBoolean(11, obj.getEstagio());
                    sqlInserir.setBoolean(12, obj.getTemporario());
                    sqlInserir.setBoolean(13, obj.getOutro());
                    sqlInserir.setString(14, obj.getEscolaridadeRequerida());
                    sqlInserir.setString(15, obj.getConhecimentoEspecifico());
                    sqlInserir.setString(16, obj.getPrincipalAtividadeCargo());
                    sqlInserir.setBoolean(17, obj.getNecessitaVeiculo());
                    sqlInserir.setString(18, obj.getTipoVeiculo());
                    sqlInserir.setBoolean(19, obj.getMudanca());
                    sqlInserir.setBoolean(20, obj.getTransferencia());
                    sqlInserir.setBoolean(21, obj.getViagem());
                    sqlInserir.setString(22, obj.getCaracteristicaPessoalImprescindivel());
                    sqlInserir.setBoolean(23, obj.getDeclaracaoValidade());

                    sqlInserir.setBoolean(24, obj.getIngles());
                    sqlInserir.setBoolean(25, obj.getEspanhol());
                    sqlInserir.setBoolean(26, obj.getFrances());
                    sqlInserir.setString(27, obj.getInglesNivel());
                    sqlInserir.setString(28, obj.getEspanholNivel());
                    sqlInserir.setString(29, obj.getFrancesNivel());

                    sqlInserir.setBoolean(30, obj.getWindows());
                    sqlInserir.setBoolean(31, obj.getWord());
                    sqlInserir.setBoolean(32, obj.getExcel());
                    sqlInserir.setBoolean(33, obj.getAccess());
                    sqlInserir.setBoolean(34, obj.getPowerPoint());
                    sqlInserir.setBoolean(35, obj.getInternet());
                    sqlInserir.setBoolean(36, obj.getSap());
                    sqlInserir.setBoolean(37, obj.getCorelDraw());
                    sqlInserir.setBoolean(38, obj.getAutoCad());
                    sqlInserir.setBoolean(39, obj.getPhotoshop());
                    sqlInserir.setBoolean(40, obj.getMicrosiga());
                    sqlInserir.setDate(41, Uteis.getDataJDBC(new Date()));
                    sqlInserir.setString(42, obj.getSituacao());
                    sqlInserir.setString(43, obj.getOutrosSoftwares());
                    sqlInserir.setString(44, obj.getMotivoEncerramento());
                    sqlInserir.setString(45, obj.getAlunoContratado());                    
                    sqlInserir.setBoolean(46, obj.getEmpresaSigilosaParaVaga());
                    sqlInserir.setInt(47, obj.getCidade().getCodigo());
                    sqlInserir.setInt(48, obj.getAreaProfissional().getCodigo());
                    sqlInserir.setInt(49, obj.getQtdDiasExpiracaoVaga());
                    

                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            getFacadeFactory().getVagaQuestaoFacade().incluirVagaQuestao(obj, usuario);
            getFacadeFactory().getVagaContatoFacade().incluirVagaContato(obj);
            getFacadeFactory().getVagaEstadoFacade().incluirVagaEstado(obj);
            getFacadeFactory().getVagaAreaFacade().incluirVagaArea(obj);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoEDataDeVagasExpiracao(List lista, UsuarioVO usuario) throws Exception {
        Iterator i = lista.iterator();
        while (i.hasNext()) {
            VagasVO vagas = (VagasVO) i.next();
            vagas.setSituacao("EX");
            gravarSituacao(vagas, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoEDataDeVagasEncerrada(List lista, UsuarioVO usuario) throws Exception {
        Iterator i = lista.iterator();
        while (i.hasNext()) {
            VagasVO vagas = (VagasVO) i.next();
            if (vagas.getEncerrar()) {
                vagas.setSituacao("EN");
                gravarSituacao(vagas, usuario);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gravarSituacao(final VagasVO obj, UsuarioVO usuario) throws Exception {
        try {
            //validarDados(obj);
            Vagas.alterar(getIdEntidade());
            final String sql;
            if (obj.getSituacao().equals("AT")) {
                sql = "UPDATE Vagas SET situacao=?, dataAtivacao=?, dataAlteracao=? WHERE ((codigo =? ))";
            } else if (obj.getSituacao().equals("EN")) {
                sql = "UPDATE Vagas SET situacao=?, dataEncerramento=?, dataAlteracao=? WHERE ((codigo =? ))";
            } else if (obj.getSituacao().equals("EX")) {
                sql = "UPDATE Vagas SET situacao=?, dataExpiracao=?, dataAlteracao=? WHERE ((codigo =? ))";
            } else {
                sql = "UPDATE Vagas SET situacao=?, dataAlteracao=? WHERE ((codigo =? ))";
            }
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getSituacao());
                    if (obj.getSituacao().equals("AT")) {
                        sqlAlterar.setDate(2, Uteis.getDataJDBC(new Date()));
                        sqlAlterar.setDate(3, Uteis.getDataJDBC(new Date()));
                        sqlAlterar.setInt(4, obj.getCodigo().intValue());
                    } else if (obj.getSituacao().equals("EN")) {
                        sqlAlterar.setDate(2, Uteis.getDataJDBC(new Date()));
                        sqlAlterar.setDate(3, Uteis.getDataJDBC(new Date()));
                        sqlAlterar.setInt(4, obj.getCodigo().intValue());
                    } else if (obj.getSituacao().equals("EX")) {
                        sqlAlterar.setDate(2, Uteis.getDataJDBC(new Date()));
                        sqlAlterar.setDate(3, Uteis.getDataJDBC(new Date()));
                        sqlAlterar.setInt(4, obj.getCodigo().intValue());
                    } else {
                        sqlAlterar.setDate(2, Uteis.getDataJDBC(new Date()));
                        sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    }
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarParceiro(final Integer codParceiroManter, final Integer codParceiroRemover) throws Exception {
        try {
            final String sql;
            sql = "UPDATE Vagas SET parceiro=?, dataAlteracao=? WHERE ((codigo =? ))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, codParceiroManter);
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(new Date()));
                    sqlAlterar.setInt(3, codParceiroRemover);
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final VagasVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            Vagas.alterar(getIdEntidade());
            final String sql = "UPDATE Vagas SET parceiro=?, cargo=?,  numeroVagas=?, superiorImediato=?, horarioTrabalho=?, salario=?,"
                    + " beneficios=?, horista=?, mensalista=?, clt=?, estagio=?, temporario=?, outro=?, escolaridadeRequerida=?, conhecimentoEspecifico=?,"
                    + " principalAtividadeCargo=?, necessitaVeiculo=?, tipoVeiculo=?, mudanca=?, transferencia=?, viagem=?, caracteristicaPessoalImprescindivel=?, declaracaoValidade=?,"
                    + " ingles=?, espanhol=?, frances=?, inglesNivel=?, espanholNivel=?, francesNivel=?, "
                    + " windows=?, word=?, excel=?, access=?, powerPoint=?, internet=?, sap=?, corelDraw=?, autoCad=?, photoshop=?, microsiga=?,"
                    + " outrosSoftwares=?, motivoEncerramento=?, alunoContratado=?, empresaSigilosaParaVaga=?, dataAlteracao=?, cidade=?, areaProfissional=?, qtdDiasExpiracaoVaga=? "
                    + " WHERE ((codigo =? ))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getParceiro().getCodigo());
                    sqlAlterar.setString(2, obj.getCargo());
                    //
                    sqlAlterar.setInt(3, obj.getNumeroVagas());
                    sqlAlterar.setString(4, obj.getSuperiorImediato());
                    //sqlAlterar.setInt(6, obj.getEstado().getCodigo());
                    sqlAlterar.setString(5, obj.getHorarioTrabalho());
                    sqlAlterar.setString(6, obj.getSalario());
                    sqlAlterar.setString(7, obj.getBeneficios());
                    sqlAlterar.setBoolean(8, obj.getHorista());
                    sqlAlterar.setBoolean(9, obj.getMensalista());
                    sqlAlterar.setBoolean(10, obj.getCLT());
                    sqlAlterar.setBoolean(11, obj.getEstagio());
                    sqlAlterar.setBoolean(12, obj.getTemporario());
                    sqlAlterar.setBoolean(13, obj.getOutro());
                    sqlAlterar.setString(14, obj.getEscolaridadeRequerida());
                    sqlAlterar.setString(15, obj.getConhecimentoEspecifico());
                    sqlAlterar.setString(16, obj.getPrincipalAtividadeCargo());
                    sqlAlterar.setBoolean(17, obj.getNecessitaVeiculo());
                    sqlAlterar.setString(18, obj.getTipoVeiculo());
                    sqlAlterar.setBoolean(19, obj.getMudanca());
                    sqlAlterar.setBoolean(20, obj.getTransferencia());
                    sqlAlterar.setBoolean(21, obj.getViagem());
                    sqlAlterar.setString(22, obj.getCaracteristicaPessoalImprescindivel());
                    sqlAlterar.setBoolean(23, obj.getDeclaracaoValidade());

                    sqlAlterar.setBoolean(24, obj.getIngles());
                    sqlAlterar.setBoolean(25, obj.getEspanhol());
                    sqlAlterar.setBoolean(26, obj.getFrances());
                    sqlAlterar.setString(27, obj.getInglesNivel());
                    sqlAlterar.setString(28, obj.getEspanholNivel());
                    sqlAlterar.setString(29, obj.getFrancesNivel());

                    sqlAlterar.setBoolean(30, obj.getWindows());
                    sqlAlterar.setBoolean(31, obj.getWord());
                    sqlAlterar.setBoolean(32, obj.getExcel());
                    sqlAlterar.setBoolean(33, obj.getAccess());
                    sqlAlterar.setBoolean(34, obj.getPowerPoint());
                    sqlAlterar.setBoolean(35, obj.getInternet());
                    sqlAlterar.setBoolean(36, obj.getSap());
                    sqlAlterar.setBoolean(37, obj.getCorelDraw());
                    sqlAlterar.setBoolean(38, obj.getAutoCad());
                    sqlAlterar.setBoolean(39, obj.getPhotoshop());
                    sqlAlterar.setBoolean(40, obj.getMicrosiga());
                    sqlAlterar.setString(41, obj.getOutrosSoftwares());
                    sqlAlterar.setString(42, obj.getMotivoEncerramento());
                    sqlAlterar.setString(43, obj.getAlunoContratado());                    
                    sqlAlterar.setBoolean(44, obj.getEmpresaSigilosaParaVaga());
                    sqlAlterar.setDate(45, Uteis.getDataJDBC(new Date()));
                    sqlAlterar.setInt(46, obj.getCidade().getCodigo());
                    sqlAlterar.setInt(47, obj.getAreaProfissional().getCodigo());
                    sqlAlterar.setInt(48, obj.getQtdDiasExpiracaoVaga());
                    sqlAlterar.setInt(49, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getVagaQuestaoFacade().alterarVagaQuestao(obj, usuario);
            getFacadeFactory().getVagaContatoFacade().alterarVagaContato(obj);
            getFacadeFactory().getVagaEstadoFacade().alterarVagaEstado(obj);
            getFacadeFactory().getVagaAreaFacade().alterarVagaArea(obj);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(VagasVO obj, UsuarioVO usuario) throws Exception {
        try {
            Vagas.excluir(getIdEntidade());
            String sql = "DELETE FROM Vagas WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

        } catch (Exception e) {
            throw e;
        }
    }

    public List consultarPorParceiro(String valorConsulta, String ordenacao, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT vagas.*, parceiro.nome from vagas ");
        sqlStr.append(" INNER JOIN parceiro ON parceiro.codigo = vagas.parceiro ");
        sqlStr.append(" WHERE parceiro.nome ILIKE('").append(valorConsulta).append("%')");
        if (situacao != null && !situacao.equals("")) {
            sqlStr.append(" AND situacao ='").append(situacao).append("' ");
        }
        if (ordenacao.equals("data")) {
            sqlStr.append(" ORDER BY vagas.dataativacao desc ");
        } else if (ordenacao.equals("codigoVaga")) {
            sqlStr.append(" ORDER BY vagas.codigo ");
        } else {
            sqlStr.append(" ORDER BY parceiro.nome ");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List<VagasVO> consultarVagasRecentementeAtivadas() throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT vagas.codigo, vagas.cargo  from vagas ");
        //sqlStr.append(" INNER JOIN cidade ON cidade.codigo = vagas.cidade ");
        sqlStr.append(" INNER JOIN parceiro ON parceiro.codigo = vagas.parceiro ");
        //sqlStr.append(" INNER JOIN estado ON estado.codigo = cidade.estado ");
        sqlStr.append(" WHERE vagas.situacao = 'AT'");
        sqlStr.append(" ORDER BY dataativacao desc ");
        sqlStr.append(" LIMIT 10 ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List<VagasVO> listaVagas = new ArrayList<VagasVO>(0);
        while (tabelaResultado.next()) {
            VagasVO vaga = new VagasVO();
            vaga.setCodigo(tabelaResultado.getInt("codigo"));
            vaga.setCargo(tabelaResultado.getString("cargo"));
            vaga.setVagaEstadoVOs(getFacadeFactory().getVagaEstadoFacade().consultarPorVaga(vaga.getCodigo()));
//            vaga.getCidade().getEstado().setSigla(tabelaResultado.getString("sigla"));
            listaVagas.add(vaga);
        }
        return listaVagas;
    }

    public List consultarPorAreaProfissional(String valorConsulta, String ordenacao, String situacao, Integer codigoParceiro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder("SELECT DISTINCT vagas.*, parceiro.nome from vagas ");
        sb.append(" INNER JOIN areaProfissional ON areaProfissional.codigo = vagas.areaProfissional");
        sb.append(" INNER JOIN parceiro ON parceiro.codigo = vagas.parceiro ");
        sb.append(" WHERE UPPER(areaProfissional.descricaoAreaProfissional) LIKE('").append(valorConsulta.toUpperCase()).append("%') AND parceiro.participabancocurriculo = true ");
        if (situacao != null && !situacao.equals("")) {
            sb.append(" AND situacao ='").append(situacao).append("' ");
        }
        if (codigoParceiro != null && !codigoParceiro.equals(0)) {
            sb.append(" AND parceiro ='").append(codigoParceiro).append("' ");
        }
        if (ordenacao.equals("data")) {
            sb.append(" ORDER BY vagas.dataativacao desc ");
        } else if (ordenacao.equals("codigoVaga")) {
            sb.append(" ORDER BY vagas.codigo ");
        } else {
            sb.append(" ORDER BY parceiro.nome ");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorCargo(String valorConsulta, String ordenacao, String situacao, Integer codigoParceiro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder("SELECT Vagas.*, parceiro.nome FROM Vagas INNER JOIN parceiro ON parceiro.codigo = vagas.parceiro WHERE upper(cargo) like('" + valorConsulta.toUpperCase() + "%')");
        if (situacao != null && !situacao.equals("")) {
            sb.append(" AND situacao ='").append(situacao).append("' ");
        }
        if (codigoParceiro != null && !codigoParceiro.equals(0)) {
            sb.append(" AND parceiro ='").append(codigoParceiro).append("' ");
        }
        if (ordenacao.equals("data")) {
            sb.append(" ORDER BY vagas.dataativacao desc ");
        } else if (ordenacao.equals("codigoVaga")) {
            sb.append(" ORDER BY vagas.codigo ");
        } else {
            sb.append(" ORDER BY parceiro.nome ");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorSalario(String valorConsulta, String ordenacao, String situacao, Integer codigoParceiro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder("SELECT Vagas.* FROM Vagas INNER JOIN parceiro ON parceiro.codigo = vagas.parceiro WHERE upper(salario) like('" + valorConsulta.toUpperCase() + "%') ");
        if (situacao != null && !situacao.equals("")) {
            sb.append(" AND situacao ='").append(situacao).append("' ");
        }
        if (codigoParceiro != null && !codigoParceiro.equals(0)) {
            sb.append(" AND parceiro ='").append(codigoParceiro).append("' ");
        }
        if (ordenacao.equals("data")) {
            sb.append(" ORDER BY vagas.dataativacao desc ");
        } else if (ordenacao.equals("codigoVaga")) {
            sb.append(" ORDER BY vagas.codigo ");
        } else {
            sb.append(" ORDER BY parceiro.nome ");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorSituacao(String valorConsulta, String ordenacao, Integer codigoParceiro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder("SELECT Vagas.* FROM Vagas INNER JOIN parceiro ON parceiro.codigo = vagas.parceiro WHERE upper(situacao) like('" + valorConsulta.toUpperCase() + "%') ");
        if (codigoParceiro != null && !codigoParceiro.equals(0)) {
            sb.append(" AND parceiro ='").append(codigoParceiro).append("' ");
        }
        if (ordenacao.equals("data")) {
            sb.append(" ORDER BY vagas.dataativacao desc ");
        } else if (ordenacao.equals("codigoVaga")) {
            sb.append(" ORDER BY vagas.codigo ");
        } else {
            sb.append(" ORDER BY parceiro.nome ");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorNumeroVagas(Integer valorConsulta, String ordenacao, String situacao, Integer codigoParceiro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder("SELECT Vagas.* FROM Vagas INNER JOIN parceiro ON parceiro.codigo = vagas.parceiro WHERE numeroVagas = " + valorConsulta + "");
        if (situacao != null && !situacao.equals("")) {
            sb.append(" AND situacao ='").append(situacao).append("' ");
        }
        if (codigoParceiro != null && !codigoParceiro.equals(0)) {
            sb.append(" AND parceiro ='").append(codigoParceiro).append("' ");
        }
        if (ordenacao.equals("data")) {
            sb.append(" ORDER BY vagas.dataativacao desc ");
        } else if (ordenacao.equals("codigoVaga")) {
            sb.append(" ORDER BY vagas.codigo ");
        } else {
            sb.append(" ORDER BY parceiro.nome ");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List<VagasVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<VagasVO> vetResultado = new ArrayList<VagasVO>(0);
        while (tabelaResultado.next()) {
            //VagasVO obj = new VagasVO();
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    public VagasVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        VagasVO obj = new VagasVO();
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.getParceiro().setCodigo(dadosSQL.getInt("parceiro"));
        montarDadosParceiro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        obj.setCargo(dadosSQL.getString("cargo"));
        obj.setDataAtivacao(dadosSQL.getDate("dataAtivacao"));
        obj.getAreaProfissional().setCodigo(dadosSQL.getInt("areaProfissional"));
        montarDadosAreaProfissional(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        obj.setNumeroVagas(dadosSQL.getInt("numeroVagas"));
        montarDadosQuantidadeCandidatos(obj);
        obj.setEmpresaSigilosaParaVaga(dadosSQL.getBoolean("empresaSigilosaParaVaga"));
        obj.setSuperiorImediato(dadosSQL.getString("superiorImediato"));
        obj.getCidade().setCodigo(dadosSQL.getInt("cidade"));
        montarDadosCidade(obj, nivelMontarDados, usuario);
//        obj.getEstado().setCodigo(dadosSQL.getInt("estado"));
//        montarDadosEstado(obj, nivelMontarDados, usuario);
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setDataAlteracao(dadosSQL.getDate("dataAlteracao"));
        obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
        obj.setVagaEstadoVOs(getFacadeFactory().getVagaEstadoFacade().consultarPorVaga(obj.getCodigo()));
        obj.setVagaAreaVOs(getFacadeFactory().getVagaAreaFacade().consultarPorVaga(obj.getCodigo()));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        obj.setHorarioTrabalho(dadosSQL.getString("horarioTrabalho"));
        obj.setSalario(dadosSQL.getString("salario"));
        obj.setBeneficios(dadosSQL.getString("beneficios"));
        obj.setCLT(dadosSQL.getBoolean("CLT"));
        obj.setEstagio(dadosSQL.getBoolean("estagio"));
        obj.setHorista(dadosSQL.getBoolean("horista"));
        obj.setTemporario(dadosSQL.getBoolean("temporario"));
        obj.setOutro(dadosSQL.getBoolean("outro"));
        obj.setMensalista(dadosSQL.getBoolean("mensalista"));
        obj.setTransferencia(dadosSQL.getBoolean("transferencia"));
        obj.setViagem(dadosSQL.getBoolean("viagem"));
        obj.setMudanca(dadosSQL.getBoolean("mudanca"));
        obj.setEscolaridadeRequerida(dadosSQL.getString("escolaridadeRequerida"));
        obj.setConhecimentoEspecifico(dadosSQL.getString("conhecimentoEspecifico"));
        obj.setPrincipalAtividadeCargo(dadosSQL.getString("principalAtividadeCargo"));
        obj.setNecessitaVeiculo(dadosSQL.getBoolean("necessitaVeiculo"));
        obj.setTipoVeiculo(dadosSQL.getString("tipoVeiculo"));
        obj.setTransferencia(dadosSQL.getBoolean("transferencia"));
        obj.setViagem(dadosSQL.getBoolean("viagem"));
        obj.setMudanca(dadosSQL.getBoolean("mudanca"));
        obj.setCaracteristicaPessoalImprescindivel(dadosSQL.getString("caracteristicaPessoalImprescindivel"));
        obj.setDeclaracaoValidade(dadosSQL.getBoolean("declaracaoValidade"));
        obj.setIngles(dadosSQL.getBoolean("ingles"));
        obj.setEspanhol(dadosSQL.getBoolean("espanhol"));
        obj.setFrances(dadosSQL.getBoolean("frances"));
        obj.setInglesNivel(dadosSQL.getString("inglesNivel"));
        obj.setEspanholNivel(dadosSQL.getString("espanholNivel"));
        obj.setFrancesNivel(dadosSQL.getString("francesNivel"));
        obj.setWindows(dadosSQL.getBoolean("windows"));
        obj.setWord(dadosSQL.getBoolean("word"));
        obj.setExcel(dadosSQL.getBoolean("excel"));
        obj.setAccess(dadosSQL.getBoolean("access"));
        obj.setPowerPoint(dadosSQL.getBoolean("powerPoint"));
        obj.setInternet(dadosSQL.getBoolean("internet"));
        obj.setSap(dadosSQL.getBoolean("sap"));
        obj.setCorelDraw(dadosSQL.getBoolean("corelDraw"));
        obj.setAutoCad(dadosSQL.getBoolean("autoCad"));
        obj.setPhotoshop(dadosSQL.getBoolean("photoshop"));
        obj.setMicrosiga(dadosSQL.getBoolean("microsiga"));
        obj.setOutrosSoftwares(dadosSQL.getString("outrosSoftwares"));
        obj.setMotivoEncerramento(dadosSQL.getString("motivoEncerramento"));
        obj.setAlunoContratado(dadosSQL.getString("alunoContratado"));
        obj.setQtdDiasExpiracaoVaga(dadosSQL.getInt("qtdDiasExpiracaoVaga"));		
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setVagaContatoVOs(getFacadeFactory().getVagaContatoFacade().consultarPorVaga(obj.getCodigo()));
        return obj;
    }

    public void montarDadosQuantidadeCandidatos(VagasVO obj) throws Exception {
        obj.setQuantidadeCandidatos(consultarQuantidadeCandidatos(obj.getCodigo()));
    }

    public static void montarDadosParceiro(VagasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getParceiro().getCodigo().intValue() == 0) {
            obj.setParceiro(new ParceiroVO());
            return;
        }

        obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiro().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosCidade(VagasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCidade().getCodigo().intValue() == 0) {
            obj.setCidade(new CidadeVO());
            return;
        }

        obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
    }
//
//    public static void montarDadosEstado(VagasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        if (obj.getEstado().getCodigo().intValue() == 0) {
//            obj.setEstado(new EstadoVO());
//            return;
//        }
//
//        obj.setEstado(getFacadeFactory().getEstadoFacade().consultarPorChavePrimaria(obj.getEstado().getCodigo(), nivelMontarDados, usuario));
//    }
//
    public static void montarDadosAreaProfissional(VagasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getAreaProfissional().getCodigo().intValue() == 0) {
            obj.setAreaProfissional(new AreaProfissionalVO());
            return;
        }

        obj.setAreaProfissional(getFacadeFactory().getAreaProfissionalFacade().consultarPorChavePrimaria(obj.getAreaProfissional().getCodigo(), false, nivelMontarDados, usuario));
    }

    public VagasVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM Vagas WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Vagas).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public Integer consultarQuantidadeCandidatos(Integer codigoPrm) {
        String sql = "SELECT count(codigo) AS quantidadeCandidatos FROM candidatosvagas WHERE vaga = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        tabelaResultado.next();
        return tabelaResultado.getInt("quantidadeCandidatos");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarCandidatarVaga(final CandidatosVagasVO candidatosVagasVO, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO candidatosVagas(vaga, pessoa, situacaoReferenteVaga ) VALUES (?,?, ?) returning codigo";
            candidatosVagasVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, candidatosVagasVO.getVaga().getCodigo());
                    sqlInserir.setInt(2, candidatosVagasVO.getPessoa().getCodigo());
                    sqlInserir.setString(3, SituacaoReferenteVagaEnum.NENHUM.toString());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        candidatosVagasVO.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            getFacadeFactory().getCandidatoVagaQuestaoFacade().incluirCandidatoVagaQuestao(candidatosVagasVO);
            candidatosVagasVO.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            candidatosVagasVO.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarSairVaga(final VagasVO obj, final PessoaVO pessoa, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "DELETE FROM candidatosVagas where vaga = ? and pessoa = ?";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo(), pessoa.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public Boolean consultarExistenciaCandidatosVagas(Integer codigoVaga, Integer codigoPessoa) throws Exception {
        String sqlStr = "SELECT * from candidatosVagas WHERE vaga = " + codigoVaga + " AND pessoa = " + codigoPessoa;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return tabelaResultado.next();
    }

    public Integer consultarQuantidadeEmpresasVagasAbertas() throws Exception {
        String sql = "select count(parceiro.codigo) as qtdEmpresasVagasAberta from parceiro where codigo in ("
                + "select vagas.parceiro from vagas "
                + "inner join parceiro on parceiro.codigo = vagas.parceiro "
                + "where "
                + "situacao = 'AT' and "
                + "parceiro.participabancocurriculum  = true "
                + "group by vagas.parceiro)";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return (tabelaResultado.getInt("qtdEmpresasVagasAberta"));
    }

    public Integer consultarQuantidadeVagasAbertas() throws Exception {
        String sql = "select count(codigo) as qtdVagasAbertas from vagas where situacao = 'AT'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return (tabelaResultado.getInt("qtdVagasAbertas"));
    }

    public Integer consultarQuantidadeVagasInativas() throws Exception {
        String sql = "select count(codigo) as qtdVagasAbertas from vagas where situacao = 'CA'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return (tabelaResultado.getInt("qtdVagasAbertas"));
    }

    public Integer consultarQuantidadeVagasEncerradas() throws Exception {
        String sql = "select count(codigo) as qtdVagasAbertas from vagas where situacao = 'EN'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return (tabelaResultado.getInt("qtdVagasAbertas"));
    }

    public Integer consultarQuantidadeVagasExpiradas() throws Exception {
        String sql = "select count(codigo) as qtdVagasAbertas from vagas where situacao = 'EX'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return (tabelaResultado.getInt("qtdVagasAbertas"));
    }

    public Integer consultarQuantidadeVagasSobAnalise() throws Exception {
        String sql = "select count(codigo) as qtdVagasAbertas from vagas where situacao <> 'AT' and situacao <> 'CA' and situacao <> 'EX' and situacao <> 'EN'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return (tabelaResultado.getInt("qtdVagasAbertas"));
    }

    public Integer consultarQuantidadeAlunosSelecionados() throws Exception {
        String sql = "select count(t) as qtdAlunosSelecionados from (select distinct pessoa from candidatosvagas  where situacaoreferentevaga ilike 'PROCESSO_SELETIVO') as t";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return (tabelaResultado.getInt("qtdAlunosSelecionados"));
    }

    public Integer consultarQuantidadeAlunosContratado() throws Exception {
        String sql = "select count(t) as qtdAlunosSelecionados from (select distinct pessoa from candidatosvagas  where situacaoreferentevaga ilike 'SELECIONADO') as t";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return (tabelaResultado.getInt("qtdAlunosSelecionados"));
    }

    public List consultarVagasExpiramQtdDias(Integer dia, UsuarioVO usu) throws Exception {
        Date dataAtivacao = Uteis.obterDataAntiga(new Date(), dia);
//        String sql = "select * from vagas where situacao = 'AT' and dataAtivacao < ?";
        String sql = "select * from vagas where situacao = 'AT' and dataAtivacao < (current_date - qtdDiasExpiracaoVaga)";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{Uteis.getDataJDBC(dataAtivacao)});
        return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usu));
    }

    public List consultarVagasParaNotificacaoDeExpiracaoBaseadoDiasParamento(Integer dia, UsuarioVO usu) throws Exception {
        //Date dataAtivacao = Uteis.obterDataAntiga(new Date(), dia);
//        String sql = "select * from vagas where situacao = 'AT' and dataAtivacao < ?";
        String sql = "select * from vagas where situacao = 'AT' and dataAtivacao < (current_date - (qtdDiasExpiracaoVaga - ?))";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{dia});
        return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usu));
    }
	
    public String consultarDataUltimoCadastro() throws Exception {
        String sql = "select dataCadastro from vagas where situacao = 'AT' order by codigo desc limit 1";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            return "";
        }
        return (Uteis.getData(tabelaResultado.getDate("dataCadastro")));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAlteracaoCandidatarVaga(final CandidatosVagasVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getIsCandidatoSelecionado()) {
            realizarValidacaoVagaDisponivel(obj.getVaga());
        }
        try {
            final String sql = "UPDATE candidatosVagas SET situacaoReferenteVaga=?  where codigo = ?";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getSituacaoReferenteVaga().toString());
                    sqlAlterar.setInt(2, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    public void realizarValidacaoVagaDisponivel(VagasVO vaga) throws ConsistirException {
        Integer qtdAlunosSelecionados = 0;
        String sqlSelect = "select count(codigo) as qtdAlunosSelecionados from candidatosvagas  where situacaoreferentevaga ilike 'SELECIONADO' and vaga = " + vaga.getCodigo();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlSelect);
        if (tabelaResultado.next()) {
            qtdAlunosSelecionados = (tabelaResultado.getInt("qtdAlunosSelecionados"));
        }
        if (qtdAlunosSelecionados >= vaga.getNumeroVagas()) {
            throw new ConsistirException("A(s) vaga(s) disponíveis já foram ocupadas.");
        }
    }

    public void realizarNavegacaoParaVisualizarCandidatos(List<CandidatosVagasVO> listaCandidatosVagasVOs, VagasVO vaga) throws Exception {
        listaCandidatosVagasVOs.clear();
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" SELECT cv.codigo as cv_codigo, cv.pessoa, cv.situacaoReferenteVaga, ");
        sqlStr.append(" pessoa.nome, pessoa.cpf, pessoa.email, ");
        sqlStr.append(" extract(year from age(pessoa.datanasc)) as idade, ");
        sqlStr.append(" matricula.situacao,  curso.nome as curso_nome, case when (select count(CurriculumPessoa.codigo) from CurriculumPessoa where CurriculumPessoa.pessoa = pessoa.codigo ) > 0 then true else false end as existeArquivoAdicional ");
        sqlStr.append(" from candidatosVagas as cv ");
        sqlStr.append(" INNER JOIN pessoa on pessoa.codigo = cv.pessoa ");
        sqlStr.append(" INNER JOIN matricula on matricula.aluno = pessoa.codigo ");
        sqlStr.append(" INNER JOIN curso on curso.codigo = matricula.curso ");
        sqlStr.append(" WHERE cv.vaga = ").append(vaga.getCodigo());
        sqlStr.append(" and (matricula.data, matricula.aluno ) IN( SELECT MAX(m1.data), m1.aluno from matricula m1 where m1.aluno = matricula.aluno ");
        sqlStr.append(" group by m1.aluno ");
        sqlStr.append(" ) ");
        sqlStr.append(" group by ");
        sqlStr.append(" cv.codigo, cv.pessoa, cv.situacaoReferenteVaga, pessoa.codigo,  ");
        sqlStr.append(" pessoa.nome, pessoa.cpf, extract(year from age(pessoa.datanasc)),  ");
        sqlStr.append(" matricula.situacao, curso.nome, pessoa.email ");
        sqlStr.append(" order by pessoa.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            CandidatosVagasVO obj = new CandidatosVagasVO();
            obj.setCodigo((tabelaResultado.getInt("cv_codigo")));
            obj.getPessoa().setCodigo((tabelaResultado.getInt("pessoa")));
            obj.getPessoa().setNome((tabelaResultado.getString("nome")));
            obj.getPessoa().setEmail((tabelaResultado.getString("email")));
            obj.getPessoa().setCPF((tabelaResultado.getString("cpf")));
            obj.setIdade(new Double(tabelaResultado.getDouble("idade")).intValue());
            obj.setNomeCurso((tabelaResultado.getString("curso_nome")));
            obj.setSituacaoCurso((tabelaResultado.getString("situacao")));
            obj.setExisteArquivoAdicional(tabelaResultado.getBoolean("existeArquivoAdicional"));
            obj.setVaga(vaga);

            if (tabelaResultado.getString("situacaoReferenteVaga") == null) {
                obj.setSituacaoReferenteVaga(SituacaoReferenteVagaEnum.NENHUM);
            } else {
                obj.setSituacaoReferenteVaga(SituacaoReferenteVagaEnum.valueOf(tabelaResultado.getString("situacaoReferenteVaga")));
            }

            listaCandidatosVagasVOs.add(obj);
            obj = null;
        }
    }

    public List<VagasVO> consultaRapidaBuscaVaga(VagasVO obj, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT vagas.* FROM vagas ");
        sqlStr.append("LEFT JOIN AreaProfissional ON vagas.AreaProfissional = AreaProfissional.codigo ");

        sqlStr.append(" WHERE vagas.situacao = 'AT' ");

        if (obj.getParceiro() != null && !obj.getParceiro().getCodigo().equals(0)) {
            sqlStr.append(" AND parceiro =").append(obj.getParceiro().getCodigo());
        }
        if (!obj.getCargo().equals("")) {
            sqlStr.append(" AND cargo ilike ('%").append(obj.getCargo()).append("%') ");
        }
        if (obj.getNecessitaVeiculo()) {
            sqlStr.append(" AND tipoVeiculo ='").append(obj.getTipoVeiculo()).append("' ");
        }
        if (obj.getTransferencia()) {
            sqlStr.append(" AND transferencia ='").append(obj.getTransferencia()).append("' ");
        }
        if (obj.getViagem()) {
            sqlStr.append(" AND viagem ='").append(obj.getViagem()).append("' ");
        }
        if (obj.getMudanca()) {
            sqlStr.append(" AND mudanca ='").append(obj.getMudanca()).append("' ");
        }
        if (!obj.getSalario().equals("") && obj.getSalario().equals("todos")) {
            sqlStr.append(" AND salario ='").append(obj.getSalario()).append("' ");
        }
        if (obj.getCLT()) {
            sqlStr.append(" AND clt ='").append(obj.getCLT()).append("' ");
        }
        if (obj.getEstagio()) {
            sqlStr.append(" AND estagio ='").append(obj.getEstagio()).append("' ");
        }
        if (obj.getMensalista()) {
            sqlStr.append(" AND mensalista ='").append(obj.getMensalista()).append("' ");
        }
        if (obj.getTemporario()) {
            sqlStr.append(" AND temporario ='").append(obj.getTemporario()).append("' ");
        }
        if (obj.getOutro()) {
            sqlStr.append(" AND outro ='").append(obj.getOutro()).append("' ");
        }
        if (obj.getIngles()) {
            sqlStr.append(" AND ingles =").append(obj.getIngles());
            if (!obj.getInglesNivel().equals("")) {
                sqlStr.append(" AND inglesNivel ='").append(obj.getInglesNivel()).append("' ");
            }
        }
        if (obj.getEspanhol()) {
            sqlStr.append(" AND espanhol =").append(obj.getEspanhol());
            if (!obj.getEspanholNivel().equals("")) {
                sqlStr.append(" AND espanholNivel ='").append(obj.getEspanholNivel()).append("' ");
            }
        }
        if (obj.getFrances()) {
            sqlStr.append(" AND frances =").append(obj.getFrances());
            if (!obj.getFrancesNivel().equals("")) {
                sqlStr.append(" AND francesNivel ='").append(obj.getFrancesNivel()).append("' ");
            }
        }
        if (obj.getWindows()) {
            sqlStr.append(" AND windows =").append(obj.getWindows());
        }
        if (obj.getWord()) {
            sqlStr.append(" AND word =").append(obj.getWord());
        }
        if (obj.getExcel()) {
            sqlStr.append(" AND excel =").append(obj.getExcel());
        }
        if (obj.getAccess()) {
            sqlStr.append(" AND access =").append(obj.getAccess());
        }
        if (obj.getPowerPoint()) {
            sqlStr.append(" AND powerPoint =").append(obj.getPowerPoint());
        }
        if (obj.getInternet()) {
            sqlStr.append(" AND internet =").append(obj.getInternet());
        }
        if (obj.getSap()) {
            sqlStr.append(" AND sap =").append(obj.getSap());
        }
        if (obj.getCorelDraw()) {
            sqlStr.append(" AND corelDraw =").append(obj.getCorelDraw());
        }
        if (obj.getAutoCad()) {
            sqlStr.append(" AND autoCad =").append(obj.getAutoCad());
        }
        if (obj.getPhotoshop()) {
            sqlStr.append(" AND photoshop =").append(obj.getPhotoshop());
        }
        if (obj.getMicrosiga()) {
            sqlStr.append(" AND microsiga =").append(obj.getMicrosiga());
        }

        if (!obj.getListaAreaProfissional().isEmpty()) {
            sqlStr.append(" AND (");
            String or = "OR ";
            int tamanhoLista = obj.getListaAreaProfissional().size();
            int aux = 1;
            for (AreaProfissionalVO ap : obj.getListaAreaProfissional()) {
                if (aux == tamanhoLista) {
                    or = "";
                }
                sqlStr.append(" areaProfissional.descricaoAreaProfissional ilike'%").append(ap.getDescricaoAreaProfissional()).append("%' ").append(or);
                aux++;
            }
            sqlStr.append(") ");
        }

        if (!obj.getSalario().equals("")) {
            sqlStr.append(" AND vagas.salario ilike'").append(obj.getSalario()).append("' ");
        }

        sqlStr.append(" ORDER BY cargo ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
    }

    public List<VagasVO> consultaRapidaBuscaVagaVisaoAluno(VagasVO obj, UsuarioVO usuario) throws Exception {
        return consultaRapidaBuscaVagaVisaoAluno(obj, null, usuario);
    }

    public List<VagasVO> consultaRapidaBuscaVagaVisaoAluno(VagasVO obj, List listaAreaProfissional, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT distinct vagas.* FROM vagas ");
        sqlStr.append("LEFT JOIN vagaarea on vagaarea.vaga = vagas.codigo ");
        sqlStr.append("LEFT JOIN AreaProfissional ON vagaarea.AreaProfissional = AreaProfissional.codigo ");
        sqlStr.append("LEFT JOIN parceiro ON vagas.parceiro = parceiro.codigo ");
        //sqlStr.append("LEFT JOIN cidade ON vagas.cidade = cidade.codigo ");
        sqlStr.append("INNER JOIN vagaestado on vagaestado.vaga = vagas.codigo ");
        sqlStr.append("INNER JOIN estado ON vagaestado.estado = estado.codigo ");

        sqlStr.append(" WHERE vagas.situacao = 'AT' ");

        if (obj.getParceiro().getNome() != null && !obj.getParceiro().getNome().equals("")) {
            sqlStr.append(" AND parceiro.nome ilike '").append(obj.getParceiro().getNome()).append("%' and (vagas.empresasigilosaparavaga = 'false' or vagas.empresasigilosaparavaga is null)");
        }
        if (!obj.getCargo().equals("")) {
            sqlStr.append(" AND cargo ilike ('%").append(obj.getCargo()).append("%') ");
        }

        if (!obj.getCodigo().equals(0)) {
            sqlStr.append(" AND vagas.codigo = ").append(obj.getCodigo()).append(" ");
        }

        if (listaAreaProfissional != null && !listaAreaProfissional.isEmpty()) {
            sqlStr.append(" AND  areaProfissional.codigo in (");
            int tamanho = listaAreaProfissional.size();
            for (int x = 0; x < tamanho; x++) {
                AreaProfissionalInteresseContratacaoVO area = (AreaProfissionalInteresseContratacaoVO) listaAreaProfissional.get(x);
                if ((tamanho - 1) != x) {
                    sqlStr.append(area.getAreaProfissional().getCodigo() + ",");
                }
                if ((tamanho - 1) == x) {
                    sqlStr.append(area.getAreaProfissional().getCodigo() + ")");
                }
            }
        } else {
            if (obj.getAreaProfissional().getCodigo() != 0) {
                sqlStr.append(" AND areaProfissional.codigo =").append(obj.getAreaProfissional().getCodigo()).append(" ");
            }
        }

        if (!obj.getSalario().equals("")) {
            sqlStr.append(" AND vagas.salario ilike'").append(obj.getSalario()).append("' ");
        }

        if (obj.getNumeroVagas() != null && obj.getNumeroVagas() > 0) {
            sqlStr.append(" AND vagas.numeroVagas = ").append(obj.getNumeroVagas()).append(" ");
        }

        if (!obj.getEstado().getCodigo().equals(0)) {
            sqlStr.append(" AND estado.codigo = ").append(obj.getEstado().getCodigo());
        }

//        if (!obj.getCidade().getCodigo().equals(0)) {
//            sqlStr.append(" AND cidade.codigo = ").append(obj.getCidade().getCodigo());
//        }

        sqlStr.append(" ORDER BY cargo ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
    }

    public List<VagasVO> consultarListaVagasPorAluno(Integer codigoPessoa, UsuarioVO usuarioLogado) throws Exception {
        StringBuilder sql = new StringBuilder();
        try {
            sql.append(" select matricula.matricula, vagas.* from pessoa ");
            sql.append("  inner join matricula on matricula.aluno = pessoa.codigo");
            sql.append("  inner join candidatosvagas on candidatosvagas.pessoa = pessoa.codigo");
            sql.append(" inner join vagas on vagas.codigo = candidatosvagas.vaga ");
            sql.append(" where pessoa.codigo = '");
            sql.append(codigoPessoa);
            sql.append("' ");
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
            return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void alterarOrdemVagaQuestao(VagasVO vagasVO, VagaQuestaoVO vagaQuestaoVO, VagaQuestaoVO vagaQuestaoVO2) throws Exception {
        Integer ordem1 = vagaQuestaoVO.getOrdemApresentacao();
        vagaQuestaoVO.setOrdemApresentacao(vagaQuestaoVO2.getOrdemApresentacao());
        vagaQuestaoVO2.setOrdemApresentacao(ordem1);
        Ordenacao.ordenarLista(vagasVO.getVagaQuestaoVOs(), "ordemApresentacao");
    }

    @Override
    public void adicionarVagaQuestao(VagasVO vagasVO, VagaQuestaoVO vagaQuestaoVO) throws Exception {
        getFacadeFactory().getVagaQuestaoFacade().validarDados(vagaQuestaoVO);
        if (vagaQuestaoVO.getOrdemApresentacao() > 0) {
            vagasVO.getVagaQuestaoVOs().set(vagaQuestaoVO.getOrdemApresentacao() - 1, vagaQuestaoVO);
        } else {

            if (!vagaQuestaoVO.getTipoVagaQuestao().equals(TipoVagaQuestaoEnum.TEXTUAL)) {
                for (OpcaoRespostaVagaQuestaoVO opcaoRespostaVagaQuestaoVO : vagaQuestaoVO.getOpcaoRespostaVagaQuestaoVOs()) {
                    getFacadeFactory().getOpcaoRespostaVagaQuestaoFacade().validarDados(opcaoRespostaVagaQuestaoVO);
                }
            }
            vagaQuestaoVO.setOrdemApresentacao(vagasVO.getVagaQuestaoVOs().size() + 1);
            vagasVO.getVagaQuestaoVOs().add(vagaQuestaoVO);
        }
    }

    @Override
    public void removerVagaQuestao(VagasVO vagasVO, VagaQuestaoVO vagaQuestaoVO) throws Exception {
        if (vagaQuestaoVO.getOrdemApresentacao() > 0) {
            vagasVO.getVagaQuestaoVOs().remove(vagaQuestaoVO.getOrdemApresentacao() - 1);
            int x = 1;
            for (VagaQuestaoVO vagaQuestaoVOs : vagasVO.getVagaQuestaoVOs()) {
                vagaQuestaoVOs.setOrdemApresentacao(x++);
            }
        }
    }

    @Override
    public void adicionarVagaContato(VagasVO vagasVO, VagaContatoVO vagaContatoVO) throws Exception {
        getFacadeFactory().getVagaContatoFacade().validarDados(vagaContatoVO);
        for (VagaContatoVO contato : vagasVO.getVagaContatoVOs()) {
            if (contato.getEmail().trim().equals(vagaContatoVO.getEmail().trim())) {
                throw new Exception("Contato já adicionado.");
            }
        }
        vagasVO.getVagaContatoVOs().add(vagaContatoVO);
    }

    @Override
    public void removerVagaContato(VagasVO vagasVO, VagaContatoVO vagaContatoVO) throws Exception {
        int x = 0;
        for (VagaContatoVO contato : vagasVO.getVagaContatoVOs()) {
            if (contato.getEmail().trim().equals(vagaContatoVO.getEmail().trim())) {
                vagasVO.getVagaContatoVOs().remove(x);
                return;
            }
            x++;
        }
    }

    public void adicionarVagaEstado(VagasVO vagasVO, VagaEstadoVO vagaEstadoVO) throws Exception {
        getFacadeFactory().getVagaEstadoFacade().validarDados(vagaEstadoVO);
        for (VagaEstadoVO estado : vagasVO.getVagaEstadoVOs()) {
            if (estado.getCodigo().intValue() == vagaEstadoVO.getEstado().getCodigo().intValue()) {
                throw new Exception("Estado já adicionado.");
            }
        }
        vagasVO.getVagaEstadoVOs().add(vagaEstadoVO);
    }

    public void removerVagaEstado(VagasVO vagasVO, VagaEstadoVO vagaEstadoVO) throws Exception {
        int x = 0;
        for (VagaEstadoVO estado : vagasVO.getVagaEstadoVOs()) {
            if (estado.getCodigo().intValue() == vagaEstadoVO.getEstado().getCodigo().intValue()) {
                vagasVO.getVagaEstadoVOs().remove(x);
                return;
            }
            x++;
        }
    }
}
