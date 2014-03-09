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
											"Oh não, alienigenas espaciais!!!Não me coma, tenho mulher e três filhos...coma eles!! \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Álcool... A causa e solução de todos os problemas. \n(Today's average: %.2f)",
											scoreavg));
				}
				break;
			case LEVEL_FIVE:
				request.getScoreImage().setImageResource(R.drawable.result_5);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"Nunca diga qualquer coisa a não ser que tenha certeza que todo mundo pensa o mesmo. \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Chorar não vai trazer de volta seu cão, a não ser que suas lágrimas tenham cheiro de ração. \n(Today's average: %.2f)",
											scoreavg));
				}
				break;
			case LEVEL_SIX:
				request.getScoreImage().setImageResource(R.drawable.result_6);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"Tenho três filhos e nenhum dinheiro...por que não posso ter nenhum filho e três dinheiros? \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Para mentir, apenas duas coisas são necessárias: alguém que minta e alguém que escute a mentira. \n(Today's average: %.2f)",
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
