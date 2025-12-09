package negocio.comuns.utilitarias.boleto;

import java.util.LinkedList;

import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;

public class GeradorDeDigitoBanestes extends GeradorDeDigitoPadrao {
	
	private static final long serialVersionUID = 1L;

	@Override
	public int geraDigitoBloco1(String bloco) {
		return calculaDVBloco(bloco);
	}

	@Override
	public int geraDigitoBloco2(String bloco) {
		return calculaDVBloco(bloco);
	}

	@Override
	public int geraDigitoBloco3(String bloco) {
		return calculaDVBloco(bloco);
	}
	
	@Override
	public int geraDigitoBloco4(String bloco) {
		return calculaDVBloco(bloco);
	}

	public int calculaDVBloco(String bloco) {
		int soma = 0;
		for (int i = bloco.length() - 1, multiplicador = 2; i >= 0; i--, multiplicador--) {
			if (multiplicador == 0) {
				multiplicador = 2;
			}
			int parcial = Integer.parseInt(String.valueOf(bloco.charAt(i))) * multiplicador;

			if (parcial > 9) {
				parcial = parcial - 9;
			}

			soma += parcial;
		}

		int resto = soma % 10;
		if (resto == 0) {
			return 0;
		} else {
			return 10 - resto;
		}
	}
	
	public String  calculaDigitoVerificadorNossoNumero(String nossoNumero) {
	 	LinkedList<Integer>  numero = new LinkedList<Integer>();
		if (nossoNumero == null ||  nossoNumero.length() > 8) {
			throw new IllegalArgumentException("Nosso Número inválido: " + nossoNumero);
		}
		
		char[] digitos = nossoNumero.toCharArray();
		for (char digito : digitos) {
			numero.add(Character.getNumericValue(digito));
		}
		Integer valor = 0;
		int posicao = 0; 
		for(int i =9 ; i<2 ; i--) {		 	
			valor = numero.get(posicao) * i;
			posicao ++ ;
			valor += valor ;
		}
		valor = valor % 11 ;
		
		if(valor > 1) {
			valor = 11- valor  ;
		}
		return String.valueOf(valor);
	}
	
	public String calculaDVNossoNumero(String nossoNumero) {
		if (nossoNumero == null ||  nossoNumero.length() > 8) {
			throw new IllegalArgumentException("Nosso Número inválido: " + nossoNumero);
		}
		DigitoPara digitoPara = new DigitoPara(UteisTexto.leftPadWithZeros(nossoNumero, 8));
		int digito = Integer.parseInt(digitoPara.comMultiplicadoresDeAte(2,9)
							.mod(11)							
							.trocandoPorSeEncontrar("1", 0)
							.calcula());
		
		if (digito > 1) {
			digito = 11-digito;
		}
		
		return String.valueOf(digito);
}
	
	
	public String calcula2DVNossoNumero(String nossoNumero) {
		if (nossoNumero == null ||  nossoNumero.length() > 9) {
			throw new IllegalArgumentException("Nosso Número inválido: " + nossoNumero);
		}
		DigitoPara digitoPara = new DigitoPara(UteisTexto.leftPadWithZeros(nossoNumero, 9));
		int digito = Integer.parseInt(digitoPara.comMultiplicadoresDeAte(2,10)
							.mod(11)							
							.trocandoPorSeEncontrar("1", 0)
							.calcula());
		
		if (digito > 1) {
			digito = 11-digito;
		}
		
		return String.valueOf(digito);
	}

	
	
	 public int CalculaDVNossoNumero(String  nossoNumero,int peso ){
         int S = 0;
         int P = 0;
         int N = 0;
         int d = 0;
         LinkedList<Integer> listaInteiros = new LinkedList<Integer>();
         char[] digitos = nossoNumero.toCharArray();
		for (char digito : digitos) {
			 N = Character.getNumericValue(digito);
			P = N * peso--;

            S += P;
			
		}  

         int R = S % 11;

         if (R == 0 || R == 1)
             d = 0;

         if (R > 1)
             d = 11 - R;

         return d;
     }
}
