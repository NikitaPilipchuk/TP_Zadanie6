class Parser(private val lexer: Lexer) {

    private var currentToken: Token = lexer.nextToken()

    private var unMinus = false
    private fun checkTokenType(type: TokenType){
        if (currentToken.type == type) {
            currentToken = lexer.nextToken()
        }
        else {
            throw InterpreterException("invalid token order")
        }
    }

    private fun factor(): Node {
        val token = currentToken

        when (token.type) {
            TokenType.PLUS -> {
                checkTokenType(TokenType.PLUS)
                return UnaryOp(token, factor())
            }
            TokenType.MINUS -> {
                checkTokenType(TokenType.MINUS)
                return UnaryOp(token, factor())
            }
            TokenType.NUMBER -> {
                checkTokenType(TokenType.NUMBER)
                return Number(token)
            }
            TokenType.LPAREN -> {
                checkTokenType(TokenType.LPAREN)
                val result = expr()
                checkTokenType(TokenType.RPAREN)
                return result
            }
            TokenType.EOF -> {
                return empty()
            }
            else -> {
                return variable()
            }
        }
        throw InterpreterException("Invalid")
    }

    fun program(): Node {
        val node = compoundStatement()
        checkTokenType(TokenType.DOT)
        return node
    }

    fun compoundStatement(): Node {
        checkTokenType(TokenType.BEGIN)
        val nodes = statementList()
        checkTokenType(TokenType.END)
        val root = Compound(mutableListOf())
        for(node in nodes) {
            root.children.add(node)
        }
        return root
    }

    fun statementList(): List<Node> {
        val node = statement()
        var results = mutableListOf<Node>(node)
        while (currentToken.type == TokenType.SEMI) {
            checkTokenType(TokenType.SEMI)
            results.add(statement())
        }
        if (currentToken.type == TokenType.ID) {
            throw InterpreterException("Invalid statment")
        }
        return results
    }

    fun statement(): Node {
        val token = currentToken
        if(token.type == TokenType.BEGIN) {
            return compoundStatement()
        }
        if(token.type == TokenType.ID) {
            return assignmentStatement()
        }
        return empty()
    }

    fun assignmentStatement(): Node {
        val left = variable()
        val token = currentToken
        checkTokenType(TokenType.ASSIGN)
        val right = expr()
        return Assign(left, token, right)
    }

    fun variable(): Node {
        val node = Var(currentToken)
        //ln("${node.token.value}, ${node.token.type}")
        checkTokenType(TokenType.ID)
        return node
    }
    fun empty(): Node {
        return NoOp()
    }

    private fun term(): Node {
        val ops = arrayListOf<TokenType>(TokenType.MUL, TokenType.DIV)
        var result = factor()

        while (ops.contains(currentToken.type)) {
            val token = currentToken
            when (token.type) {
                TokenType.DIV -> {
                    checkTokenType(TokenType.DIV)
                }
                TokenType.MUL -> {
                    checkTokenType(TokenType.MUL)
                }
            }
            result = BinOp(result, token, factor())
        }
        return result
    }
    fun expr(): Node {
        val ops = arrayListOf<TokenType>(TokenType.PLUS, TokenType.MINUS)
        var result = term()

        while (ops.contains(currentToken.type)) {
            val token = currentToken
            if (token.type == TokenType.PLUS) {
                checkTokenType(TokenType.PLUS)
            }
            if (token.type == TokenType.MINUS) {
                checkTokenType(TokenType.MINUS)
            }
            result = BinOp(result, token, term())
        }
        return result
    }
    fun parse(): Node {
        val node = program()
        if (currentToken.type != TokenType.EOF) {
            throw InterpreterException("Wrong EOF")
        }
        return node
    }
}


fun main(args: Array<String>) {
    val text = """
 BEGIN
 END.
    """.trimIndent()
    val lex = Lexer(text)
    val parser = Parser(lex)
    println(parser.parse())
}