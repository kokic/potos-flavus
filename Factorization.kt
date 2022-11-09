
import java.math.BigInteger

fun main(args: Array<String>) {
    val m = (1003917294).toBigInteger()
    println(Factorization.factor(m))
}

fun Integer.isPrime(): Boolean {
    if (this < BigInteger.TWO) return false
    var i = BigInteger.TWO
    while (i.multiply(i) <= this)
        if (mod(i++) == BigInteger.ZERO) return false
    return true
}

object Factorization {

    fun factor(x: Integer) = factorList(x).groupingBy { u -> u }.eachCount()
    fun factorList(x: Integer): List<Integer> {
        val list = mutableListOf<Integer>()
        var phase = x
        while (phase != BigInteger.ONE) {
            val rho = pollardRho(phase)
            list.add(rho)
            phase /= rho
        }
        val hybrid = list.filter { u -> !u.isPrime() }
        hybrid.forEach { u -> list.remove(u) and list.addAll(factorList(u)) }
        return list
    }

    private val pollardRange = 0..Short.MAX_VALUE
    fun pollardRho(x: Integer): Integer {
        var s = BigInteger.ONE
        var t = BigInteger.ZERO
        val c = pollardRange.random().toBigInteger()
            .mod(x - BigInteger.ONE) + BigInteger.ONE
        var step = 0
        var goal = 1
        var phas = BigInteger.ONE
        goal = 1
        while (true) {
            step = 1
            while (step <= goal) {
                t = (t * t + c).mod(x)
                phas = (phas * t.subtract(s).abs()).mod(x)
                if (step % 127 == 0) {
                    val d = x.gcd(phas)
                    if (d > BigInteger.ONE) return d
                }
                ++step
            }
            val d = x.gcd(phas)
            if (d > BigInteger.ONE) return d
            goal = goal shl 1
            s = t
            phas = BigInteger.ONE
        }
    }
}


