package com.slowinski.radoslaw.cryptomarket.controller

import com.slowinski.radoslaw.cryptomarket.CryptoFacade
import com.slowinski.radoslaw.cryptomarket.model.User
import com.slowinski.radoslaw.cryptomarket.model.Wallet
import com.slowinski.radoslaw.cryptomarket.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class CryptoController {

    @Autowired
    lateinit var cryptoFacade: CryptoFacade

    @GetMapping("/users")
    fun getUsers(): List<User> {
        return cryptoFacade.getUsers()
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    fun addUser(@RequestParam("firstName") firstName: String,
                @RequestParam("lastName") lastName: String): User {
        return cryptoFacade.addUser(firstName, lastName)
    }

    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: Long): User {
        return cryptoFacade.getUser(id)
    }

    @PostMapping("/user/{id}/sell/btc")
    fun sellBtc(@PathVariable id: Long,
                @RequestParam("amount") amount: Double): Wallet{
        return cryptoFacade.sellBtc(id, amount)
    }

    @PostMapping("/user/{id}/buy/btc")
    fun buyBtc(@PathVariable id: Long,
               @RequestParam("amount") amount: Double): Wallet{
        return cryptoFacade.buyBtc(id, amount)
    }

}