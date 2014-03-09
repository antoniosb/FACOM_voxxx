package br.com.facom.poo2.voxxx.scoreChain;

import java.util.Arrays;
import java.util.List;

import android.view.View;
import br.com.facom.poo2.voxxx.R;
import br.com.facom.poo2.voxxx.VoxxxScore;

public class ScoreLow implements ScoreHandler {

	private List<ScoreLevel> lowLevel = Arrays.asList(ScoreLevel.LEVEL_ZERO,
			ScoreLevel.LEVEL_ONE, ScoreLevel.LEVEL_TWO, ScoreLevel.LEVEL_THREE);

	private ScoreHandler next = null;

	public ScoreHandler getNext() {
		return next;
	}

	public void setNext(ScoreHandler next) {
		this.next = next;
	}

	@Override
	public void handleScore(VoxxxScore request) {
		if (isLowLevel(request)) {

			double scoreavg = request.getScoreAvg();

			switch (request.getLevel()) {
			case LEVEL_ZERO:
				request.getScoreImage().setImageResource(R.drawable.result_0);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"A culpa é minha e eu coloco ela em quem eu quiser! \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Passei a detestar a minha própria criação! Agora eu sei como Deus se sente. \n(Today's average: %.2f)",
											scoreavg));
				}
				break;
			case LEVEL_ONE:
				request.getScoreImage().setImageResource(R.drawable.result_1);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"As pessoas inventam estatísticas para provar qualquer coisa. 40%% das pessoas sabem disso. \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Bem, ele pode ter todo o dinheiro do mundo, mas tem uma coisa que ele não pode comprar... Um dinossauro! \n(Today's average: %.2f)",
											scoreavg));
				}
				break;
			case LEVEL_TWO:
				request.getScoreImage().setImageResource(R.drawable.result_2);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"Sei que nunca fui um homem muito religioso mas, se estiver aí em cima, por favor, me salve Super-Homem! \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Se algo é difícil de fazer, então não vale a pena ser feito! \n(Today's average: %.2f)",
											scoreavg));
				}
				break;
			case LEVEL_THREE:
				request.getScoreImage().setImageResource(R.drawable.result_3);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"Eu não estava mentindo! Estava escrevendo ficção com a boca. \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Preguiça é o ato de descançar, antes de estar cansado! \n(Today's average: %.2f)",
											scoreavg));
				}
				break;

			default:
				break;

			}
			request.getScoreImage().setVisibility(View.VISIBLE);
			request.getScoremsg().setVisibility(View.VISIBLE);
		} else {
			if (next != null) {
				next.handleScore(request);
			} else {
				throw new IllegalArgumentException();
			}
		}
	}

	private boolean isLowLevel(VoxxxScore request) {
		return lowLevel.contains(request.getLevel());
	}

}
