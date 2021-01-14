

class Lexer(val text: String) {
    private var pos: Int = 0
    private var currentChar: Char? = null
    private var nextChar: Char? = null
    private var prevChar: Char? = null
    init {
        currentChar = text[pos]
        nextChar = if(pos+1 < text.length) text[pos+1] else null
    }
    public fun nextToken(): Token {
        var value: String
        var type: TokenType?
        while (currentChar != null) {

            if (currentChar!!.isWhitespace()) {
                skip()
                continue
            }
            if (currentChar!!.isDigit()) {
                return Token(TokenType.NUMBER, number())
            }
            if (currentChar!!.isLetter()) {
                return idToken()
            }

            type = null
            value = "$currentChar"

            when (currentChar) {
                '+' -> type = TokenType.PLUS
                '-' -> type = TokenType.MINUS
                '/' -> type = TokenType.DIV
                '*' -> type = TokenType.MUL
                '(' -> type = TokenType.LPAREN
                ')' -> type = TokenType.RPAREN
                ';' -> type = TokenType.SEMI
                '.' -> type = TokenType.DOT
                ':' -> {
                    when (nextChar) {
                        '=' -> {
                            type = TokenType.ASSIGN
                            value += "$nextChar"
                            type?.let {forward()}
                        }
                    }
                }
            }
            /*
            if (type != null) {
                forward()
                return Token(type, value )
            }*/
            type?.let {
                forward()
                return Token(type, value)
            }
            println("$type, sdfsds $value")
            throw InterpreterException("invalid token")

        }
        return Token(TokenType.EOF, "")
    }

    private fun forward() {
        pos += 1
        if (pos > text.length - 1) {
            currentChar = null
            nextChar = null
        }
        else {
            currentChar = text[pos]
            nextChar = if(pos+1 < text.length) text[pos+1] else null
        }
    }

    private fun skip() {
        while ((currentChar != null) && currentChar!!.isWhitespace()){
            forward()
        }
    }

    private fun number(): String {
        var result = arrayListOf<Char>()
        while ((currentChar != null) && (currentChar!!.isDigit() || currentChar == '.')) {
            result.add(currentChar!!)
            forward()
        }
        return result.joinToString("")
    }

    private fun idToken(): Token {
        var keywords = HashMap <String, Token>()
        keywords["BEGIN"] = Token(TokenType.BEGIN, "BEGIN")
        keywords["END"] = Token(TokenType.END, "END")
        var result = arrayListOf<Char>()
        while ((currentChar != null) && (currentChar!!.isLetterOrDigit())) {
            result.add(currentChar!!)
            forward()
        }
        return keywords[result.joinToString("")] ?: Token(TokenType.ID, result.joinToString(""))
    }
}

fun main(args: Array<String>){
    val lexer = Lexer("BEGIN a := 2; END.")
    println(lexer.nextToken())
    println(lexer.nextToken())

    println(lexer.nextToken())

    println(lexer.nextToken())

    println(lexer.nextToken())
    println(lexer.nextToken())
    println(lexer.nextToken())

}