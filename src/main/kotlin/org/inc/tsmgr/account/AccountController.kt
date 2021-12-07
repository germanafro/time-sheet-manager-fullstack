package org.inc.tsmgr.account

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
class AccountController(
    private val service: AccountService
) {

    @GetMapping("/")
    fun accounts(): Iterable<Account> = service.getAccounts()

    @PostMapping("/import")
    fun import(@RequestBody path: String): String = service.import(path)


}