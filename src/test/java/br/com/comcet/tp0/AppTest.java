package br.com.comcet.tp0;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
* Testa se as estatísticas básicas do texto
* estão sendo calculadas corretamente.
*/

public class AppTest {

    @Test
    public void testEstatisticasBasicas() {

        // Captura o que será impresso no console.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));

        // Executa o método que calcula as estatísticas.
        App.exibirEstatisticas("Java e legal. Java e potente.");

        // Verifica se os valores esperados aparecem na saída.
        String output = outContent.toString();
        assertTrue(output.contains("Caracteres: 22"));
        assertTrue(output.contains("Palavras: 6"));
        assertTrue(output.contains("Frequente: e")); // Letra e palavra 'e'
    }

    /*
    * Testa outro texto para verificar se os
    * cálculos continuam corretos.
    */
    @Test
    public void testTextoDireto() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        App.exibirEstatisticas("Compiladores sao divertidos");

        String output = outContent.toString();
        assertTrue(output.contains("Caracteres: 25"));
        assertTrue(output.contains("Frequente: o"));
        assertTrue(output.contains("Palavras: 3"));
        assertTrue(output.contains("Frequente: compiladores"));
    }

    /*
    * Verifica o comportamento quando o texto
    * informado está vazio.
    */
    @Test
    public void testTextoVazio() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        App.exibirEstatisticas("");

        String output = outContent.toString();

        // O programa deve informar que o texto está vazio.
        assertTrue(output.contains("Texto vazio."));
    }
}