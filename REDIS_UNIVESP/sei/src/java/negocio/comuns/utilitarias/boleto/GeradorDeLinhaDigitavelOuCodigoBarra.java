package negocio.comuns.utilitarias.boleto;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.utilitarias.Uteis;

public class GeradorDeLinhaDigitavelOuCodigoBarra {

	public void geraCodigoBarraApartirLinhaDigitavel(ContaPagarVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoLancamentoContaPagar()), "O campo Tipo Lançamento deve ser informado");
		if (obj.getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra()
				//|| obj.getTipoLancamentoContaPagar().name().equals(TipoLancamentoContaPagarEnum.BRADESCO_PGCONTAS_TRIBUTOS.name())
				) {
			ContaPagarVO.validarDadosSegmentoO(obj);
			ContaPagarVO.validarPreenchimentoCorretoCamposLinhaDigitavelDadosSegmentoO(obj);
			obj.setCodigoBarra(obj.getLinhaDigitavel1().substring(0, 11) + obj.getLinhaDigitavel2().substring(0, 11) + obj.getLinhaDigitavel3().substring(0, 11) + obj.getLinhaDigitavel4().substring(0, 11));
			obj.setValor(Uteis.getValorDoubleComCasasDecimais(obj.getLinhaDigitavel1().substring(4, 11) + obj.getLinhaDigitavel2().substring(0, 4)));
//			
//			Calendar dataBase = new GregorianCalendar(1997, Calendar.OCTOBER, 7, 0, 0);	
//			dataBase.add(Calendar.DATE, new Integer(obj.getLinhaDigitavel4().substring(0, 4)));
//			obj.setDataVencimento(dataBase.getTime());	
			
			//obj.setDataVencimento(Uteis.getObterDataFatorVencimento(new Integer(obj.getLinhaDigitavel4().substring(0, 4))));			
		} else if (obj.getTipoLancamentoContaPagar().isLiquidacaoTituloCarteiraCobrancaSantander() || obj.getTipoLancamentoContaPagar().isLiquidacaoTituloCarteiraCobrancaSicoob()  || obj.getTipoLancamentoContaPagar().isLiquidacaoTituloOutroBanco() /*|| obj.getTipoLancamentoContaPagar().name().equals(TipoLancamentoContaPagarEnum.BRADESCO_TITULOTERCEIROS.name())*/) {
			ContaPagarVO.validarDadosSegmentoJ(obj);
			ContaPagarVO.validarPreenchimentoCorretoCamposLinhaDigitavelDadosSegmentoJ(obj);
			String codigoBancoDestino = obj.getLinhaDigitavel1().substring(0, 3);
			String codigoMoeda = obj.getLinhaDigitavel1().substring(3, 4);
			String fatorVencimentoValor = obj.getLinhaDigitavel8().length() < 14 ? Uteis.preencherComZerosPosicoesVagas(obj.getLinhaDigitavel8(), 14) : obj.getLinhaDigitavel8();
			String campoLivre = obj.getLinhaDigitavel1().substring(4, 5) + obj.getLinhaDigitavel2().substring(0, 4) + obj.getLinhaDigitavel3() + obj.getLinhaDigitavel4().substring(0, 5) + obj.getLinhaDigitavel5() + obj.getLinhaDigitavel6().substring(0, 5);
			obj.setCodigoBarra(codigoBancoDestino + codigoMoeda + obj.getLinhaDigitavel7() + fatorVencimentoValor + campoLivre);
			obj.setValor(Uteis.getValorDoubleComCasasDecimais(fatorVencimentoValor.substring(4,14)));

			Calendar dataBase = new GregorianCalendar(1997, Calendar.OCTOBER, 7, 0, 0);	
			dataBase.add(Calendar.DATE, new Integer(fatorVencimentoValor.substring(0, 4)));
			obj.setDataVencimento(dataBase.getTime());
		}
	}

	/**
	 * Gera a linha digitavel do boleto de acordo com normas da carta circular n. 2926 do banco central do Brasil.
	 * Disponível para consulta em caelum.stella.boleto.doc
	 *
	 */
	public void geraLinhaDigitavelApartirCodigoBarra(ContaPagarVO obj ,Boolean validar)  {
		if(validar) {			
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoLancamentoContaPagar()), "O campo Tipo Lançamento deve ser informado");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getBancoRemessaPagar()) || !Uteis.isAtributoPreenchido(obj.getBancoRemessaPagar().getNrBanco()), "O campo Número do Banco Remessa deve ser informado");
			Uteis.checkState(obj.getCodigoBarra() == null || obj.getCodigoBarra().isEmpty(), "O código de barras deve ser informado");
			}
			Uteis.checkState(obj.getCodigoBarra().length() != 44, "O código de barras  precisa ter 44 digitos");

		GeradorDeDigito dvGenerator = null;
		if (Uteis.isAtributoPreenchido(obj.getBancoRemessaPagar()) && Uteis.isAtributoPreenchido(obj.getBancoRemessaPagar().getNrBanco()) && obj.getBancoRemessaPagar().getNrBanco().equals("033")) {
			dvGenerator = new GeradorDeDigitoSantander();
		} else {
			dvGenerator = new GeradorDeDigitoPadrao();
		}
		if ((!Uteis.isAtributoPreenchido(obj.getTipoLancamentoContaPagar()) && Uteis.isAtributoPreenchido(obj.getCodigoBarra()))||   obj.getTipoLancamentoContaPagar().isLiquidacaoTituloCarteiraCobrancaSantander() || obj.getTipoLancamentoContaPagar().isLiquidacaoTituloOutroBanco() ||
				obj.getTipoLancamentoContaPagar().isLiquidacaoTituloCarteiraCobrancaSicoob()) {
			gerarLinhaBoletoBancario(obj, dvGenerator);
			
		} else if (obj.getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra()) {
			gerarLinhaConcessionarias(obj, dvGenerator);
		}
	}
	

	private void gerarLinhaBoletoBancario(ContaPagarVO obj, GeradorDeDigito dvGenerator) {
		StringBuilder builder = new StringBuilder();
		builder.append(obj.getCodigoBarra().substring(0, 3));
		builder.append(obj.getCodigoBarra().substring(3, 4));
		builder.append(obj.getCodigoBarra().substring(19, 24));
		builder.append(dvGenerator.geraDigitoMod10(builder.toString()));
		builder.append(obj.getCodigoBarra().substring(24, 34));
		builder.append(dvGenerator.geraDigitoMod10(builder.substring(10, 20)));
		builder.append(obj.getCodigoBarra().substring(34));
		builder.append(dvGenerator.geraDigitoMod10(builder.substring(21, 31)));
		builder.append(obj.getCodigoBarra().substring(4, 5));
		builder.append(obj.getCodigoBarra().substring(5, 9));
		builder.append(obj.getCodigoBarra().substring(9, 19));

		obj.setLinhaDigitavel1(builder.substring(0, 5));
		obj.setLinhaDigitavel2(builder.substring(5, 10));
		obj.setLinhaDigitavel3(builder.substring(10, 15));
		obj.setLinhaDigitavel4(builder.substring(15, 21));
		obj.setLinhaDigitavel5(builder.substring(21, 26));
		obj.setLinhaDigitavel6(builder.substring(26, 32));
		obj.setLinhaDigitavel7(builder.substring(32, 33));
		obj.setLinhaDigitavel8(builder.substring(33, 47));
		obj.setValor(Uteis.getValorDoubleComCasasDecimais(obj.getLinhaDigitavel8().substring(4,14)));

	}

	private void gerarLinhaConcessionarias(ContaPagarVO obj, GeradorDeDigito dvGenerator) {
		StringBuilder builder = new StringBuilder();
		builder.append(obj.getCodigoBarra().substring(0, 11));
		builder.append(dvGenerator.geraDigitoBloco1(obj.getCodigoBarra().substring(0, 11)));
		builder.append(obj.getCodigoBarra().substring(11, 22));
		builder.append(dvGenerator.geraDigitoBloco2(obj.getCodigoBarra().substring(11, 22)));
		builder.append(obj.getCodigoBarra().substring(22, 33));
		builder.append(dvGenerator.geraDigitoBloco3(obj.getCodigoBarra().substring(22, 33)));
		builder.append(obj.getCodigoBarra().substring(33, 44));
		builder.append(dvGenerator.geraDigitoBloco4(obj.getCodigoBarra().substring(33, 44)));
		obj.setLinhaDigitavel1(builder.substring(0, 12));
		obj.setLinhaDigitavel2(builder.substring(12, 24));
		obj.setLinhaDigitavel3(builder.substring(24, 36));
		obj.setLinhaDigitavel4(builder.substring(36, 48));
		obj.setValor(Uteis.getValorDoubleComCasasDecimais(obj.getCodigoBarra().substring(4,15)));
	}

}
