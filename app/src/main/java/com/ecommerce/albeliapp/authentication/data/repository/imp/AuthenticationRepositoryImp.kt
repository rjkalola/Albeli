package com.ecommerce.albeliapp.authentication.data.repository.imp

import com.ecommerce.albeliapp.authentication.data.remote.AuthenticationInterface
import com.ecommerce.albeliapp.authentication.data.repository.AuthenticationRepository

class AuthenticationRepositoryImp(
    private val authenticationInterface: AuthenticationInterface
) : AuthenticationRepository {

}
