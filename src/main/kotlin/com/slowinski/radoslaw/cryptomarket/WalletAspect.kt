package com.slowinski.radoslaw.cryptomarket

import com.slowinski.radoslaw.cryptomarket.exceptions.UserNotFoundException
import com.slowinski.radoslaw.cryptomarket.exceptions.WrongAmountException
import com.slowinski.radoslaw.cryptomarket.model.Credentials
import com.slowinski.radoslaw.cryptomarket.repository.UserRepository
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Aspect
@Component
class WalletAspect {

    @Autowired
    lateinit var userRepository: UserRepository

    @Before("execution(* com.slowinski.radoslaw.cryptomarket.controller.CryptoController.sellBtc(..)) && args(id, amount)")
    fun validateSellTransaction(id: Long, amount: Double) {
        validateAmount(amount)
        validateUserId(id)
    }

    @Before("execution(* com.slowinski.radoslaw.cryptomarket.controller.CryptoController.buyBtc(..)) && args(id, amount)")
    fun validateBuyTransaction(id: Long, amount: Double) {
        validateAmount(amount)
        validateUserId(id)
    }

    @Before("execution(* com.slowinski.radoslaw.cryptomarket.controller.CryptoController.deleteUser(..)) && args(id)")
    fun validateDeleteUser(id: Long) {
        validateUserId(id)
    }

    @Before("execution(* com.slowinski.radoslaw.cryptomarket.controller.CryptoController.updateUserFirstName(..)) && args(id, firstName)")
    fun validateUpdateFirstName(id: Long, firstName: String) {
        validateUserId(id)
    }

    @Before("execution(* com.slowinski.radoslaw.cryptomarket.controller.CryptoController.updateUserCredentials(..)) && args(id, credentials)")
    fun validateUpdateCredentials(id: Long, credentials: Credentials) {
        validateUserId(id)
    }

    private fun validateAmount(amount: Double) {
        if (amount < 0) {
            throw WrongAmountException()
        }
    }

    private fun validateUserId(id: Long) {
        if (!userRepository.existsById(id)) {
            throw UserNotFoundException("there is no user with id #$id")
        }
    }
}