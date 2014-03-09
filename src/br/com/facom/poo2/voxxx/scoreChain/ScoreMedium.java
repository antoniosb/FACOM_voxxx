package br.com.facom.poo2.voxxx.scoreChain;

import java.util.Arrays;
import java.util.List;

import android.util.Log;
import android.view.View;
import br.com.facom.poo2.voxxx.R;
import br.com.facom.poo2.voxxx.VoxxxScore;

public class ScoreMedium implements ScoreHandler {

	private List<ScoreLevel> lowLevel = Arrays.asList(ScoreLevel.LEVEL_FOUR,
			ScoreLevel.LEVEL_FIVE, ScoreLevel.LEVEL_SIX);

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
			case LEVEL_FOUR:
				request.getScoreImage().setImageResource(R.drawable.result_4);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"Oh n�o, alienigenas espaciais!!!N�o me coma, tenho mulher e tr�s filhos...coma eles!! \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"�lcool... A causa e solu��o de todos os problemas. \n(Today's average: %.2f)",
											scoreavg));
				}
				break;
			case LEVEL_FIVE:
				request.getScoreImage().setImageResource(R.drawable.result_5);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"Nunca diga qualquer coisa a n�o ser que tenha certeza que todo mundo pensa o mesmo. \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Chorar n�o vai trazer de volta seu c�o, a n�o ser que suas l�grimas tenham cheiro de ra��o. \n(Today's average: %.2f)",
											scoreavg));
				}
				break;
			case LEVEL_SIX:
				request.getScoreImage().setImageResource(R.drawable.result_6);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"Tenho tr�s filhos e nenhum dinheiro...por que n�o posso ter nenhum filho e tr�s dinheiros? \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Para mentir, apenas duas coisas s�o necess�rias: algu�m que minta e algu�m que escute a mentira. \n(Today's average: %.2f)",
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
