package com.slowinski.radoslaw.cryptomarket.repository

import com.slowinski.radoslaw.cryptomarket.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
}