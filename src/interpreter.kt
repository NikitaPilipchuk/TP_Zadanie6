
 class InterpreterException(message: String): Exception(message)

fun Boolean.ifTrue(action: () -> Unit) {
    if (this) {
        action()
    }
}

class Interpreter(private val verbose: Boolean = true): NodeVisitor {

    var symbolTable = mutableMapOf<String, Double>()

    private fun print(s: String) = verbose.ifTrue {println(s)}

    private fun visitNumber(node: Node): Double {
        node as Number
        print("visit $node")
        return node.token.value.toDouble()
    }

    private fun visitBinOp(node: Node): Double {
        node as BinOp
        print("visit $node")
        val left = visit(node.left) as Double
        val right = visit(node.right) as Double
        when(node.op.type) {

            TokenType.PLUS -> return left + right
            TokenType.MINUS -> return left - right
            TokenType.DIV -> return left / right
            TokenType.MUL -> return left * right
        }
        throw InterpreterException("invalid operator")
    }

    private fun visitUnaryOp(node: Node): Double {
        node as UnaryOp
        print("visit $node")

        when (node.op.type) {
            TokenType.PLUS -> return +(visit(node.expr) as Double)
            TokenType.MINUS -> return -(visit(node.expr) as Double)
        }
        throw InterpreterException("invalid unary")
    }

    private fun visitCompound(node: Node): MutableMap<String, Double> {
        node as Compound
        for (child in node.children) {
            visit(child)
        }
        return symbolTable
    }

    private fun visitAssign(node: Node) {
        node as Assign
        var varName = (node.left as Var).token.value
        symbolTable[varName] = visit(node.right) as Double
    }

    private fun visitVar(node: Node): Double {
        var varName = (node as Var).token.value
        val value = symbolTable.getValue(varName)
        return value!!
    }


    private fun visitNoOp(node: Node) {}

    fun interpret(tree:Node): MutableMap<String, Double> {
        return visit(tree) as MutableMap<String, Double>
    }

    override fun visit(node: Node): Any {
        when(node) {
            is Number -> return  visitNumber(node)
            is BinOp -> return  visitBinOp(node)
            is UnaryOp -> return  visitUnaryOp(node)
            is Compound -> return visitCompound(node)
            is Assign -> return visitAssign(node)
            is Var -> return visitVar(node)
            is NoOp -> return visitNoOp(node)
        }
        throw InterpreterException("invalid node")
    }

}

fun main(args: Array<String>) {
    val text = """
 BEGIN
     BEGIN
         number := 2;
         a := number;
         b := 10 * a + 10 * number / 4;
         c := a - - b
     END;

     x := 11;
 END.
    """.trimIndent()
    val text2 = "BEGIN a := 2; END."
    val parser = Parser(Lexer(text))
    val tree = parser.parse()
    println(tree)
    val interpreter = Interpreter()
    println(interpreter.interpret(tree))
    println(interpreter.symbolTable)
}