fun main(args: Array<String>) {

    val text = """
        BEGIN
        END.
    """.trimIndent()

    val text2 = """
        BEGIN
        	x:= 2 + 3 * (2 + 3);
            y:= 2 / 2 - 2 + 3 * ((1 + 1) + (1 + 1));
        END.
    """.trimIndent()

    val text3 = """
         BEGIN
            y := 2;
            BEGIN
                a := 3;
                a := a;
                b := 10 + a + 10 * y / 4;
                c := a - b
            END;
            x := 11;
         END.
    """.trimIndent()

    println("===================== Пример 1 =====================")
    println(text + "\n")
    var parser = Parser(Lexer(text))
    var tree = parser.parse()
    var interpreter = Interpreter(false)
    println("Значение переменных:\n" + interpreter.interpret(tree) + "\n")

    println("===================== Пример 2 =====================")
    println(text2 + "\n")
    parser = Parser(Lexer(text2))
    tree = parser.parse()
    interpreter = Interpreter(false)
    println("Значение переменных:\n" + interpreter.interpret(tree) + "\n")

    println("===================== Пример 3 =====================")
    println(text3 + "\n")
    parser = Parser(Lexer(text3))
    tree = parser.parse()
    interpreter = Interpreter(false)
    println("Значение переменных:\n" + interpreter.interpret(tree) + "\n")

}