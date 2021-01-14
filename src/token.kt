enum class TokenType {
    NUMBER,
    PLUS,
    MINUS,
    MUL,
    DIV,
    LPAREN,
    RPAREN,
    EOF,
    BEGIN,
    END,
    DOT,
    ASSIGN,
    SEMI,
    ID
}

class Token(val type: TokenType, val value: String) {

    override fun toString(): String {
        return "Token ($type, $value)"
    }
}