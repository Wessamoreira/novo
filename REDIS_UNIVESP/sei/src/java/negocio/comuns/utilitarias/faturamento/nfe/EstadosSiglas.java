/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.faturamento.nfe;

/**
 *
 * @author Euripedes Doutor
 */
public class EstadosSiglas {

    public static final String RONDONIA = "RO";
    public static final String ACRE = "AC";
    public static final String AMAZONAS = "AM";
    public static final String RORAIMA = "RR";
    public static final String PARA = "PA";
    public static final String AMAPA = "AP";
    public static final String TOCANTINS = "TO";
    public static final String MARANHAO = "MA";
    public static final String PIAUI = "PI";
    public static final String CEARA = "CE";
    public static final String RIO_GRANDE_DO_NORTE = "RN";
    public static final String PARAIBA = "PB";
    public static final String PERNAMBUCO = "PE";
    public static final String ALAGOAS = "AL";
    public static final String SERGIPE = "SE";
    public static final String BAHIA = "BA";
    public static final String MINAS_GERAIS = "MG";
    public static final String ESPIRITO_SANTO = "ES";
    public static final String RIO_DE_JANEIRO = "RJ";
    public static final String SAO_PAULO = "SP";
    public static final String PARANA = "PR";
    public static final String SANTA_CATARINA = "SC";
    public static final String RIO_GRANDE_DO_SUL = "RS";
    public static final String MATO_GROSSO_DO_SUL = "MS";
    public static final String MATO_GROSSO = "MT";
    public static final String GOIAS = "GO";
    public static final String DISTRITO_FEDERAL = "DF";



    public static String obterSiglaEstado(String codigoIBGE) {
        if (codigoIBGE.equals(EstadosCodigoIBGE.ACRE.toString())) {
            return ACRE;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.ALAGOAS.toString())) {
            return ALAGOAS;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.AMAPA.toString())) {
            return AMAPA;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.AMAZONAS.toString())) {
            return AMAZONAS;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.BAHIA.toString())) {
            return BAHIA;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.CEARA.toString())) {
            return CEARA;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.DISTRITO_FEDERAL.toString())) {
            return DISTRITO_FEDERAL;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.ESPIRITO_SANTO.toString())) {
            return ESPIRITO_SANTO;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.GOIAS.toString())) {
            return GOIAS;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.MARANHAO.toString())) {
            return MARANHAO;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.MATO_GROSSO.toString())) {
            return MATO_GROSSO;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.MATO_GROSSO_DO_SUL.toString())) {
            return MATO_GROSSO_DO_SUL;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.MINAS_GERAIS.toString())) {
            return MINAS_GERAIS;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.PARA.toString())) {
            return PARA;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.PARAIBA.toString())) {
            return PARAIBA;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.PARANA.toString())) {
            return PARANA;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.PERNAMBUCO.toString())) {
            return PERNAMBUCO;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.PIAUI.toString())) {
            return PIAUI;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.RIO_DE_JANEIRO.toString())) {
            return RIO_DE_JANEIRO;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.RIO_GRANDE_DO_NORTE.toString())) {
            return RIO_GRANDE_DO_NORTE;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.RIO_GRANDE_DO_SUL.toString())) {
            return RIO_GRANDE_DO_SUL;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.RONDONIA.toString())) {
            return RONDONIA;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.RORAIMA.toString())) {
            return RORAIMA;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.SANTA_CATARINA.toString())) {
            return SANTA_CATARINA;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.SAO_PAULO.toString())) {
            return SAO_PAULO;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.SERGIPE.toString())) {
            return SERGIPE;
        } else if (codigoIBGE.equals(EstadosCodigoIBGE.TOCANTINS.toString())) {
            return TOCANTINS;
        }
        return codigoIBGE;
    }
}
