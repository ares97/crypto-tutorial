package com.slowinski.radoslaw.cryptomarket.repository

import com.slowinski.radoslaw.cryptomarket.model.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository : JpaRepository<Wallet, Long> {
}