package com.slowinski.radoslaw.cryptomarket

import com.slowinski.radoslaw.cryptomarket.exceptions.CantFetchBtcPrice
import com.slowinski.radoslaw.cryptomarket.exceptions.NotEnoughMoneyException
import com.slowinski.radoslaw.cryptomarket.exceptions.UserNotFoundException
import com.slowinski.radoslaw.cryptomarket.model.BtcPrice
import com.slowinski.radoslaw.cryptomarket.model.User
import com.slowinski.radoslaw.cryptomarket.model.Wallet
import com.slowinski.radoslaw.cryptomarket.repository.UserRepository
import com.slowinski.radoslaw.cryptomarket.repository.WalletRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

interface CryptoFacade {
    fun getUsers(): List<User>
    fun addUser(firstName: String, lastName: String): User
    fun getUser(id: Long): User
    fun sellBtc(id: Long, amount: Double): Wallet
    fun buyBtc(id: Long, amount: Double): Wallet
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

        val bidPrice = getBtcPrice().bid
        if (btcAmount >= amount) {
            user.wallet.btc -= amount
            user.wallet.usd += amount * bidPrice
            userRepository.save(user)
        } else {
            throw NotEnoughMoneyException("you have not enough bitcoins")
        }
        return user.wallet
    }

    override fun buyBtc(id: Long, amount: Double): Wallet {
        val user = userRepository.getOne(id)
        val usdAmount = user.wallet.usd

        val askPrice = getBtcPrice().ask
        if (askPrice <= usdAmount) {
            user.wallet.usd -= amount * askPrice
            user.wallet.btc += amount
            userRepository.save(user)
        } else {
            throw NotEnoughMoneyException("you have not enough usd")
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

    fun getBtcPrice(): BtcPrice {
        val response = RestTemplate()
                .getForObject(API_GET_BTC_PRICE, BtcPrice::class.java) ?: throw CantFetchBtcPrice()

        return response
    }

    companion object {
        const val BTC_PRICE = 5000.0
        const val API_GET_BTC_PRICE = "https://www.bitstamp.net/api/v2/ticker/btcusd/"
    }
}