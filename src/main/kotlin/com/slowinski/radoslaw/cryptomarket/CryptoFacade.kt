package com.slowinski.radoslaw.cryptomarket

import com.slowinski.radoslaw.cryptomarket.exceptions.NotEnoughMoneyException
import com.slowinski.radoslaw.cryptomarket.exceptions.UserNotFoundException
import com.slowinski.radoslaw.cryptomarket.model.User
import com.slowinski.radoslaw.cryptomarket.model.Wallet
import com.slowinski.radoslaw.cryptomarket.repository.UserRepository
import com.slowinski.radoslaw.cryptomarket.repository.WalletRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface CryptoFacade {
    fun getUsers(): List<User>
    fun addUser(firstName: String, lastName: String): User
    fun getUser(id: Long): User
    fun sellBtc(id: Long, amount: Double): Wallet
}

@Service
class CryptoFacadeImpl : CryptoFacade {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var walletRepository: WalletRepository

    override fun sellBtc(id: Long, amount: Double): Wallet {
        val user = userRepository.getOne(id)
        val btcAmount = user.wallet.btc

        if (btcAmount >= amount) {
            user.wallet.btc -= amount
            user.wallet.usd += amount * BTC_PRICE
            userRepository.save(user)
        } else {
            throw NotEnoughMoneyException("you have not enought bitcoins")
        }
        return user.wallet
    }

    override fun addUser(firstName: String, lastName: String): User {
        val wallet = Wallet(btc = 5.0, usd = 1250.0)
        walletRepository.save(wallet)

        val user = User(firstName = firstName, lastName = lastName, wallet = wallet)
        userRepository.save(user)

        return user
    }

    override fun getUser(id: Long): User {
        return userRepository
                .findById(id)
                .orElseThrow { throw UserNotFoundException("can't find user with such id") }
    }


    override fun getUsers(): List<User> {
        return userRepository.findAll()
    }

    companion object {
        const val BTC_PRICE = 5000.0
    }
}