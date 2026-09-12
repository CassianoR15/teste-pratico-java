package br.com.teste;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Principal {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final NumberFormat FORMATO_NUMERO =
            NumberFormat.getNumberInstance(new Locale("pt", "BR"));

    private static final BigDecimal SALARIO_MINIMO =
            new BigDecimal("1212.00");

    public static void main(String[] args) {

        FORMATO_NUMERO.setMinimumFractionDigits(2);
        FORMATO_NUMERO.setMaximumFractionDigits(2);

        // 3.1 - Inserir todos os funcionários na mesma ordem da tabela.
        List<Funcionario> funcionarios = new ArrayList<>();

        funcionarios.add(new Funcionario(
                "Maria", LocalDate.of(2000, 10, 18),
                new BigDecimal("2009.44"), "Operador"));

        funcionarios.add(new Funcionario(
                "João", LocalDate.of(1990, 5, 12),
                new BigDecimal("2284.38"), "Operador"));

        funcionarios.add(new Funcionario(
                "Caio", LocalDate.of(1961, 5, 2),
                new BigDecimal("9836.14"), "Coordenador"));

        funcionarios.add(new Funcionario(
                "Miguel", LocalDate.of(1988, 10, 14),
                new BigDecimal("19119.88"), "Diretor"));

        funcionarios.add(new Funcionario(
                "Alice", LocalDate.of(1995, 5, 1),
                new BigDecimal("2234.68"), "Recepcionista"));

        funcionarios.add(new Funcionario(
                "Heitor", LocalDate.of(1999, 11, 19),
                new BigDecimal("1582.72"), "Operador"));

        funcionarios.add(new Funcionario(
                "Arthur", LocalDate.of(1993, 3, 31),
                new BigDecimal("4071.84"), "Contador"));

        funcionarios.add(new Funcionario(
                "Laura", LocalDate.of(1994, 8, 7),
                new BigDecimal("3017.45"), "Gerente"));

        funcionarios.add(new Funcionario(
                "Heloísa", LocalDate.of(2003, 5, 24),
                new BigDecimal("1606.85"), "Eletricista"));

        funcionarios.add(new Funcionario(
                "Helena", LocalDate.of(1996, 9, 2),
                new BigDecimal("2799.93"), "Gerente"));

        // 3.2 - Remover o funcionário João.
        funcionarios.removeIf(funcionario -> funcionario.getNome().equals("João"));

        // 3.3 - Imprimir todos os funcionários.
        System.out.println("========== 3.3 - FUNCIONÁRIOS ==========");
        imprimirFuncionarios(funcionarios);

        // 3.4 - Aumentar os salários em 10%.
        funcionarios.forEach(funcionario -> {
            BigDecimal novoSalario = funcionario.getSalario()
                    .multiply(new BigDecimal("1.10"))
                    .setScale(2, RoundingMode.HALF_UP);
            funcionario.setSalario(novoSalario);
        });

        System.out.println("\n========== 3.4 - SALÁRIOS APÓS AUMENTO DE 10% ==========");
        imprimirFuncionarios(funcionarios);

        // 3.5 - Agrupar por função.
        Map<String, List<Funcionario>> funcionariosPorFuncao =
                funcionarios.stream()
                        .collect(Collectors.groupingBy(
                                Funcionario::getFuncao,
                                LinkedHashMap::new,
                                Collectors.toList()));

        // 3.6 - Imprimir funcionários agrupados por função.
        System.out.println("\n========== 3.6 - AGRUPADOS POR FUNÇÃO ==========");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("\nFunção: " + funcao);
            imprimirFuncionarios(lista);
        });

        // 3.8 - Imprimir aniversariantes de outubro e dezembro.
        System.out.println("\n========== 3.8 - ANIVERSARIANTES DE OUTUBRO E DEZEMBRO ==========");
        funcionarios.stream()
                .filter(f -> f.getDataNascimento().getMonthValue() == 10
                        || f.getDataNascimento().getMonthValue() == 12)
                .forEach(Principal::imprimirFuncionario);

        // 3.9 - Funcionário com maior idade.
        Funcionario funcionarioMaisVelho = funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .orElse(null);

        System.out.println("\n========== 3.9 - FUNCIONÁRIO COM MAIOR IDADE ==========");
        if (funcionarioMaisVelho != null) {
            int idade = calcularIdade(funcionarioMaisVelho.getDataNascimento());
            System.out.println("Nome: " + funcionarioMaisVelho.getNome());
            System.out.println("Idade: " + idade + " anos");
        }

        // 3.10 - Lista por ordem alfabética.
        System.out.println("\n========== 3.10 - ORDEM ALFABÉTICA ==========");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(Principal::imprimirFuncionario);

        // 3.11 - Total dos salários após o aumento.
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("\n========== 3.11 - TOTAL DOS SALÁRIOS ==========");
        System.out.println("Total: R$ " + FORMATO_NUMERO.format(totalSalarios));

        // 3.12 - Quantos salários mínimos cada funcionário recebe.
        System.out.println("\n========== 3.12 - SALÁRIOS MÍNIMOS ==========");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(funcionario -> {
                    BigDecimal quantidade = funcionario.getSalario()
                            .divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);

                    System.out.println(funcionario.getNome()
                            + " recebe "
                            + quantidade.toPlainString()
                            + " salários mínimos.");
                });
    }

    private static void imprimirFuncionarios(List<Funcionario> funcionarios) {
        funcionarios.forEach(Principal::imprimirFuncionario);
    }

    private static void imprimirFuncionario(Funcionario funcionario) {
        System.out.println(
                "Nome: " + funcionario.getNome()
                + " | Nascimento: " + FORMATO_DATA.format(funcionario.getDataNascimento())
                + " | Salário: R$ " + FORMATO_NUMERO.format(funcionario.getSalario())
                + " | Função: " + funcionario.getFuncao()
        );
    }

    private static int calcularIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }
}
