/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author alepr
 */
public class DecimalFilter extends DocumentFilter {
    // Evento llamado cuando el usuario ingresa texto nuevo
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        String oldText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = oldText.substring(0, offset) + string + oldText.substring(offset);
        if (isValid(newText)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    // Evento llamado cuando el usuario reemplaza texto
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        String oldText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = oldText.substring(0, offset) + text + oldText.substring(offset + length);
        if (isValid(newText)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
    
    private boolean isValid(String text) {
        // Permitir vacío (inicio de escritura)
        if (text.isEmpty()) {
            return true;
        }

        // Permitir que escriba algo como "9." de forma intermedia
        if (!text.matches("\\d{0,2}(\\.)?(\\d{0,2})?")) {
            return false;
        }

        // Evitar que escriba más de un punto
        if (countPoints(text) > 1) {
            return false;
        }

        // Si termina en punto, dejarlo pasar (ej: "9.")
        if (text.endsWith(".")) {
            return true;
        }

        // Si el texto ya representa un número completo, validar valor
        try {
            double valor = Double.parseDouble(text);
            // Solo permitir 10 exacto, sin decimales
            if (valor == 10) {
                return text.equals("10");
            }
            return valor >= 0 && valor < 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }
  
    private int countPoints(String text) {
        return (int) text.chars().filter(c -> c == '.').count();
    }
}
