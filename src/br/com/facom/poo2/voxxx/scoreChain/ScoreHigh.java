package br.com.facom.poo2.voxxx.scoreChain;

import java.util.Arrays;
import java.util.List;

import android.util.Log;
import android.view.View;
import br.com.facom.poo2.voxxx.R;
import br.com.facom.poo2.voxxx.VoxxxScore;

public class ScoreHigh implements ScoreHandler {

	private List<ScoreLevel> lowLevel = Arrays
			.asList(ScoreLevel.LEVEL_SEVEN, ScoreLevel.LEVEL_EIGHT,
					ScoreLevel.LEVEL_NINE, ScoreLevel.LEVEL_TEN);

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
			case LEVEL_SEVEN:
				request.getScoreImage().setImageResource(R.drawable.result_7);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"O casamento é o caixão, os filhos são os pregos! \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Ao contrario do amor , o respeito não pode ser comprado. \n(Today's average: %.2f)",
											scoreavg));
				}
				break;
			case LEVEL_EIGHT:
				request.getScoreImage().setImageResource(R.drawable.result_8);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"A TV me respeita. Ela ri comigo e não de mim. \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Vou fazer o que faço de melhor, mentir para uma criança \n(Today's average: %.2f)",
											scoreavg));
				}
				break;
			case LEVEL_NINE:
				request.getScoreImage().setImageResource(R.drawable.result_9);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"Eu não acredito em duendes, eles mentem muito! \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Não bebam agua os peixes tranzam nela! \n(Today's average: %.2f)",
											scoreavg));
				}
				break;
			case LEVEL_TEN:
				request.getScoreImage().setImageResource(R.drawable.result_10);
				if (request.getScoreValue() > scoreavg) {
					request.getScoremsg()
							.setText(
									String.format(
											"A fé remove montanhas... dinamite então nem se fala! \n(Today's average: %.2f)",
											scoreavg));
				} else {
					request.getScoremsg()
							.setText(
									String.format(
											"Beber é coisa pra adultos... e crianças com carteiras de identidade falsa. \n(Today's average: %.2f)",
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
