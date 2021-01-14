
abstract class Node

interface NodeVisitor {
    fun visit(node: Node): Any
}

class Number(val token: Token): Node() {

    override fun  toString(): String {
        return "Number ($token)"
    }
}

class BinOp(val left: Node, val op: Token, val right: Node): Node() {

    override fun  toString(): String {
        return "BinOp${op.value} ($left $right)"
    }
}

class UnaryOp(val op: Token, val expr: Node): Node() {

    override fun  toString(): String {
        return "Unary${op.value} ($expr)"
    }
}

class Compound(var children: MutableList<Node>): Node() {
    override fun  toString(): String {
        return "Compound($children)"
    }
}

class Assign(val left: Node, val op: Token, val right: Node): Node() {

    override fun  toString(): String {
        return "Assign${op.value} ($left $right)"
    }
}

class Var(val token: Token): Node() {

    override fun  toString(): String {
        return "Var($token)"
    }
}

class NoOp(): Node() {
    override fun  toString(): String {
        return "NoOp"
    }
}