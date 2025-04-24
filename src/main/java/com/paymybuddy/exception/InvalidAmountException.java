package com.paymybuddy.exception;

/**
 * Exception personnalisée levée lorsqu'un montant invalide est fourni
 * dans le cadre d'une transaction ou d'une opération financière.
 *
 * Cette exception peut être utilisée pour signaler, par exemple, un montant
 * nul, négatif ou non conforme aux règles métier de l'application.
 */
public class InvalidAmountException extends RuntimeException {

    /**
     * Construit une nouvelle exception InvalidAmountException avec un message détaillé.
     *
     * @param message le message décrivant la nature du montant invalide
     */
    public InvalidAmountException(String message) {
        super(message);
    }
}
